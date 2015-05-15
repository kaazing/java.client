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

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.rules.RuleChain.outerRule;

import java.net.URI;

import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.DisableOnDebug;
import org.junit.rules.TestRule;
import org.junit.rules.Timeout;
import org.kaazing.k3po.junit.annotation.Specification;
import org.kaazing.k3po.junit.rules.K3poRule;
import org.kaazing.net.ws.WebSocket;
import org.kaazing.net.ws.WebSocketFactory;
import org.kaazing.net.ws.WebSocketMessageReader;
import org.kaazing.net.ws.WebSocketMessageWriter;

public class FramingTextMessagesIT {

	private final K3poRule k3po = new K3poRule();

	private final TestRule timeout = new DisableOnDebug(new Timeout(5, SECONDS));

	@Rule
	public final TestRule chain = outerRule(k3po).around(timeout);

    @Specification("sendTextMessageWithEmptyPayload")
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

        k3po.finish();
    }

    @Specification("sendTextMessageWithPayloadLength125")
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

        k3po.finish();
    }

    @Specification("sendTextMessageWithPayloadLength126")
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

        k3po.finish();
    }

    @Specification("sendTextMessageWithPayloadLength127")
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

        k3po.finish();
    }

    @Specification("sendTextMessageWithPayloadLength128")
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

        k3po.finish();
    }

    @Ignore("KG-12421")
    @Specification("sendTextMessageWithPayloadLength65535")
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

        k3po.finish();
    }

    @Specification("sendTextMessageWithPayloadLength65536")
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

        k3po.finish();
    }

    @Specification("sendTextMessageWithPayloadLength65536InChopsOf997Octets")
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

        k3po.finish();
    }
}
