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

import org.kaazing.gateway.client.impl.Expectations;
import org.kaazing.gateway.client.impl.WebSocketChannel;
import org.kaazing.gateway.client.impl.WebSocketHandler;
import org.kaazing.gateway.client.impl.WebSocketHandlerListener;
import org.kaazing.gateway.client.impl.util.WSURI;
import org.kaazing.gateway.client.impl.ws.WebSocketCompositeChannel;
import org.kaazing.gateway.client.impl.wsn.WebSocketNativeBalancingHandler;
import org.kaazing.gateway.client.impl.wsn.WebSocketNativeChannel;
import org.kaazing.gateway.client.util.WrappedByteBuffer;
import org.kaazing.net.ws.WebSocket;
import org.kaazing.net.ws.WebSocketFactory;
import org.kaazing.net.ws.impl.WebSocketImpl;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;

import org.jmock.Mockery;
import org.jmock.api.Invocation;
import org.jmock.lib.action.CustomAction;
import org.junit.Test;

public class WebSocketNativeBalancingHandlerTest {

    /*
     * pass through for non-kaazing gateway
     * should treat balancing message as regular message
     */
    @Test
    public void testProcessConnect() throws URISyntaxException {

        Mockery context = new Mockery();
        final WebSocketHandler nextHandler = context.mock(WebSocketHandler.class);
        final WebSocketHandlerListener listener = context.mock(WebSocketHandlerListener.class);
        final String[] requestedProtocols = new String[] { "foo" };
        final String buf = '\uf0ff' + "N";
        
        context.checking(new Expectations() {
            {
                oneOf(nextHandler).setListener(with(aNonNull(WebSocketHandlerListener.class)));
                will(saveParameter("listener", 0));
                oneOf(nextHandler).processConnect(with(aNonNull(WebSocketChannel.class)), with(aNonNull(WSURI.class)), with(equal(requestedProtocols)));
                will(new CustomAction("will fire connectionOpen") {

                    @Override
                    public Object invoke(Invocation invocation) {
                        WebSocketHandlerListener listener = (WebSocketHandlerListener)lookup("listener");
                        WebSocketChannel channel = (WebSocketChannel)invocation.getParameter(0);
                        listener.connectionOpened(channel, "");
                        return null;
                    }
                });
                oneOf(listener).connectionOpened(with(aNonNull(WebSocketChannel.class)), with(""));
                will(new CustomAction("will balancer message") {

                    @Override
                    public Object invoke(Invocation invocation) throws UnsupportedEncodingException {
                        WebSocketHandlerListener listener = (WebSocketHandlerListener)lookup("listener");
                        WebSocketChannel channel = (WebSocketChannel)invocation.getParameter(0);
                        WrappedByteBuffer message = new WrappedByteBuffer(buf.getBytes("UTF-8"));
                        listener.binaryMessageReceived(channel, message);
                        return null;
                    }
                });
                oneOf(listener).binaryMessageReceived(with(aNonNull(WebSocketChannel.class)),with(aNonNull(WrappedByteBuffer.class)));

            }
        });

        WSURI uri = new WSURI("ws://locationhost:8001/echo");
        WebSocketChannel channel = new WebSocketNativeChannel(uri);

        WebSocketNativeBalancingHandler handler = new WebSocketNativeBalancingHandler();
        handler.setNextHandler(nextHandler);
        handler.setListener(listener);

        handler.processConnect(channel, uri, requestedProtocols);
        context.assertIsSatisfied();
    }

    /*
     * wait receive balancer message for kaazing gateway
     */
    @Test
    public void testWaitBalancerMessage() throws URISyntaxException {

        Mockery context = new Mockery();
        final WebSocketHandler nextHandler = context.mock(WebSocketHandler.class);
        final WebSocketHandlerListener listener = context.mock(WebSocketHandlerListener.class);
        final String[] requestedProtocols = new String[] { "x-kaazing-handshake" };

        context.checking(new Expectations() {
            {
                oneOf(nextHandler).setListener(with(aNonNull(WebSocketHandlerListener.class)));
                will(saveParameter("listener", 0));
                oneOf(nextHandler).processConnect(with(aNonNull(WebSocketChannel.class)), with(aNonNull(WSURI.class)), with(equal(requestedProtocols)));
                will(new CustomAction("will fire connectionOpen") {

                    @Override
                    public Object invoke(Invocation invocation) {
                        WebSocketHandlerListener listener = (WebSocketHandlerListener)lookup("listener");
                        WebSocketChannel channel = (WebSocketChannel)invocation.getParameter(0);
                        listener.connectionOpened(channel, requestedProtocols[0]);
                        return null;
                    }
                });
               
            }
        });

        WSURI uri = new WSURI("ws://locationhost:8001/echo");

        WebSocketChannel channel = new WebSocketNativeChannel(uri);

        WebSocketNativeBalancingHandler handler = new WebSocketNativeBalancingHandler();
        handler.setNextHandler(nextHandler);
        handler.setListener(listener);

        handler.processConnect(channel, uri, requestedProtocols);
        context.assertIsSatisfied();
    }
    /*
     * pass through
     */
    @Test
    public void testProcessAuthorize() throws URISyntaxException {

        Mockery context = new Mockery();
        final WebSocketHandler nextHandler = context.mock(WebSocketHandler.class);
        final WebSocketHandlerListener listener = context.mock(WebSocketHandlerListener.class);

        context.checking(new Expectations() {
            {
                oneOf(nextHandler).setListener(with(aNonNull(WebSocketHandlerListener.class)));
                will(saveParameter("listener", 0));
                oneOf(nextHandler).processAuthorize(with(aNonNull(WebSocketChannel.class)), with("Application Basic relam"));
                will(new CustomAction("will fire 401 challenge") {

                    @Override
                    public Object invoke(Invocation invocation) {
                        WebSocketHandlerListener listener = (WebSocketHandlerListener)lookup("listener");
                        WebSocketChannel channel = (WebSocketChannel)invocation.getParameter(0);
                        listener.authenticationRequested(channel, "location", (String)invocation.getParameter(1));
                        return null;
                    }
                });
                oneOf(listener).authenticationRequested(with(aNonNull(WebSocketChannel.class)), with("location"), with("Application Basic relam"));
            }
        });

        WSURI uri = new WSURI("ws://locationhost:8001/echo");
        WebSocketChannel channel = new WebSocketChannel(uri);

        WebSocketNativeBalancingHandler handler = new WebSocketNativeBalancingHandler();
        handler.setNextHandler(nextHandler);
        handler.setListener(listener);
        handler.processAuthorize(channel, "Application Basic relam");
        context.assertIsSatisfied();
    }

    /*
     * pass through
     */
    @Test
    public void testProcessBinaryMessage() throws URISyntaxException {

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
                        listener.binaryMessageReceived(channel, message);
                        return null;
                    }
                });
                oneOf(listener).binaryMessageReceived(with(aNonNull(WebSocketChannel.class)), with(message));
            }
        });

        WSURI uri = new WSURI("ws://locationhost:8001/echo");
        WebSocketChannel channel = getNativeChannel(uri);

        WebSocketNativeBalancingHandler handler = new WebSocketNativeBalancingHandler();
        handler.setNextHandler(nextHandler);
        handler.setListener(listener);
        handler.processBinaryMessage(channel, message);
        context.assertIsSatisfied();
    }

    /*
     * pass through
     */
    @Test
    public void testProcessTextMessage() throws URISyntaxException {
        Mockery context = new Mockery();
        final WebSocketHandler nextHandler = context.mock(WebSocketHandler.class);
        final WebSocketHandlerListener listener = context.mock(WebSocketHandlerListener.class);
        final WrappedByteBuffer message = new WrappedByteBuffer("test message".getBytes());
        
        context.checking(new Expectations() {
            {
                oneOf(nextHandler).setListener(with(aNonNull(WebSocketHandlerListener.class)));
                will(saveParameter("listener", 0));
                oneOf(nextHandler).processTextMessage(with(aNonNull(WebSocketChannel.class)), with("test message"));
                will(new CustomAction("will fire message received") {

                    @Override
                    public Object invoke(Invocation invocation) {
                        WebSocketHandlerListener listener = (WebSocketHandlerListener)lookup("listener");
                        WebSocketChannel channel = (WebSocketChannel)invocation.getParameter(0);
                        listener.binaryMessageReceived(channel, message);
                        return null;
                    }
                });
                oneOf(listener).binaryMessageReceived(with(aNonNull(WebSocketChannel.class)), with(aNonNull(WrappedByteBuffer.class)));
            }
        });

        WSURI uri = new WSURI("ws://locationhost:8001/echo");
        WebSocketChannel channel = getNativeChannel(uri);

        WebSocketNativeBalancingHandler handler = new WebSocketNativeBalancingHandler();
        handler.setNextHandler(nextHandler);
        handler.setListener(listener);
        handler.processTextMessage(channel, "test message");
        context.assertIsSatisfied();
    }
    
    /*
     * check message leading byte, 
     * if it starts with '\uf0ff'N, fire open event
     * make sure it only fires open event twice
     */
    @Test
    public void testProcessBalancerMessage() throws URISyntaxException, UnsupportedEncodingException {
        Mockery context = new Mockery();
        final WebSocketHandler nextHandler = context.mock(WebSocketHandler.class);
        final WebSocketHandlerListener listener = context.mock(WebSocketHandlerListener.class);
        final String balancerNoMessage = '\uf0ff' + "N";
         
        context.checking(new Expectations() {
            {
                oneOf(nextHandler).setListener(with(aNonNull(WebSocketHandlerListener.class)));
                will(saveParameter("listener", 0));
                
                oneOf(nextHandler).processTextMessage(with(aNonNull(WebSocketChannel.class)), with("test message"));
                will(new CustomAction("will balancer message") {
                    @Override
                    public Object invoke(Invocation invocation) throws UnsupportedEncodingException {
                        WebSocketHandlerListener listener = (WebSocketHandlerListener)lookup("listener");
                        WebSocketChannel channel = (WebSocketChannel)invocation.getParameter(0);
                        listener.textMessageReceived(channel, balancerNoMessage);
                        return null;
                    }
                });
                
                oneOf(listener).connectionOpened(with(aNonNull(WebSocketChannel.class)), with("x-kaazing-handshake"));
                will(new CustomAction("will fire balancer message again") {
                    @Override
                    public Object invoke(Invocation invocation) throws UnsupportedEncodingException {
                        WebSocketHandlerListener listener = (WebSocketHandlerListener)lookup("listener");
                        WebSocketChannel channel = (WebSocketChannel)invocation.getParameter(0);
                        listener.textMessageReceived(channel, balancerNoMessage);
                        return null;
                    }
                });

                oneOf(listener).connectionOpened(with(aNonNull(WebSocketChannel.class)), with(""));
                will(new CustomAction("will fire 3nd balancer message") {
                    @Override
                    public Object invoke(Invocation invocation) throws UnsupportedEncodingException {
                        WebSocketHandlerListener listener = (WebSocketHandlerListener)lookup("listener");
                        WebSocketChannel channel = (WebSocketChannel)invocation.getParameter(0);
                        listener.textMessageReceived(channel, balancerNoMessage);
                        return null;
                    }
                });

                oneOf(listener).textMessageReceived(with(aNonNull(WebSocketChannel.class)), with(aNonNull(String.class))); //with(new ByteBufferMatcher(buf, "match balancer message")));
            }
        });

        WSURI uri = new WSURI("ws://locationhost:8001/echo");
        WebSocketChannel channel = getNativeChannel(uri);

        WebSocketNativeBalancingHandler handler = new WebSocketNativeBalancingHandler();
        handler.setNextHandler(nextHandler);
        handler.setListener(listener);
        handler.processTextMessage(channel, "test message");
        context.assertIsSatisfied();
    }
    
    /*
     * check message leading byte, 
     * if it starts with '\uf0ff'R, redirect 
     */
    @Test
    public void testProcessBalancerRedirectMessage() throws URISyntaxException, UnsupportedEncodingException {
        Mockery context = new Mockery();
        final WebSocketHandler nextHandler = context.mock(WebSocketHandler.class);
        final WebSocketHandlerListener listener = context.mock(WebSocketHandlerListener.class);
        final String balancerRedirectMessage = '\uf0ff' + "Rws://localhost/echo";
        
        context.checking(new Expectations() {
            {
                oneOf(nextHandler).setListener(with(aNonNull(WebSocketHandlerListener.class)));
                will(saveParameter("listener", 0));
                oneOf(nextHandler).processTextMessage(with(aNonNull(WebSocketChannel.class)), with("test message"));
                will(new CustomAction("will fire redirect message") {

                    @Override
                    public Object invoke(Invocation invocation) {
                        WebSocketHandlerListener listener = (WebSocketHandlerListener)lookup("listener");
                        WebSocketChannel channel = (WebSocketChannel)invocation.getParameter(0);
                        listener.textMessageReceived(channel, balancerRedirectMessage);
                        return null;
                    }
                });
                oneOf(nextHandler).processClose(with(aNonNull(WebSocketChannel.class)), with(0), with(aNull(String.class)));
                
            }
        });

        WSURI uri = new WSURI("ws://locationhost:8001/echo");
        WebSocketChannel channel = getNativeChannel(uri);

        WebSocketNativeBalancingHandler handler = new WebSocketNativeBalancingHandler();
        handler.setNextHandler(nextHandler);
        handler.setListener(listener);
        handler.processTextMessage(channel, "test message");
        context.assertIsSatisfied();
    }
    
    private WebSocketNativeChannel getNativeChannel(WSURI uri) throws URISyntaxException  {
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        WebSocket ws = wsFactory.createWebSocket(uri.getURI());
        WebSocketCompositeChannel cc = ((WebSocketImpl)ws).getCompositeChannel();
        
        WebSocketNativeChannel channel = new WebSocketNativeChannel(uri);
        cc.selectedChannel = channel;
        channel.setParent(cc);
        
        return channel;
    }
    
}
