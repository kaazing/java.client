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
import static org.junit.rules.RuleChain.outerRule;

import java.net.URI;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.DisableOnDebug;
import org.junit.rules.TestRule;
import org.junit.rules.Timeout;
import org.kaazing.k3po.junit.annotation.Specification;
import org.kaazing.k3po.junit.rules.K3poRule;
import org.kaazing.net.auth.ChallengeRequest;
import org.kaazing.net.auth.ChallengeResponse;
import org.kaazing.net.impl.auth.DefaultBasicChallengeHandler;
import org.kaazing.net.ws.WebSocket;
import org.kaazing.net.ws.WebSocketException;
import org.kaazing.net.ws.WebSocketFactory;

public class WebSocketConnectTimeoutTestIT {
    
	private final K3poRule k3po = new K3poRule();

	private final TestRule timeout = new DisableOnDebug(new Timeout(10, SECONDS));

	@Rule
	public final TestRule chain = outerRule(k3po).around(timeout);

    // The test connects against the script that does not respond to the native handshake request
    // eventually resulting in connect timeout to expire
    @Specification("testConnectTimeoutOnNativeHandshakeRequest")
    public void testConnectTimeoutOnNativeHandshakeRequest() throws Exception {
        WebSocketFactory factory = WebSocketFactory.createWebSocketFactory();
        factory.setDefaultConnectTimeout(5000);
        WebSocket webSocket = factory.createWebSocket(URI.create("java:ws://localhost:8001/echo"));
        boolean anticipatedExceptionCaught = false;
        try {
            webSocket.connect();
        } catch (WebSocketException exception) {
            anticipatedExceptionCaught = true;
        }
        Assert.assertTrue(anticipatedExceptionCaught);
        k3po.finish();
    }

    // The test connects against the script that does not respond to the extended handshake request
    // eventually resulting in connect timeout to expire
    @Specification("testConnectTimeoutOnExtendedHandshakeRequest")
    public void testConnectTimeoutOnExtendedHandshakeRequest() throws Exception {
        WebSocketFactory factory = WebSocketFactory.createWebSocketFactory();
        factory.setDefaultConnectTimeout(5000);
        WebSocket webSocket = factory.createWebSocket(URI.create("java:ws://localhost:8001/echo"));
        boolean anticipatedExceptionCaught = false;
        try {
            webSocket.connect();
        } catch (WebSocketException exception) {
            anticipatedExceptionCaught = true;
        }
        Assert.assertTrue(anticipatedExceptionCaught);
        k3po.finish();
    }

    // The test verifies that Connect timeout does not take into account the time involved in 
    // processing the authorization challenge while establishing the WebSocket connection.
    @Test
    @Specification("testConnectTimeoutDoesNotIncludeAuthorizationChallenge")
    public void testConnectTimeoutDoesNotIncludeAuthorizationChallenge() throws Exception {
        WebSocketFactory factory = WebSocketFactory.createWebSocketFactory();
        factory.setDefaultConnectTimeout(5000);
        factory.setDefaultChallengeHandler(new DefaultBasicChallengeHandler(){
            @Override
            public boolean canHandle(ChallengeRequest challengeRequest) {
                return true;
            }
            
            @Override
            public ChallengeResponse handle(ChallengeRequest challengeRequest) {
                try {
                    System.out.println("Challenge received, wait 7 seconds(2 seconds more than connect timeout) before responding to auth challenge.");
                    Thread.sleep(7000);
                } catch (InterruptedException e) {
                    
                } 

                char[] credentials = "Basic am9lOndlbGNvbWU=".toCharArray();
                return new ChallengeResponse(credentials, null);
            }
        });
        WebSocket webSocket = factory.createWebSocket(URI.create("java:ws://localhost:8004/echoAuth"));
        webSocket.connect();
        webSocket.close();
        k3po.finish();
    }

}
