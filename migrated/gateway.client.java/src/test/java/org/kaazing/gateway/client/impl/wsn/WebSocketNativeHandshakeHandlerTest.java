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

package org.kaazing.gateway.client.impl.wsn;

import java.net.URI;
import java.net.URISyntaxException;

import org.jmock.Mockery;
import org.jmock.api.Invocation;
import org.jmock.lib.action.CustomAction;
import org.junit.Ignore;
import org.junit.Test;
import org.kaazing.gateway.client.impl.Expectations;
import org.kaazing.gateway.client.impl.WebSocketChannel;
import org.kaazing.gateway.client.impl.WebSocketHandler;
import org.kaazing.gateway.client.impl.WebSocketHandlerListener;
import org.kaazing.gateway.client.impl.util.WSURI;
import org.kaazing.gateway.client.util.WrappedByteBuffer;
import org.kaazing.net.ws.WebSocket;
import org.kaazing.net.ws.WebSocketFactory;
import org.kaazing.net.ws.impl.WebSocketImpl;

public class WebSocketNativeHandshakeHandlerTest {

    
    /*
     * pass through for non kaazing gateway
     */
    @Test
    public void testProcessConnect() throws URISyntaxException, Exception {

        Mockery context = new Mockery();
        final WebSocketHandler nextHandler = context.mock(WebSocketHandler.class);
        final WebSocketHandlerListener listener = context.mock(WebSocketHandlerListener.class);

        context.checking(new Expectations() {

            {
                oneOf(nextHandler).setListener(with(aNonNull(WebSocketHandlerListener.class)));
                will(saveParameter("listener", 0));
                oneOf(nextHandler).processConnect(with(aNonNull(WebSocketChannel.class)), with(aNonNull(WSURI.class)),
                                                  with(equal(new String[] {"x-kaazing-handshake", "foo"})));
                will(new CustomAction("will fire connectionOpen") {

                    @Override
                    public Object invoke(Invocation invocation) {
                        WebSocketHandlerListener listener = (WebSocketHandlerListener)lookup("listener");
                        WebSocketChannel channel = (WebSocketChannel)invocation.getParameter(0);
                        listener.connectionOpened(channel, "foo");
                        return null;
                    }
                });
                oneOf(listener).connectionOpened(with(aNonNull(WebSocketChannel.class)), with(aNull(String.class)));
            }
        });

        WSURI uri = new WSURI("ws://localhost:8001/echo");
        WebSocketChannel channel = createNativeChannel(uri);

        WebSocketNativeHandshakeHandler handler = new WebSocketNativeHandshakeHandler();
        handler.setNextHandler(nextHandler);
        handler.setListener(listener);

        handler.processConnect(channel, uri, new String[] {"foo"});
        context.assertIsSatisfied();
    }

    /*
     * should send authorizeToken
     */
    @Test
    public void testProcessAuthorize() throws URISyntaxException, Exception {

        Mockery context = new Mockery();
        final WebSocketHandler nextHandler = context.mock(WebSocketHandler.class);
        final WebSocketHandlerListener listener = context.mock(WebSocketHandlerListener.class);

        context.checking(new Expectations() {

            {
                oneOf(nextHandler).setListener(with(aNonNull(WebSocketHandlerListener.class)));
                will(saveParameter("listener", 0));
                oneOf(nextHandler).processBinaryMessage(with(aNonNull(WebSocketChannel.class)),with(aNonNull(WrappedByteBuffer.class)));
            }
        });

        WSURI uri = new WSURI("ws://localhost:8001/echo");
        WebSocketChannel channel = createNativeChannel(uri);
        WebSocketNativeHandshakeHandler handler = new WebSocketNativeHandshakeHandler();
        handler.setNextHandler(nextHandler);
        handler.setListener(listener);
        handler.processAuthorize(channel, "Application Basic relam");
        context.assertIsSatisfied();
    }
    
    /*
     * pass through
     */
    @Test @Ignore(" channel readyState must be Open for this test to run")
    public void testProcessBinaryMessage() throws URISyntaxException, Exception {

        Mockery context = new Mockery();
        final WebSocketHandler nextHandler = context.mock(WebSocketHandler.class);
        final WebSocketHandlerListener listener = context.mock(WebSocketHandlerListener.class);

        final WrappedByteBuffer message = new WrappedByteBuffer("test message".getBytes());
        context.checking(new Expectations() {

            {
                oneOf(nextHandler).setListener(with(aNonNull(WebSocketHandlerListener.class)));
                will(saveParameter("listener", 0));
                oneOf(nextHandler).processBinaryMessage(with(aNonNull(WebSocketChannel.class)), with(message));
                will(new CustomAction("will fire text message received") {

                    @Override
                    public Object invoke(Invocation invocation) {
                        WebSocketHandlerListener listener = (WebSocketHandlerListener)lookup("listener");
                        WebSocketChannel channel = (WebSocketChannel)invocation.getParameter(0);
                        listener.connectionOpened(channel, null);
                        listener.binaryMessageReceived(channel, message);
                        return null;
                    }
                });
                oneOf(listener).binaryMessageReceived(with(aNonNull(WebSocketChannel.class)), with(message));
            }
        });

        WSURI uri = new WSURI("ws://localhost:8001/echo");
        WebSocketChannel channel = createNativeChannel(uri);

        WebSocketNativeHandshakeHandler handler = new WebSocketNativeHandshakeHandler();
        handler.setNextHandler(nextHandler);
        handler.setListener(listener);
        handler.processBinaryMessage(channel, message);
        context.assertIsSatisfied();
    }

    /*
     * pass through
     */
    @Test @Ignore(" channel readyState must be Open for this test to run")
    public void testProcessTextMessage() throws URISyntaxException, Exception {
        Mockery context = new Mockery();
        final WebSocketHandler nextHandler = context.mock(WebSocketHandler.class);
        final WebSocketHandlerListener listener = context.mock(WebSocketHandlerListener.class);
        final WrappedByteBuffer message = new WrappedByteBuffer("test message".getBytes());
        
        context.checking(new Expectations() {

            {
                oneOf(nextHandler).setListener(with(aNonNull(WebSocketHandlerListener.class)));
                will(saveParameter("listener", 0));
                oneOf(nextHandler).processTextMessage(with(aNonNull(WebSocketChannel.class)), with("test message"));
                will(new CustomAction("will fire connectionOpen") {

                    @Override
                    public Object invoke(Invocation invocation) {
                        WebSocketHandlerListener listener = (WebSocketHandlerListener)lookup("listener");
                        WebSocketChannel channel = (WebSocketChannel)invocation.getParameter(0);
                        listener.connectionOpened(channel, null);
                        listener.binaryMessageReceived(channel, message);
                        return null;
                    }
                });
                oneOf(listener).binaryMessageReceived(with(aNonNull(WebSocketChannel.class)), with(new ByteBufferMatcher("test message", "test")));
            }
        });

        WSURI uri = new WSURI("ws://localhost:8001/echo");
        WebSocketChannel channel = createNativeChannel(uri);

        WebSocketNativeHandshakeHandler handler = new WebSocketNativeHandshakeHandler();
        handler.setNextHandler(nextHandler);
        handler.setListener(listener);
        handler.processTextMessage(channel, "test message");
        context.assertIsSatisfied();
    }
    
    /*
     * extented handshake
     * processOpen Sec-Websocket-Protocol: x-kaazing-extented-handshake header
     * recieve respones with x-kaazing-extented-handshake header
     * send second request with Sec-Websocket-Extension: x-kaazing-revalidate header
     * receive 101
     * wait for balancer message
     * //fire Open event
     */
    @Test
    public void testExtendedHandshakeSuccess() throws Exception {

        Mockery context = new Mockery();
        final WebSocketHandler nextHandler = context.mock(WebSocketHandler.class);
        final WebSocketHandlerListener listener = context.mock(WebSocketHandlerListener.class);
        context.checking(new Expectations() {

            {
                oneOf(nextHandler).setListener(with(aNonNull(WebSocketHandlerListener.class)));
                will(saveParameter("listener", 0));
                oneOf(nextHandler).processConnect(with(aNonNull(WebSocketChannel.class)), with(aNonNull(WSURI.class)), with(equal(new String[] {"x-kaazing-handshake", "foo"})));
                will(new CustomAction("will fire connectionOpen with x-kaazing-extension") {

                    @Override
                    public Object invoke(Invocation invocation) {
                        WebSocketHandlerListener listener = (WebSocketHandlerListener)lookup("listener");
                        WebSocketChannel channel = (WebSocketChannel)invocation.getParameter(0);
                        listener.connectionOpened(channel, "x-kaazing-handshake");
                        return null;
                    }
                });
                oneOf(nextHandler).processBinaryMessage(with(aNonNull(WebSocketChannel.class)), with(aNonNull(WrappedByteBuffer.class)));
                will(new CustomAction("will fire message received with 101") {

                    @Override
                    public Object invoke(Invocation invocation) {
                        String response = "HTTP/1.1 101 Created\r\n" +
                                "Content-Type: text/plain\r\n" +
                                "Content-Length: 63\r\n" +
                                "WebSocket-Protocol: foo\r\n\r\n";
                        WebSocketHandlerListener listener = (WebSocketHandlerListener)lookup("listener");
                        WebSocketChannel channel = (WebSocketChannel)invocation.getParameter(0);
                        listener.binaryMessageReceived(channel, new WrappedByteBuffer(response.getBytes()));
                        return null;
                    }
                });
                
                //oneOf(listener).connectionOpened(with(aNonNull(WebSocketChannel.class)), with("foo"));
            }
        });

        WSURI uri = new WSURI("ws://localhost:8001/echo");
        WebSocketChannel channel = createNativeChannel(uri);

        WebSocketNativeHandshakeHandler handler = new WebSocketNativeHandshakeHandler();
        handler.setNextHandler(nextHandler);
        handler.setListener(listener);

        handler.processConnect(channel, uri, new String[] {"foo"});
        context.assertIsSatisfied();
    }
    
    /*
     * extended handshake
     * processOpen Sec-Websocket-Protocol: x-kaazing-extended-handshake header
     * recieve respones with x-kaazing-extended-handshake header
     * send second request with Sec-Websocket-Extension: x-kaazing-revalidate header
     * receive 400
     * receive a empty message to indicate end of message
     * close current connection
     * fire close event
     */
    @Test
    public void testExtendedHandshakeFailed() throws URISyntaxException, Exception {

        Mockery context = new Mockery();
        final WebSocketHandler nextHandler = context.mock(WebSocketHandler.class);
        final WebSocketHandlerListener listener = context.mock(WebSocketHandlerListener.class);
        context.checking(new Expectations() {

            {
                oneOf(nextHandler).setListener(with(aNonNull(WebSocketHandlerListener.class)));
                will(saveParameter("listener", 0));
                oneOf(nextHandler).processConnect(with(aNonNull(WebSocketChannel.class)), with(aNonNull(WSURI.class)), with(equal(new String[] {"x-kaazing-handshake", "foo"})));
                will(new CustomAction("will fire connectionOpen with x-kaazing-extension") {

                    @Override
                    public Object invoke(Invocation invocation) {
                        WebSocketHandlerListener listener = (WebSocketHandlerListener)lookup("listener");
                        WebSocketChannel channel = (WebSocketChannel)invocation.getParameter(0);
                        listener.connectionOpened(channel, "x-kaazing-handshake");
                        return null;
                    }
                });
                
                oneOf(nextHandler).processBinaryMessage(with(aNonNull(WebSocketChannel.class)), with(aNonNull(WrappedByteBuffer.class)));
                will(new CustomAction("will fire message received with 400") {
                    @Override
                    public Object invoke(Invocation invocation) {
                        String response = "HTTP/1.1 400 Bad request\r\n" +
"Content-Type: text/html\r\n\r\n";
                        WebSocketHandlerListener listener = (WebSocketHandlerListener)lookup("listener");
                        WebSocketChannel channel = (WebSocketChannel)invocation.getParameter(0);
                        listener.binaryMessageReceived(channel, new WrappedByteBuffer(response.getBytes()));
                        listener.binaryMessageReceived(channel, new WrappedByteBuffer("".getBytes()));
                        return null;
                    }
                });
                
                //oneOf(nextHandler).processClose(with(aNonNull(WebSocketChannel.class)));
                oneOf(listener).connectionFailed(with(aNonNull(WebSocketChannel.class)), with(aNonNull(Exception.class)));
            }
        });

        WSURI uri = new WSURI("ws://localhost:8001/echo");
        WebSocketChannel channel = createNativeChannel(uri);
        
        WebSocketNativeHandshakeHandler handler = new WebSocketNativeHandshakeHandler();
        handler.setNextHandler(nextHandler);
        handler.setListener(listener);

        handler.processConnect(channel, uri, new String[] {"foo"});
        context.assertIsSatisfied();         
    }
    
    /*
     * extended handshake
     * processOpen Sec-Websocket-Protocol: x-kaazing-extended-handshake header
     * recieve respones with x-kaazing-extended-handshake header
     * send second request with Sec-Websocket-Extension: x-kaazing-revalidate header
     * receive 401
     * close current connection
     * fire close event
     */
    @Test
    public void testExtendedHandshakeWith401() throws URISyntaxException, Exception {

         Mockery context = new Mockery();
        final WebSocketHandler nextHandler = context.mock(WebSocketHandler.class);
        final WebSocketHandlerListener listener = context.mock(WebSocketHandlerListener.class);
        context.checking(new Expectations() {

            {
                oneOf(nextHandler).setListener(with(aNonNull(WebSocketHandlerListener.class)));
                will(saveParameter("listener", 0));
                oneOf(nextHandler).processConnect(with(aNonNull(WebSocketChannel.class)), with(aNonNull(WSURI.class)), with(equal(new String[] {"x-kaazing-handshake", "foo"})));
                will(new CustomAction("will fire connectionOpen with x-kaazing-extension") {

                    @Override
                    public Object invoke(Invocation invocation) {
                        WebSocketHandlerListener listener = (WebSocketHandlerListener)lookup("listener");
                        WebSocketChannel channel = (WebSocketChannel)invocation.getParameter(0);
                        listener.connectionOpened(channel, "x-kaazing-handshake");
                        return null;
                    }
                });
                oneOf(nextHandler).processBinaryMessage(with(aNonNull(WebSocketChannel.class)), with(aNonNull(WrappedByteBuffer.class)));
                will(new CustomAction("will fire message received with 401") {

                    @Override
                    public Object invoke(Invocation invocation) {
                        String response = "HTTP/1.1 401 Unauthorized\r\n" +
"WWW-Authenticate: Application Basic realm=\"Kaazing WebSocket Gateway Demo\"\r\n" +
"Access-Control-Allow-Origin: http://localhost:8001\r\n" +
"Access-Control-Allow-Credentials: true\r\n" +
"Access-Control-Allow-Headers: content-type\r\n" +
"Content-Type: text/html\r\n\r\n" +
"<html><head></head><body><h1>401 Unauthorized</h1></body></html>";
                        WebSocketHandlerListener listener = (WebSocketHandlerListener)lookup("listener");
                        WebSocketChannel channel = (WebSocketChannel)invocation.getParameter(0);
                        listener.binaryMessageReceived(channel, new WrappedByteBuffer(response.getBytes()));

                        //an empty string to indicate end of message
                        listener.binaryMessageReceived(channel, new WrappedByteBuffer("".getBytes()));
                        
                        return null;
                    }
                });
                oneOf(listener).authenticationRequested(with(aNonNull(WebSocketChannel.class)), with("ws://localhost:8001/echo"), with("Application Basic realm=\"Kaazing WebSocket Gateway Demo\""));
            }
        });

        WSURI uri = new WSURI("ws://localhost:8001/echo");
        WebSocketChannel channel = createNativeChannel(uri);

        WebSocketNativeHandshakeHandler handler = new WebSocketNativeHandshakeHandler();
        handler.setNextHandler(nextHandler);
        handler.setListener(listener);
        
        handler.processConnect(channel, uri, new String[] {"foo"});
        context.assertIsSatisfied();        
    }
    
    // --------------------- Private Methods -------------------------------
    // Returns WebSocketChannel that is setup appropriately with 
    // WebSocketCompositeChannel and the WebSocket.
    private WebSocketChannel createNativeChannel(WSURI wsuri) 
            throws URISyntaxException, Exception {
        URI              uri = wsuri.getURI();
        WebSocketChannel channel = new WebSocketNativeChannel(wsuri);
        
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        WebSocket ws = wsFactory.createWebSocket(uri, (String[])null);

        channel.setParent(((WebSocketImpl)ws).getCompositeChannel());

        return channel;
    }
}
