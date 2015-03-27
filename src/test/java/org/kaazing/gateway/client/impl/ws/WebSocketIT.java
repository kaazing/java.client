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

package org.kaazing.gateway.client.impl.ws;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.rules.RuleChain.outerRule;

import java.io.IOException;
import java.net.URI;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.DisableOnDebug;
import org.junit.rules.TestRule;
import org.junit.rules.Timeout;
import org.kaazing.k3po.junit.annotation.Specification;
import org.kaazing.k3po.junit.rules.K3poRule;
import org.kaazing.net.ws.WebSocket;
import org.kaazing.net.ws.WebSocketException;
import org.kaazing.net.ws.WebSocketFactory;
import org.kaazing.net.ws.WebSocketMessageReader;
import org.kaazing.net.ws.WebSocketMessageWriter;


public class WebSocketIT {
    boolean success;

	private final K3poRule k3po = new K3poRule();

	private final TestRule timeout = new DisableOnDebug(new Timeout(5, SECONDS));

	@Rule
	public final TestRule chain = outerRule(k3po).around(timeout);

    @Specification("test.that.websocket.connect.does.not.request.bridge")
    public void websocketDoesNotRequestBridgeTest() throws Exception {

        success = false;
        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);
        try {
            webSocket.connect();
            Thread.sleep(2000);
        } catch (WebSocketException e) {
            if (e.getMessage().contains("Connection failed")) {
                success = true;
            }
        }
        assertTrue(success);
        k3po.finish();
    }
        
    @Specification("test.websocket.connect.disconnect")
    public void websocketConnectDisconnect() throws Exception {

        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        WebSocket webSocket = wsFactory.createWebSocket(URI.create("ws://localhost:8001/echo"));

        webSocket.connect();    
        // get reader before sending message
        WebSocketMessageReader reader = webSocket.getMessageReader();
        
        //send a message
        webSocket.getMessageWriter().writeText("Hello");
        
        //receive using reader
        reader.next();
        String message = (String) reader.getText();
        
        // close websocket
        webSocket.close();
        assertEquals("Hello", message);
        k3po.finish();
    }

    
    //test close connection when idle timeout expires
    @Specification("test.websocket.idle.timeout")
    public void websocketIdleTimeoutConnectionClosedTest() throws Exception {
        success = false;
        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);

        webSocket.connect();
        Thread.sleep(1000);
        try {
            webSocket.getMessageReader().next();
            int tmp = webSocket.getReader().read();  //this call should be back with -1
            if (tmp == -1) {
                success = true;
            }
        } catch(IOException ex) {
            if (ex.getMessage().contains("WebSocket is not connected")) {
                success = true;
            }
        }
        
        assertTrue(success);
        k3po.finish();
    }

    //test connection keep open when ping/pong are transmitted
    @Specification("test.websocket.idle.timeout.ping.pong")
    public void websocketIdleTimeoutPingPongTest() throws Exception {
        WebSocket webSocket;
        URI location = new URI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        webSocket = wsFactory.createWebSocket(location);

        webSocket.connect();
        
        // get reader before sending message
        WebSocketMessageReader reader = webSocket.getMessageReader();
        WebSocketMessageWriter writer = webSocket.getMessageWriter();
        
        //send "PINGME" to robot to start ping/pong transmision
        Thread.sleep(250);
        writer.writeText("PINGME");
        
        // send one more "PINGME"
        Thread.sleep(250);
        writer.writeText("PINGME");
        
        //send Hello
        Thread.sleep(300);
        writer.writeText("Hello");

        //read Hello
        reader.next();
        String message = (String) reader.getText();
        webSocket.close();

        
        assertEquals("Hello", message);
        k3po.finish();
    }

    
    /**
     * Sets the test up @Test
     */
    @Before
    public void setUp() throws Exception {

    }

    /**
     * Tears the test up after each @Test
     */
    @After
    public void tearDown() throws Exception {

    }
}