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

public class FragmentationIT {

    @Rule
    public RobotRule robot = new RobotRule();

    @Ignore("KG-12447")
    @Robotic(script = "sendContinuationFrameWithFINEqualsFalseWhenThereIsNothingToContinueThenTextMessageInTwoFragmentsTwice")
    @Test(timeout = 1500)
    public void sendContinuationFrameWithFINEqualsFalseWhenThereIsNothingToContinueThenTextMessageInTwoFragmentsTwice()
            throws Exception {
        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);

        webSocket.connect();
        robot.join();
    }

    @Ignore("KG-12447")
    @Robotic(script = "sendContinuationFrameWithFINEqualsTrueWhenThereIsNothingToContinueThenTextMessageInTwoFragmentsTwice")
    @Test(timeout = 1500)
    public void sendContinuationFrameWithFINEqualsTrueWhenThereIsNothingToContinueThenTextMessageInTwoFragmentsTwice()
            throws Exception {
        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);

        webSocket.connect();
        robot.join();
    }

    @Ignore("KG-12447")
    @Robotic(script = "sendPingInTwoFragments")
    @Test(timeout = 1500)
    public void sendPingInTwoFragments() throws Exception {
        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);

        webSocket.connect();
        robot.join();
    }

    @Ignore("KG-12447")
    @Robotic(script = "sendPongInTwoFragments")
    @Test(timeout = 1500)
    public void sendPongInTwoFragments() throws Exception {
        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);

        webSocket.connect();
        robot.join();
    }

    @Ignore("KG-12447")
    @Robotic(script = "sendTextMessageAfterContinuationFrameWithFINEqualsFalseWhenThereIsNothingToContinueInFrameWiseChops")
    @Test(timeout = 1500)
    public void sendTextMessageAfterContinuationFrameWithFINEqualsFalseWhenThereIsNothingToContinueInFrameWiseChops()
            throws Exception {
        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);

        webSocket.connect();
        robot.join();
    }

    @Ignore("KG-12447")
    @Robotic(script = "sendTextMessageAfterContinuationFrameWithFINEqualsFalseWhenThereIsNothingToContinueSentInOctetWiseChops")
    @Test(timeout = 1500)
    public void sendTextMessageAfterContinuationFrameWithFINEqualsFalseWhenThereIsNothingToContinueSentInOctetWiseChops()
            throws Exception {
        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);

        webSocket.connect();
        robot.join();
    }

    @Ignore("KG-12447")
    @Robotic(script = "sendTextMessageAfterContinuationFrameWithFINEqualsFalseWhenThereIsNothingToContinueSentInOneChop")
    @Test(timeout = 1500)
    public void sendTextMessageAfterContinuationFrameWithFINEqualsFalseWhenThereIsNothingToContinueSentInOneChop()
            throws Exception {
        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);

        webSocket.connect();
        robot.join();
    }

    @Ignore("KG-12447")
    @Robotic(script = "sendTextMessageAfterContinuationFrameWithFINEqualsTrueWhenNothingToContinueSentInFrameWiseChops")
    @Test(timeout = 1500)
    public void sendTextMessageAfterContinuationFrameWithFINEqualsTrueWhenNothingToContinueSentInFrameWiseChops()
            throws Exception {
        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);

        webSocket.connect();
        robot.join();
    }

    @Ignore("KG-12447")
    @Robotic(script = "sendTextMessageAfterContinuationFrameWithFINEqualsTrueWhenThereIsNothingToContinueSentInOctetWiseChops")
    @Test(timeout = 1500)
    public void sendTextMessageAfterContinuationFrameWithFINEqualsTrueWhenThereIsNothingToContinueSentInOctetWiseChops()
            throws Exception {
        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);

        webSocket.connect();
        robot.join();
    }

    @Ignore("KG-12447")
    @Robotic(script = "sendTextMessageAfterContinuationFrameWithFINEqualsTrueWhenThereIsNothingToContinueSentInOneChop")
    @Test(timeout = 1500)
    public void sendTextMessageAfterContinuationFrameWithFINEqualsTrueWhenThereIsNothingToContinueSentInOneChop()
            throws Exception {
        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);

        webSocket.connect();
        robot.join();
    }

    @Ignore("KG-12447")
    @Robotic(script = "sendTextMessageInMultipleFramesWithPingswithPayloadsInBetween")
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

        robot.join();
    }

    @Ignore("KG-12447")
    @Robotic(script = "sendTextMessageInMultipleFramesWithPingswithPayloadsInBetweenAndAllFramesWithSYNCEqualsTrue")
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

        robot.join();
    }

    @Ignore("KG-12447")
    @Robotic(script = "sendTextMessageInTwoFragments")
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

        robot.join();
    }

    @Ignore("KG-12447")
    @Robotic(script = "sendTextMessageInTwoFragmentsInFrameWiseChops")
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

        robot.join();
    }

    @Ignore("KG-12447")
    @Robotic(script = "sendTextMessageInTwoFragmentsInOctetWiseChops")
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

        robot.join();
    }

    @Ignore("KG-12447")
    @Robotic(script = "sendTextMessageInTwoFragmentsThenContinuationWithFINEqualsFalseAndNothingToContinueThenUnfragmentedTextMessage")
    @Test(timeout = 1500)
    public void sendTextMessageInTwoFragmentsThenContinuationWithFINEqualsFalseAndNothingToContinueThenUnfragmentedTextMessage()
            throws Exception {
        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);

        webSocket.connect();
        robot.join();
    }

    @Ignore("KG-12447")
    @Robotic(script = "sendTextMessageInTwoFragmentsWithBothFrameOpcodesSetToText")
    @Test(timeout = 1500)
    public void sendTextMessageInTwoFragmentsWithBothFrameOpcodesSetToText() throws Exception {
        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);

        webSocket.connect();
        robot.join();
    }

    @Ignore("KG-12447")
    @Robotic(script = "sendTextMessageInTwoFragmentsWithOnePingWithPayloadInBetween")
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

        robot.join();
    }

    @Ignore("KG-12447")
    @Robotic(script = "sendTextMessageInTwoFragmentsWithOnePingWithPayloadInBetweenInFrameWiseChops")
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

        robot.join();
    }

    @Ignore("KG-12447")
    @Robotic(script = "sendTextMessageInTwoFragmentsWithOnePingWithPayloadInBetweenInOctetWiseChops")
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

        robot.join();
    }
}
