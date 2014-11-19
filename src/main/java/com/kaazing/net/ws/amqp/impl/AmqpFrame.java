/**
 * Copyright (c) 2007-2014, Kaazing Corporation. All rights reserved.
 */

package com.kaazing.net.ws.amqp.impl;

import java.util.Map;

import com.kaazing.gateway.client.common.util.WrappedByteBuffer;
import com.kaazing.net.ws.amqp.impl.AmqpBuffer.Arg;

public final class AmqpFrame {

    private FrameHeader header;
    private short channelId;
    private short type;
    private WrappedByteBuffer body;
    private String methodName;
    private Map<String, Object> contentProperties;
    private Arg[] args;
     
    public FrameHeader getHeader() {
        return header;
    }

    public void setHeader(FrameHeader header1) {
        header = header1;
    }

    public short getChannelId() {
        return channelId;
    }

    public void setChannelId(short channel) {
        this.channelId = channel;
    }

    public short getType() {
        return type;
    }

    public void setType(short type) {
        this.type = type;
    }

    public WrappedByteBuffer getBody() {
        return body;
    }

    public void setBody(WrappedByteBuffer body) {
        this.body = body;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Map<String, Object> getContentProperties() {
        return contentProperties;
    }

    public void setContentProperties(Map<String, Object> contentProperties) {
        this.contentProperties = contentProperties;
    }

    public Arg[] getArgs() {
        return args;
    }

    public void setArgs(Arg[] args) {
        this.args = args;
    }   
}
