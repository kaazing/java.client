/**
 * Copyright (c) 2007-2012, Kaazing Corporation. All rights reserved.
 */

package com.kaazing.gateway.amqp.client;

import java.net.URI;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EventListener;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.kaazing.gateway.amqp.client.ConnectionEvent.Kind;
import com.kaazing.gateway.amqp.client.impl.AmqpBuffer;
import com.kaazing.gateway.amqp.client.impl.AmqpFrame;
import com.kaazing.gateway.amqp.client.impl.AmqpMethod;
import com.kaazing.gateway.amqp.client.impl.AsyncClient;
import com.kaazing.gateway.amqp.client.impl.ClientChannelBehaviours;
import com.kaazing.gateway.amqp.client.impl.ClientStateMachineListener;
import com.kaazing.gateway.amqp.client.impl.Continuation;
import com.kaazing.gateway.amqp.client.impl.ErrorHandler;
import com.kaazing.gateway.amqp.client.impl.EventTargetSupport;
import com.kaazing.gateway.amqp.client.impl.MethodLookup;
import com.kaazing.gateway.amqp.client.impl.Rules;
import com.kaazing.gateway.amqp.client.impl.AmqpBuffer.Arg;
import com.kaazing.gateway.amqp.client.impl.AmqpBuffer.BasicProperties;
import com.kaazing.gateway.amqp.client.impl.Action;
import com.kaazing.gateway.client.html5.ByteBuffer;
import com.kaazing.gateway.client.html5.ByteSocket;
import com.kaazing.gateway.client.html5.ByteSocketAdapter;
import com.kaazing.gateway.client.html5.ByteSocketEvent;

/**
 * AmqpClient provides a WebSocket-based Java client API to communicate with any compatible AMQP server that supports AMQP 0.9-1 spec.
 * For more information on AMQP please refer to http://www.amqp.org
 */
public final class AmqpClient {
    /**
     * Default protocol header for 0-9-1:    'A', 'M, 'Q', 'P', 0, 0, 9, 1
     * For the releases as of May 20, 09:
     * To talk to openamq, use:              'A', 'M, 'Q', 'P', 0, 0, 9, 1
     * To talk to rabbit, use this instead:  'A', 'M, 'Q', 'P', 1, 1, 8, 0
     * To talk to qpid, use this instead:    'A', 'M, 'Q', 'P', 1, 1, 0, 9
     */
    private static final byte[] PROTOCOL_0_9_1_DEFAULT_HEADER = new byte[] {65, 77, 81, 80, 0, 0, 9, 1};
    private static final String AMQP = "AMQP";
    private static final ScheduledExecutorService SCHEDULER = new ScheduledThreadPoolExecutor(1);

    /**
     * Protocol header properties
     */
    static HashMap<String, byte[]> protocolHeader = new HashMap<String, byte[]>();

    HashMap<Integer, AmqpChannel>        channels = new HashMap<Integer, AmqpChannel>();
    ByteSocket                           websocket;
    ReadyState                           readyState;

    private AsyncClient                  asyncClient = new AsyncClient();
    private AmqpBuffer                   inBuffer = new AmqpBuffer();
    private String                       userName;
    private String                       password;
    private int                          channelCount = 0;
    private String                       virtualHost;
    private int                          id;
    private Boolean                      hasNegotiated;
    private String                       url;
    private int                          remaining, readFrameAt = 0;
    private EventTargetSupport           changes = new EventTargetSupport();
    private ClientStateMachineListener   csmListener =
                                           new ClientStateMachineListenerImpl(this);

    /**
     * Values are CONNECTING = 0, OPEN = 1 and CLOSED = 2;
     */
    public enum ReadyState {
        CONNECTING, OPEN, CLOSED;
    }

    /**
     * Creates a new AmqpClient with default options.
     */
    public AmqpClient() {
        this.init();
    }

    /**
     * Returns the current state of the AmqpClient
     *
     * @return ReadyState
     */
    public ReadyState getReadyState() {
        return readyState;
    }

    private void init() {
        asyncClient.initAsyncClient();
        asyncClient.getStateMachine().setClientStateMachineListener(csmListener);

        this.id = 0;
        Rules rule;
        List<Rules> list;

        rule = new Rules();
        list = new ArrayList<Rules>();
        String[] str = {"startConnectionFrame"};
        rule.inputs = str;
        rule.targetState = "starting";
        list.add(rule);
        rule = new Rules();
        String[] str7 = {"closeConnectionFrame"};
        rule.inputs = str7;
        rule.targetState = "closing";
        list.add(rule);
        asyncClient.getStateMachine().addState("handshaking", list, ClientChannelBehaviours.HANDSHAKE_START_HANDLER, ClientChannelBehaviours.DEFAULT_BEHAVIOR_HANDLER);

        rule = new Rules();
        list = new ArrayList<Rules>();
        String[] str1 = {"startOkConnectionAction"};
        rule.inputs = str1;
        rule.targetState = "started";
        list.add(rule);
        asyncClient.getStateMachine().addState("starting", list, ClientChannelBehaviours.STARTING_HANDLER, ClientChannelBehaviours.DEFAULT_BEHAVIOR_HANDLER);

        rule = new Rules();
        list = new ArrayList<Rules>();
        String[] str2 = {"tuneConnectionFrame"};
        rule.inputs = str2;
        rule.targetState = "tuning";
        list.add(rule);
        asyncClient.getStateMachine().addState("started", list, ClientChannelBehaviours.DEFAULT_BEHAVIOR_HANDLER, ClientChannelBehaviours.DEFAULT_BEHAVIOR_HANDLER);

        rule = new Rules();
        list = new ArrayList<Rules>();
        String[] str3 = {"tuneOkConnectionAction"};
        rule.inputs = str3;
        rule.targetState = "tuned";
        list.add(rule);
        asyncClient.getStateMachine().addState("tuning", list, ClientChannelBehaviours.TUNE_CONNECTION_HANDLER, ClientChannelBehaviours.ADVANCE_ACTIONS_HANDLER);

        rule = new Rules();
        list = new ArrayList<Rules>();
        String[] str4 = {"openConnectionAction"};
        rule.inputs = str4;
        rule.targetState = "opening";
        list.add(rule);
        asyncClient.getStateMachine().addState("tuned", list, ClientChannelBehaviours.DEFAULT_BEHAVIOR_HANDLER, ClientChannelBehaviours.DEFAULT_BEHAVIOR_HANDLER);

        rule = new Rules();
        list = new ArrayList<Rules>();
        String[] str5 = {"openOkConnectionFrame"};
        rule.inputs = str5;
        rule.targetState = "ready";
        list.add(rule);
        asyncClient.getStateMachine().addState("opening", list, ClientChannelBehaviours.REGISTER_SYNCHRONOUS_REQUEST, ClientChannelBehaviours.GENERIC_RESPONSE_HANDLER);

        rule = new Rules();
        list = new ArrayList<Rules>();
        String[] str6 = { // channel management
        "openOkChannelFrame", "closeChannelFrame", "closeOkChannelFrame",

        "flowOkChannelFrame", "flowChannelFrame",

        // queues and exchanges
                "declareOkExchangeFrame", "declareOkQueueFrame", "bindOkQueueFrame", "unbindOkQueueFrame", "deleteOkQueueFrame", "deleteOkExchangeFrame",

                // transactions
                "commitOkTxFrame", "rollbackOkTxFrame", "selectOkTxFrame",

                // browsing
                "purgeOkQueueFrame", "cancelOkBasicFrame", "getOkBasicFrame", "getEmptyBasicFrame", "consumeOkBasicFrame", "recoverOkBasicFrame", "rejectOkBasicFrame",

                // async message delivery
                "deliverBasicFrame", "bodyFrame", "headerFrame"};
        rule.inputs = str6;
        rule.targetState = "ready";
        list.add(rule);
        rule = new Rules();
        String[] str10 = {"closeConnectionFrame", "closeConnectionAction"};
        rule.inputs = str10;
        rule.targetState = "closing";
        list.add(rule);
        asyncClient.getStateMachine().addState("ready", list, ClientChannelBehaviours.IDLING_HANDLER, ClientChannelBehaviours.DEFAULT_BEHAVIOR_HANDLER);

        rule = new Rules();
        list = new ArrayList<Rules>();
        String[] str8 = {"closeOkConnectionFrame"};
        rule.targetState = "closed";
        rule.inputs = str8;
        list.add(rule);
        asyncClient.getStateMachine().addState("closing", list, ClientChannelBehaviours.GENERIC_RESPONSE_HANDLER, ClientChannelBehaviours.DEFAULT_BEHAVIOR_HANDLER);

        rule = new Rules();
        list = new ArrayList<Rules>();
        // Doesnt allow a null Object to be sent so NA is used.
        String[] str9 = {"NA"};
        rule.targetState = "";
        rule.inputs = str9;
        list.add(rule);
        asyncClient.getStateMachine().addState("closed", list, ClientChannelBehaviours.CLOSED_HANDLER, ClientChannelBehaviours.DEFAULT_BEHAVIOR_HANDLER);

        /* Initialize basic properties */
        ArrayList<BasicProperties> basicProperties = new ArrayList<BasicProperties>();

        BasicProperties properties;
        properties = new BasicProperties();
        properties.name = "contentType";
        properties.domain = "Shortstr";
        properties.label = "MIME content type";
        basicProperties.add(properties);

        properties = new BasicProperties();
        properties.name = "contentEncoding";
        properties.domain = "Shortstr";
        properties.label = "MIME content encoding";
        basicProperties.add(properties);

        properties = new BasicProperties();
        properties.name = "headers";
        properties.domain = "Table";
        properties.label = "message header field table";
        basicProperties.add(properties);

        properties = new BasicProperties();
        properties.name = "deliveryMode";
        properties.domain = "Octet";
        properties.label = "non-persistent (1) or persistent (2)";
        basicProperties.add(properties);

        properties = new BasicProperties();
        properties.name = "priority";
        properties.domain = "Octet";
        properties.label = "message priority, 0 to 9";
        basicProperties.add(properties);

        properties = new BasicProperties();
        properties.name = "correlationId";
        properties.domain = "Shortstr";
        properties.label = "application correlation identifier";
        basicProperties.add(properties);

        properties = new BasicProperties();
        properties.name = "replyTo";
        properties.domain = "Shortstr";
        properties.label = "address to reply to";
        basicProperties.add(properties);

        properties = new BasicProperties();
        properties.name = "expiration";
        properties.domain = "Shortstr";
        properties.label = "message expiration specification";
        basicProperties.add(properties);

        properties = new BasicProperties();
        properties.name = "messageId";
        properties.domain = "Shortstr";
        properties.label = "application message identifier";
        basicProperties.add(properties);

        properties = new BasicProperties();
        properties.name = "timestamp";
        properties.domain = "Timestamp";
        properties.label = "message timestamp";
        basicProperties.add(properties);

        properties = new BasicProperties();
        properties.name = "type";
        properties.domain = "Shortstr";
        properties.label = "message type name";
        basicProperties.add(properties);

        properties = new BasicProperties();
        properties.name = "userId";
        properties.domain = "Shortstr";
        properties.label = "creating user id";
        basicProperties.add(properties);

        properties = new BasicProperties();
        properties.name = "appId";
        properties.domain = "Shortstr";
        properties.label = "creating application id";
        basicProperties.add(properties);

        properties = new BasicProperties();
        properties.name = "reserved";
        properties.domain = "Shortstr";
        properties.label = "reserved, must be empty";
        basicProperties.add(properties);

        AmqpBuffer.basicProperties = basicProperties;

        if (protocolHeader.size() == 0) {
            protocolHeader.put("0-9-1", PROTOCOL_0_9_1_DEFAULT_HEADER);
        }
    }

    /**
     * Add a listener for ConnectionEvents
     * @param listener
     */
    public void addConnectionListener(ConnectionListener listener) {
        changes.addEventListener(AMQP, listener);
    }

    /**
     * Returns the list of ConnectionListeners registered with this AmqpClient instance.
     *
     * @return List<ConnectionListener>   list of ConnectionListeners
     */
    public List<ConnectionListener> getConnectionListeners() {
        if (changes == null) {
            return (List<ConnectionListener>)Collections.EMPTY_LIST;
        }

        List<EventListener> listeners = changes.getListenerList(AMQP);
        if ((listeners == null) || listeners.isEmpty()) {
            return (List<ConnectionListener>)Collections.EMPTY_LIST;
        }

        ArrayList<ConnectionListener> list = new ArrayList<ConnectionListener>();
        for (EventListener listener : listeners) {
            list.add((ConnectionListener)listener);
        }

        return list;
    }

    /**
     * Remove a listener for ConnectionEvents
     * @param listener
     */
    public void removeConnectionListener(ConnectionListener listener) {
        changes.removeEventListener(AMQP, listener);
    }

    /**
     * Connect to AMQP Broker via Kaazing Gateway
     *
     * @param url           Location of AMQP Broker or Server
     * @param virtualHost   Name of the virtual host
     * @param username      Username to connect to the AMQP Server
     * @param password      Password to connect to the AMQP Server
     */
    public void connect(String url, String virtualHost, String username, String password) {
        if (websocket != null && websocket.getReadyState() != ByteSocket.ReadyState.CLOSED) {
            throw new IllegalStateException("AmqpClient already connected");
        }
        this.readyState = ReadyState.CONNECTING;
        this.url = url;
        this.userName = username;
        this.password = password;
        this.virtualHost = virtualHost;
        this.hasNegotiated = false;

        asyncClient.getStateMachine().enterState("handshaking", "", this.url);
    }

    /**
     * Disconnects from Amqp Server.
     */
    public void disconnect() {
        if (readyState == ReadyState.OPEN) {
            // readyState should be set to ReadyState.CLOSED ONLY AFTER the
            // the WebSocket has been successfully closed in
            // socketClosedHandler().
            this.closeConnection(0, "", 0, 0, null, null);
        } else if (readyState == ReadyState.CONNECTING) {
            socketClosedHandler();
            websocket = null;
        }
    }

    /**
     * Opens a channel on server.
     *
     * @return AmqpChannel
     */
    public AmqpChannel openChannel() {
        int id = ++this.channelCount;
        AmqpChannel chan = new AmqpChannel(id, this);
        channels.put(id, chan);
        return chan;
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////
    // State Behaviour. //make function private
    //
    // //////////////////////////////////////////////////////////////////////////////////////////////

    void defaultBehaviorHandler(Object context, String input, Object args, String stateName) {
    }

    void handshakeStartHandler(Object context, String input, Object args, String stateName) {
        try {

            this.websocket = new ByteSocket();
            websocket.addByteSocketListener(new ByteSocketAdapter() {

                @Override
                public void onClose(ByteSocketEvent closedEvent) {
                    socketClosedHandler();
                }

                @Override
                public synchronized void onMessage(ByteSocketEvent messageEvent) {
                    socketMessageHandler(messageEvent.getData());
                }

                @Override
                public void onOpen(ByteSocketEvent openEvent) {
                    socketOpenHandler();
                }

            });
            websocket.connect(new URI(args.toString()));
        }
        catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    void startingHandler(Object context, String input, Object args, String stateName) {
        AmqpBuffer buf = new AmqpBuffer();
        AmqpArguments clientProperties = new AmqpArguments();
        clientProperties.addLongString("library", "KaazingAmqpClient");
        clientProperties.addLongString("library_version", "3.3.0");
        clientProperties.addLongString("library_platform", "Java");
        String mechanism = "AMQPLAIN";

        String username = this.userName;
        String password = this.password;

        String response = buf.encodeAuthAmqPlain(username, password);
        String locale = "en_US";

        this.startOkConnection(clientProperties, mechanism, response, locale);
    }

    void tuneConnectionHandler(Object context, String input, Object args, String stateName) {
        AmqpFrame frame1 = new AmqpFrame();
        frame1 = (AmqpFrame)args;

        int channelmax = (Integer)frame1.getArgs()[0].value;
        long framemax = (Long)frame1.getArgs()[1].value;
        int heartbeat = (Integer)frame1.getArgs()[2].value;

        this.tuneOkConnection((int)channelmax, (int)framemax, (int)heartbeat, null, null);
        this.openConnection(virtualHost, null, null);
    }

    void genericResponseHandler(Object context, String input, Object f, String stateName) {
        if (f instanceof Action) {
            return;
        }

        if (input == "nowaitAction") {
            asyncClient.setWaitingAction(null);
            return;
        }

        Action action = (Action)asyncClient.getWaitingAction();
        if (action.actionName != null) {
            AmqpFrame frame = (AmqpFrame)f;
            int channel = frame.getChannelId();
            if (channel == 0) {
                // connection event
                if (frame.getMethodName() == "openOkConnection") {
                    readyState = ReadyState.OPEN;
                    ConnectionEvent e = new ConnectionEvent(this, Kind.OPEN, frame.getArgs());
                    fireOnOpen(e);
                    if (action.continuation != null) {
                        action.continuation.onCompleted(e);
                    }
                }
                else if (frame.getMethodName() == "closeConnection") {
                    // The broker is closing the connection. This might be due to an
                	// application error. So, we will fire the error event.
                    ConnectionEvent event = new ConnectionEvent(this, Kind.ERROR, frame.getArgs());
                    fireOnError(event);

                    // And, when we close the ByteSocket in closedHandler(), we will
                    // be notified when the socket was really closed via
                    // socketClosedHandler() in which we can fire the close
                    // event by invoking ConnectionListener.onConnectionClose().
                    closedHandler(null, null, null, null);

                    // This will be a no-op as we no longer support Continuation
                    // in Java.
                    if (action.continuation != null) {
                        ConnectionEvent e = new ConnectionEvent(this, Kind.CLOSE);
                        action.continuation.onCompleted(e);
                    }
                }
            }
        }
    }

    void advanceActionsHandler(Object context, String input, Object frame, String stateName) {
        asyncClient.processActions();
    }

    void idlingHandler(Object context, String input, Object frame, String stateName) {
        AmqpFrame frame1 = (AmqpFrame)frame;
        if (frame1.getChannelId() == 0) {
        }
        else if (this.channels.containsKey((int)frame1.getChannelId())) {
            // dispatch frame to the channel's io layer
            AmqpChannel channel = this.channels.get((int)frame1.getChannelId());
            this.channelReadHandler(channel, input, frame);
        }
        else {
            // error and closing connection.
            throw new IllegalStateException("Error and closing connection in IdlingHandler");
        }
    }

    // This method is invoked on the way back when AmqpClient.disconnect() is 
    // invoked. We should close the channels ensuring that the registered
    // ChannelListener(s) get the CLOSE event. And, then we should ensure that
    // the ConnectionListener(s) on AmqpClient object get the CLOSE event. We
    // should also close the WebSocket, if it is not closed. Eventually, this
    // should be the order following in all our client implementations.
    void closedHandler(Object context, String input, Object frame, String stateName) {
        if (getReadyState() == ReadyState.CLOSED) {
            return;
        }
        
        // TODO: Determine whether channels need to be cleaned up.
        if (this.channels.size() != 0) {
            for (int i = 1; i <= this.channels.size(); i++) { // 1-based
                AmqpChannel channel = this.channels.get(i);
                AmqpFrame   f = new AmqpFrame();
                
                // Close each channel which should result in CLOSE event
                // to be fired for each of the registered ChannelListener(s).
                f.setMethodName("closeChannel");
                f.setChannelId((short) i);
                channel.channelClosedHandler(this, "", f, "closeChannel");
            }
        }
        
        try {
            // Mark the readyState to be CLOSED.
            readyState = ReadyState.CLOSED;
            
            // Fire CLOSE event for the ConnectionListener(s) registered
            // on AmqpClient object.
            fireOnClosed(new ConnectionEvent(this, Kind.CLOSE));

            // If the WebSocket(or ByteSocket) is still not closed, then 
            // invoke the close() method. The callback in this case will be a
            // a no-op as we have completed the book-keeping here.
            if (websocket.getReadyState() != ByteSocket.ReadyState.CLOSED) {
                websocket.close();
            }
        }
        catch (Exception e1) {
            throw new IllegalStateException(e1);
        }

    }

    private void socketOpenHandler() {
        byte[] header = protocolHeader.get("0-9-1");

        ByteBuffer headerbuf = ByteBuffer.allocate(512);
        headerbuf.putBytes(header);
        headerbuf.flip();
        try {
            this.websocket.send(headerbuf);
        }
        catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    // Invoked from ByteSocketListener(or ByteSocketAdapeter)'s onClose()
    // method. In this method, we do the necessary book-keeping such as
    // closing the channels, raising events, and such.
    private void socketClosedHandler() {
        // If the book-keeping has already been completed earlier, then
        // we should just bail.
        if (getReadyState() == ReadyState.CLOSED) {
            return;
        }
        
        // TODO: Determine whether channels need to be cleaned up
        if (this.channels.size() != 0) {
            // Iterate over the channels and close them.
            for (int i = 1; i <= this.channels.size(); i++) { // 1-based
                AmqpChannel channel = this.channels.get(i);
                AmqpFrame   f = new AmqpFrame();
                
                // Close the channel and ensure that the CLOSE event is
                // fired on the registered ChannelListener objects.
                f.setMethodName("closeChannel");
                f.setChannelId((short) i);
                channel.channelClosedHandler(this, "", f, "closeChannel");
            }
        }
        
        // Mark the readyState as CLOSED.
        readyState = ReadyState.CLOSED;

        // Fire the CLOSE event on the ConnectionListener(s) registered on
        // the AmqpClient object.
        fireOnClosed(new ConnectionEvent(this, Kind.CLOSE));
    }

    private boolean compareStringToBuffer(String s, AmqpBuffer b) {
        if (b.remaining() < s.length()) {
            return false;
        }
        else {
            int limit = b.limit();
            b.limit(s.length());
            String bufferString = b.getString(Charset.forName("UTF-8"));
            b.limit(limit);
            return s == bufferString;
        }
    }

    private void socketMessageHandler(ByteBuffer data) {
        inBuffer.putBuffer(data);
        inBuffer.limit(inBuffer.position());
        inBuffer.position(readFrameAt);
        inBuffer.mark();

        if (!this.hasNegotiated && inBuffer.remaining() > 7) {
            if (compareStringToBuffer("AMQP", inBuffer)) {
                List<Arg> val = new ArrayList<Arg>();

                byte[] serverVersion = {inBuffer.get(), inBuffer.get(), inBuffer.get(), inBuffer.get()};

                Arg arg = inBuffer.new Arg();
                arg.name = "replyText";
                arg.value = "Server supports no version of the AMQP protocol after " + serverVersion[2] + "-" + serverVersion[3];
                val.add(arg);

                ConnectionEvent event = new ConnectionEvent(this, Kind.ERROR, (Arg[])val.toArray());
                fireOnError(event);

                // And, then we will close the ByteSocket. We will be
                // notified when the socket is really closed via
                // socketClosedHandler() in which we can fire the close
                // event by invoking ConnectionListener.onConnectionClose().
                closedHandler(null, null, null, null);
                return;
            }
            else {
                inBuffer.reset();
                this.hasNegotiated = true;
            }
        }
        AmqpFrame frame = null;

        while ((frame = inBuffer.getFrame()) != null) {
            AmqpFrame f = frame;
            String frameName = f.getMethodName() + "Frame";

            asyncClient.getStateMachine().feedInput(frameName, f);
        }

        remaining = inBuffer.remaining();
        inBuffer.compact();
        readFrameAt = inBuffer.position() - remaining;
    }

    private void sendFrame(AmqpBuffer buffer) {
        try {
            ReadyState readyState = getReadyState();
            if (readyState != ReadyState.CLOSED) {
                this.websocket.send(buffer);
            }
        }
        catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    void write(Object client, Object[] arg) {
        AmqpMethod amqpMethod = new AmqpMethod();
        int channel;
        Object[] args = {};
        amqpMethod = (AmqpMethod)arg[1];

        channel = Integer.parseInt(arg[2].toString());
        args = (Object[])arg[3];

        AmqpBuffer buf = new AmqpBuffer();
        buf.putMethodFrame(amqpMethod, (short)channel, args);
        buf.flip();
        sendFrame(buf);
    }

    /**
     * Sends a StartOkConnection to server.
     *
     * @param clientProperties
     * @param mechanism
     * @param response
     * @param locale
     * @return AmqpClient
     */
    AmqpClient startOkConnection(AmqpArguments clientProperties, String mechanism, String response, String locale) {
        this.startOkConnection(clientProperties, mechanism, response, locale, null, null);
        return this;
    }

    /**
     * Sends a StartOkConnection to server.
     *
     * @param clientProperties
     * @param mechanism
     * @param response
     * @param locale
     * @param callback
     * @param error
     * @return AmqpClient
     */
    AmqpClient startOkConnection(AmqpArguments clientProperties, String mechanism, String response, String locale, Continuation callback, ErrorHandler error) {
        Object[] args = {clientProperties, mechanism, response, locale};
        AmqpBuffer bodyArg = null;

        String methodName = "startOkConnection";
        String methodId = "10" + "11";
        AmqpMethod amqpMethod = MethodLookup.LookupMethod(methodId);
        Object[] arguments = {this, amqpMethod, 0, args, bodyArg};
        asyncClient.enqueueAction(methodName, "write", arguments, callback, error);
        return this;
    }

    /**
     * Sends a TuneOkConnection to server.
     *
     * @param channelMax
     * @param frameMax
     * @param heartbeat
     * @return AmqpClient
     */
    AmqpClient tuneOkConnection(int channelMax, int frameMax, int heartbeat) {
        this.tuneOkConnection(channelMax, frameMax, heartbeat, null, null);
        return this;
    }

    /**
     * Sends a TuneOkConnection to server.
     *
     * @param channelMax
     * @param frameMax
     * @param heartbeat
     * @param callback
     * @param error
     * @return AmqpClient
     */
    AmqpClient tuneOkConnection(int channelMax, int frameMax, int heartbeat, Continuation callback, ErrorHandler error) {
        Object[] args = {channelMax, frameMax, heartbeat};
        AmqpBuffer bodyArg = null;

        String methodName = "tuneOkConnection";
        String methodId = "10" + "31";
        AmqpMethod amqpMethod = MethodLookup.LookupMethod(methodId);
        Object[] arguments = {this, amqpMethod, this.id, args, bodyArg};
        asyncClient.enqueueAction(methodName, "write", arguments, callback, error);
        return this;
    }

    /**
     * Opens a connection on AMQP server.
     *
     * @param virtualHost
     * @return AmqpClient
     */
    AmqpClient openConnection(String virtualHost) {
        this.openConnection(virtualHost, null, null);
        return this;
    }

    /**
     * Opens a connection on AMQP server.
     *
     * @param virtualHost
     * @param callback
     * @param error
     * @return AmqpClient
     */
    AmqpClient openConnection(String virtualHost, Continuation callback, ErrorHandler error) {
        // "", false are reserved1 and reserved2.
        Object[] args = {virtualHost, "", false};
        AmqpBuffer bodyArg = null;

        String methodName = "openConnection";
        String methodId = "10" + "40";
        AmqpMethod amqpMethod = MethodLookup.LookupMethod(methodId);
        Object[] arguments = {this, amqpMethod, this.id, args, bodyArg};
        asyncClient.enqueueAction(methodName, "write", arguments, callback, error);
        return this;
    }

    /**
     * Closes the Amqp Server connection.
     *
     * @param replyCode
     * @param replyText
     * @param classId
     * @param methodId1
     * @return AmqpClient
     */
    AmqpClient closeConnection(int replyCode, String replyText, int classId, int methodId1) {
        this.closeConnection(replyCode, replyText, classId, methodId1, null, null);
        return this;
    }

    /**
     * Closes the Amqp Server connection.
     *
     * @param replyCode
     * @param replyText
     * @param classId
     * @param methodId1
     * @param callback
     * @param error
     * @return AmqpClient
     */
    AmqpClient closeConnection(int replyCode, String replyText, int classId, int methodId1, Continuation callback, ErrorHandler error) {
        Object[] args = {replyCode, replyText, classId, methodId1};
        AmqpBuffer bodyArg = null;

        String methodName = "closeConnection";
        String methodId = "10" + "50";
        AmqpMethod amqpMethod = MethodLookup.LookupMethod(methodId);
        Object[] arguments = {this, amqpMethod, this.id, args, bodyArg};
        asyncClient.enqueueAction(methodName, "write", arguments, callback, error);
        return this;
    }

    /**
     * Sends a CloseOkConnection to server.
     *
     * @return AmqpClient
     */
    AmqpClient closeOkConnection() {
        this.closeOkConnection(null, null);
        return this;
    }

    /**
     * Sends a CloseOkConnection to server.
     *
     * @param callback
     * @param error
     * @return AmqpClient
     */
    AmqpClient closeOkConnection(Continuation callback, ErrorHandler error) {
        Object[] args = {};
        AmqpBuffer bodyArg = null;

        String methodName = "closeOkConnection";
        String methodId = "10" + "50";
        AmqpMethod amqpMethod = MethodLookup.LookupMethod(methodId);
        Object[] arguments = {this, amqpMethod, this.id, args, bodyArg};
        asyncClient.enqueueAction(methodName, "write", arguments, callback, error);
        return this;
    }

    void channelReadHandler(AmqpChannel channel, String input, Object frame) {
        // Feed input state machine.
        channel.feedInput(input, frame);
    }

    /**
     * registerSynchronousRequest puts the client into a waiting state that will be able to call the continuation for a method that expects a particular
     * synchronous response This also lets us call the error cb when there is a close frame (which AMQP uses to raise exceptions) with a reason why the last
     * command failed.
     */
    void registerSynchronousRequest(Object context, String input, Object frame, String stateName) {
        Action action = (Action)frame;
        AmqpMethod amqpMethod = (AmqpMethod)action.args[1];
        if (amqpMethod.synchronous) {
            asyncClient.setWaitingAction(action);
        }
        else {
            // method is not synchronous
        }
    }

    /**
     * Occurs when the connection to the AMQP server is closed
     *
     * @param e
     */
    private void fireOnClosed(ConnectionEvent e) {
        List<EventListener> listeners = changes.getListenerList(AMQP);
        for (EventListener listener : listeners) {
            ConnectionListener amqpListener = (ConnectionListener)listener;
            amqpListener.onConnectionClose(e);
        }
    }

    /**
     * Occurs when the AMQP server/broker reports an issue with the application code
     *
     * @param e
     */
    void fireOnError(ConnectionEvent e) {
        List<EventListener> listeners = changes.getListenerList(AMQP);
        for (EventListener listener : listeners) {
            ConnectionListener amqpListener = (ConnectionListener)listener;
            amqpListener.onConnectionError(e);
        }
    }

    /**
     * Occurs when the connection is established to the AMQP server
     *
     * @param e
     */
    void fireOnOpen(ConnectionEvent e) {
        List<EventListener> listeners = changes.getListenerList(AMQP);
        for (EventListener listener : listeners) {
            ConnectionListener amqpListener = (ConnectionListener)listener;
            amqpListener.onConnectionOpen(e);
        }
    }

    /**
     * Occurs when the connection is established to the AMQP server
     *
     * @param e
     */
    void fireOnConnecting(ConnectionEvent e) {
        List<EventListener> listeners = changes.getListenerList(AMQP);
        for (EventListener listener : listeners) {
            ConnectionListener amqpListener = (ConnectionListener)listener;
            amqpListener.onConnecting(e);
        }
    }

    class ActionObject {
        public AmqpChannel channel;
        public String methodName;
        public Object[] args;
    }

    class ClientStateMachineListenerImpl implements ClientStateMachineListener
    {
        private AmqpClient     _client;

        ClientStateMachineListenerImpl(AmqpClient client) {
            _client = client;
        }

        @Override
        public void closeConnection(Object context,
                                    String input,
                                    Object args,
                                    String stateName) {
          _client.closedHandler(context, input, args, stateName);
        }

        @Override
        public void createAndSendFrame(Object context, Object[] args) {
            _client.write(context, args);
        }

        @Override
        public void handleGenericResponse(Object context,
                                          String input,
                                          Object args, String stateName) {
            _client.genericResponseHandler(context, input, args, stateName);
        }

        @Override
        public void handleIdling(Object context,
                                 String input,
                                 Object args,
                                 String stateName) {
            _client.idlingHandler(context, input, args, stateName);
        }

        @Override
        public AmqpClient getClient()
        {
            return _client;
        }

        @Override
        public void startHandshake(Object context,
                                   String input,
                                   Object args,
                                   String stateName) {
            _client.handshakeStartHandler(context, input, args, stateName);
        }

        @Override
        public void performDefaultBehavior(Object context,
                                           String input,
                                           Object args,
                                           String stateName) {
            _client.defaultBehaviorHandler(context, input, args, stateName);
        }

        @Override
        public void performAdvancedActions(Object context,
                                           String input,
                                           Object args,
                                           String stateName) {
            _client.advanceActionsHandler(context, input, args, stateName);
        }

        @Override
        public void registerSynchronousRequest(Object context,
                                               String input,
                                               Object args,
                                               String stateName) {
            _client.registerSynchronousRequest(context, input, args, stateName);
        }

        @Override
        public void startConnection(Object context,
                                    String input,
                                    Object args,
                                    String stateName) {
            _client.startingHandler(context, input, args, stateName);
        }

        @Override
        public void tuneConnection(Object context,
                                   String input,
                                   Object args,
                                   String stateName) {
            _client.tuneConnectionHandler(context, input, args, stateName);
        }

    }
}
