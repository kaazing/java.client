/**
 * Copyright 2007-2015, Kaazing Corporation. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kaazing.gateway.bridge;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.kaazing.gateway.bridge.XoaEvent.XoaEventKind;
import org.kaazing.gateway.client.transport.AuthenticateEvent;
import org.kaazing.gateway.client.transport.BridgeDelegate;
import org.kaazing.gateway.client.transport.CloseEvent;
import org.kaazing.gateway.client.transport.ErrorEvent;
import org.kaazing.gateway.client.transport.LoadEvent;
import org.kaazing.gateway.client.transport.MessageEvent;
import org.kaazing.gateway.client.transport.OpenEvent;
import org.kaazing.gateway.client.transport.ProgressEvent;
import org.kaazing.gateway.client.transport.ReadyStateChangedEvent;
import org.kaazing.gateway.client.transport.RedirectEvent;
import org.kaazing.gateway.client.transport.http.HttpRequestDelegate;
import org.kaazing.gateway.client.transport.http.HttpRequestDelegateFactory;
import org.kaazing.gateway.client.transport.http.HttpRequestDelegateImpl;
import org.kaazing.gateway.client.transport.http.HttpRequestDelegateListener;
import org.kaazing.gateway.client.transport.ws.WebSocketDelegate;
import org.kaazing.gateway.client.transport.ws.WebSocketDelegateFactory;
import org.kaazing.gateway.client.transport.ws.WebSocketDelegateImpl;
import org.kaazing.gateway.client.transport.ws.WebSocketDelegateListener;

public class CrossOriginProxy extends PropertyChangeSupport implements XoaEventListener {
    private static final String CLASS_NAME = CrossOriginProxy.class.getName();

    static final Logger LOG = Logger.getLogger(CLASS_NAME);

    private static final String SOA_MESSAGE = "soaMessage";
    private static final String XOP_MESSAGE = "xopMessage";
    private static final String OBJECT_TYPE_HTTPREQUEST = "HTTPREQUEST";
    private static final String OBJECT_TYPE_WEBSOCKET = "WEBSOCKET";
    private static final String[] EMPTY_ARGS = new String[] {};
    private static final long serialVersionUID = -3694699522384518136L;

    private XoaEventProcessor xoaEventProcessor = new XoaEventProcessor(this);
    private Map<Integer, BridgeDelegate> xoaObjectMap = new ConcurrentHashMap<Integer, BridgeDelegate>();

    private boolean soaValidated = false;
    private boolean rejectFurtherMessages = false;

    private String xOrigin = null;
    
    WebSocketDelegateFactory WEB_SOCKET_DELEGATE_FACTORY = new WebSocketDelegateFactory() {
        @Override
        public WebSocketDelegate createWebSocketDelegate(URI xoaUrl, URI originUrl, String wsProtocols) {
            return new WebSocketDelegateImpl(xoaUrl, originUrl, wsProtocols == null ? null : wsProtocols.split(","), 0);
        }
    };

    HttpRequestDelegateFactory HTTP_REQUEST_DELEGATE_FACTORY = new HttpRequestDelegateFactory() {
        @Override
        public HttpRequestDelegate createHttpRequestDelegate() {
            return new HttpRequestDelegateImpl();
        }
    };

    public CrossOriginProxy() {
        super("null");
        LOG.entering(CLASS_NAME, "<init>");

        this.addPropertyChangeListener(SOA_MESSAGE, new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                LOG.entering(CLASS_NAME, "propertyChange");

                /**
                 * Process an event from Source Origin to WebSocket or HttpRequest delegates
                 * @param event
                 */
                Object[] eventParts = (Object[])evt.getNewValue();
                if (eventParts == null) {
                    LOG.severe("Event value is null");
                    return;
                }
                else if (eventParts.length == 0) {
                    LOG.severe("Event value has no parameters");
                    return;
                }
                
                Integer handlerId = (Integer)eventParts[0];
                
                // if at first message we do not validate the same origin - it may be an attack
                // so prevent further messages from getting processed at all
                if (rejectFurtherMessages) {
                    dispatchEvent(XoaEvent.createErrorEvent(handlerId));
                    return;
                }

                if (!soaValidated) {
                    if (!validateSameOrigin((String[]) eventParts[2])) {
                        rejectFurtherMessages = true;
                        dispatchEvent(XoaEvent.createErrorEvent(handlerId));
                        return;
                    }
                    soaValidated = true;
                }

                XoaEventKind eventName = XoaEventKind.getName((String) eventParts[1]);
                Object[] params = (Object[]) eventParts[2];

                XoaEvent event = new XoaEvent(handlerId, eventName, params);
                xoaEventProcessor.offer(event);
            }
        });
    }

    /**
     * Dispatch an event to Source Origin
     * @param event
     */
    private void dispatchEvent(XoaEvent event) {
        LOG.entering(CLASS_NAME, "dispatchEvent", event);
        
        try {
            Integer handlerId = event.getHandlerId();
            
            XoaEventKind eventKind = event.getEvent();
            if (LOG.isLoggable(Level.FINEST)) {
                LOG.finest("XOA --> SOA: " + handlerId  + " event: " + eventKind.name());
            }
            
            BridgeDelegate handler = xoaObjectMap.get(handlerId);
            if (handler == null) {
                if (LOG.isLoggable(Level.FINEST)) {
                    LOG.finest("No handler for handler id: " + handlerId);
                }
                return;
            }
            
            /* Allow only one bridge crossing per handler at a time */
            synchronized (handler) {
                // dispatch to SOA
                Object[] args = { handlerId, eventKind.toString(), event.getParams() };
                firePropertyChange(XOP_MESSAGE, null, args);
            }

        } catch (Exception e) {
            LOG.log(Level.FINE, "While dispatching event: "+e.getMessage(), e);
            throw new IllegalStateException("Unable to dispatch an event from the cross origin proxy", e);
        }

    }

    HttpRequestDelegate createHttpRequestDelegate(final Integer id) {
        LOG.entering(CLASS_NAME, "createHttpRequestDelegate", id);

        // There is a race here if processing is not serialized for a handler!
        HttpRequestDelegate reqDelegate = HTTP_REQUEST_DELEGATE_FACTORY.createHttpRequestDelegate();
        reqDelegate.setListener(new HttpRequestDelegateListener() {
            
            @Override
            public void opened(OpenEvent event) {
                if (LOG.isLoggable(Level.FINE)) {
                    LOG.fine("HttpRequestDelegateListener opened: "+id);
                }
                dispatchEvent(new XoaEvent(id, XoaEventKind.OPEN, event.getParams()));
            }
            
            @Override
            public void readyStateChanged(ReadyStateChangedEvent event) {
                if (LOG.isLoggable(Level.FINER)) {
                    LOG.finer("HttpRequestDelegateListener readyStateChanged: "+id);
                }
                dispatchEvent(new XoaEvent(id, XoaEventKind.READYSTATECHANGE, event.getParams()));
            }
            
            @Override
            public void progressed(ProgressEvent progressEvent) {
                if (LOG.isLoggable(Level.FINEST)) {
                    LOG.finest("HttpRequestDelegateListener progressed: "+id);
                }
                ByteBuffer payload = progressEvent.getPayload();
                dispatchEvent(new XoaEvent(id, XoaEventKind.PROGRESS, new Object[] { payload }));
            }
            
            @Override
            public void loaded(LoadEvent event) {
                if (LOG.isLoggable(Level.FINER)) {
                    LOG.finer("HttpRequestDelegateListener loaded: "+id);
                }
                ByteBuffer responseBuffer = (ByteBuffer)event.getResponseBuffer();
                dispatchEvent(new XoaEvent(id, XoaEventKind.LOAD, new Object[] { responseBuffer }));
            }
            
            @Override
            public void closed(CloseEvent event) {
                if (LOG.isLoggable(Level.FINE)) {
                    LOG.fine("HttpRequestDelegateListener closed: "+id);
                }
                try {
                    dispatchEvent(new XoaEvent(id, XoaEventKind.CLOSED, EMPTY_ARGS));
                }
                finally {
                    removeHandler(id);
                }
            }
            
            @Override
            public void errorOccurred(ErrorEvent event) {
                if (LOG.isLoggable(Level.FINE)) {
                    LOG.fine("HttpRequestDelegateListener errorOccurred");
                }
                try {
                    dispatchEvent(XoaEvent.createErrorEvent(id));
                }
                finally {
                    removeHandler(id);
                }
            }
        });
        
        return reqDelegate;
    }

    private WebSocketDelegate createWebSocketDelegate(final Integer id, String xoaUrlStr, String wsProtocols) {
        LOG.entering(CLASS_NAME, "createWebSocketDelegate", id);
        URI originUrl;
        try {
            // add version string
            xoaUrlStr += ((xoaUrlStr.indexOf("?") == -1) ? "?" : "&") + ".kv=10.05";

            URI xoaUrl = new URI(xoaUrlStr);
            originUrl = new URI(xOrigin);

            WebSocketDelegate wsDelegate = WEB_SOCKET_DELEGATE_FACTORY.createWebSocketDelegate(xoaUrl, originUrl, wsProtocols);
            wsDelegate.setListener(new WebSocketDelegateListener() {

                @Override
                public void authenticationRequested(AuthenticateEvent authenticateEvent) {
                    if (LOG.isLoggable(Level.FINE)) {
                        LOG.fine("WebSocketDelegateListener authenticationRequested: "+id);
                    }
                    String authenticate = authenticateEvent.getChallenge();
                    dispatchEvent(new XoaEvent(id, XoaEventKind.AUTHENTICATE, new String[] { authenticate }));
                }

                @Override
                public void opened(OpenEvent event) {
                    if (LOG.isLoggable(Level.FINE)) {
                        LOG.fine("WebSocketDelegateListener opened: "+id);
                    }
                    String protocol = event.getProtocol();
                    dispatchEvent(new XoaEvent(id, XoaEventKind.OPEN, new String[] { protocol }));
                }

                @Override
                public void redirected(RedirectEvent redirectEvent) {
                    if (LOG.isLoggable(Level.FINE)) {
                        LOG.fine("WebSocketDelegateListener redirected: "+id);
                    }
                    String location = redirectEvent.getLocation();
                    dispatchEvent(new XoaEvent(id, XoaEventKind.REDIRECT, new String[] { location }));
                }
                
                @Override
                public void messageReceived(MessageEvent messageEvent) {
                    if (LOG.isLoggable(Level.FINEST)) {
                        LOG.finest("WebSocketDelegateListener messageReceived: "+id);
                    }
                    ByteBuffer message = messageEvent.getData();
                    dispatchEvent(new XoaEvent(id, XoaEventKind.MESSAGE, new Object[] { message, messageEvent.getMessageType() }));
                }

                @Override
                public void closed(CloseEvent event) {
                    if (LOG.isLoggable(Level.FINE)) {
                        LOG.fine("WebSocketDelegateListener closed: "+id);
                    }
                    try {
                        dispatchEvent(new XoaEvent(id, XoaEventKind.CLOSED, EMPTY_ARGS));
                    }
                    finally {
                        removeHandler(id);
                    }
                }

                @Override
                public void errorOccurred(ErrorEvent event) {
                    if (LOG.isLoggable(Level.FINE)) {
                        LOG.fine("WebSocketDelegateListener errorOccurred: "+id);
                    }
                    try {
                        dispatchEvent(XoaEvent.createErrorEvent(id));
                    }
                    finally {
                        removeHandler(id);
                    }
                }
            });
            
            return wsDelegate;

        } catch (Exception e) {
            LOG.log(Level.FINE, "While processing the Java client bridge event queue: "+e.getMessage(), e);
            throw new RuntimeException("While processing the Java client bridge event queue: " + e);
        }
    }

    protected URL getCallerUrl() {
        return this.getClass().getResource("ValidateOrigin.class");
    }

    private boolean validateSameOrigin(String[] params) {
        LOG.entering(CLASS_NAME, "validateSameOrigin", (Object) params);

        try {
            Class<?> validationClass = this.getClass().getClassLoader().loadClass("org.kaazing.gateway.bridge.ValidateOrigin");
            int modifiers = validationClass.getModifiers();
            if (Modifier.isFinal(modifiers) && Modifier.isPrivate(modifiers)) {
                LOG.finest("Invalid modifiers");
                return false;
            }

            URL callerUrl = getCallerUrl();
            
            // jar:http://localhost:8000/demo/stomp.jar!/org/kaazing/gateway/bridge/ValidateOrigin.class
            String origin = callerUrl.toString();
            LOG.finest("Caller origin: " + origin);

            if (origin.startsWith("jar:")) {
                // URL like jar:http://www.foo.com/bar/baz.jar!/COM/foo/Quux.class
                origin = origin.substring(4, origin.indexOf("!"));
                URI originUri = new URI(origin);
                if (originUri.getScheme().equals("file")) {
                    origin = originUri.getScheme() + "://" + originUri.getAuthority();
                }
                else {
                    origin = originUri.getScheme() + "://" + getCanonicalHostPort(originUri);
                }
            }

            if (origin.startsWith("http:") || origin.startsWith("https:")) {
                // URL like [scheme:][//authority][path][?query][#fragment]
                URI originUri = new URI(origin);
                xOrigin = originUri.getScheme() + "://" + getCanonicalHostPort(originUri); 
            }
            else if (origin.startsWith("file:") ||
                     origin.startsWith("bundleresource:") ||   // Felix/Equinox
                     origin.startsWith("bundle:") ||           // Felix/Equinox
                     origin.startsWith("code-source:") ||      // OC4J
                     origin.startsWith("zip:") ||              // WebLogic
                     origin.startsWith("vfszip:") ||           // JBoss5
                     origin.startsWith("vfs:") ||              // JBoss6
                     origin.startsWith("vfsfile:") ||          // JBoss6
                     origin.startsWith("wsjar:")) {            // WebSphere
                try {
                    this.getClass().getClassLoader().getParent();
                    URI targetURI = new URI(params[1]);
                    xOrigin = "privileged://" + getCanonicalHostPort(targetURI);
                } catch (Exception e) {
                    LOG.log(Level.FINE, "Unable to determine source origin: "+e.getMessage(), e);
                    xOrigin = "null";
                }
            }
            else {
                LOG.severe("Unable to locate the origin of the parent class loader");
                throw new IllegalArgumentException("Unable to locate the origin of the parent class loader");
            }
            LOG.finest("Bridge: xOrigin: " + xOrigin);
        } catch (Exception e) {
            LOG.log(Level.FINE, "Unable to locate the origin validation class: "+e.getMessage(), e);
            return false;
        }

        // Start the event handler thread that processes all messages from the
        // same origin code
        xoaEventProcessor.start();
        return true;
    }

    private String getCanonicalHostPort(URI uri) {
        int port = uri.getPort();
        if (port == -1) {
            String scheme = uri.getScheme();
            if (scheme.equals("https") || scheme.equals("wss") || scheme.equals("wse+ssl")) {
                port = 443;
            }
            else {
                port = 80;
            }
        }
        return uri.getHost()+":"+port;
    }

    private void removeHandler(Integer handlerId) {
        if (xoaObjectMap.remove(handlerId) != null) {
            LOG.fine("Removed handler: "+handlerId);

            // If no more handlers are available in the object map, then stop the
            // event handler thread.
            if (xoaObjectMap.size() == 0) {
                LOG.fine("Stopping xoaEventProcessor");
                xoaEventProcessor.stop();
            }
        }
    }

    @Override
    public void handleEvent(XoaEvent ev) {
        LOG.entering(CLASS_NAME, "handleEvent", ev);
        
        if (ev.getEvent() == XoaEventKind.CREATE) {
            try {
                processCreateEvent(ev);
            } catch (Exception e) {
                LOG.log(Level.SEVERE, "While processing create event: "+e.getMessage(), e);
                throw new RuntimeException("While processing create event: "+e);
            }
        }
        else {
            Integer handlerId = ev.getHandlerId();
            BridgeDelegate delegate = xoaObjectMap.get(handlerId);

            if (delegate == null) {
                throw new IllegalArgumentException("Unable to locate delegate: " + handlerId);
            }
            else if (delegate instanceof WebSocketDelegate) {
                processEvent((WebSocketDelegate)delegate, ev);
            }
            else if (delegate instanceof HttpRequestDelegate) {
                processEvent((HttpRequestDelegate)delegate, ev);
            }
            else {
                String reason = "Unknown delegate: "+handlerId+" type: "+delegate.getClass().getName();
                LOG.severe(reason);
                throw new IllegalArgumentException(reason);
            }
        }
    }

    
    private void processCreateEvent(XoaEvent ev) throws Exception {
        Object[] params = ev.getParams();
        final Integer handlerId = ev.getHandlerId();
        String type = (String)params[0];
        
        if (type.equals(OBJECT_TYPE_WEBSOCKET)) {
            LOG.fine("Creating WebSocketDelegate: HandlerID: "+handlerId);
            
            String xoaUrlStr = (String) params[1];
            String wsProtocols = (params.length > 3) ? (String)params[2] : null;
            
            WebSocketDelegate wsDelegate = createWebSocketDelegate(handlerId, xoaUrlStr, wsProtocols == null? null: wsProtocols);
            xoaObjectMap.put(handlerId, wsDelegate);
            
            wsDelegate.processOpen();
        }
        else if (type.equals(OBJECT_TYPE_HTTPREQUEST)) {
            LOG.fine("Creating HttpRequestDelegate: HandlerID: "+handlerId);

            String xoaUrlStr = (String) params[1];
            String method = (String) params[2];
            String asyncText = (String) params[3];
            boolean async = asyncText.equals("Y");
            
            // add version string
            LOG.finest("xoaUrlStr = " + xoaUrlStr);
            xoaUrlStr += ((xoaUrlStr.indexOf("?") == -1) ? "?" : "&") + ".kv=10.05";

            validateUrl(xoaUrlStr);

            HttpRequestDelegate req = createHttpRequestDelegate(handlerId);
            xoaObjectMap.put(handlerId, req);

            req.processOpen(method, new URL(xoaUrlStr), xOrigin, async, 0);
        }
        else {
            LOG.severe("Unknown type " + type);
            throw new IllegalArgumentException("Unknown type : " + type + " passed to CrossOriginProxy");
        }
    }

    private void processEvent(WebSocketDelegate wsDelegate, XoaEvent ev) {
        LOG.entering(CLASS_NAME, "handleWebSocketEvent", ev);

        try {
            Object[] params = ev.getParams();
            
            switch (ev.getEvent()) {
            case AUTHORIZE:
                wsDelegate.processAuthorize((String) params[0]);
                break;
            
            case POSTMESSAGE:
                if (params[0] instanceof String) {
                    wsDelegate.processSend((String)params[0]);
                }
                else {
                    wsDelegate.processSend((ByteBuffer)params[0]);
                }
                break;
            
            case DISCONNECT:
                wsDelegate.processDisconnect();
                break;
            
            default:
                LOG.severe("Unknown event: "+ev.getEvent().toString());
                throw new IllegalArgumentException("Unknown event: "+ev.getEvent().toString());
            }
        }
        catch (IOException e) {
            LOG.severe(e.toString());
            throw new RuntimeException("While dispatching event "+ev.getEvent().name+" to WebsocketDelegate: " + e.getMessage(), e);
        }
    }

    private void processEvent(HttpRequestDelegate reqDelegate, XoaEvent ev) {
        LOG.entering(CLASS_NAME, "handleHttpRequestEvent", ev);

        try {
            Object[] params = ev.getParams();
    
            switch (ev.getEvent()) {
            case SETREQUESTHEADER:
                reqDelegate.setRequestHeader((String) params[0], (String) params[1]);
                break;
            
            case SEND:
                reqDelegate.processSend((ByteBuffer) params[0]);
                break;
            
            case ABORT:
                try {
                    reqDelegate.processAbort();
                }
                finally {
                    removeHandler(ev.getHandlerId());
                }
                break;
    
            default:
                LOG.severe("Unknown event: "+ev.getEvent().toString());
                throw new IllegalArgumentException("Unknown event: "+ev.getEvent().toString());
            }
        }
        catch (Exception e) {
            LOG.severe(e.toString());
            throw new RuntimeException("While dispatching event "+ev.getEvent().name+" to HttpRequestDelegate: " + e.getMessage(), e);
        }
    }

    private void validateUrl(String xoaUrlStr) throws URISyntaxException {
        URI xoaUri = new URI(xoaUrlStr);
        String scheme = xoaUri.getScheme();
        if (!scheme.equals("http") && !scheme.equals("https")) {
            LOG.severe("Invalid scheme: "+scheme);
            throw new IllegalArgumentException("Invalid scheme: "+scheme);
        }
    }
}
