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

package org.kaazing.gateway.client.impl.autobahn.utf8handling;

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

public class UTF8HandlingIT {
	private final K3poRule k3po = new K3poRule();

	private final TestRule timeout = new DisableOnDebug(new Timeout(5, SECONDS));

	@Rule
	public final TestRule chain = outerRule(k3po).around(timeout);

    @Specification("sendTextMessageOfLengthZero")
    public void sendTextMessageOfLengthZero() throws Exception {
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

    @Ignore("KG-12464")
    @Specification("sendTextMessageWithInvalidUTF8PayloadIn3Chops")
    public void sendTextMessageWithInvalidUTF8PayloadIn3Chops() throws Exception {
        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);

        webSocket.connect();

        k3po.finish();
    }

    @Ignore("KG-12464")
    @Specification("sendTextMessageWithInvalidUTF8PayloadIn3Chops2")
    public void sendTextMessageWithInvalidUTF8PayloadIn3Chops2() throws Exception {
        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);

        webSocket.connect();

        k3po.finish();
    }

    @Ignore("KG-12464")
    @Specification("sendTextMessageWithInvalidUTF8PayloadIn3Fragments")
    public void sendTextMessageWithInvalidUTF8PayloadIn3Fragments() throws Exception {
        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);

        webSocket.connect();

        k3po.finish();
    }

    @Ignore("KG-12464")
    @Specification("sendTextMessageWithInvalidUTF8PayloadIn3Fragments2")
    public void sendTextMessageWithInvalidUTF8PayloadIn3Fragments2() throws Exception {
        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);

        webSocket.connect();

        k3po.finish();
    }

    @Ignore("KG-12464")
    @Specification("sendTextMessageWithInvalidUTF8PayloadInOneFragment")
    public void sendTextMessageWithInvalidUTF8PayloadInOneFragment() throws Exception {
        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);

        webSocket.connect();

        k3po.finish();
    }

    @Ignore("KG-12464")
    @Specification("sendTextMessageWithInvalidUTF8PayloadInOneFragment2")
    public void sendTextMessageWithInvalidUTF8PayloadInOneFragment2() throws Exception {
        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);
        webSocket.connect();
        k3po.finish();
    }

    @Ignore("KG-12464")
    @Specification("sendTextMessageWithInvalidUTF8PayloadInOneFragment3")
    public void sendTextMessageWithInvalidUTF8PayloadInOneFragment3() throws Exception {
        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);
        webSocket.connect();
        k3po.finish();
    }

    @Ignore("KG-12464")
    @Specification("sendTextMessageWithInvalidUTF8PayloadInOneFragment4")
    public void sendTextMessageWithInvalidUTF8PayloadInOneFragment4() throws Exception {
        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);
        webSocket.connect();
        k3po.finish();
    }

    @Ignore("KG-12464")
    @Specification("sendTextMessageWithInvalidUTF8PayloadInOneFragment5")
    public void sendTextMessageWithInvalidUTF8PayloadInOneFragment5() throws Exception {
        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);
        webSocket.connect();
        k3po.finish();
    }

    @Ignore("KG-12464")
    @Specification("sendTextMessageWithInvalidUTF8PayloadInOneFragment6")
    public void sendTextMessageWithInvalidUTF8PayloadInOneFragment6() throws Exception {
        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);
        webSocket.connect();
        k3po.finish();
    }

    @Ignore("KG-12464")
    @Specification("sendTextMessageWithInvalidUTF8PayloadInOneFragment7")
    public void sendTextMessageWithInvalidUTF8PayloadInOneFragment7() throws Exception {
        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);
        webSocket.connect();
        k3po.finish();
    }

    @Ignore("KG-12464")
    @Specification("sendTextMessageWithInvalidUTF8PayloadInOneFragment8")
    public void sendTextMessageWithInvalidUTF8PayloadInOneFragment8() throws Exception {
        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);
        webSocket.connect();
        k3po.finish();
    }

    @Ignore("KG-12464")
    @Specification("sendTextMessageWithInvalidUTF8PayloadInOneFragment9")
    public void sendTextMessageWithInvalidUTF8PayloadInOneFragment9() throws Exception {
        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);
        webSocket.connect();
        k3po.finish();
    }

    @Ignore("KG-12464")
    @Specification("sendTextMessageWithInvalidUTF8PayloadInOneFragment10")
    public void sendTextMessageWithInvalidUTF8PayloadInOneFragment10() throws Exception {
        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);
        webSocket.connect();
        k3po.finish();
    }

    @Ignore("KG-12464")
    @Specification("sendTextMessageWithInvalidUTF8PayloadInOneFragment11")
    public void sendTextMessageWithInvalidUTF8PayloadInOneFragment11() throws Exception {
        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);
        webSocket.connect();
        k3po.finish();
    }

    @Ignore("KG-12464")
    @Specification("sendTextMessageWithInvalidUTF8PayloadInOneFragment12")
    public void sendTextMessageWithInvalidUTF8PayloadInOneFragment12() throws Exception {
        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);
        webSocket.connect();
        k3po.finish();
    }

    @Ignore("KG-12464")
    @Specification("sendTextMessageWithInvalidUTF8PayloadInOneFragment13")
    public void sendTextMessageWithInvalidUTF8PayloadInOneFragment13() throws Exception {
        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);
        webSocket.connect();
        k3po.finish();
    }

    @Ignore("KG-12464")
    @Specification("sendTextMessageWithInvalidUTF8PayloadInOneFragment14")
    public void sendTextMessageWithInvalidUTF8PayloadInOneFragment14() throws Exception {
        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);
        webSocket.connect();
        k3po.finish();
    }

    @Ignore("KG-12464")
    @Specification("sendTextMessageWithInvalidUTF8PayloadInOneFragment15")
    public void sendTextMessageWithInvalidUTF8PayloadInOneFragment15() throws Exception {
        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);
        webSocket.connect();
        k3po.finish();
    }

    @Ignore("KG-12464")
    @Specification("sendTextMessageWithInvalidUTF8PayloadInOneFragment16")
    public void sendTextMessageWithInvalidUTF8PayloadInOneFragment16() throws Exception {
        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);
        webSocket.connect();
        k3po.finish();
    }

    @Ignore("KG-12464")
    @Specification("sendTextMessageWithInvalidUTF8PayloadInOneFragment17")
    public void sendTextMessageWithInvalidUTF8PayloadInOneFragment17() throws Exception {
        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);
        webSocket.connect();
        k3po.finish();
    }

    @Ignore("KG-12464")
    @Specification("sendTextMessageWithInvalidUTF8PayloadInOneFragment18")
    public void sendTextMessageWithInvalidUTF8PayloadInOneFragment18() throws Exception {
        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);
        webSocket.connect();
        k3po.finish();
    }

    @Ignore("KG-12464")
    @Specification("sendTextMessageWithInvalidUTF8PayloadInOneFragment19")
    public void sendTextMessageWithInvalidUTF8PayloadInOneFragment19() throws Exception {
        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);
        webSocket.connect();
        k3po.finish();
    }

    @Ignore("KG-12464")
    @Specification("sendTextMessageWithInvalidUTF8PayloadInOneFragment20")
    public void sendTextMessageWithInvalidUTF8PayloadInOneFragment20() throws Exception {
        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);
        webSocket.connect();
        k3po.finish();
    }

    @Ignore("KG-12464")
    @Specification("sendTextMessageWithInvalidUTF8PayloadInOneFragment21")
    public void sendTextMessageWithInvalidUTF8PayloadInOneFragment21() throws Exception {
        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);
        webSocket.connect();
        k3po.finish();
    }

    @Ignore("KG-12464")
    @Specification("sendTextMessageWithInvalidUTF8PayloadInOneFragment22")
    public void sendTextMessageWithInvalidUTF8PayloadInOneFragment22() throws Exception {
        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);
        webSocket.connect();
        k3po.finish();
    }

    @Ignore("KG-12464")
    @Specification("sendTextMessageWithInvalidUTF8PayloadInOneFragment23")
    public void sendTextMessageWithInvalidUTF8PayloadInOneFragment23() throws Exception {
        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);
        webSocket.connect();
        k3po.finish();
    }

    @Ignore("KG-12464")
    @Specification("sendTextMessageWithInvalidUTF8PayloadInOneFragment24")
    public void sendTextMessageWithInvalidUTF8PayloadInOneFragment24() throws Exception {
        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);
        webSocket.connect();
        k3po.finish();
    }

    @Ignore("KG-12464")
    @Specification("sendTextMessageWithInvalidUTF8PayloadInOneFragment25")
    public void sendTextMessageWithInvalidUTF8PayloadInOneFragment25() throws Exception {
        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);
        webSocket.connect();
        k3po.finish();
    }

    @Ignore("KG-12464")
    @Specification("sendTextMessageWithInvalidUTF8PayloadInOneFragment26")
    public void sendTextMessageWithInvalidUTF8PayloadInOneFragment26() throws Exception {
        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);
        webSocket.connect();
        k3po.finish();
    }

    @Ignore("KG-12464")
    @Specification("sendTextMessageWithInvalidUTF8PayloadInOneFragment27")
    public void sendTextMessageWithInvalidUTF8PayloadInOneFragment27() throws Exception {
        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);
        webSocket.connect();
        k3po.finish();
    }

    @Ignore("KG-12464")
    @Specification("sendTextMessageWithInvalidUTF8PayloadInOneFragment28")
    public void sendTextMessageWithInvalidUTF8PayloadInOneFragment28() throws Exception {
        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);
        webSocket.connect();
        k3po.finish();
    }

    @Ignore("KG-12464")
    @Specification("sendTextMessageWithInvalidUTF8PayloadInOneFragment29")
    public void sendTextMessageWithInvalidUTF8PayloadInOneFragment29() throws Exception {
        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);
        webSocket.connect();
        k3po.finish();
    }

    @Ignore("KG-12464")
    @Specification("sendTextMessageWithInvalidUTF8PayloadInOneFragment30")
    public void sendTextMessageWithInvalidUTF8PayloadInOneFragment30() throws Exception {
        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);
        webSocket.connect();
        k3po.finish();
    }

    @Ignore("KG-12464")
    @Specification("sendTextMessageWithInvalidUTF8PayloadInOneFragment31")
    public void sendTextMessageWithInvalidUTF8PayloadInOneFragment31() throws Exception {
        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);
        webSocket.connect();
        k3po.finish();
    }

    @Ignore("KG-12464")
    @Specification("sendTextMessageWithInvalidUTF8PayloadInOneFragment32")
    public void sendTextMessageWithInvalidUTF8PayloadInOneFragment32() throws Exception {
        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);
        webSocket.connect();
        k3po.finish();
    }

    @Ignore("KG-12464")
    @Specification("sendTextMessageWithInvalidUTF8PayloadInOneFragment33")
    public void sendTextMessageWithInvalidUTF8PayloadInOneFragment33() throws Exception {
        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);
        webSocket.connect();
        k3po.finish();
    }

    @Ignore("KG-12464")
    @Specification("sendTextMessageWithInvalidUTF8PayloadInOneFragment34")
    public void sendTextMessageWithInvalidUTF8PayloadInOneFragment34() throws Exception {
        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);
        webSocket.connect();
        k3po.finish();
    }

    @Ignore("KG-12464")
    @Specification("sendTextMessageWithInvalidUTF8PayloadInOneFragment35")
    public void sendTextMessageWithInvalidUTF8PayloadInOneFragment35() throws Exception {
        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);
        webSocket.connect();
        k3po.finish();
    }

    @Ignore("KG-12464")
    @Specification("sendTextMessageWithInvalidUTF8PayloadInOneFragment36")
    public void sendTextMessageWithInvalidUTF8PayloadInOneFragment36() throws Exception {
        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);
        webSocket.connect();
        k3po.finish();
    }

    @Ignore("KG-12464")
    @Specification("sendTextMessageWithInvalidUTF8PayloadInOneFragment37")
    public void sendTextMessageWithInvalidUTF8PayloadInOneFragment37() throws Exception {
        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);
        webSocket.connect();
        k3po.finish();
    }

    @Ignore("KG-12464")
    @Specification("sendTextMessageWithInvalidUTF8PayloadInOneFragment38")
    public void sendTextMessageWithInvalidUTF8PayloadInOneFragment38() throws Exception {
        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);
        webSocket.connect();
        k3po.finish();
    }

    @Ignore("KG-12464")
    @Specification("sendTextMessageWithInvalidUTF8PayloadInOneFragment39")

    public void sendTextMessageWithInvalidUTF8PayloadInOneFragment39() throws Exception {
        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);
        webSocket.connect();
        k3po.finish();
    }

    @Ignore("KG-12464")
    @Specification("sendTextMessageWithInvalidUTF8PayloadInOneFragment40")

    public void sendTextMessageWithInvalidUTF8PayloadInOneFragment40() throws Exception {
        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);
        webSocket.connect();
        k3po.finish();
    }

    @Ignore("KG-12464")
    @Specification("sendTextMessageWithInvalidUTF8PayloadInOneFragment41")

    public void sendTextMessageWithInvalidUTF8PayloadInOneFragment41() throws Exception {
        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);
        webSocket.connect();
        k3po.finish();
    }

    @Ignore("KG-12464")
    @Specification("sendTextMessageWithInvalidUTF8PayloadInOneFragment42")

    public void sendTextMessageWithInvalidUTF8PayloadInOneFragment42() throws Exception {
        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);
        webSocket.connect();
        k3po.finish();
    }

    @Ignore("KG-12464")
    @Specification("sendTextMessageWithInvalidUTF8PayloadInOneFragment43")

    public void sendTextMessageWithInvalidUTF8PayloadInOneFragment43() throws Exception {
        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);
        webSocket.connect();
        k3po.finish();
    }

    @Ignore("KG-12464")
    @Specification("sendTextMessageWithInvalidUTF8PayloadInOneFragment44")

    public void sendTextMessageWithInvalidUTF8PayloadInOneFragment44() throws Exception {
        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);
        webSocket.connect();
        k3po.finish();
    }

    @Ignore("KG-12464")
    @Specification("sendTextMessageWithInvalidUTF8PayloadInOneFragment45")

    public void sendTextMessageWithInvalidUTF8PayloadInOneFragment45() throws Exception {
        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);
        webSocket.connect();
        k3po.finish();
    }

    @Ignore("KG-12464")
    @Specification("sendTextMessageWithInvalidUTF8PayloadInOneFragment46")

    public void sendTextMessageWithInvalidUTF8PayloadInOneFragment46() throws Exception {
        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);
        webSocket.connect();
        k3po.finish();
    }

    @Ignore("KG-12464")
    @Specification("sendTextMessageWithInvalidUTF8PayloadInOneFragment47")

    public void sendTextMessageWithInvalidUTF8PayloadInOneFragment47() throws Exception {
        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);
        webSocket.connect();
        k3po.finish();
    }

    @Ignore("KG-12464")
    @Specification("sendTextMessageWithInvalidUTF8PayloadInOneFragment48")

    public void sendTextMessageWithInvalidUTF8PayloadInOneFragment48() throws Exception {
        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);
        webSocket.connect();
        k3po.finish();
    }

    @Ignore("KG-12464")
    @Specification("sendTextMessageWithInvalidUTF8PayloadInOneFragment49")

    public void sendTextMessageWithInvalidUTF8PayloadInOneFragment49() throws Exception {
        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);
        webSocket.connect();
        k3po.finish();
    }

    @Ignore("KG-12464")
    @Specification("sendTextMessageWithInvalidUTF8PayloadInOneFragment50")

    public void sendTextMessageWithInvalidUTF8PayloadInOneFragment50() throws Exception {
        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);
        webSocket.connect();
        k3po.finish();
    }

    @Ignore("KG-12464")
    @Specification("sendTextMessageWithInvalidUTF8PayloadInOneFragment51")

    public void sendTextMessageWithInvalidUTF8PayloadInOneFragment51() throws Exception {
        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);
        webSocket.connect();
        k3po.finish();
    }

    @Ignore("KG-12464")
    @Specification("sendTextMessageWithInvalidUTF8PayloadInOneFragment52")

    public void sendTextMessageWithInvalidUTF8PayloadInOneFragment52() throws Exception {
        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);
        webSocket.connect();
        k3po.finish();
    }

    @Ignore("KG-12464")
    @Specification("sendTextMessageWithInvalidUTF8PayloadInOneFragment53")

    public void sendTextMessageWithInvalidUTF8PayloadInOneFragment53() throws Exception {
        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);
        webSocket.connect();
        k3po.finish();
    }

    @Ignore("KG-12464")
    @Specification("sendTextMessageWithInvalidUTF8PayloadInOneFragment54")

    public void sendTextMessageWithInvalidUTF8PayloadInOneFragment54() throws Exception {
        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);
        webSocket.connect();
        k3po.finish();
    }

    @Ignore("KG-12464")
    @Specification("sendTextMessageWithInvalidUTF8PayloadInOneFragment55")

    public void sendTextMessageWithInvalidUTF8PayloadInOneFragment55() throws Exception {
        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);
        webSocket.connect();
        k3po.finish();
    }

    @Ignore("KG-12464")
    @Specification("sendTextMessageWithInvalidUTF8PayloadInOneFragment56")

    public void sendTextMessageWithInvalidUTF8PayloadInOneFragment56() throws Exception {
        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);
        webSocket.connect();
        k3po.finish();
    }

    @Ignore("KG-12464")
    @Specification("sendTextMessageWithInvalidUTF8PayloadInOneFragment57")

    public void sendTextMessageWithInvalidUTF8PayloadInOneFragment57() throws Exception {
        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);
        webSocket.connect();
        k3po.finish();
    }

    @Ignore("KG-12464")
    @Specification("sendTextMessageWithInvalidUTF8PayloadInOneFragment58")

    public void sendTextMessageWithInvalidUTF8PayloadInOneFragment58() throws Exception {
        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);
        webSocket.connect();
        k3po.finish();
    }

    @Ignore("KG-12464")
    @Specification("sendTextMessageWithInvalidUTF8PayloadInOneFragment59")

    public void sendTextMessageWithInvalidUTF8PayloadInOneFragment59() throws Exception {
        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);
        webSocket.connect();
        k3po.finish();
    }

    @Ignore("KG-12464")
    @Specification("sendTextMessageWithInvalidUTF8PayloadInOneFragment60")

    public void sendTextMessageWithInvalidUTF8PayloadInOneFragment60() throws Exception {
        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);
        webSocket.connect();
        k3po.finish();
    }

    @Ignore("KG-12464")
    @Specification("sendTextMessageWithInvalidUTF8PayloadInOneFragment61")

    public void sendTextMessageWithInvalidUTF8PayloadInOneFragment61() throws Exception {
        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);
        webSocket.connect();
        k3po.finish();
    }

    @Ignore("KG-12464")
    @Specification("sendTextMessageWithInvalidUTF8PayloadInOneFragment62")

    public void sendTextMessageWithInvalidUTF8PayloadInOneFragment62() throws Exception {
        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);
        webSocket.connect();
        k3po.finish();
    }

    @Ignore("KG-12464")
    @Specification("sendTextMessageWithInvalidUTF8PayloadInOneFragment63")

    public void sendTextMessageWithInvalidUTF8PayloadInOneFragment63() throws Exception {
        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);
        webSocket.connect();
        k3po.finish();
    }

    @Ignore("KG-12464")
    @Specification("sendTextMessageWithInvalidUTF8PayloadInOneFragment64")

    public void sendTextMessageWithInvalidUTF8PayloadInOneFragment64() throws Exception {
        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);
        webSocket.connect();
        k3po.finish();
    }

    @Ignore("KG-12464")
    @Specification("sendTextMessageWithInvalidUTF8PayloadInOneFragment65")

    public void sendTextMessageWithInvalidUTF8PayloadInOneFragment65() throws Exception {
        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);
        webSocket.connect();
        k3po.finish();
    }

    @Ignore("KG-12464")
    @Specification("sendTextMessageWithInvalidUTF8PayloadInOneFragment66")

    public void sendTextMessageWithInvalidUTF8PayloadInOneFragment66() throws Exception {
        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);
        webSocket.connect();
        k3po.finish();
    }

    @Ignore("KG-12464")
    @Specification("sendTextMessageWithInvalidUTF8PayloadInOneFragment67")

    public void sendTextMessageWithInvalidUTF8PayloadInOneFragment67() throws Exception {
        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);
        webSocket.connect();
        k3po.finish();
    }

    @Ignore("KG-12464")
    @Specification("sendTextMessageWithInvalidUTF8PayloadInOneFragment68")

    public void sendTextMessageWithInvalidUTF8PayloadInOneFragment68() throws Exception {
        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);
        webSocket.connect();
        k3po.finish();
    }

    @Ignore("KG-12464")
    @Specification("sendTextMessageWithInvalidUTF8PayloadInOneFragment69")

    public void sendTextMessageWithInvalidUTF8PayloadInOneFragment69() throws Exception {
        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);
        webSocket.connect();
        k3po.finish();
    }

    @Ignore("KG-12464")
    @Specification("sendTextMessageWithInvalidUTF8PayloadInOneFragment70")

    public void sendTextMessageWithInvalidUTF8PayloadInOneFragment70() throws Exception {
        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);
        webSocket.connect();
        k3po.finish();
    }

    @Ignore("KG-12447 and KG-12464")
    @Specification("sendTextMessageWithInvalidUTF8PayloadInOneOctetFragments")

    public void sendTextMessageWithInvalidUTF8PayloadInOneOctetFragments() throws Exception {
        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);
        webSocket.connect();
        k3po.finish();
    }

    @Specification("sendTextMessageWithValidUTF8PayloadInOneFragment")

    public void sendTextMessageWithValidUTF8PayloadInOneFragment() throws Exception {
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

    @Specification("sendTextMessageWithValidUTF8PayloadInOneFragment2")

    public void sendTextMessageWithValidUTF8PayloadInOneFragment2() throws Exception {
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

    @Specification("sendTextMessageWithValidUTF8PayloadInOneFragment3")

    public void sendTextMessageWithValidUTF8PayloadInOneFragment3() throws Exception {
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

    @Specification("sendTextMessageWithValidUTF8PayloadInOneFragment4")

    public void sendTextMessageWithValidUTF8PayloadInOneFragment4() throws Exception {
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

    @Specification("sendTextMessageWithValidUTF8PayloadInOneFragment5")

    public void sendTextMessageWithValidUTF8PayloadInOneFragment5() throws Exception {
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

    @Specification("sendTextMessageWithValidUTF8PayloadInOneFragment6")

    public void sendTextMessageWithValidUTF8PayloadInOneFragment6() throws Exception {
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


    @Specification("sendTextMessageWithValidUTF8PayloadInOneFragment7")

    public void sendTextMessageWithValidUTF8PayloadInOneFragment7() throws Exception {
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


    @Specification("sendTextMessageWithValidUTF8PayloadInOneFragment8")

    public void sendTextMessageWithValidUTF8PayloadInOneFragment8() throws Exception {
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


    @Specification("sendTextMessageWithValidUTF8PayloadInOneFragment9")

    public void sendTextMessageWithValidUTF8PayloadInOneFragment9() throws Exception {
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


    @Specification("sendTextMessageWithValidUTF8PayloadInOneFragment10")

    public void sendTextMessageWithValidUTF8PayloadInOneFragment10() throws Exception {
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


    @Specification("sendTextMessageWithValidUTF8PayloadInOneFragment11")

    public void sendTextMessageWithValidUTF8PayloadInOneFragment11() throws Exception {
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


    @Specification("sendTextMessageWithValidUTF8PayloadInOneFragment12")

    public void sendTextMessageWithValidUTF8PayloadInOneFragment12() throws Exception {
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


    @Specification("sendTextMessageWithValidUTF8PayloadInOneFragment13")

    public void sendTextMessageWithValidUTF8PayloadInOneFragment13() throws Exception {
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


    @Specification("sendTextMessageWithValidUTF8PayloadInOneFragment14")

    public void sendTextMessageWithValidUTF8PayloadInOneFragment14() throws Exception {
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


    @Specification("sendTextMessageWithValidUTF8PayloadInOneFragment15")

    public void sendTextMessageWithValidUTF8PayloadInOneFragment15() throws Exception {
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


    @Specification("sendTextMessageWithValidUTF8PayloadInOneFragment16")

    public void sendTextMessageWithValidUTF8PayloadInOneFragment16() throws Exception {
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


    @Specification("sendTextMessageWithValidUTF8PayloadInOneFragment17")

    public void sendTextMessageWithValidUTF8PayloadInOneFragment17() throws Exception {
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


    @Specification("sendTextMessageWithValidUTF8PayloadInOneFragment18")

    public void sendTextMessageWithValidUTF8PayloadInOneFragment18() throws Exception {
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


    @Specification("sendTextMessageWithValidUTF8PayloadInOneFragment19")

    public void sendTextMessageWithValidUTF8PayloadInOneFragment19() throws Exception {
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


    @Specification("sendTextMessageWithValidUTF8PayloadInOneFragment20")

    public void sendTextMessageWithValidUTF8PayloadInOneFragment20() throws Exception {
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


    @Specification("sendTextMessageWithValidUTF8PayloadInOneFragment21")

    public void sendTextMessageWithValidUTF8PayloadInOneFragment21() throws Exception {
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


    @Specification("sendTextMessageWithValidUTF8PayloadInOneFragment22")

    public void sendTextMessageWithValidUTF8PayloadInOneFragment22() throws Exception {
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


    @Specification("sendTextMessageWithValidUTF8PayloadInOneFragment23")

    public void sendTextMessageWithValidUTF8PayloadInOneFragment23() throws Exception {
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


    @Specification("sendTextMessageWithValidUTF8PayloadInOneFragment24")

    public void sendTextMessageWithValidUTF8PayloadInOneFragment24() throws Exception {
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


    @Specification("sendTextMessageWithValidUTF8PayloadInOneFragment25")

    public void sendTextMessageWithValidUTF8PayloadInOneFragment25() throws Exception {
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


    @Specification("sendTextMessageWithValidUTF8PayloadInOneFragment26")

    public void sendTextMessageWithValidUTF8PayloadInOneFragment26() throws Exception {
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


    @Specification("sendTextMessageWithValidUTF8PayloadInOneFragment27")

    public void sendTextMessageWithValidUTF8PayloadInOneFragment27() throws Exception {
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


    @Specification("sendTextMessageWithValidUTF8PayloadInOneFragment28")

    public void sendTextMessageWithValidUTF8PayloadInOneFragment28() throws Exception {
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


    @Specification("sendTextMessageWithValidUTF8PayloadInOneFragment29")

    public void sendTextMessageWithValidUTF8PayloadInOneFragment29() throws Exception {
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


    @Specification("sendTextMessageWithValidUTF8PayloadInOneFragment30")

    public void sendTextMessageWithValidUTF8PayloadInOneFragment30() throws Exception {
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


    @Specification("sendTextMessageWithValidUTF8PayloadInOneFragment31")

    public void sendTextMessageWithValidUTF8PayloadInOneFragment31() throws Exception {
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


    @Specification("sendTextMessageWithValidUTF8PayloadInOneFragment32")

    public void sendTextMessageWithValidUTF8PayloadInOneFragment32() throws Exception {
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


    @Specification("sendTextMessageWithValidUTF8PayloadInOneFragment33")

    public void sendTextMessageWithValidUTF8PayloadInOneFragment33() throws Exception {
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


    @Specification("sendTextMessageWithValidUTF8PayloadInOneFragment34")

    public void sendTextMessageWithValidUTF8PayloadInOneFragment34() throws Exception {
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


    @Specification("sendTextMessageWithValidUTF8PayloadInOneFragment35")

    public void sendTextMessageWithValidUTF8PayloadInOneFragment35() throws Exception {
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


    @Specification("sendTextMessageWithValidUTF8PayloadInOneFragment36")

    public void sendTextMessageWithValidUTF8PayloadInOneFragment36() throws Exception {
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


    @Specification("sendTextMessageWithValidUTF8PayloadInOneFragment37")

    public void sendTextMessageWithValidUTF8PayloadInOneFragment37() throws Exception {
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


    @Specification("sendTextMessageWithValidUTF8PayloadInOneFragment38")

    public void sendTextMessageWithValidUTF8PayloadInOneFragment38() throws Exception {
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


    @Specification("sendTextMessageWithValidUTF8PayloadInOneFragment39")

    public void sendTextMessageWithValidUTF8PayloadInOneFragment39() throws Exception {
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


    @Specification("sendTextMessageWithValidUTF8PayloadInOneFragment40")

    public void sendTextMessageWithValidUTF8PayloadInOneFragment40() throws Exception {
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


    @Specification("sendTextMessageWithValidUTF8PayloadInOneFragment41")

    public void sendTextMessageWithValidUTF8PayloadInOneFragment41() throws Exception {
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


    @Specification("sendTextMessageWithValidUTF8PayloadInOneFragment42")

    public void sendTextMessageWithValidUTF8PayloadInOneFragment42() throws Exception {
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


    @Specification("sendTextMessageWithValidUTF8PayloadInOneFragment43")

    public void sendTextMessageWithValidUTF8PayloadInOneFragment43() throws Exception {
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


    @Specification("sendTextMessageWithValidUTF8PayloadInOneFragment44")

    public void sendTextMessageWithValidUTF8PayloadInOneFragment44() throws Exception {
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


    @Specification("sendTextMessageWithValidUTF8PayloadInOneFragment45")

    public void sendTextMessageWithValidUTF8PayloadInOneFragment45() throws Exception {
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


    @Specification("sendTextMessageWithValidUTF8PayloadInOneFragment46")

    public void sendTextMessageWithValidUTF8PayloadInOneFragment46() throws Exception {
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


    @Specification("sendTextMessageWithValidUTF8PayloadInOneFragment47")

    public void sendTextMessageWithValidUTF8PayloadInOneFragment47() throws Exception {
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


    @Specification("sendTextMessageWithValidUTF8PayloadInOneFragment48")

    public void sendTextMessageWithValidUTF8PayloadInOneFragment48() throws Exception {
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


    @Specification("sendTextMessageWithValidUTF8PayloadInOneFragment49")

    public void sendTextMessageWithValidUTF8PayloadInOneFragment49() throws Exception {
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


    @Specification("sendTextMessageWithValidUTF8PayloadInOneFragment50")

    public void sendTextMessageWithValidUTF8PayloadInOneFragment50() throws Exception {
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


    @Specification("sendTextMessageWithValidUTF8PayloadInOneFragment51")
    @Test(timeout = 1000)
    public void sendTextMessageWithValidUTF8PayloadInOneFragment51() throws Exception {
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


    @Specification("sendTextMessageWithValidUTF8PayloadInOneFragment52")

    public void sendTextMessageWithValidUTF8PayloadInOneFragment52() throws Exception {
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


    @Specification("sendTextMessageWithValidUTF8PayloadInOneFragment53")

    public void sendTextMessageWithValidUTF8PayloadInOneFragment53() throws Exception {
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


    @Specification("sendTextMessageWithValidUTF8PayloadInOneFragment54")

    public void sendTextMessageWithValidUTF8PayloadInOneFragment54() throws Exception {
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


    @Specification("sendTextMessageWithValidUTF8PayloadInOneFragment55")

    public void sendTextMessageWithValidUTF8PayloadInOneFragment55() throws Exception {
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


    @Specification("sendTextMessageWithValidUTF8PayloadInOneFragment56")

    public void sendTextMessageWithValidUTF8PayloadInOneFragment56() throws Exception {
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

    @Ignore("KG-12463")
    @Specification("sendTextMessageWithValidUTF8PayloadInOneFragment57")

    public void sendTextMessageWithValidUTF8PayloadInOneFragment57() throws Exception {
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


    @Specification("sendTextMessageWithValidUTF8PayloadInOneFragment58")

    public void sendTextMessageWithValidUTF8PayloadInOneFragment58() throws Exception {
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


    @Specification("sendTextMessageWithValidUTF8PayloadInOneFragment59")

    public void sendTextMessageWithValidUTF8PayloadInOneFragment59() throws Exception {
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


    @Specification("sendTextMessageWithValidUTF8PayloadInOneFragment60")

    public void sendTextMessageWithValidUTF8PayloadInOneFragment60() throws Exception {
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


    @Specification("sendTextMessageWithValidUTF8PayloadInOneFragment61")

    public void sendTextMessageWithValidUTF8PayloadInOneFragment61() throws Exception {
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


    @Specification("sendTextMessageWithValidUTF8PayloadInOneFragment62")

    public void sendTextMessageWithValidUTF8PayloadInOneFragment62() throws Exception {
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


    @Specification("sendTextMessageWithValidUTF8PayloadInOneFragment63")

    public void sendTextMessageWithValidUTF8PayloadInOneFragment63() throws Exception {
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


    @Specification("sendTextMessageWithValidUTF8PayloadInOneFragment64")

    public void sendTextMessageWithValidUTF8PayloadInOneFragment64() throws Exception {
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

    @Ignore("KG-12447")
    @Specification("sendTextMessageWithValidUTF8PayloadInOneOctetFragments")

    public void sendTextMessageWithValidUTF8PayloadInOneOctetFragments() throws Exception {
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

    @Ignore("KG-12447")
    @Specification("sendTextMessageWithValidUTF8PayloadInOneOctetFragments2")

    public void sendTextMessageWithValidUTF8PayloadInOneOctetFragments2() throws Exception {
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

    @Ignore("KG-12447")
    @Specification("sendTextMessageWithValidUTF8PayloadInTwoFragmentsFragmentedOnCodePointBoundary")
    public void sendTextMessageWithValidUTF8PayloadInTwoFragmentsFragmentedOnCodePointBoundary() throws Exception {
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

    @Ignore("KG-12447")
    @Specification("sendThreeFragmentedTextMessagesFirstAndLastLengthZeroMiddleNonEmpty")

    public void sendThreeFragmentedTextMessagesFirstAndLastLengthZeroMiddleNonEmpty() throws Exception {
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

    @Ignore("KG-12447")
    @Specification("sendThreeFragmentedTextMessagesOfLengthZero")
    public void sendThreeFragmentedTextMessagesOfLengthZero() throws Exception {
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
