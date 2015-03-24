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

package org.kaazing.gateway.client.impl.autobahn.fragmentation;

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

public class FragmentationIT {

	private final K3poRule k3po = new K3poRule();

	private final TestRule timeout = new DisableOnDebug(new Timeout(5, SECONDS));

	@Rule
	public final TestRule chain = outerRule(k3po).around(timeout);

    @Ignore("KG-12447")
    @Specification("sendContinuationFrameWithFINEqualsFalseWhenThereIsNothingToContinueThenTextMessageInTwoFragmentsTwice")
    @Test(timeout = 1500)
    public void sendContinuationFrameWithFINEqualsFalseWhenThereIsNothingToContinueThenTextMessageInTwoFragmentsTwice()
            throws Exception {
        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);

        webSocket.connect();
        k3po.finish();
    }

    @Ignore("KG-12447")
    @Specification("sendContinuationFrameWithFINEqualsTrueWhenThereIsNothingToContinueThenTextMessageInTwoFragmentsTwice")
    @Test(timeout = 1500)
    public void sendContinuationFrameWithFINEqualsTrueWhenThereIsNothingToContinueThenTextMessageInTwoFragmentsTwice()
            throws Exception {
        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);

        webSocket.connect();
        k3po.finish();
    }

    @Ignore("KG-12447")
    @Specification("sendPingInTwoFragments")
    @Test(timeout = 1500)
    public void sendPingInTwoFragments() throws Exception {
        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);

        webSocket.connect();
        k3po.finish();
    }

    @Ignore("KG-12447")
    @Specification("sendPongInTwoFragments")
    @Test(timeout = 1500)
    public void sendPongInTwoFragments() throws Exception {
        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);

        webSocket.connect();
        k3po.finish();
    }

    @Ignore("KG-12447")
    @Specification("sendTextMessageAfterContinuationFrameWithFINEqualsFalseWhenThereIsNothingToContinueInFrameWiseChops")
    @Test(timeout = 1500)
    public void sendTextMessageAfterContinuationFrameWithFINEqualsFalseWhenThereIsNothingToContinueInFrameWiseChops()
            throws Exception {
        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);

        webSocket.connect();
        k3po.finish();
    }

    @Ignore("KG-12447")
    @Specification("sendTextMessageAfterContinuationFrameWithFINEqualsFalseWhenThereIsNothingToContinueSentInOctetWiseChops")
    @Test(timeout = 1500)
    public void sendTextMessageAfterContinuationFrameWithFINEqualsFalseWhenThereIsNothingToContinueSentInOctetWiseChops()
            throws Exception {
        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);

        webSocket.connect();
        k3po.finish();
    }

    @Ignore("KG-12447")
    @Specification("sendTextMessageAfterContinuationFrameWithFINEqualsFalseWhenThereIsNothingToContinueSentInOneChop")
    @Test(timeout = 1500)
    public void sendTextMessageAfterContinuationFrameWithFINEqualsFalseWhenThereIsNothingToContinueSentInOneChop()
            throws Exception {
        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);

        webSocket.connect();
        k3po.finish();
    }

    @Ignore("KG-12447")
    @Specification("sendTextMessageAfterContinuationFrameWithFINEqualsTrueWhenNothingToContinueSentInFrameWiseChops")
    @Test(timeout = 1500)
    public void sendTextMessageAfterContinuationFrameWithFINEqualsTrueWhenNothingToContinueSentInFrameWiseChops()
            throws Exception {
        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);

        webSocket.connect();
        k3po.finish();
    }

    @Ignore("KG-12447")
    @Specification("sendTextMessageAfterContinuationFrameWithFINEqualsTrueWhenThereIsNothingToContinueSentInOctetWiseChops")
    @Test(timeout = 1500)
    public void sendTextMessageAfterContinuationFrameWithFINEqualsTrueWhenThereIsNothingToContinueSentInOctetWiseChops()
            throws Exception {
        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);

        webSocket.connect();
        k3po.finish();
    }

    @Ignore("KG-12447")
    @Specification("sendTextMessageAfterContinuationFrameWithFINEqualsTrueWhenThereIsNothingToContinueSentInOneChop")
    @Test(timeout = 1500)
    public void sendTextMessageAfterContinuationFrameWithFINEqualsTrueWhenThereIsNothingToContinueSentInOneChop()
            throws Exception {
        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);

        webSocket.connect();
        k3po.finish();
    }

    @Ignore("KG-12447")
    @Specification("sendTextMessageInMultipleFramesWithPingswithPayloadsInBetween")
    @Test(timeout = 5000)
    public void sendTextMessageInMultipleFramesWithPingswithPayloadsInBetween() throws Exception {
        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);

        webSocket.connect();

        WebSocketMessageWriter writer = webSocket.getMessageWriter();
        WebSocketMessageReader reader = webSocket.getMessageReader();

        reader.next();
        CharSequence text = reader.getText();

        // wait for response to pings to be sent
        Thread.sleep(10);

        writer.writeText(text);

        k3po.finish();
    }

    @Ignore("KG-12447")
    @Specification("sendTextMessageInMultipleFramesWithPingswithPayloadsInBetweenAndAllFramesWithSYNCEqualsTrue")
    @Test(timeout = 5000)
    public void sendTextMessageInMultipleFramesWithPingswithPayloadsInBetweenAndAllFramesWithSYNCEqualsTrue()
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

        // wait for response to pings to be sent
        Thread.sleep(10);

        writer.writeText(text);

        k3po.finish();
    }

    @Ignore("KG-12447")
    @Specification("sendTextMessageInTwoFragments")
    @Test(timeout = 5000)
    public void sendTextMessageInTwoFragments() throws Exception {
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
    @Specification("sendTextMessageInTwoFragmentsInFrameWiseChops")
    @Test(timeout = 5000)
    public void sendTextMessageInTwoFragmentsInFrameWiseChops() throws Exception {
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
    @Specification("sendTextMessageInTwoFragmentsInOctetWiseChops")
    @Test(timeout = 5000)
    public void sendTextMessageInTwoFragmentsInOctetWiseChops() throws Exception {
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
    @Specification("sendTextMessageInTwoFragmentsThenContinuationWithFINEqualsFalseAndNothingToContinueThenUnfragmentedTextMessage")
    @Test(timeout = 1500)
    public void sendTextMessageInTwoFragmentsThenContinuationWithFINEqualsFalseAndNothingToContinueThenUnfragmentedTextMessage()
            throws Exception {
        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);

        webSocket.connect();
        k3po.finish();
    }

    @Ignore("KG-12447")
    @Specification("sendTextMessageInTwoFragmentsWithBothFrameOpcodesSetToText")
    @Test(timeout = 1500)
    public void sendTextMessageInTwoFragmentsWithBothFrameOpcodesSetToText() throws Exception {
        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);

        webSocket.connect();
        k3po.finish();
    }

    @Ignore("KG-12447")
    @Specification("sendTextMessageInTwoFragmentsWithOnePingWithPayloadInBetween")
    @Test(timeout = 5000)
    public void sendTextMessageInTwoFragmentsWithOnePingWithPayloadInBetween() throws Exception {
        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);

        webSocket.connect();

        WebSocketMessageWriter writer = webSocket.getMessageWriter();
        WebSocketMessageReader reader = webSocket.getMessageReader();

        reader.next();
        CharSequence text = reader.getText();

        // wait for response to ping
        Thread.sleep(10);
        writer.writeText(text);

        k3po.finish();
    }

    @Ignore("KG-12447")
    @Specification("sendTextMessageInTwoFragmentsWithOnePingWithPayloadInBetweenInFrameWiseChops")
    @Test(timeout = 5000)
    public void sendTextMessageInTwoFragmentsWithOnePingWithPayloadInBetweenInFrameWiseChops() throws Exception {
        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);

        webSocket.connect();

        WebSocketMessageWriter writer = webSocket.getMessageWriter();
        WebSocketMessageReader reader = webSocket.getMessageReader();

        reader.next();
        CharSequence text = reader.getText();

        // wait for response to ping
        Thread.sleep(10);
        writer.writeText(text);

        k3po.finish();
    }

    @Ignore("KG-12447")
    @Specification("sendTextMessageInTwoFragmentsWithOnePingWithPayloadInBetweenInOctetWiseChops")
    @Test(timeout = 5000)
    public void sendTextMessageInTwoFragmentsWithOnePingWithPayloadInBetweenInOctetWiseChops() throws Exception {
        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);

        webSocket.connect();

        WebSocketMessageWriter writer = webSocket.getMessageWriter();
        WebSocketMessageReader reader = webSocket.getMessageReader();

        reader.next();
        CharSequence text = reader.getText();

        // wait for response to ping
        Thread.sleep(10);
        writer.writeText(text);

        k3po.finish();
    }
}
