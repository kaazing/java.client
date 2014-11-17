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

import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;

import org.kaazing.net.ws.WebSocket;
import org.kaazing.net.ws.WebSocketFactory;
import org.kaazing.net.ws.WebSocketMessageReader;
import org.kaazing.net.ws.WebSocketMessageWriter;
import org.kaazing.robot.junit.annotation.Robotic;
import org.kaazing.robot.junit.rules.RobotRule;

public class FramingTextMessagesIT {

    @Rule
    public RobotRule robot = new RobotRule();

    @Robotic(script = "sendTextMessageWithEmptyPayload")
    @Test(timeout = 1500)
    public void sendTextMessageWithEmptyPayload() throws Exception {

        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);

        webSocket.connect();

        WebSocketMessageReader reader = webSocket.getMessageReader();
        WebSocketMessageWriter writer = webSocket.getMessageWriter();

        reader.next();
        CharSequence text = reader.getText();
        writer.writeText(text);

        robot.join();
    }

    @Robotic(script = "sendTextMessageWithPayloadLength125")
    @Test(timeout = 1500)
    public void sendTextMessageWithPayloadLength125() throws Exception {

        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);

        webSocket.connect();

        WebSocketMessageReader reader = webSocket.getMessageReader();
        WebSocketMessageWriter writer = webSocket.getMessageWriter();

        reader.next();
        CharSequence text = reader.getText();
        writer.writeText(text);

        robot.join();
    }

    @Robotic(script = "sendTextMessageWithPayloadLength126")
    @Test(timeout = 1500)
    public void sendTextMessageWithPayloadLength126() throws Exception {

        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);

        webSocket.connect();

        WebSocketMessageReader reader = webSocket.getMessageReader();
        WebSocketMessageWriter writer = webSocket.getMessageWriter();

        reader.next();
        CharSequence text = reader.getText();
        writer.writeText(text);

        robot.join();
    }

    @Robotic(script = "sendTextMessageWithPayloadLength127")
    @Test(timeout = 1500)
    public void sendTextMessageWithPayloadLength127() throws Exception {

        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);

        webSocket.connect();

        WebSocketMessageReader reader = webSocket.getMessageReader();
        WebSocketMessageWriter writer = webSocket.getMessageWriter();

        reader.next();
        CharSequence text = reader.getText();
        writer.writeText(text);

        robot.join();
    }

    @Robotic(script = "sendTextMessageWithPayloadLength128")
    @Test(timeout = 1500)
    public void sendTextMessageWithPayloadLength128() throws Exception {

        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);

        webSocket.connect();

        WebSocketMessageReader reader = webSocket.getMessageReader();
        WebSocketMessageWriter writer = webSocket.getMessageWriter();

        reader.next();
        CharSequence text = reader.getText();
        writer.writeText(text);

        robot.join();
    }

    @Ignore("KG-12421")
    @Robotic(script = "sendTextMessageWithPayloadLength65535")
    @Test(timeout = 5000)
    public void sendTextMessageWithPayloadLength65535() throws Exception {

        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);

        webSocket.connect();

        WebSocketMessageReader reader = webSocket.getMessageReader();
        WebSocketMessageWriter writer = webSocket.getMessageWriter();

        reader.next();
        CharSequence text = reader.getText();
        writer.writeText(text);

        robot.join();
    }

    @Robotic(script = "sendTextMessageWithPayloadLength65536")
    @Test(timeout = 5000)
    public void sendTextMessageWithPayloadLength65536() throws Exception {

        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);

        webSocket.connect();

        WebSocketMessageReader reader = webSocket.getMessageReader();
        WebSocketMessageWriter writer = webSocket.getMessageWriter();

        reader.next();
        CharSequence text = reader.getText();
        writer.writeText(text);

        robot.join();
    }

    @Robotic(script = "sendTextMessageWithPayloadLength65536InChopsOf997Octets")
    @Test(timeout = 5000)
    public void sendTextMessageWithPayloadLength65536InChopsOf997Octets() throws Exception {

        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);

        webSocket.connect();

        WebSocketMessageReader reader = webSocket.getMessageReader();
        WebSocketMessageWriter writer = webSocket.getMessageWriter();

        reader.next();
        CharSequence text = reader.getText();
        writer.writeText(text);

        robot.join();
    }
}
