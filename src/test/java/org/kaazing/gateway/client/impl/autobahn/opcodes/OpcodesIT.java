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

package org.kaazing.gateway.client.impl.autobahn.opcodes;

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

public class OpcodesIT {
	private final K3poRule k3po = new K3poRule();

	private final TestRule timeout = new DisableOnDebug(new Timeout(5, SECONDS));

	@Rule
	public final TestRule chain = outerRule(k3po).around(timeout);

    @Ignore("KG-12432")
    @Specification("sendFrameWithReservedControlOpcodeEquals11")
    public void sendFrameWithReservedControlOpcodeEquals11() throws Exception {

        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);

        webSocket.connect();

        k3po.finish();
    }

    @Ignore("KG-12432")
    @Specification("sendFrameWithReservedControlOpcodeEquals12AndNonEmptyPayload")
    public void sendFrameWithReservedControlOpcodeEquals12AndNonEmptyPayload() throws Exception {

        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);

        webSocket.connect();

        k3po.finish();
    }

    @Ignore("KG-12432")
    @Specification("sendFrameWithReservedControlOpcodeEquals13ThenPing")
    public void sendFrameWithReservedControlOpcodeEquals13ThenPing() throws Exception {

        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);

        webSocket.connect();

        WebSocketMessageWriter writer = webSocket.getMessageWriter();
        WebSocketMessageReader reader = webSocket.getMessageReader();

        reader.next();
        CharSequence text = reader.getText();
        writer.writeText(text);

        k3po.finish();
    }

    @Ignore("KG-12432")
    @Specification("sendFrameWithReservedNonControlOpcodeEquals3")
    public void sendFrameWithReservedNonControlOpcodeEquals3() throws Exception {

        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);

        webSocket.connect();

        k3po.finish();
    }

    @Ignore("KG-12432")
    @Specification("sendFrameWithReservedNonControlOpcodeEquals4AndNonEmptyPayload")
    public void sendFrameWithReservedNonControlOpcodeEquals4AndNonEmptyPayload() throws Exception {

        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);

        webSocket.connect();

        k3po.finish();
    }

    @Ignore("KG-12432")
    @Specification("sendSmallTextMessageThenFrameWithReservedControlOpcodeEquals14AndNonEmptyPayloadThenPing")
    public void sendSmallTextMessageThenFrameWithReservedControlOpcodeEquals14AndNonEmptyPayloadThenPing()
            throws Exception {

        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);

        webSocket.connect();

        WebSocketMessageWriter writer = webSocket.getMessageWriter();
        WebSocketMessageReader reader = webSocket.getMessageReader();

        reader.next();
        CharSequence text = reader.getText();
        writer.writeText(text);

        k3po.finish();
    }

    @Ignore("KG-12432")
    @Specification("sendSmallTextMessageThenFrameWithReservedControlOpcodeEquals15AndNonEmptyPayloadThenPing")
    public void sendSmallTextMessageThenFrameWithReservedControlOpcodeEquals15AndNonEmptyPayloadThenPing()
            throws Exception {

        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);

        webSocket.connect();

        WebSocketMessageWriter writer = webSocket.getMessageWriter();
        WebSocketMessageReader reader = webSocket.getMessageReader();

        reader.next();
        CharSequence text = reader.getText();
        writer.writeText(text);

        k3po.finish();
    }

    @Ignore("KG-12432")
    @Specification("sendSmallTextMessageThenFrameWithReservedNonControlOpcodeEquals5ThenPing")
    public void sendSmallTextMessageThenFrameWithReservedNonControlOpcodeEquals5ThenPing() throws Exception {

        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);

        webSocket.connect();

        WebSocketMessageWriter writer = webSocket.getMessageWriter();
        WebSocketMessageReader reader = webSocket.getMessageReader();

        reader.next();
        CharSequence text = reader.getText();
        writer.writeText(text);

        k3po.finish();
    }

    @Ignore("KG-12432")
    @Specification("sendSmallTextMessageThenFrameWithReservedNonControlOpcodeEquals6AndNonEmptyPayloadThenPing")
    public void sendSmallTextMessageThenFrameWithReservedNonControlOpcodeEquals6AndNonEmptyPayloadThenPing()
            throws Exception {

        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);

        webSocket.connect();

        WebSocketMessageWriter writer = webSocket.getMessageWriter();
        WebSocketMessageReader reader = webSocket.getMessageReader();

        reader.next();
        CharSequence text = reader.getText();
        writer.writeText(text);

        k3po.finish();
    }

    @Ignore("KG-12432")
    @Specification("sendSmallTextMessageThenFrameWithReservedNonControlOpcodeEquals7AndNonEmptyPayloadThenPing")
    public void sendSmallTextMessageThenFrameWithReservedNonControlOpcodeEquals7AndNonEmptyPayloadThenPing()
            throws Exception {

        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);

        webSocket.connect();

        WebSocketMessageWriter writer = webSocket.getMessageWriter();
        WebSocketMessageReader reader = webSocket.getMessageReader();

        reader.next();
        CharSequence text = reader.getText();
        writer.writeText(text);

        k3po.finish();
    }
}
