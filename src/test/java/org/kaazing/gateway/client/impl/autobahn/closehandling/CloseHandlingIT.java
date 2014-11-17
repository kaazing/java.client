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

public class CloseHandlingIT {
    @Rule
    public RobotRule robot = new RobotRule();

    @Robotic(script = "send256KMessageFollowedByCloseThenPing")
    @Test(timeout = 10000)
    public void send256KMessageFollowedByCloseThenPing() throws Exception {

        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);

        webSocket.connect();
        robot.join();
    }

    @Robotic(script = "sendCloseFrameWithCloseCode")
    @Test(timeout = 1500)
    public void sendCloseFrameWithCloseCode() throws Exception {

        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);

        webSocket.connect();
        robot.join();
    }

    @Robotic(script = "sendCloseFrameWithCloseCodeAndCloseReason")
    @Test(timeout = 1500)
    public void sendCloseFrameWithCloseCodeAndCloseReason() throws Exception {

        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);

        webSocket.connect();
        robot.join();
    }

    @Robotic(script = "sendCloseFrameWithCloseCodeAndCloseReasonOfMaximumLength")
    @Test(timeout = 1500)
    public void sendCloseFrameWithCloseCodeAndCloseReasonOfMaximumLength() throws Exception {

        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);

        webSocket.connect();
        robot.join();
    }

    @Ignore("KG-12453")
    @Robotic(script = "sendCloseFrameWithCloseCodeAndCloseReasonWhichIsTooLong")
    @Test(timeout = 1500)
    public void sendCloseFrameWithCloseCodeAndCloseReasonWhichIsTooLong() throws Exception {

        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);

        webSocket.connect();
        robot.join();
    }

    @Ignore("KG-12453")
    @Robotic(script = "sendCloseFrameWithInvalidCloseCode1004")
    @Test(timeout = 1500)
    public void sendCloseFrameWithInvalidCloseCode1004() throws Exception {

        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);

        webSocket.connect();
        robot.join();
    }

    @Ignore("KG-12453")
    @Robotic(script = "sendCloseFrameWithInvalidCloseCode1006")
    @Test(timeout = 1500)
    public void sendCloseFrameWithInvalidCloseCode1006() throws Exception {

        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);

        webSocket.connect();
        robot.join();
    }

    @Ignore("KG-12453")
    @Robotic(script = "sendCloseFrameWithInvalidCloseCode1012")
    @Test(timeout = 1500)
    public void sendCloseFrameWithInvalidCloseCode1012() throws Exception {

        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);

        webSocket.connect();
        robot.join();
    }

    @Ignore("KG-12453")
    @Robotic(script = "sendCloseFrameWithInvalidCloseCode1013")
    @Test(timeout = 1500)
    public void sendCloseFrameWithInvalidCloseCode1013() throws Exception {

        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);

        webSocket.connect();
        robot.join();
    }

    @Ignore("KG-12453")
    @Robotic(script = "sendCloseFrameWithInvalidCloseCode1014")
    @Test(timeout = 1500)
    public void sendCloseFrameWithInvalidCloseCode1014() throws Exception {

        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);

        webSocket.connect();
        robot.join();
    }

    @Ignore("KG-12453")
    @Robotic(script = "sendCloseFrameWithInvalidCloseCode1015")
    @Test(timeout = 1500)
    public void sendCloseFrameWithInvalidCloseCode1015() throws Exception {

        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);

        webSocket.connect();
        robot.join();
    }

    @Ignore("KG-12453")
    @Robotic(script = "sendCloseFrameWithInvalidCloseCode1016")
    @Test(timeout = 1500)
    public void sendCloseFrameWithInvalidCloseCode1016() throws Exception {

        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);

        webSocket.connect();
        robot.join();
    }

    @Ignore("KG-12453")
    @Robotic(script = "sendCloseFrameWithInvalidCloseCode1100")
    @Test(timeout = 1500)
    public void sendCloseFrameWithInvalidCloseCode1100() throws Exception {

        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);

        webSocket.connect();
        robot.join();
    }

    @Ignore("KG-12453")
    @Robotic(script = "sendCloseFrameWithInvalidCloseCode2000")
    @Test(timeout = 1500)
    public void sendCloseFrameWithInvalidCloseCode2000() throws Exception {

        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);

        webSocket.connect();
        robot.join();
    }

    @Ignore("KG-12453")
    @Robotic(script = "sendCloseFrameWithInvalidCloseCode2999")
    @Test(timeout = 1500)
    public void sendCloseFrameWithInvalidCloseCode2999() throws Exception {

        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);

        webSocket.connect();
        robot.join();
    }

    @Ignore("KG-12453")
    @Robotic(script = "sendCloseFrameWithInvalidCloseCode999")
    @Test(timeout = 1500)
    public void sendCloseFrameWithInvalidCloseCode999() throws Exception {

        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);

        webSocket.connect();
        robot.join();
    }

    @Ignore("KG-12453")
    @Robotic(script = "sendCloseFrameWithInvalidUTF8Payload")
    @Test(timeout = 1500)
    public void sendCloseFrameWithInvalidUTF8Payload() throws Exception {

        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);

        webSocket.connect();
        robot.join();
    }

    @Ignore("KG-12453")
    @Robotic(script = "sendCloseFrameWithPayloadLength1")
    @Test(timeout = 1500)
    public void sendCloseFrameWithPayloadLength1() throws Exception {

        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);

        webSocket.connect();
        robot.join();
    }

    @Robotic(script = "sendCloseFrameWithPayloadLengthZero")
    @Test(timeout = 1500)
    public void sendCloseFrameWithPayloadLengthZero() throws Exception {

        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);

        webSocket.connect();
        robot.join();
    }

    @Robotic(script = "sendCloseWithCloseCode5000")
    @Test(timeout = 1500)
    public void sendCloseWithCloseCode5000() throws Exception {

        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);

        webSocket.connect();
        robot.join();
    }

    @Robotic(script = "sendCloseWithCloseCode65535")
    @Test(timeout = 1500)
    public void sendCloseWithCloseCode65535() throws Exception {

        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);

        webSocket.connect();
        robot.join();
    }

    @Ignore("KG-12453")
    @Robotic(script = "sendCloseWithInvalidCloseCode1005")
    @Test(timeout = 1500)
    public void sendCloseWithInvalidCloseCode1005() throws Exception {

        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);

        webSocket.connect();
        robot.join();
    }

    @Ignore("KG-12453")
    @Robotic(script = "sendCloseWithInvalidCloseCodeZero")
    @Test(timeout = 1500)
    public void sendCloseWithInvalidCloseCodeZero() throws Exception {

        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);

        webSocket.connect();
        robot.join();
    }

    @Robotic(script = "sendCloseWithValidCloseCode1000")
    @Test(timeout = 1500)
    public void sendCloseWithValidCloseCode1000() throws Exception {

        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);

        webSocket.connect();
        robot.join();
    }

    @Robotic(script = "sendCloseWithValidCloseCode1001")
    @Test(timeout = 1500)
    public void sendCloseWithValidCloseCode1001() throws Exception {

        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);

        webSocket.connect();
        robot.join();
    }

    @Robotic(script = "sendCloseWithValidCloseCode1002")
    @Test(timeout = 1500)
    public void sendCloseWithValidCloseCode1002() throws Exception {

        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);

        webSocket.connect();
        robot.join();
    }

    @Robotic(script = "sendCloseWithValidCloseCode1003")
    @Test(timeout = 1500)
    public void sendCloseWithValidCloseCode1003() throws Exception {

        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);

        webSocket.connect();
        robot.join();
    }

    @Robotic(script = "sendCloseWithValidCloseCode1007")
    @Test(timeout = 1500)
    public void sendCloseWithValidCloseCode1007() throws Exception {

        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);

        webSocket.connect();
        robot.join();
    }

    @Robotic(script = "sendCloseWithValidCloseCode1008")
    @Test(timeout = 1500)
    public void sendCloseWithValidCloseCode1008() throws Exception {

        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);

        webSocket.connect();
        robot.join();
    }

    @Robotic(script = "sendCloseWithValidCloseCode1009")
    @Test(timeout = 1500)
    public void sendCloseWithValidCloseCode1009() throws Exception {

        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);

        webSocket.connect();
        robot.join();
    }

    @Robotic(script = "sendCloseWithValidCloseCode1010")
    @Test(timeout = 1500)
    public void sendCloseWithValidCloseCode1010() throws Exception {

        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);

        webSocket.connect();
        robot.join();
    }

    @Robotic(script = "sendCloseWithValidCloseCode1011")
    @Test(timeout = 1500)
    public void sendCloseWithValidCloseCode1011() throws Exception {

        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);

        webSocket.connect();
        robot.join();
    }

    @Robotic(script = "sendCloseWithValidCloseCode3000")
    @Test(timeout = 1500)
    public void sendCloseWithValidCloseCode3000() throws Exception {

        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);

        webSocket.connect();
        robot.join();
    }

    @Robotic(script = "sendCloseWithValidCloseCode3999")
    @Test(timeout = 1500)
    public void sendCloseWithValidCloseCode3999() throws Exception {

        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);

        webSocket.connect();
        robot.join();
    }

    @Robotic(script = "sendCloseWithValidCloseCode4000")
    @Test(timeout = 1500)
    public void sendCloseWithValidCloseCode4000() throws Exception {

        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);

        webSocket.connect();
        robot.join();
    }

    @Robotic(script = "sendCloseWithValidCloseCode4999")
    @Test(timeout = 1500)
    public void sendCloseWithValidCloseCode4999() throws Exception {

        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);

        webSocket.connect();
        robot.join();
    }

    @Robotic(script = "sendMessageFragment1FollowedByCloseThenFragment")
    @Test(timeout = 1500)
    public void sendMessageFragment1FollowedByCloseThenFragment() throws Exception {

        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);

        webSocket.connect();
        robot.join();
    }

    @Robotic(script = "sendPingAfterCloseMessage")
    @Test(timeout = 1500)
    public void sendPingAfterCloseMessage() throws Exception {

        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);

        webSocket.connect();
        robot.join();
    }

    @Robotic(script = "sendTextMessageAfterCloseFrame")
    @Test(timeout = 1500)
    public void sendTextMessageAfterCloseFrame() throws Exception {

        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);

        webSocket.connect();
        robot.join();
    }

    @Robotic(script = "sendTextMessageThenCloseFrame")
    @Test(timeout = 1500)
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
        robot.join();
    }

    @Robotic(script = "sendTwoCloseFrames")
    @Test(timeout = 1500)
    public void sendTwoCloseFrames() throws Exception {

        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);

        webSocket.connect();
        robot.join();
    }
}
