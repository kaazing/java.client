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

package org.kaazing.gateway.client.impl.wseb;

import org.jmock.Mockery;
import org.jmock.api.Invocation;
import org.jmock.lib.action.CustomAction;
import org.junit.Assert;
import org.junit.Test;
import org.kaazing.gateway.client.impl.Expectations;
import org.kaazing.gateway.client.impl.http.HttpRequest;
import org.kaazing.gateway.client.impl.http.HttpRequestHandler;
import org.kaazing.gateway.client.impl.http.HttpRequestListener;
import org.kaazing.gateway.client.impl.http.HttpResponse;
import org.kaazing.gateway.client.impl.util.WSURI;
import org.kaazing.gateway.client.impl.ws.WebSocketCompositeChannel;
import org.kaazing.gateway.client.util.HttpURI;
import org.kaazing.gateway.client.util.WrappedByteBuffer;
import org.kaazing.net.ws.WebSocket;
import org.kaazing.net.ws.WebSocketFactory;
import org.kaazing.net.ws.impl.WebSocketImpl;


public class CreateHandlerTest {

    /*
     * pass through function call
     * but event should not pass through
     */
    @Test
    public void testProcessOpen() throws Exception {

        Mockery context = new Mockery();
        final HttpRequestHandler nextHandler = context.mock(HttpRequestHandler.class);
        final CreateHandlerListener listener = context.mock(CreateHandlerListener.class);

        context.checking(new Expectations() {

            {
                oneOf(nextHandler).setListener(with(aNonNull(HttpRequestListener.class)));
                will(saveParameter("listener", 0));
                oneOf(nextHandler).processOpen(with(aNonNull(HttpRequest.class)));
                will(new CustomAction("will fire connectionOpen") {

                    @Override
                    public Object invoke(Invocation invocation) {
                        HttpRequestListener listener = (HttpRequestListener)lookup("listener");
                        HttpRequest channel = (HttpRequest)invocation.getParameter(0);
                        listener.requestOpened(channel);
                        return null;
                    }
                });
                //no event should pass through
                never(listener).createCompleted(with(any(CreateChannel.class)), with(any(HttpURI.class)), with(any(HttpURI.class)), with(any(String.class)));
            }
        });

        HttpURI uri = new HttpURI("http://localhost:8001/echo");

        CreateChannel channel = getCreateChannel();

        CreateHandler handler = CreateHandlerImpl.FACTORY.createCreateHandler();
        handler.setNextHandler(nextHandler);
        handler.setListener(listener);

        handler.processOpen(channel, uri);
        context.assertIsSatisfied();
    }
    
    @Test
    public void testProcessOpenWithProtocol() throws Exception {

        Mockery context = new Mockery();
        final HttpRequestHandler nextHandler = context.mock(HttpRequestHandler.class);
        final CreateHandlerListener listener = context.mock(CreateHandlerListener.class);
        final String bumpProtocol = "x-kaazing-bump";
        context.checking(new Expectations() {

            {
                oneOf(nextHandler).setListener(with(aNonNull(HttpRequestListener.class)));
                will(saveParameter("listener", 0));
                oneOf(nextHandler).processOpen(with(aNonNull(HttpRequest.class)));
                will(new CustomAction("will fire connectionOpen") {

                    @Override
                    public Object invoke(Invocation invocation) {
                        HttpRequestListener listener = (HttpRequestListener)lookup("listener");
                        HttpRequest request = (HttpRequest)invocation.getParameter(0);
                        String protocol = request.getHeaders().get("X-WebSocket-Protocol");
                        Assert.assertNotNull("X-WebSocket-Protocol header should not be null", protocol);
                        Assert.assertEquals("X-WebSocket-Protocol header value should be x-kaazing-bump", protocol, bumpProtocol);
                        listener.requestOpened(request);
                        return null;
                    }
                });
                //no event should pass through
                never(listener).createCompleted(with(any(CreateChannel.class)), with(any(HttpURI.class)), with(any(HttpURI.class)), with(any(String.class)));
            }
        });

        HttpURI uri = new HttpURI("http://localhost:8001/echo");

        CreateChannel channel = getCreateChannel();
        channel.setProtocols(new String[]{bumpProtocol});

        CreateHandler handler = CreateHandlerImpl.FACTORY.createCreateHandler();
        handler.setNextHandler(nextHandler);
        handler.setListener(listener);

        handler.processOpen(channel, uri);
        context.assertIsSatisfied();
    }
    
    @Test
    public void testProcessOpenWithMultipleProtocols() throws Exception {

        Mockery context = new Mockery();
        final HttpRequestHandler nextHandler = context.mock(HttpRequestHandler.class);
        final CreateHandlerListener listener = context.mock(CreateHandlerListener.class);
        String bumpProtocol = "x-kaazing-bump";
        String handshakeProtocol = "x-kaazing-handshake";
        final String[] protocols = new String[]{handshakeProtocol, bumpProtocol};
        final String expectedProtocolHeaderValue = handshakeProtocol + "," + bumpProtocol;
        context.checking(new Expectations() {

            {
                oneOf(nextHandler).setListener(with(aNonNull(HttpRequestListener.class)));
                will(saveParameter("listener", 0));
                oneOf(nextHandler).processOpen(with(aNonNull(HttpRequest.class)));
                will(new CustomAction("will fire connectionOpen") {

                    @Override
                    public Object invoke(Invocation invocation) {
                        HttpRequestListener listener = (HttpRequestListener)lookup("listener");
                        HttpRequest request = (HttpRequest)invocation.getParameter(0);
                        String protocols = request.getHeaders().get("X-WebSocket-Protocol");
                        Assert.assertNotNull("X-WebSocket-Protocol header should not be null", protocols);
                        Assert.assertEquals("X-WebSocket-Protocol header value should be 'x-kaazing-handshake,x-kaazing-bump'", protocols, expectedProtocolHeaderValue);
                        listener.requestOpened(request);
                        return null;
                    }
                });
                //no event should pass through
                never(listener).createCompleted(with(any(CreateChannel.class)), with(any(HttpURI.class)), with(any(HttpURI.class)), with(any(String.class)));
            }
        });

        HttpURI uri = new HttpURI("http://localhost:8001/echo");

        CreateChannel channel = getCreateChannel();
        channel.setProtocols(protocols);

        CreateHandler handler = CreateHandlerImpl.FACTORY.createCreateHandler();
        handler.setNextHandler(nextHandler);
        handler.setListener(listener);

        handler.processOpen(channel, uri);
        context.assertIsSatisfied();
    }

    /*
     * pass through function call
     * but event should not pass through
     */
    @Test
    public void testRequestReady() throws Exception {

        Mockery context = new Mockery();
        final HttpRequestHandler nextHandler = context.mock(HttpRequestHandler.class);
        final CreateHandlerListener listener = context.mock(CreateHandlerListener.class);

        context.checking(new Expectations() {

            {
                oneOf(nextHandler).setListener(with(aNonNull(HttpRequestListener.class)));
                will(saveParameter("listener", 0));
                oneOf(nextHandler).processOpen(with(aNonNull(HttpRequest.class)));
                will(new CustomAction("will fire requestReady") {

                    @Override
                    public Object invoke(Invocation invocation) {
                        HttpRequestListener listener = (HttpRequestListener)lookup("listener");
                        HttpRequest channel = (HttpRequest)invocation.getParameter(0);
                        listener.requestReady(channel);
                        return null;
                    }
                });
                //no event should pass through
                never(listener).createCompleted(with(any(CreateChannel.class)), with(any(HttpURI.class)), with(any(HttpURI.class)), with(any(String.class)));
            }
        });

        HttpURI uri = new HttpURI("http://localhost:8001/echo");

        CreateChannel channel = getCreateChannel();

        CreateHandler handler = CreateHandlerImpl.FACTORY.createCreateHandler();
        handler.setNextHandler(nextHandler);
        handler.setListener(listener);

        handler.processOpen(channel, uri);
        context.assertIsSatisfied();
    }

        /*
     * pass through function call
     * but event should not pass through
     */
    @Test
    public void testRequestProgressed() throws Exception {

        Mockery context = new Mockery();
        final HttpRequestHandler nextHandler = context.mock(HttpRequestHandler.class);
        final CreateHandlerListener listener = context.mock(CreateHandlerListener.class);

        context.checking(new Expectations() {

            {
                oneOf(nextHandler).setListener(with(aNonNull(HttpRequestListener.class)));
                will(saveParameter("listener", 0));
                oneOf(nextHandler).processOpen(with(aNonNull(HttpRequest.class)));
                will(new CustomAction("will fire requestProgressed") {

                    @Override
                    public Object invoke(Invocation invocation) {
                        HttpRequestListener listener = (HttpRequestListener)lookup("listener");
                        HttpRequest channel = (HttpRequest)invocation.getParameter(0);
                        listener.requestProgressed(channel, null);
                        return null;
                    }
                });
                //no event should pass through
                never(listener).createCompleted(with(any(CreateChannel.class)), with(any(HttpURI.class)), with(any(HttpURI.class)), with(any(String.class)));
            }
        });

        HttpURI uri = new HttpURI("http://localhost:8001/echo");

        CreateChannel channel = getCreateChannel();

        CreateHandler handler = CreateHandlerImpl.FACTORY.createCreateHandler();
        handler.setNextHandler(nextHandler);
        handler.setListener(listener);

        handler.processOpen(channel, uri);
        context.assertIsSatisfied();
    }

    /*
     * pass through function call
     * but event should not pass through
     */
    @Test
    public void testRequestAborted() throws Exception {

        Mockery context = new Mockery();
        final HttpRequestHandler nextHandler = context.mock(HttpRequestHandler.class);
        final CreateHandlerListener listener = context.mock(CreateHandlerListener.class);

        context.checking(new Expectations() {

            {
                oneOf(nextHandler).setListener(with(aNonNull(HttpRequestListener.class)));
                will(saveParameter("listener", 0));
                oneOf(nextHandler).processOpen(with(aNonNull(HttpRequest.class)));
                will(new CustomAction("will fire requestAborted") {

                    @Override
                    public Object invoke(Invocation invocation) {
                        HttpRequestListener listener = (HttpRequestListener)lookup("listener");
                        HttpRequest channel = (HttpRequest)invocation.getParameter(0);
                        listener.requestAborted(channel);
                        return null;
                    }
                });
                //no event should pass through
                never(listener).createCompleted(with(any(CreateChannel.class)), with(any(HttpURI.class)), with(any(HttpURI.class)), with(any(String.class)));
            }
        });

        HttpURI uri = new HttpURI("http://localhost:8001/echo");

        CreateChannel channel = getCreateChannel();

        CreateHandler handler = CreateHandlerImpl.FACTORY.createCreateHandler();
        handler.setNextHandler(nextHandler);
        handler.setListener(listener);

        handler.processOpen(channel, uri);
        context.assertIsSatisfied();
    }

    /*
     * pass through function call
     * but event should not pass through
     */
    @Test
    public void testRequestClosed() throws Exception {

        Mockery context = new Mockery();
        final HttpRequestHandler nextHandler = context.mock(HttpRequestHandler.class);
        final CreateHandlerListener listener = context.mock(CreateHandlerListener.class);

        context.checking(new Expectations() {

            {
                oneOf(nextHandler).setListener(with(aNonNull(HttpRequestListener.class)));
                will(saveParameter("listener", 0));
                oneOf(nextHandler).processOpen(with(aNonNull(HttpRequest.class)));
                will(new CustomAction("will fire requestClosed") {

                    @Override
                    public Object invoke(Invocation invocation) {
                        HttpRequestListener listener = (HttpRequestListener)lookup("listener");
                        HttpRequest channel = (HttpRequest)invocation.getParameter(0);
                        listener.requestClosed(channel);
                        return null;
                    }
                });
                //no event should pass through
                never(listener).createCompleted(with(any(CreateChannel.class)), with(any(HttpURI.class)), with(any(HttpURI.class)), with(any(String.class)));
            }
        });

        HttpURI uri = new HttpURI("http://localhost:8001/echo");

        CreateChannel channel = getCreateChannel();

        CreateHandler handler = CreateHandlerImpl.FACTORY.createCreateHandler();
        handler.setNextHandler(nextHandler);
        handler.setListener(listener);

        handler.processOpen(channel, uri);
        context.assertIsSatisfied();
    }

    /*
     * pass through function call
     * fire createCompleted
     */
    @Test
    public void testRequestLoaded() throws Exception {

        Mockery context = new Mockery();
        final HttpRequestHandler nextHandler = context.mock(HttpRequestHandler.class);
        final CreateHandlerListener listener = context.mock(CreateHandlerListener.class);

        context.checking(new Expectations() {

            {
                oneOf(nextHandler).setListener(with(aNonNull(HttpRequestListener.class)));
                will(saveParameter("listener", 0));
                oneOf(nextHandler).processOpen(with(aNonNull(HttpRequest.class)));
                will(new CustomAction("will fire requestLoaded") {

                    @Override
                    public Object invoke(Invocation invocation) {
                        HttpRequestListener listener = (HttpRequestListener)lookup("listener");
                        HttpRequest channel = (HttpRequest)invocation.getParameter(0);
                        HttpResponse response = new HttpResponse();

                        String body = "http://localhost:8001/echo/;e/ub?.kv=10.05&.kz=uLsDpoYK6UyfchrIlU4p0paUdS2BssL9\n" +
"http://localhost:8001/echo/;e/db?.kv=10.05&.kz=uLsDpoYK6UyfchrIlU4p0paUdS2BssL9";
                        response.setBody(new WrappedByteBuffer(body.getBytes()));
                        response.setHeader("X-WebSocket-Extensions", "x-kaazing-http-revalidate; 56414C49");
                        listener.requestLoaded(channel, response);
                        return null;
                    }
                });
                //
                oneOf(listener).createCompleted(with(aNonNull(CreateChannel.class)), with(aNonNull(HttpURI.class)), with(aNonNull(HttpURI.class)), with(aNull(String.class)));

            }
        });

        HttpURI uri = new HttpURI("http://localhost:8001/echo");

        CreateChannel channel = getCreateChannel();
        CreateHandler handler = CreateHandlerImpl.FACTORY.createCreateHandler();
        handler.setNextHandler(nextHandler);
        handler.setListener(listener);

        handler.processOpen(channel, uri);
        context.assertIsSatisfied();
    }
    
    /*
     * pass through function call
     * fire createFailed event
     */
    @Test
    public void testErrorOccured() throws Exception {

        Mockery context = new Mockery();
        final HttpRequestHandler nextHandler = context.mock(HttpRequestHandler.class);
        final CreateHandlerListener listener = context.mock(CreateHandlerListener.class);

        context.checking(new Expectations() {

            {
                oneOf(nextHandler).setListener(with(aNonNull(HttpRequestListener.class)));
                will(saveParameter("listener", 0));
                oneOf(nextHandler).processOpen(with(aNonNull(HttpRequest.class)));
                will(new CustomAction("will fire errorOccurred") {

                    @Override
                    public Object invoke(Invocation invocation) {
                        HttpRequestListener listener = (HttpRequestListener)lookup("listener");
                        HttpRequest channel = (HttpRequest)invocation.getParameter(0);
                        listener.errorOccurred(channel, new IllegalArgumentException());
                        return null;
                    }
                });
                //fire createFailed
                oneOf(listener).createFailed(with(aNonNull(CreateChannel.class)), with(aNonNull(Exception.class)));
            }
        });

        HttpURI uri = new HttpURI("http://localhost:8001/echo");

        CreateChannel channel = getCreateChannel();

        CreateHandler handler = CreateHandlerImpl.FACTORY.createCreateHandler();
        handler.setNextHandler(nextHandler);
        handler.setListener(listener);

        handler.processOpen(channel, uri);
        context.assertIsSatisfied();
    }
    
    // Returns a CreateChannel whose hierarchy is all setup properly all the
    // up to the WebSocket.
    private CreateChannel getCreateChannel() throws Exception {
        CreateChannel channel = new CreateChannel();
        WSURI wsUri = new WSURI("ws://localhost:8001/echo");
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        WebSocket ws = wsFactory.createWebSocket(wsUri.getURI(), (String[])null);
        WebSocketCompositeChannel cc = ((WebSocketImpl)ws).getCompositeChannel();
        WebSocketEmulatedChannel ec = new WebSocketEmulatedChannel(wsUri);
        
        ec.setParent(cc);
        channel.setParent(ec);

        return channel;
    }

}
