/**
 * Copyright 2007-2015, Kaazing Corporation. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kaazing.gateway.client.impl.wsn;

import java.net.PasswordAuthentication;
import java.net.URI;
import java.net.URISyntaxException;

import org.jmock.Mockery;
import org.jmock.api.Invocation;
import org.jmock.lib.action.CustomAction;
import org.junit.Test;
import org.kaazing.gateway.client.impl.Expectations;
import org.kaazing.gateway.client.impl.WebSocketChannel;
import org.kaazing.gateway.client.impl.WebSocketHandler;
import org.kaazing.gateway.client.impl.WebSocketHandlerListener;
import org.kaazing.gateway.client.impl.util.WSURI;
import org.kaazing.gateway.client.util.WrappedByteBuffer;
import org.kaazing.net.auth.BasicChallengeHandler;
import org.kaazing.net.auth.ChallengeHandler;
import org.kaazing.net.auth.ChallengeRequest;
import org.kaazing.net.auth.ChallengeResponse;
import org.kaazing.net.auth.LoginHandler;
import org.kaazing.net.ws.WebSocket;
import org.kaazing.net.ws.WebSocketFactory;
import org.kaazing.net.ws.impl.WebSocketImpl;


public class WebSocketNativeAuthenticationHandlerTest {


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
            }
        });

        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        ChallengeHandler ch = new DummyChallellengeHandler();
        wsFactory.setDefaultChallengeHandler(ch);
        WebSocket        ws = wsFactory.createWebSocket(URI.create("ws://locationhost:8001/echo"), (String[])null);

        WSURI uri = new WSURI("ws://locationhost:8001/echo");
        WebSocketNativeChannel channel = new WebSocketNativeChannel(uri);
        channel.setParent(((WebSocketImpl)ws).getCompositeChannel());
        ((WebSocketImpl)ws).getCompositeChannel().setChallengeHandler(ch);

        WebSocketNativeAuthenticationHandler handler = new WebSocketNativeAuthenticationHandler();
        handler.setNextHandler(nextHandler);
        handler.setListener(listener);

        handler.processConnect(channel, uri, requestedProtocols);
        context.assertIsSatisfied();
    }

    @Test
    public void testProcessAuthorizeWithoutChallengeHandler() throws URISyntaxException {

        Mockery context = new Mockery();
        final WebSocketHandler nextHandler = context.mock(WebSocketHandler.class);
        final WebSocketHandlerListener listener = context.mock(WebSocketHandlerListener.class);

        context.checking(new Expectations() {

            {
                oneOf(nextHandler).setListener(with(aNonNull(WebSocketHandlerListener.class)));
                will(saveParameter("listener", 0));
                oneOf(nextHandler).processTextMessage(with(aNonNull(WebSocketChannel.class)), with("Application Basic"));
                will(new CustomAction("will fire 401 challenge") {

                    @Override
                    public Object invoke(Invocation invocation) {
                        WebSocketHandlerListener listener = (WebSocketHandlerListener)lookup("listener");
                       
                        WebSocketNativeChannel channel = (WebSocketNativeChannel)invocation.getParameter(0);
                        listener.authenticationRequested(channel, "ws://localhost:8000/echo", "Application Basic");
                        return null;
                    }
                });
                // oneOf(nextHandler).processClose(with(aNonNull(WebSocketChannel.class)), with(CloseCommandMessage.CLOSE_ABNORMAL), with(aNull(String.class)));
                oneOf(nextHandler).processClose(with(aNonNull(WebSocketChannel.class)), with(1000), with(aNull(String.class)));
                oneOf(listener).connectionClosed(with(aNonNull(WebSocketChannel.class)), with(aNonNull(Exception.class)));
            }
        });

        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        ChallengeHandler ch =  new DummyChallellengeHandler();
        wsFactory.setDefaultChallengeHandler(ch);
        WebSocket        ws = wsFactory.createWebSocket(URI.create("ws://locationhost:8001/echo"), (String[])null);

        WSURI uri = new WSURI("ws://locationhost:8001/echo");
        WebSocketNativeChannel channel = new WebSocketNativeChannel(uri);
        channel.setParent(((WebSocketImpl)ws).getCompositeChannel());
        ((WebSocketImpl)ws).getCompositeChannel().setChallengeHandler(ch);

        WebSocketNativeAuthenticationHandler handler = new WebSocketNativeAuthenticationHandler();
        handler.setNextHandler(nextHandler);
        handler.setListener(listener);
        
        // use processTextMessage to trigger the Authentication event 
        nextHandler.processTextMessage(channel, "Application Basic");
        context.assertIsSatisfied();
    }

    @Test
    public void testProcessAuthorizeWithChallengeHandler() throws URISyntaxException {

        Mockery context = new Mockery();
        final WebSocketHandler nextHandler = context.mock(WebSocketHandler.class);
        final WebSocketHandlerListener listener = context.mock(WebSocketHandlerListener.class);

        context.checking(new Expectations() {

            {
                oneOf(nextHandler).setListener(with(aNonNull(WebSocketHandlerListener.class)));
                will(saveParameter("listener", 0));
                oneOf(nextHandler).processTextMessage(with(aNonNull(WebSocketChannel.class)), with("Application Basic"));
                will(new CustomAction("will fire 401 challenge") {

                    @Override
                    public Object invoke(Invocation invocation) {
                        WebSocketHandlerListener listener = (WebSocketHandlerListener)lookup("listener");
                       
                        WebSocketChannel channel = (WebSocketChannel)invocation.getParameter(0);
                        listener.authenticationRequested(channel, "ws://localhost:8000/echo", "Application Basic");
                        return null;
                    }
                });
                oneOf(nextHandler).processAuthorize(with(aNonNull(WebSocketChannel.class)), with("Basic am9lOndlbGNvbWU="));
            }
        });

        //create challenge handler
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        ChallengeHandler ch = BasicChallengeHandler.create().setLoginHandler(new MyLoginHandler());
        wsFactory.setDefaultChallengeHandler(ch);
        WebSocket        ws = wsFactory.createWebSocket(URI.create("ws://locationhost:8001/echo"), (String[])null);

        WSURI uri = new WSURI("ws://locationhost:8001/echo");
        WebSocketNativeChannel channel = new WebSocketNativeChannel(uri);
        channel.setParent(((WebSocketImpl)ws).getCompositeChannel());
        ((WebSocketImpl)ws).getCompositeChannel().setChallengeHandler(ch);
        
        WebSocketNativeAuthenticationHandler handler = new WebSocketNativeAuthenticationHandler();
        handler.setNextHandler(nextHandler);
        handler.setListener(listener);
        
        // use processTextMessage to trigger the Authentication event 
        nextHandler.processTextMessage(channel, "Application Basic");
        context.assertIsSatisfied();
        wsFactory.setDefaultChallengeHandler(new DummyChallellengeHandler());
    }

    
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
        WebSocketNativeChannel channel = new WebSocketNativeChannel(uri);

        WebSocketNativeAuthenticationHandler handler = new WebSocketNativeAuthenticationHandler();
        handler.setNextHandler(nextHandler);
        handler.setListener(listener);
        handler.processBinaryMessage(channel, message);
        context.assertIsSatisfied();
    }

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
                will(new CustomAction("will fire connectionOpen") {

                    @Override
                    public Object invoke(Invocation invocation) {
                        WebSocketHandlerListener listener = (WebSocketHandlerListener)lookup("listener");
                        WebSocketChannel channel = (WebSocketChannel)invocation.getParameter(0);
                        listener.binaryMessageReceived(channel, message);
                        return null;
                    }
                });
                oneOf(listener).binaryMessageReceived(with(aNonNull(WebSocketChannel.class)), with(new ByteBufferMatcher("test message", "test")));
            }
        });

        WSURI uri = new WSURI("ws://locationhost:8001/echo");
        WebSocketNativeChannel channel = new WebSocketNativeChannel(uri);

        WebSocketNativeAuthenticationHandler handler = new WebSocketNativeAuthenticationHandler();
        handler.setNextHandler(nextHandler);
        handler.setListener(listener);
        handler.processTextMessage(channel, "test message");
        context.assertIsSatisfied();
    }

    private class MyLoginHandler extends LoginHandler {

        @Override
        public PasswordAuthentication getCredentials() {
            return new PasswordAuthentication("joe", "welcome".toCharArray());
        }
        
        
    }

    private class DummyChallellengeHandler extends ChallengeHandler {

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
