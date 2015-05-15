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

import java.net.URISyntaxException;

import junit.framework.Assert;

import org.jmock.Mockery;
import org.jmock.api.Invocation;
import org.jmock.lib.action.CustomAction;
import org.junit.Test;
import org.kaazing.gateway.client.impl.Expectations;
import org.kaazing.gateway.client.impl.WebSocketHandler;
import org.kaazing.gateway.client.impl.WebSocketHandlerListener;
import org.kaazing.gateway.client.impl.ws.ReadyState;
import org.kaazing.gateway.client.impl.ws.WebSocketSelectedChannel;
import org.kaazing.gateway.client.impl.ws.WebSocketSelectedHandler;
import org.kaazing.gateway.client.impl.ws.WebSocketSelectedHandlerImpl;
import org.kaazing.gateway.client.impl.wsn.WebSocketNativeChannel;
import org.kaazing.gateway.client.impl.util.WSURI;
import org.kaazing.gateway.client.util.WrappedByteBuffer;

public class WebSocketSelectedHandlerTest {

    
    /*
     * step 1 fire connection open one time - channel readyState should be OPEN
     * step 2: pass mesasgeReceived event up
     */
    @Test
    public void testProcessConnect() throws URISyntaxException {

        Mockery context = new Mockery();
        final WebSocketHandler nextHandler = context.mock(WebSocketHandler.class);
        final WebSocketHandlerListener listener = context.mock(WebSocketHandlerListener.class);
        final String[] requestedProtocols = new String[] {"foo"};

        context.checking(new Expectations() {

            {
                oneOf(nextHandler).setListener(with(aNonNull(WebSocketHandlerListener.class)));
                will(saveParameter("listener", 0));
                oneOf(nextHandler).processConnect(with(aNonNull(WebSocketSelectedChannel.class)), with(aNonNull(WSURI.class)), with(equal(requestedProtocols)));
                will(new CustomAction("will fire 2 connectionOpen events") {

                    @Override
                    public Object invoke(Invocation invocation) {
                        WebSocketHandlerListener listener = (WebSocketHandlerListener)lookup("listener");
                        WebSocketSelectedChannel channel = (WebSocketSelectedChannel)invocation.getParameter(0);
                        listener.connectionOpened(channel, "foo");
                        listener.connectionOpened(channel, "foo");
                        return null;
                    }
                });
                oneOf(listener).connectionOpened(with(aNonNull(WebSocketSelectedChannel.class)), with("foo"));
                will(new CustomAction("will fire 2 MessageReceived events") {

                    @Override
                    public Object invoke(Invocation invocation) {
                        WebSocketHandlerListener listener = (WebSocketHandlerListener)lookup("listener");
                        WebSocketSelectedChannel channel = (WebSocketSelectedChannel)invocation.getParameter(0);
                        
                        Assert.assertEquals(channel.getReadyState(), ReadyState.OPEN);
                        
                        listener.binaryMessageReceived(channel, new WrappedByteBuffer("foo".getBytes()));
                        listener.binaryMessageReceived(channel, new WrappedByteBuffer("foo2".getBytes()));
                        return null;
                    }
                });
                oneOf(listener).binaryMessageReceived(with(aNonNull(WebSocketSelectedChannel.class)), with(new ByteBufferMatcher("foo", "message")));
                oneOf(listener).binaryMessageReceived(with(aNonNull(WebSocketSelectedChannel.class)), with(new ByteBufferMatcher("foo2", "message")));

            }
        });

        WSURI uri = new WSURI("ws://localhost:8001/echo");
        WebSocketNativeChannel channel = new WebSocketNativeChannel(uri);

        WebSocketSelectedHandler handler = new WebSocketSelectedHandlerImpl();
        handler.setNextHandler(nextHandler);
        handler.setListener(listener);

        handler.processConnect(channel, uri, requestedProtocols);
        context.assertIsSatisfied();
    }

    /*
     * step 1 fire connection open one time - channel readyState should be OPEN
     * step 2: fire connection close event
     */
    @Test
    public void testProcessConnectionClosed() throws URISyntaxException {

        Mockery context = new Mockery();
        final WebSocketHandler nextHandler = context.mock(WebSocketHandler.class);
        final WebSocketHandlerListener listener = context.mock(WebSocketHandlerListener.class);
        final String[] requestedProtocols = new String[] {"foo"};

        context.checking(new Expectations() {

            {
                oneOf(nextHandler).setListener(with(aNonNull(WebSocketHandlerListener.class)));
                will(saveParameter("listener", 0));
                oneOf(nextHandler).processConnect(with(aNonNull(WebSocketSelectedChannel.class)), with(aNonNull(WSURI.class)), with(equal(requestedProtocols)));
                will(new CustomAction("will fire 2 connectionOpen events") {

                    @Override
                    public Object invoke(Invocation invocation) {
                        WebSocketHandlerListener listener = (WebSocketHandlerListener)lookup("listener");
                        WebSocketSelectedChannel channel = (WebSocketSelectedChannel)invocation.getParameter(0);
                        listener.connectionOpened(channel, "foo");
                        return null;
                    }
                });
                oneOf(listener).connectionOpened(with(aNonNull(WebSocketSelectedChannel.class)), with("foo"));
                will(new CustomAction("will fire 2 connectionClosed events") {

                    @Override
                    public Object invoke(Invocation invocation) {
                        WebSocketHandlerListener listener = (WebSocketHandlerListener)lookup("listener");
                        WebSocketSelectedChannel channel = (WebSocketSelectedChannel)invocation.getParameter(0);
                        
                        Assert.assertEquals(channel.getReadyState(), ReadyState.OPEN);
                        
                        listener.connectionClosed(channel, false, 1006, null);
                        listener.connectionClosed(channel, false, 1006, null);
                        return null;
                    }
                });
                oneOf(listener).connectionClosed(with(aNonNull(WebSocketSelectedChannel.class)), with(false), with(1006), with(aNull(String.class))); //only pass one event up
               
            }
        });

        WSURI uri = new WSURI("ws://localhost:8001/echo");
        WebSocketNativeChannel channel = new WebSocketNativeChannel(uri);

        WebSocketSelectedHandler handler = new WebSocketSelectedHandlerImpl();
        handler.setNextHandler(nextHandler);
        handler.setListener(listener);

        handler.processConnect(channel, uri, requestedProtocols);
        context.assertIsSatisfied();
    }

    /*
     * step 1 fire connection open one time - channel readyState should be OPEN
     * step 2: fire connection failed event
     */
    @Test
    public void testProcessConnectionFailed() throws URISyntaxException {

        Mockery context = new Mockery();
        final WebSocketHandler nextHandler = context.mock(WebSocketHandler.class);
        final WebSocketHandlerListener listener = context.mock(WebSocketHandlerListener.class);
        final String[] requestedProtocols = new String[] {"foo"};

        context.checking(new Expectations() {

            {
                oneOf(nextHandler).setListener(with(aNonNull(WebSocketHandlerListener.class)));
                will(saveParameter("listener", 0));
                oneOf(nextHandler).processConnect(with(aNonNull(WebSocketSelectedChannel.class)), with(aNonNull(WSURI.class)), with(equal(requestedProtocols)));
                will(new CustomAction("will fire 2 connectionOpen events") {

                    @Override
                    public Object invoke(Invocation invocation) {
                        WebSocketHandlerListener listener = (WebSocketHandlerListener)lookup("listener");
                        WebSocketSelectedChannel channel = (WebSocketSelectedChannel)invocation.getParameter(0);
                        listener.connectionOpened(channel, "foo");
                        return null;
                    }
                });
                oneOf(listener).connectionOpened(with(aNonNull(WebSocketSelectedChannel.class)), with("foo"));
                will(new CustomAction("will fire 2 connectionClosed events") {

                    @Override
                    public Object invoke(Invocation invocation) {
                        WebSocketHandlerListener listener = (WebSocketHandlerListener)lookup("listener");
                        WebSocketSelectedChannel channel = (WebSocketSelectedChannel)invocation.getParameter(0);
                        
                        Assert.assertEquals(channel.getReadyState(), ReadyState.OPEN);
                        
                        listener.connectionFailed(channel, new IllegalStateException());
                        listener.connectionFailed(channel, new IllegalStateException());  // ???
                        return null;
                    }
                });
                oneOf(listener).connectionFailed(with(aNonNull(WebSocketSelectedChannel.class)), with(aNonNull(Exception.class)));//only pass one event up
               
            }
        });

        WSURI uri = new WSURI("ws://localhost:8001/echo");
        WebSocketNativeChannel channel = new WebSocketNativeChannel(uri);

        WebSocketSelectedHandler handler = new WebSocketSelectedHandlerImpl();
        handler.setNextHandler(nextHandler);
        handler.setListener(listener);

        handler.processConnect(channel, uri, requestedProtocols);
        context.assertIsSatisfied();
    }

    /*
     * if channel readyState != Open, should not raise message event
     */
    @Test
    public void testMessageReceived() throws URISyntaxException {

        Mockery context = new Mockery();
        final WebSocketHandler nextHandler = context.mock(WebSocketHandler.class);
        final WebSocketHandlerListener listener = context.mock(WebSocketHandlerListener.class);
        final String[] requestedProtocols = new String[] {"foo"};

        context.checking(new Expectations() {

            {
                oneOf(nextHandler).setListener(with(aNonNull(WebSocketHandlerListener.class)));
                will(saveParameter("listener", 0));
                oneOf(nextHandler).processConnect(with(aNonNull(WebSocketSelectedChannel.class)), with(aNonNull(WSURI.class)), with(equal(requestedProtocols)));
                will(new CustomAction("will fire MessageReceived") {

                    @Override
                    public Object invoke(Invocation invocation) {
                        WebSocketHandlerListener listener = (WebSocketHandlerListener)lookup("listener");
                        WebSocketSelectedChannel channel = (WebSocketSelectedChannel)invocation.getParameter(0);
                        listener.binaryMessageReceived(channel, new WrappedByteBuffer("foo".getBytes()));
                        return null;
                    }
                });
                never(listener).binaryMessageReceived(with(aNonNull(WebSocketSelectedChannel.class)), with(new ByteBufferMatcher("foo", "message")));
            }
        });

        WSURI uri = new WSURI("ws://localhost:8001/echo");
        WebSocketNativeChannel channel = new WebSocketNativeChannel(uri);

        WebSocketSelectedHandler handler = new WebSocketSelectedHandlerImpl();
        handler.setNextHandler(nextHandler);
        handler.setListener(listener);

        handler.processConnect(channel, uri, requestedProtocols);
        context.assertIsSatisfied();
    }

}
