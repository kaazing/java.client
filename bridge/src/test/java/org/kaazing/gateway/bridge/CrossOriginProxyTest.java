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
import java.net.URI;
import java.net.URL;
import java.nio.ByteBuffer;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.api.Invocation;
import org.jmock.lib.action.CustomAction;
import org.junit.Test;
import org.kaazing.gateway.client.transport.http.HttpRequestDelegate;
import org.kaazing.gateway.client.transport.http.HttpRequestDelegateFactory;
import org.kaazing.gateway.client.transport.http.HttpRequestDelegateListener;
import org.kaazing.gateway.client.transport.ws.WebSocketDelegate;
import org.kaazing.gateway.client.transport.ws.WebSocketDelegateFactory;

public class CrossOriginProxyTest {

    protected static final int HANDLER_ID = 7482;
    private static String SOA_MESSAGE = "soaMessage";
    private static String XOP_MESSAGE = "xopMessage";

    @Test
    public void testCrossOriginProxyRequest() throws Exception {
        
        Mockery context = new Mockery();
        final WebSocketDelegate webSocketDelegate = context.mock(WebSocketDelegate.class);
        final HttpRequestDelegate httpRequestDelegate = context.mock(HttpRequestDelegate.class);
        final PropertyChangeListener propertyChangeListener = context.mock(PropertyChangeListener.class);

        final CrossOriginProxy xop = new CrossOriginProxy();

        context.checking(new Expectations() {
            {
                oneOf(propertyChangeListener).propertyChange(with(any(PropertyChangeEvent.class)));
                will(new CustomAction("OPEN") {

                    @Override
                    public Object invoke(Invocation invocation) throws Throwable {
                        PropertyChangeEvent event = (PropertyChangeEvent)invocation.getParameter(0);
                        Object params[] = (Object[])event.getNewValue();
                        
                        System.out.println(params[0].toString());
                        System.out.println(params[1].toString());
                        
                        if (params[0].toString().equals(Integer.toString(HANDLER_ID)) &&
                            params[1].toString().equals("open")) {
                            
                            System.out.println("OPEN EVENT RECEIVED");
                            Integer handlerId = new Integer(HANDLER_ID);
                            Object[] sendMessage = {
                                handlerId,
                                "send",
                                new Object[] { ByteBuffer.allocate(0), ByteBuffer.allocate(0) }
                            };
                            xop.firePropertyChange(SOA_MESSAGE, null, sendMessage);
                        }
                        else if (params[0].toString().equals(Integer.toString(HANDLER_ID)) &&
                                 params[1].toString().equals("progress")) {
                                
                            System.out.println("PROGRESS EVENT RECEIVED");
                            ByteBuffer buf = (ByteBuffer)((Object[])params[2])[0];
                            
                            while (buf.hasRemaining()) {
                                System.out.print((char)buf.get());
                            }
                        }
                        else if (params[0].toString().equals(Integer.toString(HANDLER_ID)) &&
                                 params[1].toString().equals("load")) {
                                
                            System.out.println("LOAD EVENT RECEIVED");
                        }
                        
                        return null;
                    }
                });
                
                oneOf(httpRequestDelegate).setListener(with(any(HttpRequestDelegateListener.class)));
                oneOf(httpRequestDelegate).processOpen("GET", new URL("http://localhost:8001/echo/;e/cb?.kv=10.05"), "privileged://localhost:8001", true, 0);
                will(new CustomAction("OPEN") {
                    @Override
                    public Object invoke(Invocation invocation) throws Throwable {
                        Integer handlerId = new Integer(HANDLER_ID);
                        Object[] openMessage = {
                            handlerId,
                            "open",
                            new Object[] {}
                        };
                        xop.firePropertyChange(SOA_MESSAGE, null, openMessage);
                        return null;
                    }
                });
            }
        });

        xop.WEB_SOCKET_DELEGATE_FACTORY = new WebSocketDelegateFactory() {
            @Override
            public WebSocketDelegate createWebSocketDelegate(URI xoaUrl, URI originUrl, String wsProtocol) {
                return webSocketDelegate;
            }
        };
        
        xop.HTTP_REQUEST_DELEGATE_FACTORY = new HttpRequestDelegateFactory() {
            @Override
            public HttpRequestDelegate createHttpRequestDelegate() {
                return httpRequestDelegate;
            }
        };
        
        xop.addPropertyChangeListener(XOP_MESSAGE, propertyChangeListener);
        
        Integer handlerId = new Integer(HANDLER_ID);
        Object[] createMessage = {
            handlerId,
            "create",
            new String[] { handlerId.toString(), "HTTPREQUEST", "http://localhost:8001/echo/;e/cb", "GET", "Y", "" }
        };
        xop.firePropertyChange(SOA_MESSAGE, null, createMessage);
        
    }
}
