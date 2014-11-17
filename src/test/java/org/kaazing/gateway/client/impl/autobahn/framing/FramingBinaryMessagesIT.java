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

package org.kaazing.gateway.client.impl.autobahn.framing;

import java.net.URI;
import java.nio.ByteBuffer;

import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;

import org.kaazing.net.ws.WebSocket;
import org.kaazing.net.ws.WebSocketFactory;
import org.kaazing.net.ws.WebSocketMessageReader;
import org.kaazing.net.ws.WebSocketMessageWriter;
import org.kaazing.robot.junit.annotation.Robotic;
import org.kaazing.robot.junit.rules.RobotRule;

public class FramingBinaryMessagesIT {

    @Rule
    public RobotRule robot = new RobotRule();

    @Robotic(script = "sendBinaryMessageWithPayloadLengthZero")
    @Test(timeout = 1500)
    public void sendBinaryMessageWithPayloadLengthZero() throws Exception {

        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);

        webSocket.connect();

        WebSocketMessageReader reader = webSocket.getMessageReader();
        WebSocketMessageWriter writer = webSocket.getMessageWriter();

        reader.next();
        ByteBuffer buffer = reader.getBinary();
        writer.writeBinary(buffer);

        robot.join();
    }

    @Robotic(script = "sendBinaryMessageWithPayloadLength125")
    @Test(timeout = 1500)
    public void sendBinaryMessageWithPayloadLength125() throws Exception {

        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);

        webSocket.connect();

        WebSocketMessageReader reader = webSocket.getMessageReader();
        WebSocketMessageWriter writer = webSocket.getMessageWriter();

        reader.next();
        ByteBuffer buffer = reader.getBinary();
        writer.writeBinary(buffer);

        robot.join();
    }

    @Robotic(script = "sendBinaryMessageWithPayloadLength126")
    @Test(timeout = 1500)
    public void sendBinaryMessageWithPayloadLength126() throws Exception {

        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);

        webSocket.connect();

        WebSocketMessageReader reader = webSocket.getMessageReader();
        WebSocketMessageWriter writer = webSocket.getMessageWriter();

        reader.next();
        ByteBuffer buffer = reader.getBinary();
        writer.writeBinary(buffer);

        robot.join();
    }

    @Robotic(script = "sendBinaryMessageWithPayloadLength127")
    @Test(timeout = 1500)
    public void sendBinaryMessageWithPayloadLength127() throws Exception {

        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);

        webSocket.connect();

        WebSocketMessageReader reader = webSocket.getMessageReader();
        WebSocketMessageWriter writer = webSocket.getMessageWriter();

        reader.next();
        ByteBuffer buffer = reader.getBinary();
        writer.writeBinary(buffer);

        robot.join();
    }

    @Robotic(script = "sendBinaryMessageWithPayloadLength128")
    @Test(timeout = 1500)
    public void sendBinaryMessageWithPayloadLength128() throws Exception {

        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);

        webSocket.connect();

        WebSocketMessageReader reader = webSocket.getMessageReader();
        WebSocketMessageWriter writer = webSocket.getMessageWriter();

        reader.next();
        ByteBuffer buffer = reader.getBinary();
        writer.writeBinary(buffer);

        robot.join();
    }

    @Ignore("KG-12421")
    @Robotic(script = "sendBinaryMessageWithPayloadLength65535")
    @Test(timeout = 5000)
    public void sendBinaryMessageWithPayloadLength65535() throws Exception {

        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);

        webSocket.connect();

        WebSocketMessageReader reader = webSocket.getMessageReader();
        WebSocketMessageWriter writer = webSocket.getMessageWriter();

        reader.next();
        ByteBuffer buffer = reader.getBinary();
        writer.writeBinary(buffer);

        robot.join();
    }

    @Robotic(script = "sendBinaryMessageWithPayloadLength65536")
    @Test(timeout = 5000)
    public void sendBinaryMessageWithPayloadLength65536() throws Exception {

        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);

        webSocket.connect();

        WebSocketMessageReader reader = webSocket.getMessageReader();
        WebSocketMessageWriter writer = webSocket.getMessageWriter();

        reader.next();
        ByteBuffer buffer = reader.getBinary();
        writer.writeBinary(buffer);

        robot.join();
    }

    @Robotic(script = "sendBinaryMessageWithPayloadLength65536InChopsOf997Octets")
    @Test(timeout = 5000)
    public void sendBinaryMessageWithPayloadLength65536InChopsOf997Octets() throws Exception {

        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);

        webSocket.connect();

        WebSocketMessageReader reader = webSocket.getMessageReader();
        WebSocketMessageWriter writer = webSocket.getMessageWriter();

        reader.next();
        ByteBuffer buffer = reader.getBinary();
        writer.writeBinary(buffer);

        robot.join();
    }
}
