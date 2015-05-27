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

import java.net.URISyntaxException;

import org.jmock.api.Invocation;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.jmock.lib.action.CustomAction;
import org.jmock.lib.concurrent.Synchroniser;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Rule;
import org.junit.Test;
import org.kaazing.gateway.client.impl.CommandMessage;
import org.kaazing.gateway.client.impl.Expectations;
import org.kaazing.gateway.client.impl.http.HttpRequest;
import org.kaazing.gateway.client.impl.http.HttpRequest.Method;
import org.kaazing.gateway.client.impl.http.HttpRequestHandler;
import org.kaazing.gateway.client.impl.http.HttpRequestListener;
import org.kaazing.gateway.client.impl.http.HttpResponse;
import org.kaazing.gateway.client.util.HttpURI;
import org.kaazing.gateway.client.util.WrappedByteBuffer;


public class DownstreamHandlerTest {

    @Rule
    public final JUnitRuleMockery context = new JUnitRuleMockery();
    
    /*
     * call nextHandler.processOpen()
     * but event should not pass through
     */
    @Test
    public void testProcessConnect() throws URISyntaxException {

        context.setThreadingPolicy(new Synchroniser());
        context.setImposteriser(ClassImposteriser.INSTANCE);
        final HttpRequestHandler nextHandler = context.mock(HttpRequestHandler.class);
        final DownstreamHandlerListener listener = context.mock(DownstreamHandlerListener.class);
        final HttpResponse mockResponse = context.mock(HttpResponse.class);
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
                        channel.setResponse(mockResponse);
                        listener.requestOpened(channel);
                        return null;
                    }
                });
                oneOf(mockResponse).getHeader(with("X-Idle-Timeout")); will(returnValue(null));
                oneOf(listener).downstreamOpened(with(any(DownstreamChannel.class)));
            }
        });

        HttpURI uri = new HttpURI("http://localhost:8001/echo?.kid=629160430515185");

        DownstreamChannel channel = new DownstreamChannel(uri, null);

        DownstreamHandler handler = new DownstreamHandlerImpl();
        handler.setNextHandler(nextHandler);
        handler.setListener(listener);

        handler.processConnect(channel, uri);
    }
    
    @Test
    public void testDownStreamFailedOnIdleTimeout() throws Exception {

        context.setThreadingPolicy(new Synchroniser());
        context.setImposteriser(ClassImposteriser.INSTANCE);
        final HttpRequestHandler nextHandler = context.mock(HttpRequestHandler.class);
        final DownstreamHandlerListener listener = context.mock(DownstreamHandlerListener.class);
        final HttpResponse mockResponse = context.mock(HttpResponse.class);
        
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
                        channel.setResponse(mockResponse);
                        listener.requestOpened(channel);
                        return null;
                    }
                });
                oneOf(mockResponse).getHeader(with("X-Idle-Timeout")); will(returnValue("2"));
                oneOf(listener).downstreamOpened(with(any(DownstreamChannel.class)));
                oneOf(listener).downstreamFailed(with(any(DownstreamChannel.class)), with(any(Exception.class)));
            }
        });

        HttpURI uri = new HttpURI("http://localhost:8001/echo?.kid=629160430515185");

        DownstreamChannel channel = new DownstreamChannel(uri, null);

        DownstreamHandler handler = new DownstreamHandlerImpl();
        handler.setNextHandler(nextHandler);
        handler.setListener(listener);

        handler.processConnect(channel, uri);
        
        // sleep for a 2.5 seconds so that the idle duration is greater than idle timeout (2 seconds)
        Thread.sleep(2500);
    }

    /*
     * pass through function call
     * but event should not pass through
     */
    @Test
    public void testRequestReady() throws URISyntaxException {

        final HttpRequestHandler nextHandler = context.mock(HttpRequestHandler.class);
        final DownstreamHandlerListener listener = context.mock(DownstreamHandlerListener.class);

        context.setThreadingPolicy(new Synchroniser());
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
                oneOf(nextHandler).processSend(with(aNonNull(HttpRequest.class)), with(aNonNull(WrappedByteBuffer.class))); //with(new ByteBufferMatcher(">|<", "kaazing flag")));
           }
        });

        HttpURI uri = new HttpURI("http://localhost:8001/echo?.kid=629160430515185");

        DownstreamChannel channel = new DownstreamChannel(uri, null);
        channel.attemptProxyModeFallback.set(false);

        DownstreamHandler handler = new DownstreamHandlerImpl();
        handler.setNextHandler(nextHandler);
        handler.setListener(listener);

        handler.processConnect(channel, uri);
    }

    /*
     * pass through function call
     * fire messageReceived / commandReceived
     */
    @Test
    public void testRequestProgressed() throws URISyntaxException {

        final HttpRequestHandler nextHandler = context.mock(HttpRequestHandler.class);
        final DownstreamHandlerListener listener = context.mock(DownstreamHandlerListener.class);

        context.setThreadingPolicy(new Synchroniser());
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
                        WrappedByteBuffer payLoad = new WrappedByteBuffer(new byte[] {-128,3,70,79,79,1,48,48,-1,1,48,48,-1}); //received "FOO"
                        listener.requestProgressed(channel, payLoad);
                        return null;
                    }
                });
                oneOf(listener).binaryMessageReceived(with(aNonNull(DownstreamChannel.class)), with(new ByteBufferMatcher("FOO", "message")));
                will(new CustomAction("will fire requestProgressed - control frame") {

                    @Override
                    public Object invoke(Invocation invocation) throws URISyntaxException {
                        HttpRequestListener listener = (HttpRequestListener)lookup("listener");
                        HttpRequest request = new HttpRequest(Method.POST,  new HttpURI("http://localhost:8001/echo?.kid=629160430515185"));
                        request.parent = (DownstreamChannel)invocation.getParameter(0);
                        WrappedByteBuffer payLoad = new WrappedByteBuffer(new byte[] {1,48,48,-1});
                        listener.requestProgressed(request, payLoad);
                        return null;
                    }
                });
                //no messageReceived fired on control frame message
                never(listener).binaryMessageReceived(with(any(DownstreamChannel.class)), with(any(WrappedByteBuffer.class)));
             }
        });

        HttpURI uri = new HttpURI("http://localhost:8001/echo?.kid=629160430515185");

        DownstreamChannel channel = new DownstreamChannel(uri, null);
        channel.attemptProxyModeFallback.set(false);

        DownstreamHandler handler = new DownstreamHandlerImpl();
        handler.setNextHandler(nextHandler);
        handler.setListener(listener);

        handler.processConnect(channel, uri);
    }

    /*
     * pass through function call
     * but event should not pass through
     */
    @Test
    public void testRequestAborted() throws URISyntaxException {

        final HttpRequestHandler nextHandler = context.mock(HttpRequestHandler.class);
        final DownstreamHandlerListener listener = context.mock(DownstreamHandlerListener.class);

        context.setThreadingPolicy(new Synchroniser());
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
            }
        });

        HttpURI uri = new HttpURI("http://localhost:8001/echo?.kid=629160430515185");

        DownstreamChannel channel = new DownstreamChannel(uri, null);
        channel.attemptProxyModeFallback.set(false);

        DownstreamHandler handler = new DownstreamHandlerImpl();
        handler.setNextHandler(nextHandler);
        handler.setListener(listener);

        handler.processConnect(channel, uri);
    }

    /*
     * pass through function call
     * but event should not pass through
     */
    @Test
    public void testRequestClosed() throws URISyntaxException {

        final HttpRequestHandler nextHandler = context.mock(HttpRequestHandler.class);
        final DownstreamHandlerListener listener = context.mock(DownstreamHandlerListener.class);

        context.setThreadingPolicy(new Synchroniser());
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

                //channel.outstandingRequests.remove(request);
            }
        });

        HttpURI uri = new HttpURI("http://localhost:8001/echo?.kid=629160430515185");

        DownstreamChannel channel = new DownstreamChannel(uri, null);
        channel.attemptProxyModeFallback.set(false);

        DownstreamHandler handler = new DownstreamHandlerImpl();
        handler.setNextHandler(nextHandler);
        handler.setListener(listener);

        handler.processConnect(channel, uri);
    }

    /*
     * pass through function call
     * reconnect(channel);
     */
    @Test
    public void testRequestLoaded() throws URISyntaxException {

        final HttpRequestHandler nextHandler = context.mock(HttpRequestHandler.class);
        final DownstreamHandlerListener listener = context.mock(DownstreamHandlerListener.class);

        context.setThreadingPolicy(new Synchroniser());
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
                        HttpResponse response = new HttpResponse();
                        
                        WrappedByteBuffer buffer = new WrappedByteBuffer(new byte[]{0x01, 0x30, 0x32, (byte) 0xFF, 0x01, 0x30, 0x31, (byte) 0xFF});
                        listener.requestProgressed(channel, buffer);
                        listener.requestLoaded(channel, response);
                        return null;
                    }
                });
                oneOf(listener).commandMessageReceived(with(aNonNull(DownstreamChannel.class)), with(aNonNull(CommandMessage.class)));
                oneOf(listener).downstreamClosed(with(aNonNull(DownstreamChannel.class)));
            }
        });

        HttpURI uri = new HttpURI("http://localhost:8001/echo?.kid=629160430515185");

        DownstreamChannel channel = new DownstreamChannel(uri, null);
        channel.attemptProxyModeFallback.set(false);

        DownstreamHandler handler = new DownstreamHandlerImpl();
        handler.setNextHandler(nextHandler);
        handler.setListener(listener);

        handler.processConnect(channel, uri);
    }

    /*
     * pass through function call
     * fire createFailed event
     */
    @Test
    public void testErrorOccured() throws URISyntaxException {

        final HttpRequestHandler nextHandler = context.mock(HttpRequestHandler.class);
        final DownstreamHandlerListener listener = context.mock(DownstreamHandlerListener.class);

        context.setThreadingPolicy(new Synchroniser());
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
                        listener.errorOccurred(channel, new IllegalStateException());
                        return null;
                    }
                });
                //fire createFailed
                oneOf(listener).downstreamFailed(with(aNonNull(DownstreamChannel.class)), with(aNonNull(Exception.class)));
            }
        });

        HttpURI uri = new HttpURI("http://localhost:8001/echo?.kid=629160430515185");

        DownstreamChannel channel = new DownstreamChannel(uri, null);
        channel.attemptProxyModeFallback.set(false);

        DownstreamHandler handler = new DownstreamHandlerImpl();
        handler.setNextHandler(nextHandler);
        handler.setListener(listener);

        handler.processConnect(channel, uri);
    }

}
