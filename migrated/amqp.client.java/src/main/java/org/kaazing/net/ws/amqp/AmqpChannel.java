/**
 * Copyright (c) 2007-2014 Kaazing Corporation. All rights reserved.
 * 
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.kaazing.net.ws.amqp;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EventListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.kaazing.net.ws.amqp.AmqpClient.ReadyState;
import org.kaazing.net.ws.amqp.ChannelEvent.Kind;
import org.kaazing.net.ws.amqp.ChannelEvent.MethodName;
import org.kaazing.net.ws.amqp.impl.Action;
import org.kaazing.net.ws.amqp.impl.AmqpBuffer;
import org.kaazing.net.ws.amqp.impl.AmqpFrame;
import org.kaazing.net.ws.amqp.impl.AmqpMethod;
import org.kaazing.net.ws.amqp.impl.AsyncClient;
import org.kaazing.net.ws.amqp.impl.ChannelStateMachineListener;
import org.kaazing.net.ws.amqp.impl.ClientChannelBehaviours;
import org.kaazing.net.ws.amqp.impl.Continuation;
import org.kaazing.net.ws.amqp.impl.ErrorHandler;
import org.kaazing.net.ws.amqp.impl.EventTargetSupport;
import org.kaazing.net.ws.amqp.impl.MethodLookup;
import org.kaazing.net.ws.amqp.impl.Rules;
import org.kaazing.net.ws.amqp.impl.AmqpBuffer.Arg;

import org.kaazing.gateway.client.util.WrappedByteBuffer;

/**
 * AMQP Channel AmqpChannel represents the channel created on the AMQP connection to the server. Declaring of exchanges, queues, binding queues to exchanges,
 * sending and receiving messages and transactions are handled using methods on this class.
 */

public final class AmqpChannel {

    private static final String AMQP = "AMQPCHANNEL";
    private static final String ZOMBIE_CHANNEL = "This channel has been closed " +
      " and is no longer associated with any AmqpClient object. Please create a " +
      " new instance of AmqpChannel using AmqpClient.openChannel() method.";
    
    AsyncClient        asyncClient = new AsyncClient();

    private EventTargetSupport          changes = new EventTargetSupport();
    private boolean                     isFlowOn = true;
    private int                         id = 0;
    private AmqpClient                  client = null;
    private AmqpFrame                   headerFrame = new AmqpFrame();
    private ChannelStateMachineListener chsmListener = 
                                       new ChannelStateMachineListenerImpl(this);
    private ReadyState                  readyState;
    
    /**
     * Creates a new AmqpChannel instance.
     * 
     * @param channelId
     * @param client
     * @param callback
     * @param error
     */
    AmqpChannel(int channelId, AmqpClient client) {

        this.client = client;
        this.id = channelId;
        // transaction state
        // _transacted = false;
        // type of response the client is waiting for
        // _waitingFor = null;

        asyncClient.initAsyncClient();

        this.init(null, null);
    }
    
    boolean feedInput(String input, Object frame) {
        AmqpFrame    f = (AmqpFrame) frame;

        // Check whether there are any errors that will need the channel
        // to be closed down.
        if (f.getMethodName().startsWith("closeChannel")) {
            AmqpFrame    errFrame = new AmqpFrame();
            errFrame.setMethodName("error");
            errFrame.setArgs(f.getArgs());
     
            List<ChannelListener> channelListeners = getChannelListeners();
            ChannelEvent          closeEvent = new ChannelEvent(this,
                                                                ChannelEvent.Kind.CLOSE,
                                                                null);
            ChannelEvent          errEvent = new ChannelEvent(this, errFrame);
            
            // Since all errors are hard-errors, which means that the channel has
            // been closed by the AMQP Broker. Fire the error event and the close
            // event for the channel by invoking  ChannelListener.onError() and 
            // ChannelListener.onClose() methods.
            for (ChannelListener listener : channelListeners) {
                listener.onError(errEvent);
                listener.onClose(closeEvent);
            }

            return false;
        }

        return asyncClient.getStateMachine().feedInput(input, frame);
    }

    /**
     * Adds the specified ChannelListener allowing the application to listen
     * to the channel's lifecycle events.
     * 
     * @param listener    ChannelListener to be added to receive events
     */
    public void addChannelListener(ChannelListener listener) {
        changes.addEventListener(AMQP, listener);
    }

    /**
     * Returns the Client/Connection that is used to multiplex the channel.
     * @return
     */
    public AmqpClient getAmqpClient() {
        return client;
    }
    
    /**
     * Returns the list of ChannelListeners registered with this AmqpChannel instance.
     * 
     * @return List<ChannelListener>   list of ChannelListeners
     */
    public List<ChannelListener> getChannelListeners() {
        if (changes == null) {
            return (List<ChannelListener>)Collections.EMPTY_LIST;
        }

        List<EventListener> listeners = changes.getListenerList(AMQP);
        if ((listeners == null) || listeners.isEmpty()) {
            return (List<ChannelListener>)Collections.EMPTY_LIST;
        }

        ArrayList<ChannelListener> list = new ArrayList<ChannelListener>();
        for (EventListener listener : listeners) {
            list.add((ChannelListener)listener);
        }

        return list;
    }

    /**
     * Removes the specified ChannelListener.
     * 
     * @param listener    ChannelListener to be deleted
     */
    public void removeChannelListener(ChannelListener listener) {
        changes.removeEventListener(AMQP, listener);
    }

    private Map<String, Object> argsToMap(Arg[] args) {
        if (args == null) {
            return (Map<String, Object>)Collections.EMPTY_MAP;
        }

        Map<String, Object> map = new HashMap<String, Object>();
        for (int i = 0; i < args.length; i++) {
            map.put(args[i].name, args[i].value);
        }

        return map;
    }
    
    private void init(Continuation callback, ErrorHandler error) {
        asyncClient.getStateMachine().setChannelStateMachineListener(chsmListener);

        Rules rule;
        List<Rules> list;

        rule = new Rules();
        list = new ArrayList<Rules>();
        // transition to a waiting state (synchronous requests)
        String[] str1 = {"openChannelAction", "closeChannelAction", "consumeBasicAction", "flowChannelAction", "declareExchangeAction", "declareQueueAction", "bindQueueAction",
                "unbindQueueAction", "deleteQueueAction", "deleteExchangeAction", "purgeQueueAction", "cancelBasicAction", "selectTxAction", "commitTxAction", "rollbackTxAction",};
        rule.inputs = str1;
        rule.targetState = "waiting";
        list.add(rule);

        // perform asynchronous action and stay in the ready state..
        rule = new Rules();
        String[] str2 = {"publishBasicAction", "ackBasicAction"};
        rule.inputs = str2;
        rule.targetState = "channelReady";
        list.add(rule);

        rule = new Rules();
        String[] str14 = {"getBasicAction"};
        rule.inputs = str14;
        rule.targetState = "getting";
        list.add(rule);

        // start reading a delivered message.
        rule = new Rules();
        String[] str3 = {"deliverBasicFrame"};
        rule.inputs = str3;
        rule.targetState = "readingContentHeader";
        list.add(rule);
        asyncClient.getStateMachine().addState("channelReady", list, ClientChannelBehaviours.ADVANCE_ACTIONS_CHANNEL_HANDLER, ClientChannelBehaviours.TX_CHECKING_HANDLER);

        list = new ArrayList<Rules>();
        rule = new Rules();
        String[] str11 = {"getOkBasicFrame"};
        rule.inputs = str11;
        rule.targetState = "readingContentHeader";
        list.add(rule);
        rule = new Rules();
        String[] str12 = {"getEmptyBasicFrame"};
        rule.inputs = str12;
        rule.targetState = "channelReady";
        list.add(rule);
        rule = new Rules();
        String[] str13 = {"closeChannelFrame"};
        rule.inputs = str13;
        rule.targetState = "closing";
        list.add(rule);
        asyncClient.getStateMachine().addState("getting", list, ClientChannelBehaviours.CHANNEL_REGISTER_SYNCHRONOUS_REQUEST, ClientChannelBehaviours.GET_EMPTY_RESPONSE_HANDLER);

        rule = new Rules();
        list = new ArrayList<Rules>();
        String[] str4 = { // channel management
        "openOkChannelFrame", "closeOkChannelFrame", "flowOkChannelFrame",
                // queues and exchanges
                "declareOkExchangeFrame", "declareOkQueueFrame", "bindOkQueueFrame", "unbindOkQueueFrame", "deleteOkQueueFrame", "deleteOkExchangeFrame", "purgeOkQueueFrame",
                "cancelOkBasicFrame",
                // transactions
                "commitOkTxFrame", "rollbackOkTxFrame", "selectOkTxFrame",
                // browsing

                "getOkBasicFrame", "getEmptyBasicFrame", "consumeOkBasicFrame",

                // sometimes (nowait) we dont want to return anything
                "nowaitAction"};
        rule.inputs = str4;
        rule.targetState = "channelReady";
        list.add(rule);
        asyncClient.getStateMachine()
                .addState("waiting", list, ClientChannelBehaviours.CHANNEL_REGISTER_SYNCHRONOUS_REQUEST, ClientChannelBehaviours.CHANNEL_GENERIC_RESPONSE_HANDLER);

        rule = new Rules();
        list = new ArrayList<Rules>();
        String[] str5 = {"headerFrame"};
        rule.inputs = str5;
        rule.targetState = "readingContentBody";
        list.add(rule);
        asyncClient.getStateMachine().addState("readingContentHeader", list, ClientChannelBehaviours.MESSAGE_DELIVERY_HANDLER, ClientChannelBehaviours.CONTENT_HEADER_HANDLER);

        rule = new Rules();
        list = new ArrayList<Rules>();
        String[] str6 = {"bodyFrame"};
        rule.inputs = str6;
        rule.targetState = "channelReady";
        list.add(rule);
        asyncClient.getStateMachine().addState("readingContentBody", list, ClientChannelBehaviours.DEFAULT_BEHAVIOR_HANDLER, ClientChannelBehaviours.MESSAGE_BODY_HANDLER);

        rule = new Rules();
        list = new ArrayList<Rules>();
        String[] str9 = {"closeOkAction"};
        rule.targetState = "closed";
        rule.inputs = str9;
        list.add(rule);
        asyncClient.getStateMachine().addState("closing", list, ClientChannelBehaviours.GENERIC_ERROR_HANDLER, ClientChannelBehaviours.DEFAULT_BEHAVIOR_HANDLER);

        rule = new Rules();
        list = new ArrayList<Rules>();
        // Doesn't allow a null object to be sent so na is used.
        String[] str10 = {"NT"};
        rule.targetState = "";
        rule.inputs = str10;
        list.add(rule);
        asyncClient.getStateMachine().addState("closed", list, ClientChannelBehaviours.DEFAULT_BEHAVIOR_HANDLER, ClientChannelBehaviours.CHANNEL_CLOSED_HANDLER);

        // _asyncClient._stateMachine.enterState("channelReady", "", null);
        if (client.getReadyState() == ReadyState.OPEN) {
            this.openChannel();
        }
    }

    void defaultBehaviorHandler(Object context, String input, Object args, String stateName) {
    }

    void txCheckingHandler(Object context, String input, Object args, String stateName) {
    }

    /**
     * Creates a Channel to the AMQP server on the given clients connection
     * 
     * @return AmqpChannel
     */
    AmqpChannel openChannel() {
        if (readyState == ReadyState.OPEN) {
            // If the channel is already open, just bail.
            return this;
        }
        
        // try {
            Object[] args = {""};
            WrappedByteBuffer bodyArg = null;
            HashMap<String, Object> headersArg = null;

            String methodName = "openChannel";
            String methodId = "20" + "10";
            AmqpMethod amqpMethod = MethodLookup.LookupMethod(methodId);
            Object[] arguments = {this, amqpMethod, this.id, args, bodyArg, headersArg};

            asyncClient.getStateMachine().enterState("channelReady", "", null);

            asyncClient.enqueueAction(methodName, "channelWrite", arguments, null, null);
            readyState = ReadyState.CONNECTING;
        /*}
        catch (Exception ex) {
            if (errorHandler != null) {
                AmqpEvent e = new ChannelEvent(this, Kind.ERROR, ex.getMessage());
                errorHandler.error(e);
            }
        }
        */
        return this;
    }

    /**
     * This method asks the peer to pause or restart the flow of content data sent by a consumer. This is a simple flow-control mechanism that a peer can use to
     * avoid overflowing its queues or otherwise finding itself receiving more messages than it can process.
     * 
     * @param enabled
     *            If true, the peer starts sending content frames, else the peer stops sending content frames.
     */

    public AmqpChannel flowChannel(boolean active) {
        isFlowOn = active;
        Object[] args = {active};
        WrappedByteBuffer bodyArg = null;
        HashMap<String, Object> headersArg = null;

        String methodId = "20" + "20";
        String methodName = "flowChannel";
        AmqpMethod amqpMethod = MethodLookup.LookupMethod(methodId);;
        Object[] arguments = {this, amqpMethod, this.id, args, bodyArg, headersArg};

        if (client.getReadyState() == ReadyState.OPEN) {
            asyncClient.enqueueAction(methodName, "channelWrite", arguments, null, null);
        }
        return this;
    }

    /**
     * Confirms to the peer that a flow command was received and processed.
     * 
     * @param active
     * @param callback
     * @param error
     * @return AmqpChannel
     */
    public AmqpChannel flowOkChannel(boolean active) {
        Object[] args = {active};
        WrappedByteBuffer bodyArg = null;
        HashMap<String, Object> headersArg = null;

        String methodName = "flowOkChannel";
        String methodId = "20" + "21";
        AmqpMethod amqpMethod = MethodLookup.LookupMethod(methodId);
        Object[] arguments = {this, amqpMethod, this.id, args, bodyArg, headersArg};
        asyncClient.enqueueAction(methodName, "channelWrite", arguments, null, null);
        return this;
    }

    /**
     * This method indicates that the sender wants to close the channel.
     * 
     * @param replyCode
     * @param replyText
     * @param classId
     * @param methodId1
     * @return AmqpChannel
     */
    public AmqpChannel closeChannel(int replyCode, String replyText, int classId, int methodId1) {
        if (readyState == ReadyState.CLOSED) {
            return this;
        }
        
        Object[] args = {replyCode, replyText, classId, methodId1};
        WrappedByteBuffer bodyArg = null;
        HashMap<String, Object> headersArg = null;

        String methodName = "closeChannel";
        String methodId = "20" + "40";
        AmqpMethod amqpMethod = MethodLookup.LookupMethod(methodId);
        Object[] arguments = {this, amqpMethod, this.id, args, bodyArg, headersArg};
        asyncClient.enqueueAction(methodName, "channelWrite", arguments, null, null);
        return this;
    }

    /**
     * Confirms to the peer that a flow command was received and processed.
     * 
     * @return AmqpChannel
     */
    private AmqpChannel closeOkChannel() {
        if (readyState == ReadyState.CLOSED) {
            // If the channel has been closed, just bail.
            return this;
        }
        
        Object[] args = {};
        WrappedByteBuffer bodyArg = null;
        HashMap<String, Object> headersArg = null;

        String methodName = "closeOkChannel";
        String methodId = "20" + "41";
        AmqpMethod amqpMethod = MethodLookup.LookupMethod(methodId);;
        Object[] arguments = {this, amqpMethod, this.id, args, bodyArg, headersArg};
        asyncClient.enqueueAction(methodName, "channelWrite", arguments, null, null);
        return this;
    }

    /**
     * This method creates an exchange if it does not already exist, and if the exchange exists, verifies that it is of the correct and expected class.
     * 
     * @param exchange
     * @param type
     * @param passive
     * @param durable
     * @param noWait
     * @param arguments
     * @return AmqpChannel
     */
    public AmqpChannel declareExchange(String exchange, String type, boolean passive, boolean durable, boolean noWait, AmqpArguments arguments) {
        Object[] args = {0, exchange, type, passive, durable, false, false, noWait, arguments};
        WrappedByteBuffer bodyArg = null;
        HashMap<String, Object> headersArg = null;

        String methodName = "declareExchange";
        String methodId = "40" + "10";
        AmqpMethod amqpMethod = MethodLookup.LookupMethod(methodId);
        Object[] methodArguments = {this, amqpMethod, this.id, args, bodyArg, headersArg};

        boolean hasnowait = false;
        for (int i = 0; i < amqpMethod.allParameters.size(); i++) {
            String argname = amqpMethod.allParameters.get(i).name;
            if (argname == "noWait") {
                hasnowait = true;
                break;
            }
        }

        if (client.getReadyState() == ReadyState.OPEN) {
            asyncClient.enqueueAction(methodName, "channelWrite", methodArguments, null, null);
        }

        if (hasnowait && noWait) {
            asyncClient.enqueueAction("nowait", null, null, null, null);
        }
        return this;
    }

    /**
     * This method deletes an exchange. When an exchange is deleted all queue bindings on the exchange are canceled.
     * 
     * @param exchange
     * @param ifUnused
     * @param noWait
     * @return AmqpChannel
     */
    public AmqpChannel deleteExchange(String exchange, boolean ifUnused, boolean noWait) {
        Object[] args = {0, exchange, ifUnused, noWait};
        WrappedByteBuffer bodyArg = null;
        HashMap<String, Object> headersArg = null;

        String methodName = "deleteExchange";
        String methodId = "40" + "20";
        AmqpMethod amqpMethod = MethodLookup.LookupMethod(methodId);
        Object[] arguments = {this, amqpMethod, this.id, args, bodyArg, headersArg};

        boolean hasnowait = false;
        for (int i = 0; i < amqpMethod.allParameters.size(); i++) {
            String argname = amqpMethod.allParameters.get(i).name;
            if (argname == "noWait") {
                hasnowait = true;
                break;
            }
        }
        asyncClient.enqueueAction(methodName, "channelWrite", arguments, null, null);

        if (hasnowait && noWait) {
            asyncClient.enqueueAction("nowait", null, null, null, null);
        }
        
        return this;
    }

    /**
     * This method creates or checks a queue. When creating a new queue the client can specify various properties that control the durability of the queue and
     * its contents, and the level of sharing for the queue.
     * 
     * @param queue
     * @param passive
     * @param durable
     * @param exclusive
     * @param autoDelete
     * @param noWait
     * @param arguments
     * @return AmqpChannel
     */
    public AmqpChannel declareQueue(String queue, boolean passive, boolean durable, boolean exclusive, boolean autoDelete, boolean noWait, AmqpArguments arguments) {
        Object[] args = {0, queue, passive, durable, exclusive, autoDelete, noWait, arguments};
        WrappedByteBuffer bodyArg = null;
        HashMap<String, Object> headersArg = null;

        String methodName = "declareQueue";
        String methodId = "50" + "10";
        AmqpMethod amqpMethod = MethodLookup.LookupMethod(methodId);
        Object[] methodArguments = {this, amqpMethod, this.id, args, bodyArg, headersArg};

        boolean hasnowait = false;
        for (int i = 0; i < amqpMethod.allParameters.size(); i++) {
            String argname = amqpMethod.allParameters.get(i).name;
            if (argname == "noWait") {
                hasnowait = true;
                break;
            }
        }

        if (client.getReadyState() == ReadyState.OPEN) {
            asyncClient.enqueueAction(methodName, "channelWrite", methodArguments, null, null);
        }

        if (hasnowait && noWait) {
            asyncClient.enqueueAction("nowait", null, null, null, null);
        }
        
        return this;
    }

    /**
     * This method deletes a queue. When a queue is deleted any pending messages are sent to a dead-letter queue if this is defined in the server configuration,
     * and all consumers on the queue are canceled.
     * 
     * @param queue
     * @param ifUnused
     * @param ifEmpty
     * @param noWait
     * @return AmqpChannel
     */
    public AmqpChannel deleteQueue(String queue, boolean ifUnused, boolean ifEmpty, boolean noWait) {
        Object[] args = {0, queue, ifUnused, ifEmpty, noWait};
        WrappedByteBuffer bodyArg = null;
        HashMap<String, Object> headersArg = null;

        String methodName = "deleteQueue";
        String methodId = "50" + "40";
        AmqpMethod amqpMethod = MethodLookup.LookupMethod(methodId);
        Object[] arguments = {this, amqpMethod, this.id, args, bodyArg, headersArg};

        boolean hasnowait = false;
        for (int i = 0; i < amqpMethod.allParameters.size(); i++) {
            String argname = amqpMethod.allParameters.get(i).name;
            if (argname == "noWait") {
                hasnowait = true;
                break;
            }
        }

        asyncClient.enqueueAction(methodName, "channelWrite", arguments, null, null);

        if (hasnowait && noWait) {
            asyncClient.enqueueAction("nowait", null, null, null, null);

        }
        return this;
    }


    /**
     * QUEUE methods
     */

    /**
     * * This method binds a queue to an exchange. Until a queue is bound it will not receive any messages. In a classic messaging model, store-and-forward
     * queues are bound to a direct exchange and subscription queues are bound to a topic exchange.
     */
    public AmqpChannel bindQueue(String queue, String exchange, String routingKey, boolean noWait, AmqpArguments arguments) {
        Object[] args = {0, queue, exchange, routingKey, noWait, arguments};
        WrappedByteBuffer bodyArg = null;
        HashMap<String, Object> headersArg = null;

        String methodName = "bindQueue";
        String methodId = "50" + "20";
        AmqpMethod amqpMethod = MethodLookup.LookupMethod(methodId);
        Object[] methodArguments = {this, amqpMethod, this.id, args, bodyArg, headersArg};

        boolean hasnowait = false;
        for (int i = 0; i < amqpMethod.allParameters.size(); i++) {
            String argname = amqpMethod.allParameters.get(i).name;
            if (argname == "noWait") {
                hasnowait = true;
                break;
            }
        }

        if (client.getReadyState() == ReadyState.OPEN) {
            asyncClient.enqueueAction(methodName, "channelWrite", methodArguments, null, null);
        }

        if (hasnowait && noWait) {
            asyncClient.enqueueAction("nowait", null, null, null, null);
        }
        return this;
    }

    /**
     * This method unbinds a queue from an exchange.
     * 
     * @param queue
     * @param exchange
     * @param routingKey
     * @param arguments
     * @return AmqpChannel
     */
    public AmqpChannel unbindQueue(String queue, String exchange, String routingKey, AmqpArguments arguments) {
        Object[] args = {0, queue, exchange, routingKey, arguments};
        WrappedByteBuffer bodyArg = null;
        HashMap<String, Object> headersArg = null;

        String methodName = "unbindQueue";
        String methodId = "50" + "50";
        AmqpMethod amqpMethod = MethodLookup.LookupMethod(methodId);
        Object[] methodArguments = {this, amqpMethod, this.id, args, bodyArg, headersArg};
        asyncClient.enqueueAction(methodName, "channelWrite", methodArguments, null, null);
        return this;
    }

    /**
     * This method removes all messages from a queue which are not awaiting acknowledgment.
     * 
     * @param queue
     * @param noWait
     * @return AmqpChannel
     */
    public AmqpChannel purgeQueue(String queue, boolean noWait) {
        Object[] args = {0, queue, noWait};
        WrappedByteBuffer bodyArg = null;
        HashMap<String, Object> headersArg = null;

        String methodName = "purgeQueue";
        String methodId = "50" + "30";
        AmqpMethod amqpMethod = MethodLookup.LookupMethod(methodId);
        Object[] arguments = {this, amqpMethod, this.id, args, bodyArg, headersArg};

        boolean hasnowait = false;
        for (int i = 0; i < amqpMethod.allParameters.size(); i++) {
            String argname = amqpMethod.allParameters.get(i).name;
            if (argname == "noWait") {
                hasnowait = true;
                break;
            }
        }
        asyncClient.enqueueAction(methodName, "channelWrite", arguments, null, null);

        if (hasnowait && noWait) {
            asyncClient.enqueueAction("nowait", null, null, null, null);

        }
        return this;
    }

    
    /**
     * BASIC Methods
     */

    /**
     * This method requests a specific quality of service. The QoS can be specified for the current channel or for all channels on the connection. The
     * particular properties and semantics of a qos method always depend on the content class semantics. Though the qos method could in principle apply to both
     * peers, it is currently meaningful only for the server.
     * 
     * @param prefetchSize
     * @param prefetchCount
     * @param global
     */
    public AmqpChannel qosBasic(int prefetchSize, int prefetchCount, boolean global) {
        Object[] args = {prefetchSize, prefetchCount, global};
        WrappedByteBuffer bodyArg = null;
        HashMap<String, Object> headersArg = null;

        String methodName = "qosBasic";
        String methodId = "60" + "10";
        AmqpMethod amqpMethod = MethodLookup.LookupMethod(methodId);
        Object[] arguments = {this, amqpMethod, this.id, args, bodyArg, headersArg};
        asyncClient.enqueueAction(methodName, "channelWrite", arguments, null, null);
        return this;
    }

    /**
     * This method asks the server to start a "consumer", which is a transient request for messages from a specific queue. Consumers last as long as the channel
     * they were declared on, or until the client cancels them.
     * 
     * @param queue
     * @param consumerTag
     * @param noLocal
     * @param noAck
     * @param exclusive
     * @param noWait
     * @param arguments
     * @return AmqpChannel
     */
    public AmqpChannel consumeBasic(String queue, String consumerTag, boolean noLocal, boolean noAck, boolean exclusive, boolean noWait, AmqpArguments arguments) {
        Object[] args = {0, queue, consumerTag, noLocal, noAck, exclusive, noWait, arguments};
        WrappedByteBuffer bodyArg = null;
        HashMap<String, Object> headersArg = null;

        String methodName = "consumeBasic";
        String methodId = "60" + "20";
        AmqpMethod amqpMethod = MethodLookup.LookupMethod(methodId);
        Object[] methodArguments = {this, amqpMethod, this.id, args, bodyArg, headersArg};

        boolean hasnowait = false;
        for (int i = 0; i < amqpMethod.allParameters.size(); i++) {
            String argname = amqpMethod.allParameters.get(i).name;
            if (argname == "noWait") {
                hasnowait = true;
                break;
            }
        }

        if (client.getReadyState() == ReadyState.OPEN) {
            asyncClient.enqueueAction(methodName, "channelWrite", methodArguments, null, null);
        }

        if (hasnowait && noWait) {
            asyncClient.enqueueAction("nowait", null, null, null, null);

        }
        return this;
    }

    /**
     * This method cancels a consumer. This does not affect already delivered messages, but it does mean the server will not send any more messages for that
     * consumer. The client may receive an arbitrary number of messages in between sending the cancel method and receiving the cancel-ok reply.
     * 
     * @param consumerTag
     * @param noWait
     * @return AmqpChannel
     */
    public AmqpChannel cancelBasic(String consumerTag, boolean noWait) {
        Object[] args = {consumerTag, noWait};
        WrappedByteBuffer bodyArg = null;
        HashMap<String, Object> headersArg = null;

        String methodName = "cancelBasic";
        String methodId = "60" + "30";
        AmqpMethod amqpMethod = MethodLookup.LookupMethod(methodId);
        Object[] arguments = {this, amqpMethod, this.id, args, bodyArg, headersArg};

        boolean hasnowait = false;
        for (int i = 0; i < amqpMethod.allParameters.size(); i++) {
            String argname = amqpMethod.allParameters.get(i).name;
            if (argname == "noWait") {
                hasnowait = true;
                break;
            }
        }
        asyncClient.enqueueAction(methodName, "channelWrite", arguments, null, null);

        if (hasnowait && noWait) {
            asyncClient.enqueueAction("nowait", null, null, null, null);

        }
        return this;
    }

    /*
     * This method publishes a message to a specific exchange. The message will be routed to queues as defined by the exchange configuration and distributed to
     * any active consumers when the transaction, if any, is committed.
     * 
     * @param body
     * @param headers
     * @param exchange
     * @param routingKey
     * @param mandatory
     * @param immediate
     * @return AmqpChannel
     * 
     */
    private AmqpChannel publishBasic(ByteBuffer body, HashMap<String, Object> headers, String exchange, String routingKey, boolean mandatory, boolean immediate) {
        Object[] args = {0, exchange, routingKey, mandatory, immediate};
        WrappedByteBuffer bodyArg = null;
        if (body != null) {
            bodyArg = new WrappedByteBuffer(body);
        }
        HashMap<String, Object> headersArg = new HashMap<String, Object>();
        headersArg = headers;

        String methodName = "publishBasic";
        String methodId = "60" + "40";
        AmqpMethod amqpMethod = MethodLookup.LookupMethod(methodId);
        Object[] arguments = {this, amqpMethod, this.id, args, bodyArg, headersArg};

        asyncClient.enqueueAction(methodName, "channelWrite", arguments, null, null);

        return this;
    }

    /**
     * This method publishes a message to a specific exchange. The message will be routed to queues as defined by the exchange configuration and distributed to
     * any active consumers when the transaction, if any, is committed.
     * 
     * @param body
     * @param headers
     * @param exchange
     * @param routingKey
     * @param mandatory
     * @param immediate
     * @return AmqpChannel
     */
    public AmqpChannel publishBasic(ByteBuffer body, AmqpProperties properties, String exchange, String routingKey, boolean mandatory, boolean immediate) {
        @SuppressWarnings("unchecked")
        Map<String, Object> amqpProps = (Map<String, Object>)Collections.EMPTY_MAP;
        if (properties != null) {
            amqpProps = properties.getProperties();
        }
        
        HashMap<String, Object> props = (HashMap<String, Object>)amqpProps;
        return publishBasic(body, props, exchange, routingKey, mandatory, immediate);
    }
    
    /**
     * Gets messages from the queue and dispatches it to the listener
     * 
     * @param queue
     * @param noAck
     * @return AmqpChannel
     */
    public AmqpChannel getBasic(String queue, boolean noAck) {
        Object[] args = {0, queue, noAck};
        WrappedByteBuffer bodyArg = null;
        HashMap<String, Object> headersArg = null;

        String methodName = "getBasic";
        String methodId = "60" + "70";
        AmqpMethod amqpMethod = MethodLookup.LookupMethod(methodId);
        Object[] arguments = {this, amqpMethod, this.id, args, bodyArg, headersArg};
        asyncClient.enqueueAction(methodName, "channelWrite", arguments, null, null);
        return this;
    }

    /**
     * This method acknowledges one or more messages delivered via the Deliver or Get-Ok methods. The client can ask to confirm a single message or a set of
     * messages up to and including a specific message.
     * 
     * @param deliveryTag
     * @param multiple
     * @return AmqpChannel
     */
    public AmqpChannel ackBasic(long deliveryTag, boolean multiple) {
        Object[] args = {deliveryTag, multiple};
        WrappedByteBuffer bodyArg = null;
        HashMap<String, Object> headersArg = null;

        String methodName = "ackBasic";
        String methodId = "60" + "80";
        AmqpMethod amqpMethod = MethodLookup.LookupMethod(methodId);;
        Object[] arguments = {this, amqpMethod, this.id, args, bodyArg, headersArg};
        asyncClient.enqueueAction(methodName, "channelWrite", arguments, null, null);
        return this;
    }

    /**
     * This method allows a client to reject a message. It can be used to interrupt and cancel large incoming messages, or return untreatable messages to their
     * original queue.
     * 
     * @param deliveryTag
     * @param requeue
     * @return AmqpChannel
     */
    public AmqpChannel rejectBasic(long deliveryTag, boolean requeue) {
        Object[] args = {deliveryTag, requeue};
        WrappedByteBuffer bodyArg = null;
        HashMap<String, Object> headersArg = null;

        String methodName = "rejectBasic";
        String methodId = "60" + "90";
        AmqpMethod amqpMethod = MethodLookup.LookupMethod(methodId);

        Object[] arguments = {this, amqpMethod, this.id, args, bodyArg, headersArg};
        asyncClient.enqueueAction(methodName, "channelWrite", arguments, null, null);
        return this;
    }

    /**
     * This method asks the server to redeliver all unacknowledged messages on a specified channel. Zero or more messages may be redelivered. This method
     * replaces the asynchronous Recover.
     * 
     * @param requeue
     * @return AmqpChannel
     */
    public AmqpChannel recoverBasic(boolean requeue) {
        Object[] args = {requeue};
        WrappedByteBuffer bodyArg = null;
        HashMap<String, Object> headersArg = null;

        String methodName = "recoverBasic";
        String methodId = "60" + "110";
        AmqpMethod amqpMethod = MethodLookup.LookupMethod(methodId);
        Object[] arguments = {this, amqpMethod, this.id, args, bodyArg, headersArg};
        asyncClient.enqueueAction(methodName, "channelWrite", arguments, null, null);
        return this;
    }

    /**
     * TX related methods
     */

    /**
     * This method sets the channel to use standard transactions. The client must use this method at least once on a channel before using the Commit or Rollback
     * methods.
     */
    public AmqpChannel selectTx() {
        Object[] args = {};
        WrappedByteBuffer bodyArg = null;
        HashMap<String, Object> headersArg = null;

        String methodName = "selectTx";
        String methodId = "90" + "10";
        AmqpMethod amqpMethod = MethodLookup.LookupMethod(methodId);
        Object[] arguments = {this, amqpMethod, this.id, args, bodyArg, headersArg};
        asyncClient.enqueueAction(methodName, "channelWrite", arguments, null, null);
        return this;
    }

    /**
     * This method commits all message publications and acknowledgments performed in the current transaction. A new transaction starts immediately after a
     * commit.
     * 
     * @return AmqpChannel
     */
    public AmqpChannel commitTx() {
        Object[] args = {};
        WrappedByteBuffer bodyArg = null;
        HashMap<String, Object> headersArg = null;

        String methodName = "commitTx";
        String methodId = "90" + "20";
        AmqpMethod amqpMethod = MethodLookup.LookupMethod(methodId);
        Object[] arguments = {this, amqpMethod, this.id, args, bodyArg, headersArg};
        asyncClient.enqueueAction(methodName, "channelWrite", arguments, null, null);
        return this;
    }

    /**
     * This method abandons all message publications and acknowledgments performed in the current transaction. A new transaction starts immediately after a
     * rollback. Note that unacked messages will not be automatically redelivered by rollback; if that is required an explicit recover call should be issued.
     * 
     * @return AmqpChannel
     */
    public AmqpChannel rollbackTx() {
        Object[] args = {};
        WrappedByteBuffer bodyArg = null;
        HashMap<String, Object> headersArg = null;

        String methodName = "rollbackTx";
        String methodId = "90" + "30";
        AmqpMethod amqpMethod = MethodLookup.LookupMethod(methodId);
        Object[] arguments = {this, amqpMethod, this.id, args, bodyArg, headersArg};
        asyncClient.enqueueAction(methodName, "channelWrite", arguments, null, null);
        return this;
    }

    void channelGenericResponseHandler(Object context, String input, Object fr, String stateName) {
        AmqpFrame frame = (AmqpFrame)fr;
        ChannelEvent event = new ChannelEvent(this.client.channels.get(frame.getChannelId()), frame);

        Action action = (Action)asyncClient.getWaitingAction();
        Continuation con = action.continuation;

        if (action.actionName != null) {
            dispatchEvent(event, con);
        }
    }

    private void dispatchEvent(ChannelEvent event, Continuation con) {
        switch (event.getMethodName()) {
        case OPENOKCHANNEL:
            fireOnOpen(event);
            dispatchContinuation(MethodName.OPENOKCHANNEL, con, event);
            break;
        case DECLAREOKEXCHANGE:
            fireOnDeclareExchange(event);
            dispatchContinuation(MethodName.DECLAREOKEXCHANGE, con, event);
            break;
        case DECLAREOKQUEUE:
            fireOnDeclareQueue(event);
            dispatchContinuation(MethodName.DECLAREOKQUEUE, con, event);
            break;
        case BINDOKQUEUE:
            fireOnBindQueue(event);
            dispatchContinuation(MethodName.BINDOKQUEUE, con, event);
            break;
        case CONSUMEOKBASIC:
            fireOnConsumeBasic(event);
            dispatchContinuation(MethodName.CONSUMEOKBASIC, con, event);
            break;
        case COMMITOKTX:
            fireOnCommitTransaction(event);
            dispatchContinuation(MethodName.COMMITOKTX, con, event);
            break;
        case ROLLBACKOKTX:
            fireOnRollbackTransaction(event);
            dispatchContinuation(MethodName.ROLLBACKOKTX, con, event);
            break;
        case SELECTOKTX:
            fireOnSelectTransaction(event);
            dispatchContinuation(MethodName.SELECTOKTX, con, event);
            break;
        case BODY:
            fireOnMessage(event);
            dispatchContinuation(MethodName.BODY, con, event);
            break;
        case FLOWOKCHANNEL:
            fireOnFlow(event);
            dispatchContinuation(MethodName.FLOWOKCHANNEL, con, event);
            break;
        case CLOSEOKCHANNEL:
            fireOnClosed(event);
            dispatchContinuation(MethodName.CLOSEOKCHANNEL, con, event);
            break;
        case UNBINDOKQUEUE:
            fireOnUnbind(event);
            dispatchContinuation(MethodName.UNBINDOKQUEUE, con, event);
            break;
        case DELETEOKQUEUE:
            fireOnDeleteQueue(event);
            dispatchContinuation(MethodName.DELETEOKQUEUE, con, event);
            break;
        case DELETEOKEXCHANGE:
            fireOnDeleteExchange(event);
            dispatchContinuation(MethodName.DELETEOKEXCHANGE, con, event);
            break;
        case CANCELOKBASIC:
            fireOnCancelBasic(event);
            dispatchContinuation(MethodName.CANCELOKBASIC, con, event);
            break;
        case GETOKBASIC:
            fireOnGetBasic(event);
            dispatchContinuation(MethodName.GETOKBASIC, con, event);
            break;
        case PURGEOKQUEUE:
            fireOnPurgeQueue(event);
            dispatchContinuation(MethodName.PURGEOKQUEUE, con, event);
            break;
        default:
            throw new IllegalStateException("Channel GenericResponseHandler:- frame.methodName not found.");
        }
    }

    private void dispatchContinuation(MethodName methodName, Continuation continuation, AmqpEvent e) {
        if (continuation != null) {
            switch (methodName) {
            case DECLAREOKEXCHANGE:
            case DECLAREOKQUEUE:
            case BINDOKQUEUE:
            case CONSUMEOKBASIC:
            case COMMITOKTX:
            case ROLLBACKOKTX:
            case SELECTOKTX:
            case FLOWOKCHANNEL:
                continuation.onCompleted(e);
                break;
                
            case OPENOKCHANNEL:
            case BODY:
            case CLOSEOKCHANNEL:
            case UNBINDOKQUEUE:
            case DELETEOKQUEUE:
            case DELETEOKEXCHANGE:
            case CANCELOKBASIC:
            case GETOKBASIC:
            case PURGEOKQUEUE:
                // Existing code ignored continuations
                break;
                
            default:
                throw new IllegalStateException("Inside Channel dispatchContinuation");
            }
        }
    }

    void getEmptyResponseHandler(Object context, String input, Object frame, String stateName) {
        AmqpFrame newFrame = (AmqpFrame)frame;

        Action action = (Action)asyncClient.getWaitingAction();
        if (action.actionName != null) {
            asyncClient.setWaitingAction(null);
            if (input == "closeChannelFrame") {
                ChannelEvent event = new ChannelEvent(this.client.channels.get(newFrame.getChannelId()), newFrame);
                this.fireOnClosed(event);
                if (action.error != null) {
                    action.error.error(event);
                }
            }
        }
    }

    void advanceActionsChannelHandler(Object context, String input, Object frame, String stateName) {
        asyncClient.processActions();
    }

    void channelWrite(Object context, Object[] arg) {
        AmqpBuffer buf = new AmqpBuffer();
        AmqpMethod amqpMethod = new AmqpMethod();
        HashMap<String, Object> headers = new HashMap<String, Object>();
        int channel;
        Object[] args = {};
        WrappedByteBuffer body;
        amqpMethod = (AmqpMethod)arg[1];
        Object client = arg[0];
        channel = (Integer)arg[2];
        args = (Object[])arg[3];
        body = (WrappedByteBuffer)arg[4];
        headers = (HashMap<String, Object>)arg[5];
        short classIndex = amqpMethod.classIndex;
        buf.putMethodFrame(amqpMethod, (short)channel, args);
        
        if (amqpMethod.hasContent) {
            short weight = 0;
            buf.putHeaderFrame((short)channel, classIndex, weight, body.remaining(), headers);
            buf.putBodyFrame((short)channel, body);
        }
        buf.flip();
        sendFrame(client, buf);
    }

    private void sendFrame(Object client, AmqpBuffer buffer) {
        try {
            if (this.client.getReadyState() == ReadyState.OPEN) {
                this.client.send(buffer);
            }
        }
        catch (Exception e) {
            // TODO Auto-generated catch block
            throw new IllegalStateException("Exception in _SENDFRAME- " + e.getMessage());
        }
    }

    // -------------------------------------------------------------
    // Handler Methods
    // -------------------------------------------------------------
    void deliveryHandler(Object context, String input, Object frame, String stateName) {
        headerFrame = (AmqpFrame)frame;
    }

    void contentHeaderHandler(Object context, String input, Object frame, String stateName) {
        AmqpFrame f = (AmqpFrame)frame;
        if (headerFrame != null) {
            headerFrame.setContentProperties(f.getContentProperties());
        }
    }

    void messageBodyHandler(Object context, String input, Object frame, String stateName) {
        AmqpFrame f = (AmqpFrame)frame;
        int channelId = f.getChannelId();
        AmqpChannel channel = client.channels.get(Integer.valueOf(channelId));
        ByteBuffer body = null;
        
        if (f.getBody() != null) {
        	body = (ByteBuffer) f.getBody().getNioByteBuffer().flip();
        }

        ChannelEvent e = new ChannelEvent(channel, 
                                          ChannelEvent.getKind(f), 
                                          headerFrame.getArgs(), 
                                          headerFrame.getContentProperties(), 
                                          body);
        this.fireOnMessage(e);
    }

    void beginTxHandler(AmqpChannel context) {
        // this._transacted = true;
    }

    void genericErrorHandler(Object context, String input, Object args, String stateName) {
        throw new IllegalStateException("GenericErrorHandler");
    }

    /**
     * registerSynchronousRequest puts the client into a waiting state that will be able to call the continuation for a method that expects a particular
     * synchronous response This also lets us call the error cb when there is a close frame (which AMQP uses to raise exceptions) with a reason why the last
     * command failed.
     */
    void channelRegisterSynchronousRequest(Object context, String input, Object frame, String stateName) {
        if (frame != null) {
            Action action = (Action)frame;
            AmqpMethod amqpMethod = (AmqpMethod)action.args[1];
            if (amqpMethod.synchronous) {
                asyncClient.setWaitingAction(action);
            }
            else {
                // method is not synchronous
            }
        }
    }

    void channelClosedHandler(Object context, String input, Object args, String stateName) {
        this.closeOkChannel();
        AmqpFrame f = (AmqpFrame)args;
        ChannelEvent e = new ChannelEvent(this.client.channels.get(f.getChannelId()), f);
        this.fireOnClosed(e);
    }

    /**
     * Fired when channel is Closed
     * 
     * @param e
     */
    private void fireOnClosed(ChannelEvent e) {
        if (readyState == ReadyState.CLOSED) {
            // If the channel has been closed, then we should not fire events.
            return;
        }
        
        readyState = ReadyState.CLOSED;
        List<EventListener> listeners = changes.getListenerList(AMQP);
        for (EventListener listener : listeners) {
            ChannelListener amqpListener = (ChannelListener)listener;
            amqpListener.onClose(e);
        }
    }

    /**
     * Fired when channel is Closed
     * 
     * @param e
     */
    private void fireOnError(ChannelEvent e) {
        List<EventListener> listeners = changes.getListenerList(AMQP);
        for (EventListener listener : listeners) {
            ChannelListener amqpListener = (ChannelListener)listener;
            amqpListener.onError(e);
        }
    }

    /**
     * Fired when channel is opened
     * 
     * @param e
     */
    private void fireOnOpen(ChannelEvent e) {
        if (readyState == ReadyState.OPEN) {
            // If the channel has already been opened, then we should not fire
            // any events.
            return;
        }
        
        readyState = ReadyState.OPEN;
        List<EventListener> listeners = changes.getListenerList(AMQP);
        for (EventListener listener : listeners) {
            ChannelListener amqpListener = (ChannelListener)listener;
            amqpListener.onOpen(e);
        }
    }

    /**
     * Fired when exchange is declared
     * 
     * @param e
     */
    private void fireOnDeclareExchange(ChannelEvent e) {
        List<EventListener> listeners = changes.getListenerList(AMQP);
        for (EventListener listener : listeners) {
            ChannelListener amqpListener = (ChannelListener)listener;
            amqpListener.onDeclareExchange(e);
        }
    }

    /**
     * Fired when Queue is declared
     * 
     * @param e
     */
    private void fireOnDeclareQueue(ChannelEvent e) {
        List<EventListener> listeners = changes.getListenerList(AMQP);
        for (EventListener listener : listeners) {
            ChannelListener amqpListener = (ChannelListener)listener;
            amqpListener.onDeclareQueue(e);
        }
    }

    /**
     * Fired when Queue is bound
     * 
     * @param e
     */
    private void fireOnBindQueue(ChannelEvent e) {
        List<EventListener> listeners = changes.getListenerList(AMQP);
        for (EventListener listener : listeners) {
            ChannelListener amqpListener = (ChannelListener)listener;
            amqpListener.onBindQueue(e);
        }
    }

    /**
     * Fired on consume basic
     * 
     * @param e
     */
    private void fireOnConsumeBasic(ChannelEvent e) {
        List<EventListener> listeners = changes.getListenerList(AMQP);
        for (EventListener listener : listeners) {
            ChannelListener amqpListener = (ChannelListener)listener;
            amqpListener.onConsumeBasic(e);
        }
    }

    /**
     * Fired on transaction commit
     * 
     * @param e
     */
    private void fireOnCommitTransaction(ChannelEvent e) {
        List<EventListener> listeners = changes.getListenerList(AMQP);
        for (EventListener listener : listeners) {
            ChannelListener amqpListener = (ChannelListener)listener;
            amqpListener.onCommit(e);
        }
    }

    /**
     * Fired on transaction rollbacked
     * 
     * @param e
     */
    private void fireOnRollbackTransaction(ChannelEvent e) {
        List<EventListener> listeners = changes.getListenerList(AMQP);
        for (EventListener listener : listeners) {
            ChannelListener amqpListener = (ChannelListener)listener;
            amqpListener.onRollback(e);
        }
    }

    /**
     * Fired on transaction select
     * 
     * @param e
     */
    private void fireOnSelectTransaction(ChannelEvent e) {
        List<EventListener> listeners = changes.getListenerList(AMQP);
        for (EventListener listener : listeners) {
            ChannelListener amqpListener = (ChannelListener)listener;
            amqpListener.onSelect(e);
        }
    }

    /**
     * Fired on message retreival
     * 
     * @param e
     */
    private void fireOnMessage(ChannelEvent e) {
        List<EventListener> listeners = changes.getListenerList(AMQP);
        for (EventListener listener : listeners) {
            ChannelListener amqpListener = (ChannelListener)listener;
            amqpListener.onMessage(e);
        }
    }

    /**
     * Fired when Flow is On/Off
     * 
     * @param e
     */
    private void fireOnFlow(ChannelEvent e) {
        List<EventListener> listeners = changes.getListenerList(AMQP);
        for (EventListener listener : listeners) {
            ChannelListener amqpListener = (ChannelListener)listener;
            amqpListener.onFlow(e);
        }
    }

    /**
     * Fired on Unbind event
     * 
     * @param e
     */
    private void fireOnUnbind(ChannelEvent e) {
        List<EventListener> listeners = changes.getListenerList(AMQP);
        for (EventListener listener : listeners) {
            ChannelListener amqpListener = (ChannelListener)listener;
            amqpListener.onUnbind(e);
        }
    }

    /**
     * Fired when queue is deleted
     * 
     * @param e
     */
    private void fireOnDeleteQueue(ChannelEvent e) {
        List<EventListener> listeners = changes.getListenerList(AMQP);
        for (EventListener listener : listeners) {
            ChannelListener amqpListener = (ChannelListener)listener;
            amqpListener.onDeleteQueue(e);
        }
    }

    /**
     * Fired when exchange is deleted
     * 
     * @param e
     */
    private void fireOnDeleteExchange(ChannelEvent e) {
        List<EventListener> listeners = changes.getListenerList(AMQP);
        for (EventListener listener : listeners) {
            ChannelListener amqpListener = (ChannelListener)listener;
            amqpListener.onDeleteExchange(e);
        }
    }

    /**
     * Fired on cancel basic
     * 
     * @param e
     */
    private void fireOnCancelBasic(ChannelEvent e) {
        List<EventListener> listeners = changes.getListenerList(AMQP);
        for (EventListener listener : listeners) {
            ChannelListener amqpListener = (ChannelListener)listener;
            amqpListener.onCancelBasic(e);
        }
    }

    /**
     * Fired on get basic
     * 
     * @param e
     */
    private void fireOnGetBasic(ChannelEvent e) {
        List<EventListener> listeners = changes.getListenerList(AMQP);
        for (EventListener listener : listeners) {
            ChannelListener amqpListener = (ChannelListener)listener;
            amqpListener.onGetBasic(e);
        }
    }

    /**
     * Fired on purge queue
     * 
     * @param e
     */
    private void fireOnPurgeQueue(ChannelEvent e) {
        List<EventListener> listeners = changes.getListenerList(AMQP);
        for (EventListener listener : listeners) {
            ChannelListener amqpListener = (ChannelListener)listener;
            amqpListener.onPurgeQueue(e);
        }
    }

    /**
     * Fired on recover basic
     * 
     * @param e
     */
    private void fireOnRecoverBasic(ChannelEvent e) {
        List<EventListener> listeners = changes.getListenerList(AMQP);
        for (EventListener listener : listeners) {
            ChannelListener amqpListener = (ChannelListener)listener;
            amqpListener.onRecoverBasic(e);
        }
    }

    /**
     * Fired on reject basic
     * 
     * @param e
     */
    private void fireOnRejectBasic(ChannelEvent e) {
        List<EventListener> listeners = changes.getListenerList(AMQP);
        for (EventListener listener : listeners) {
            ChannelListener amqpListener = (ChannelListener)listener;
            amqpListener.onRejectBasic(e);
        }
    }
    
    class ChannelStateMachineListenerImpl implements ChannelStateMachineListener {

        private AmqpChannel     _channel;

        ChannelStateMachineListenerImpl(AmqpChannel  channel)
        {
            _channel = channel;
        }

        @Override
        public void checkTransactions(Object context, 
                                      String input,
                                      Object args, 
                                      String stateName) {
            _channel.txCheckingHandler(context, input, args, stateName);
        }

        @Override
        public void closeChannel(Object context, 
                                 String input, 
                                 Object args,
                                 String stateName) {
            _channel.channelClosedHandler(context, input, args, stateName);
        }

        @Override
        public void createAndSendFrame(Object   context,
                                       Object[] args) {
            _channel.channelWrite(context, args);
        }

        @Override
        public void handleAdvancedActions(Object context, 
                                          String input,
                                          Object args, 
                                          String stateName) {
            _channel.advanceActionsChannelHandler(context, input, args, stateName);            
        }

        @Override
        public void handleContentHeader(Object context, 
                                        String input,
                                        Object args, 
                                        String stateName) {
            _channel.contentHeaderHandler(context, input, args, stateName);            
        }

        @Override
        public void handleDelivery(Object   context,
                                   String   input,
                                   Object   args, 
                                   String   stateName) {
            _channel.deliveryHandler(context, input, args, stateName);
        }
        
        @Override
        public void handleDefaultBehavior(Object context, 
                                          String input,
                                          Object args, 
                                          String stateName) {
            _channel.defaultBehaviorHandler(context, input, args, stateName);            
        }

        @Override
        public void handleEmptyResponse(Object context, 
                                        String input,
                                        Object args, 
                                        String stateName) {
            _channel.getEmptyResponseHandler(context, input, args, stateName);            
        }

        @Override
        public void handleGenericError(Object context, 
                                       String input,
                                       Object args, 
                                       String stateName) {
            _channel.genericErrorHandler(context, input, args, stateName);            
        }

        @Override
        public void handleGenericResponse(Object context, 
                                          String input,
                                          Object args, 
                                          String stateName) {
            _channel.channelGenericResponseHandler(context, input, args, stateName);            
        }

        @Override
        public void handleMessageBody(Object context, 
                                      String input,
                                      Object args, 
                                      String stateName) {
            _channel.messageBodyHandler(context, input, args, stateName);            
        }

        @Override
        public AmqpChannel getChannel() {
            return _channel;
        }

        @Override
        public void registerSynchronousRequest(Object context, 
                                               String input,
                                               Object args, 
                                               String stateName) {
            _channel.channelRegisterSynchronousRequest(context, input, args, stateName);            
        }
    }
}
