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

package org.kaazing.gateway.client.impl.http;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.net.PasswordAuthentication;
import java.net.URISyntaxException;

import org.jmock.Mockery;
import org.junit.Test;
import org.kaazing.gateway.client.impl.Channel;
import org.kaazing.gateway.client.impl.Expectations;
import org.kaazing.gateway.client.impl.http.HttpRequest.Method;
import org.kaazing.gateway.client.impl.util.WSURI;
import org.kaazing.gateway.client.impl.wseb.WebSocketEmulatedChannel;
import org.kaazing.gateway.client.util.HttpURI;
import org.kaazing.gateway.client.util.WrappedByteBuffer;
import org.kaazing.net.auth.ChallengeHandler;
import org.kaazing.net.auth.ChallengeRequest;
import org.kaazing.net.auth.ChallengeResponse;
import org.kaazing.net.auth.LoginHandler;

public class HttpRequestAuthenticationHandlerTest {
    private static final String HTTP_401_RESPONSE_TEXT = "HTTP/1.1 401 Unauthorized\r\nWWW-Authenticate: Application Basic realm=\"Kaazing WebSocket Gateway Demo\"\r\nAccess-Control-Allow-Origin: http://localhost:8000\r\nAccess-Control-Allow-Credentials: true\r\nContent-Type: text/html\r\n\r\n<html><head></head><body><h1>401 Unauthorized</h1></body></html>";
    private static final String CREATE_RESPONSE_TEXT = "http://localhost:8000/;e/ub\nhttp://localhost:8000/;e/db";

    @Test
    public void testIsHttpResponse() {
        WrappedByteBuffer buf = WrappedByteBuffer.wrap(HTTP_401_RESPONSE_TEXT.getBytes());
        assertEquals(true, HttpRequestAuthenticationHandler.isHTTPResponse(buf));
        buf = WrappedByteBuffer.wrap(CREATE_RESPONSE_TEXT.getBytes());
        assertEquals(false, HttpRequestAuthenticationHandler.isHTTPResponse(buf));
    }

    @Test
    public void testIsHttpResponseSmall() {
        WrappedByteBuffer buf = WrappedByteBuffer.wrap("HTTP".getBytes());
        assertEquals(false, HttpRequestAuthenticationHandler.isHTTPResponse(buf));
    }

    @Test
    public void testGetLines() {
        WrappedByteBuffer buf = WrappedByteBuffer.wrap(HTTP_401_RESPONSE_TEXT.getBytes());
        String[] lines = HttpRequestAuthenticationHandler.getLines(buf);
        assertNotNull(lines);
        assertEquals(7, lines.length);
        assertEquals("HTTP/1.1 401 Unauthorized", lines[0]);
        assertEquals(401, Integer.parseInt(lines[0].split(" ")[1]));
        String wwwAuthenticate = null;
        for (int i = 1; i < lines.length; i++) {
            if (lines[i].startsWith("WWW-Authenticate: ")) {
                wwwAuthenticate = lines[i].substring("WWW-Authenticate: ".length());
                break;
            }
        }
        assertEquals("Application Basic realm=\"Kaazing WebSocket Gateway Demo\"", wwwAuthenticate);
    }
    
    @Test
    public void testProcessConnect() throws URISyntaxException {

        Mockery context = new Mockery();
        final HttpRequestHandler nextHandler = context.mock(HttpRequestHandler.class);
        final HttpRequestListener listener = context.mock(HttpRequestListener.class);

        context.checking(new Expectations() {

            {
                oneOf(nextHandler).setListener(with(aNonNull(HttpRequestListener.class)));
                will(saveParameter("listener", 0));
                oneOf(nextHandler).processOpen(with(aNonNull(HttpRequest.class)));
            }
        });

        HttpURI uri = new HttpURI("http://localhost:8001/echo");
        HttpRequest request = new HttpRequest(Method.POST, uri);
        Channel createChannel = new Channel();
        createChannel.setParent(new WebSocketEmulatedChannel(new WSURI("ws://localhost:8001/echo")));
        request.parent = createChannel;
        HttpRequestAuthenticationHandler handler = new HttpRequestAuthenticationHandler();
        //now use mock object for nextHandler
        handler.setNextHandler(nextHandler);
        handler.setListener(listener);
        
        handler.processOpen(request);
        context.assertIsSatisfied();
    }
    
    private class MyLoginHandler extends LoginHandler {

        @Override
        public PasswordAuthentication getCredentials() {
            return new PasswordAuthentication("joe", "welcome".toCharArray());
        }       
    }

    private class DummyChallengeHandler extends ChallengeHandler {

        @Override
        public boolean canHandle(ChallengeRequest challengeRequest) {
            return false;
        }

        @Override
        public ChallengeResponse handle(ChallengeRequest challengeRequest) {
            return null;
        }
    }
}
