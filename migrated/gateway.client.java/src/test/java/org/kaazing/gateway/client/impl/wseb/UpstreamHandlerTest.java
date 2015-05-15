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

import org.jmock.Mockery;
import org.jmock.api.Invocation;
import org.jmock.lib.action.CustomAction;
import org.junit.Test;
import org.kaazing.gateway.client.impl.Expectations;
import org.kaazing.gateway.client.impl.http.HttpRequest;
import org.kaazing.gateway.client.impl.http.HttpRequestHandler;
import org.kaazing.gateway.client.impl.http.HttpRequestListener;
import org.kaazing.gateway.client.impl.http.HttpResponse;
import org.kaazing.gateway.client.impl.util.WebSocketUtil;
import org.kaazing.gateway.client.impl.wseb.UpstreamHandlerListener;
import org.kaazing.gateway.client.util.HttpURI;
import org.kaazing.gateway.client.util.WrappedByteBuffer;


public class UpstreamHandlerTest {

    /*
     * no action in processOpen()
     */
    @Test
    public void testProcessConnect() throws URISyntaxException {

        Mockery context = new Mockery();
        final HttpRequestHandler nextHandler = context.mock(HttpRequestHandler.class);
        final UpstreamHandlerListener listener = context.mock(UpstreamHandlerListener.class);

        context.checking(new Expectations() {

            {
                oneOf(nextHandler).setListener(with(aNonNull(HttpRequestListener.class)));
                will(saveParameter("listener", 0));
                never(nextHandler).processOpen(with(aNonNull(HttpRequest.class)));
                
            }
        });

        HttpURI uri = new HttpURI("http://localhost:8001/echo");

        UpstreamChannel channel = new UpstreamChannel(uri, null);

        UpstreamHandler handler = new UpstreamHandlerImpl();
        //now use mock object for nextHandler
        handler.setNextHandler(nextHandler);
        handler.setListener(listener);
        
        handler.processOpen(channel);
        context.assertIsSatisfied();
    }

    /*
     * test send text message
     * and RequestReady
     */
    @Test
    public void testSendTextMessage() throws URISyntaxException {

        Mockery context = new Mockery();
        final HttpRequestHandler nextHandler = context.mock(HttpRequestHandler.class);
        final UpstreamHandlerListener listener = context.mock(UpstreamHandlerListener.class);

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
                byte[] payload = encodeText("Foo").array();
                oneOf(nextHandler).processSend(with(aNonNull(HttpRequest.class)), with(new ByteBufferMatcher(new String(payload), "send message")));
           }
        });

        HttpURI uri = new HttpURI("http://localhost:8001/echo");

        UpstreamChannel channel = new UpstreamChannel(uri, null);

        UpstreamHandler handler = new UpstreamHandlerImpl();
        handler.setNextHandler(nextHandler);
        handler.setListener(listener);

        handler.processTextMessage(channel, "Foo");
        context.assertIsSatisfied();
    }

    /*
     * test send binary message
     * and RequestReady
     */
    @Test
    public void testSendBinaryMessage() throws URISyntaxException {

        Mockery context = new Mockery();
        final HttpRequestHandler nextHandler = context.mock(HttpRequestHandler.class);
        final UpstreamHandlerListener listener = context.mock(UpstreamHandlerListener.class);

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
                byte[] payload = encodeBinary(new WrappedByteBuffer("Foo".getBytes())).array();
                oneOf(nextHandler).processSend(with(aNonNull(HttpRequest.class)), with(new ByteBufferMatcher(new String(payload), "send message")));
           }
        });

        HttpURI uri = new HttpURI("http://localhost:8001/echo");

        UpstreamChannel channel = new UpstreamChannel(uri, null);

        UpstreamHandler handler = new UpstreamHandlerImpl();
        handler.setNextHandler(nextHandler);
        handler.setListener(listener);

        handler.processBinaryMessage(channel, new WrappedByteBuffer("Foo".getBytes()));
        context.assertIsSatisfied();
    }
    
    /*
     * test processClose - send close Command Frame
     */
    @Test
    public void testprocessClose() throws URISyntaxException {

        Mockery context = new Mockery();
        final HttpRequestHandler nextHandler = context.mock(HttpRequestHandler.class);
        final UpstreamHandlerListener listener = context.mock(UpstreamHandlerListener.class);

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
                //send close websocket command + reconnect command
                byte[] payload = { (byte)0x01, (byte)0x30, (byte)0x32, (byte)0xff, (byte)0x01, (byte)0x30, (byte)0x31, (byte)0xff };
                oneOf(nextHandler).processSend(with(aNonNull(HttpRequest.class)), with(new ByteBufferMatcher(new String(payload), "send message")));
           }
        });

        HttpURI uri = new HttpURI("http://localhost:8001/echo");

        UpstreamChannel channel = new UpstreamChannel(uri, null);

        UpstreamHandler handler = new UpstreamHandlerImpl();
        handler.setNextHandler(nextHandler);
        handler.setListener(listener);

        handler.processClose(channel, 0, null);
        context.assertIsSatisfied();
    }
    
    /*
     * test RequestLoaded
     */
    @Test
    public void testRequestLoaded() throws URISyntaxException {

        Mockery context = new Mockery();
        final HttpRequestHandler nextHandler = context.mock(HttpRequestHandler.class);
        final UpstreamHandlerListener listener = context.mock(UpstreamHandlerListener.class);

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
                        listener.requestLoaded(channel, new HttpResponse());
                        return null;
                    }
                });
                oneOf(nextHandler).processOpen(with(aNonNull(HttpRequest.class)));
           }
        });

        HttpURI uri = new HttpURI("http://localhost:8001/echo");

        UpstreamChannel channel = new UpstreamChannel(uri, null);

        UpstreamHandler handler = new UpstreamHandlerImpl();
        handler.setNextHandler(nextHandler);
        handler.setListener(listener);

        handler.processBinaryMessage(channel, new WrappedByteBuffer("Foo".getBytes()));
        context.assertIsSatisfied();
    }
    
    /*
     * test send binary message
     * and errorOccured
     */
    @Test
    public void testErrorOccured() throws URISyntaxException {

        Mockery context = new Mockery();
        final HttpRequestHandler nextHandler = context.mock(HttpRequestHandler.class);
        final UpstreamHandlerListener listener = context.mock(UpstreamHandlerListener.class);

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
                        listener.errorOccurred(channel, new IllegalStateException());
                        return null;
                    }
                });
                oneOf(listener).upstreamFailed(with(aNonNull(UpstreamChannel.class)), with(aNonNull(Exception.class)));
           }
        });

        HttpURI uri = new HttpURI("http://localhost:8001/echo");

        UpstreamChannel channel = new UpstreamChannel(uri, null);

        UpstreamHandler handler = new UpstreamHandlerImpl();
        handler.setNextHandler(nextHandler);
        handler.setListener(listener);

        handler.processBinaryMessage(channel, new WrappedByteBuffer("Foo".getBytes()));
        context.assertIsSatisfied();
    }
    
    private static final byte WS_TEXT_FRAME_END = (byte) 0xff;
    private static final byte WS_BINARY_FRAME_START = (byte) 0x80;
    private static final byte WS_PERLENGTH_TEXT_FRAME_START = (byte) 0x81;
    private static final byte[] RECONNECT_EVENT_BYTES = { (byte)0x01, (byte)0x30, (byte)0x31, (byte)0xff };
        
    private WrappedByteBuffer encodeText(String payload) {
        
    	byte[] bytes = payload.getBytes();
    	int length = bytes.length;
    	
        // The largest frame that can be received is 5 bytes (encoded 32 bit length header + trailing byte)
        WrappedByteBuffer frame = WrappedByteBuffer.allocate(length + 6);
        frame.put(WS_PERLENGTH_TEXT_FRAME_START);           // write text type header
        WebSocketUtil.encodeLength(frame, length);  // write length prefix
        frame.putBytes(bytes);       // write payload
        
        frame.putBytes(RECONNECT_EVENT_BYTES); //automatic add reconnect command
        frame.flip();
        return frame;
    }
    
    private WrappedByteBuffer encodeBinary(WrappedByteBuffer message) {
        int length = message.remaining();
        
        // The largest frame that can be received is 5 bytes (encoded 32 bit length header + trailing byte)
        WrappedByteBuffer frame = WrappedByteBuffer.allocate(length + 6);
        frame.put(WS_BINARY_FRAME_START);           // write binary type header
        WebSocketUtil.encodeLength(frame, length);  // write length prefix
        frame.putBuffer(message.duplicate());       // write payload
        
        frame.putBytes(RECONNECT_EVENT_BYTES);  //automatic add reconnect command
        frame.flip();
        return frame;
    }

    
}
