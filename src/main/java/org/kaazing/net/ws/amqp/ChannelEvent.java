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
import java.util.HashMap;
import java.util.Map;

import org.kaazing.net.ws.amqp.impl.AmqpFrame;
import org.kaazing.net.ws.amqp.impl.AmqpBuffer.Arg;

public final class ChannelEvent extends AmqpEvent {

    /**
     * The Kind of ChannelEvent
     */
    public enum Kind {
        // TODO: Document the kinds of ChannelEvent
        OPEN,
        MESSAGE,
        CLOSE,
        GET,
        DECLAREQUEUE,
        DECLAREEXCHANGE,
        FLOW,
        BINDQUEUE,
        UNBINDQUEUE,
        DELETEQUEUE,
        DELETEEXCHANGE,
        CANCEL,
        COMMITTRANSACTION,
        ROLLBACKTRANSACTION,
        SELECTTRANSACTION,
        CONSUME,
        PURGEQUEUE,
        RECOVER,
        REJECT,
        ERROR
    }

    public enum MethodName {
        DELIVERBASIC,
        CLOSEOKCHANNEL,
        CLOSECHANNEL,
        GETOKBASIC,
        GETEMPTYBASIC,
        OPENOKCHANNEL,
        DECLAREOKQUEUE,
        DECLAREOKEXCHANGE,
        FLOWOKCHANNEL,
        BINDOKQUEUE,
        UNBINDOKQUEUE,
        DELETEOKQUEUE,
        DELETEOKEXCHANGE,
        CANCELOKBASIC,
        COMMITOKTX,
        ROLLBACKOKTX,
        SELECTOKTX,
        CONSUMEOKBASIC,
        PURGEOKQUEUE,
        RECOVEROKBASIC,
        REJECTOKBASIC,
        BODY,
        ERROR,
        UNDEFINED
    }

    private Kind                    kind;
    private ByteBuffer              body;
    private Map<String, Object>     headers; // = new HashMap<String, Object>();
    private MethodName              methodName;
    private String                  errorMessage;
    private AmqpProperties          properties;

    static Kind getKind(AmqpFrame frame) {
        return ChannelEvent.getKind(MethodName.valueOf(frame.getMethodName().toUpperCase()));
    }
    
    private static Kind getKind(MethodName methodName) {
        switch(methodName) {
        case OPENOKCHANNEL:        return Kind.OPEN;
        case BODY:              return Kind.MESSAGE;
        case DELIVERBASIC:        return Kind.MESSAGE;
        case CLOSEOKCHANNEL:    return Kind.CLOSE;
        case CLOSECHANNEL:        return Kind.CLOSE;
        case GETOKBASIC:        return Kind.GET;
        case GETEMPTYBASIC:        return Kind.GET;
        case DECLAREOKQUEUE:    return Kind.DECLAREQUEUE;
        case DECLAREOKEXCHANGE: return Kind.DECLAREEXCHANGE;
        case FLOWOKCHANNEL:        return Kind.FLOW;
        case BINDOKQUEUE:        return Kind.BINDQUEUE;
        case UNBINDOKQUEUE:        return Kind.UNBINDQUEUE;
        case DELETEOKQUEUE:        return Kind.DELETEQUEUE;
        case DELETEOKEXCHANGE:    return Kind.DELETEEXCHANGE;
        case CANCELOKBASIC:        return Kind.CANCEL;
        case COMMITOKTX:        return Kind.COMMITTRANSACTION;
        case ROLLBACKOKTX:        return Kind.ROLLBACKTRANSACTION;
        case SELECTOKTX:        return Kind.SELECTTRANSACTION;
        case CONSUMEOKBASIC:    return Kind.CONSUME;
        case PURGEOKQUEUE:        return Kind.PURGEQUEUE;
        case RECOVEROKBASIC:    return Kind.RECOVER;
        case REJECTOKBASIC:        return Kind.REJECT;
        case ERROR:                return Kind.ERROR;
            
        default:
            throw new IllegalStateException("AMQP: unknown event name " + methodName);
        }          
    }

    private AmqpChannel channel;
//    int messageCount;
//    String deliveryTag;
//    String consumerTag;

    ChannelEvent(AmqpChannel channel, AmqpFrame frame) {
        this(channel, getKind(frame), frame.getArgs(), null, frame.getBody() == null ? null : frame.getBody().getNioByteBuffer());
        this.methodName = MethodName.valueOf(frame.getMethodName().toUpperCase());
        if (methodName == MethodName.ERROR) {
        	errorMessage = (String) super.getArgument("replyText");
        }
    }

    public ChannelEvent(AmqpClient client, Kind kind, String errorMessage) {
        this(client.channels.get(0), kind, null, null, null);
        this.errorMessage = errorMessage;
    }
    
    public ChannelEvent(AmqpChannel channel, Kind kind, String errorMessage) {
        this(channel, kind, null, null, null);
        this.errorMessage = errorMessage;
    }

    public ChannelEvent(AmqpChannel channel, Kind kind, Arg[] args, Map<String, Object> headers, ByteBuffer body) {
        super(args);
        this.channel = channel;
        this.kind = kind;
        this.headers = headers;
        this.body = body;
        this.properties = new AmqpProperties(headers);
    }

    /**
     * @return the kind
     */
    public Kind getKind() {
        return kind;
    }

    /**
     * @return the body
     */
    public ByteBuffer getBody() {
        return body;
    }

    /**
     * Returns AmqpProperties associated with the message. This includes the
     * pre-defined properties as per AMQP 0-9-1 spec. It also includes the
     * custom headers that can be obtained using AmqpProperties.getHeaders()
     * method.
     * 
     * @return AmqpProperties associated with the message
     */
    public AmqpProperties getAmqpProperties() {
        return properties;
    }
    
    /**
     * Returns the AMQPChannel associated with the event.
     * 
     * @return AmqpChannel channel
     */
    public AmqpChannel getChannel() {
        return channel;
    }

    /**
     * Returns the ChannelEvent.MethodName associated with the event.
     * 
     * @return MethodName
     */
    public MethodName getMethodName() {
        return methodName;
    }

    /**
     * Gets the error message for error events
     * 
     * @return String    representing the error message, if it is an
     *                   error event. Otherwise, returns a null.
     */
    public String getMessage() {
        if (kind == Kind.ERROR) {
            return errorMessage;
        }
        
        return null;
    }

    /**
     * Returns For flow events, returns true if flow is active
     * 
     * @return boolean true if event specifies flow is active
     * @throws IllegalStateException if this event is not a flow event
     */
    public boolean isFlowActive() {
        Object val = this.getArgument("active");
        if (val instanceof Integer) {
            return ((Integer)val).intValue() == 1;
        }
        else {
            throw new IllegalStateException("ChannelEvent does not contain the 'active' argument");
        }
    }
}
