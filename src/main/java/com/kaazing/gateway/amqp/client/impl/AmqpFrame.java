/**
 * Copyright (c) 2007-2012, Kaazing Corporation. All rights reserved.
 */

package com.kaazing.gateway.amqp.client.impl;

import java.util.HashMap;

import com.kaazing.gateway.client.html5.ByteBuffer;
import com.kaazing.gateway.amqp.client.impl.AmqpBuffer.Arg;

public final class AmqpFrame {

    private FrameHeader header;
    private short channelId;
    private short type;
    private ByteBuffer body;
    private String methodName;
    private HashMap<String, Object> contentProperties;
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

    public ByteBuffer getBody() {
        return body;
    }

    public void setBody(ByteBuffer body) {
        this.body = body;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public HashMap<String, Object> getContentProperties() {
        return contentProperties;
    }

    public void setContentProperties(HashMap<String, Object> contentProperties) {
        this.contentProperties = contentProperties;
    }

    public Arg[] getArgs() {
        return args;
    }

    public void setArgs(Arg[] args) {
        this.args = args;
    }   
}
