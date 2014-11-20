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
 * under the License. */

package org.kaazing.net.ws.amqp.impl;

import java.util.Map;

import org.kaazing.net.ws.amqp.impl.AmqpBuffer.Arg;

import org.kaazing.gateway.client.util.WrappedByteBuffer;

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
