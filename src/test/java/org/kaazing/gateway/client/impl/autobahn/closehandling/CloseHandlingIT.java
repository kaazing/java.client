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

package org.kaazing.gateway.client.impl.autobahn.closehandling;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.rules.RuleChain.outerRule;

import java.net.URI;

import org.junit.Ignore;
import org.junit.Rule;
import org.junit.rules.DisableOnDebug;
import org.junit.rules.TestRule;
import org.junit.rules.Timeout;
import org.kaazing.k3po.junit.annotation.Specification;
import org.kaazing.k3po.junit.rules.K3poRule;
import org.kaazing.net.ws.WebSocket;
import org.kaazing.net.ws.WebSocketFactory;
import org.kaazing.net.ws.WebSocketMessageReader;
import org.kaazing.net.ws.WebSocketMessageWriter;

public class CloseHandlingIT {
	private final K3poRule k3po = new K3poRule();

	private final TestRule timeout = new DisableOnDebug(new Timeout(5, SECONDS));

	@Rule
	public final TestRule chain = outerRule(k3po).around(timeout);

    @Specification("send256KMessageFollowedByCloseThenPing")
    public void send256KMessageFollowedByCloseThenPing() throws Exception {

        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);

        webSocket.connect();
        k3po.finish();
    }

    @Specification("sendCloseFrameWithCloseCode")
    public void sendCloseFrameWithCloseCode() throws Exception {

        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);

        webSocket.connect();
        k3po.finish();
    }

    @Specification("sendCloseFrameWithCloseCodeAndCloseReason")
    public void sendCloseFrameWithCloseCodeAndCloseReason() throws Exception {

        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);

        webSocket.connect();
        k3po.finish();
    }

    @Specification("sendCloseFrameWithCloseCodeAndCloseReasonOfMaximumLength")
    public void sendCloseFrameWithCloseCodeAndCloseReasonOfMaximumLength() throws Exception {

        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);

        webSocket.connect();
        k3po.finish();
    }

    @Ignore("KG-12453")
    @Specification("sendCloseFrameWithCloseCodeAndCloseReasonWhichIsTooLong")
    public void sendCloseFrameWithCloseCodeAndCloseReasonWhichIsTooLong() throws Exception {

        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);

        webSocket.connect();
        k3po.finish();
    }

    @Ignore("KG-12453")
    @Specification("sendCloseFrameWithInvalidCloseCode1004")
    public void sendCloseFrameWithInvalidCloseCode1004() throws Exception {

        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);

        webSocket.connect();
        k3po.finish();
    }

    @Ignore("KG-12453")
    @Specification("sendCloseFrameWithInvalidCloseCode1006")
    public void sendCloseFrameWithInvalidCloseCode1006() throws Exception {

        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);

        webSocket.connect();
        k3po.finish();
    }

    @Ignore("KG-12453")
    @Specification("sendCloseFrameWithInvalidCloseCode1012")
    public void sendCloseFrameWithInvalidCloseCode1012() throws Exception {

        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);

        webSocket.connect();
        k3po.finish();
    }

    @Ignore("KG-12453")
    @Specification("sendCloseFrameWithInvalidCloseCode1013")
    public void sendCloseFrameWithInvalidCloseCode1013() throws Exception {

        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);

        webSocket.connect();
        k3po.finish();
    }

    @Ignore("KG-12453")
    @Specification("sendCloseFrameWithInvalidCloseCode1014")
    public void sendCloseFrameWithInvalidCloseCode1014() throws Exception {

        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);

        webSocket.connect();
        k3po.finish();
    }

    @Ignore("KG-12453")
    @Specification("sendCloseFrameWithInvalidCloseCode1015")
    public void sendCloseFrameWithInvalidCloseCode1015() throws Exception {

        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);

        webSocket.connect();
        k3po.finish();
    }

    @Ignore("KG-12453")
    @Specification("sendCloseFrameWithInvalidCloseCode1016")
    public void sendCloseFrameWithInvalidCloseCode1016() throws Exception {

        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);

        webSocket.connect();
        k3po.finish();
    }

    @Ignore("KG-12453")
    @Specification("sendCloseFrameWithInvalidCloseCode1100")
    public void sendCloseFrameWithInvalidCloseCode1100() throws Exception {

        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);

        webSocket.connect();
        k3po.finish();
    }

    @Ignore("KG-12453")
    @Specification("sendCloseFrameWithInvalidCloseCode2000")
    public void sendCloseFrameWithInvalidCloseCode2000() throws Exception {

        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);

        webSocket.connect();
        k3po.finish();
    }

    @Ignore("KG-12453")
    @Specification("sendCloseFrameWithInvalidCloseCode2999")
    public void sendCloseFrameWithInvalidCloseCode2999() throws Exception {

        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);

        webSocket.connect();
        k3po.finish();
    }

    @Ignore("KG-12453")
    @Specification("sendCloseFrameWithInvalidCloseCode999")
    public void sendCloseFrameWithInvalidCloseCode999() throws Exception {

        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);

        webSocket.connect();
        k3po.finish();
    }

    @Ignore("KG-12453")
    @Specification("sendCloseFrameWithInvalidUTF8Payload")
    public void sendCloseFrameWithInvalidUTF8Payload() throws Exception {

        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);

        webSocket.connect();
        k3po.finish();
    }

    @Ignore("KG-12453")
    @Specification("sendCloseFrameWithPayloadLength1")
    public void sendCloseFrameWithPayloadLength1() throws Exception {

        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);

        webSocket.connect();
        k3po.finish();
    }

    @Specification("sendCloseFrameWithPayloadLengthZero")
    public void sendCloseFrameWithPayloadLengthZero() throws Exception {

        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);

        webSocket.connect();
        k3po.finish();
    }

    @Specification("sendCloseWithCloseCode5000")
    public void sendCloseWithCloseCode5000() throws Exception {

        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);

        webSocket.connect();
        k3po.finish();
    }

    @Specification("sendCloseWithCloseCode65535")
    public void sendCloseWithCloseCode65535() throws Exception {

        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);

        webSocket.connect();
        k3po.finish();
    }

    @Ignore("KG-12453")
    @Specification("sendCloseWithInvalidCloseCode1005")
    public void sendCloseWithInvalidCloseCode1005() throws Exception {

        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);

        webSocket.connect();
        k3po.finish();
    }

    @Ignore("KG-12453")
    @Specification("sendCloseWithInvalidCloseCodeZero")
    public void sendCloseWithInvalidCloseCodeZero() throws Exception {

        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);

        webSocket.connect();
        k3po.finish();
    }

    @Specification("sendCloseWithValidCloseCode1000")
    public void sendCloseWithValidCloseCode1000() throws Exception {

        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);

        webSocket.connect();
        k3po.finish();
    }

    @Specification("sendCloseWithValidCloseCode1001")
    public void sendCloseWithValidCloseCode1001() throws Exception {

        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);

        webSocket.connect();
        k3po.finish();
    }

    @Specification("sendCloseWithValidCloseCode1002")
    public void sendCloseWithValidCloseCode1002() throws Exception {

        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);

        webSocket.connect();
        k3po.finish();
    }

    @Specification("sendCloseWithValidCloseCode1003")
    public void sendCloseWithValidCloseCode1003() throws Exception {

        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);

        webSocket.connect();
        k3po.finish();
    }

    @Specification("sendCloseWithValidCloseCode1007")
    public void sendCloseWithValidCloseCode1007() throws Exception {

        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);

        webSocket.connect();
        k3po.finish();
    }

    @Specification("sendCloseWithValidCloseCode1008")
    public void sendCloseWithValidCloseCode1008() throws Exception {

        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);

        webSocket.connect();
        k3po.finish();
    }

    @Specification("sendCloseWithValidCloseCode1009")
    public void sendCloseWithValidCloseCode1009() throws Exception {

        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);

        webSocket.connect();
        k3po.finish();
    }

    @Specification("sendCloseWithValidCloseCode1010")
    public void sendCloseWithValidCloseCode1010() throws Exception {

        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);

        webSocket.connect();
        k3po.finish();
    }

    @Specification("sendCloseWithValidCloseCode1011")
    public void sendCloseWithValidCloseCode1011() throws Exception {

        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);

        webSocket.connect();
        k3po.finish();
    }

    @Specification("sendCloseWithValidCloseCode3000")
    public void sendCloseWithValidCloseCode3000() throws Exception {

        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);

        webSocket.connect();
        k3po.finish();
    }

    @Specification("sendCloseWithValidCloseCode3999")
    public void sendCloseWithValidCloseCode3999() throws Exception {

        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);

        webSocket.connect();
        k3po.finish();
    }

    @Specification("sendCloseWithValidCloseCode4000")
    public void sendCloseWithValidCloseCode4000() throws Exception {

        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);

        webSocket.connect();
        k3po.finish();
    }

    @Specification("sendCloseWithValidCloseCode4999")
    public void sendCloseWithValidCloseCode4999() throws Exception {

        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);

        webSocket.connect();
        k3po.finish();
    }

    @Specification("sendMessageFragment1FollowedByCloseThenFragment")
    public void sendMessageFragment1FollowedByCloseThenFragment() throws Exception {

        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);

        webSocket.connect();
        k3po.finish();
    }

    @Specification("sendPingAfterCloseMessage")
    public void sendPingAfterCloseMessage() throws Exception {

        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);

        webSocket.connect();
        k3po.finish();
    }

    @Specification("sendTextMessageAfterCloseFrame")
    public void sendTextMessageAfterCloseFrame() throws Exception {

        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);

        webSocket.connect();
        k3po.finish();
    }

    @Specification("sendTextMessageThenCloseFrame")
    public void sendTextMessageThenCloseFrame() throws Exception {

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

    @Specification("sendTwoCloseFrames")
    public void sendTwoCloseFrames() throws Exception {

        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);

        webSocket.connect();
        k3po.finish();
    }
}
