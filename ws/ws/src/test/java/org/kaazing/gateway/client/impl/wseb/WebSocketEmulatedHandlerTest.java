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
import org.kaazing.gateway.client.impl.WebSocketChannel;
import org.kaazing.gateway.client.impl.WebSocketHandlerListener;
import org.kaazing.gateway.client.impl.util.WSURI;
import org.kaazing.gateway.client.util.HttpURI;


public class WebSocketEmulatedHandlerTest {

    /*
     * test successful connection
     */
    @Test
    public void testProcessOpen() throws URISyntaxException {

        Mockery context = new Mockery();
        final CreateHandler createHandler = context.mock(CreateHandler.class);
        final UpstreamHandler upstreamHandler = context.mock(UpstreamHandler.class);
        final DownstreamHandler downstreamHandler = context.mock(DownstreamHandler.class);
        final WebSocketHandlerListener listener = context.mock(WebSocketHandlerListener.class);
        
        CreateHandlerFactory defaultCreateHandlerFactory = WebSocketEmulatedHandler.createHandlerFactory;
        UpstreamHandlerFactory defaultUpstreamHandlerFactory = WebSocketEmulatedHandler.upstreamHandlerFactory;
        DownstreamHandlerFactory defaultDownstreamHandlerFactory = WebSocketEmulatedHandler.downstreamHandlerFactory;

        context.checking(new Expectations() {
            {
                oneOf(createHandler).setListener(with(aNonNull(CreateHandlerListener.class)));
                will(saveParameter("createListener", 0));
                
                oneOf(downstreamHandler).setListener(with(aNonNull(DownstreamHandlerListener.class)));
                will(saveParameter("downstreamListener", 0));
                
                oneOf(upstreamHandler).setListener(with(aNonNull(UpstreamHandlerListener.class)));
                will(saveParameter("upstreamListener", 0));
                
                oneOf(createHandler).processOpen(with(aNonNull(CreateChannel.class)), with(aNonNull(HttpURI.class)));
                will(new CustomAction("will fire createCompleted") {

                    @Override
                    public Object invoke(Invocation invocation) throws URISyntaxException {
                        CreateHandlerListener listener = (CreateHandlerListener)lookup("createListener");
                        CreateChannel channel = (CreateChannel)invocation.getParameter(0);
                        listener.createCompleted(channel, new HttpURI("http://localhost:8001/echo/dt"), new HttpURI("http://localhost:8001/echo/dt"), "foo");
                        return null;
                    }
                });
                
                oneOf(downstreamHandler).processConnect(with(aNonNull(DownstreamChannel.class)), with(aNonNull(HttpURI.class)));
                will(new CustomAction("will fire downstream connected") {

                    @Override
                    public Object invoke(Invocation invocation) throws URISyntaxException {
                        DownstreamHandlerListener listener = (DownstreamHandlerListener)lookup("downstreamListener");
                        DownstreamChannel channel = (DownstreamChannel)invocation.getParameter(0);
                        listener.downstreamOpened(channel);
                        return null;
                    }
                });
                
                oneOf(listener).connectionOpened(with(aNonNull(WebSocketChannel.class)), with("foo"));
            }
        });

        WSURI uri = new WSURI("ws://localhost:8001/echo");
        WebSocketEmulatedChannel channel = new WebSocketEmulatedChannel(uri);

        //override CreateHandlerFactory
        WebSocketEmulatedHandler.createHandlerFactory = new CreateHandlerFactory() {
            @Override
            public CreateHandler createCreateHandler() {
               return createHandler;
            }
        };
        
        //override upstreamHandlerFactory
        WebSocketEmulatedHandler.upstreamHandlerFactory = new UpstreamHandlerFactory() {
            @Override
            public UpstreamHandler createUpstreamHandler() {
                return upstreamHandler;
            }
        };
        
        //override downstreamHandlerFactory
        WebSocketEmulatedHandler.downstreamHandlerFactory = new DownstreamHandlerFactory() {
            @Override
            public DownstreamHandler createDownstreamHandler() {
                return downstreamHandler;
            }
        };
        
        WebSocketEmulatedHandler handler = new WebSocketEmulatedHandler();
        handler.setListener(listener);

        handler.processConnect(channel, uri, new String[]{"foo"});
        context.assertIsSatisfied();
        
        WebSocketEmulatedHandler.createHandlerFactory = defaultCreateHandlerFactory;
        WebSocketEmulatedHandler.upstreamHandlerFactory = defaultUpstreamHandlerFactory;
        WebSocketEmulatedHandler.downstreamHandlerFactory = defaultDownstreamHandlerFactory;
    }

    /*
     * test failed connection
     */
    @Test
    public void testProcessFailed() throws URISyntaxException {

        Mockery context = new Mockery();
        final CreateHandler createHandler = context.mock(CreateHandler.class);
        final UpstreamHandler upstreamHandler = context.mock(UpstreamHandler.class);
        final DownstreamHandler downstreamHandler = context.mock(DownstreamHandler.class);
        final WebSocketHandlerListener listener = context.mock(WebSocketHandlerListener.class);

        CreateHandlerFactory defaultCreateHandlerFactory = WebSocketEmulatedHandler.createHandlerFactory;
        UpstreamHandlerFactory defaultUpstreamHandlerFactory = WebSocketEmulatedHandler.upstreamHandlerFactory;
        DownstreamHandlerFactory defaultDownstreamHandlerFactory = WebSocketEmulatedHandler.downstreamHandlerFactory;

        context.checking(new Expectations() {

            {
                oneOf(createHandler).setListener(with(aNonNull(CreateHandlerListener.class)));
                will(saveParameter("createListener", 0));
                oneOf(downstreamHandler).setListener(with(aNonNull(DownstreamHandlerListener.class)));
                will(saveParameter("downstreamListener", 0));
                oneOf(upstreamHandler).setListener(with(aNonNull(UpstreamHandlerListener.class)));
                will(saveParameter("upstreamListener", 0));
                oneOf(createHandler).processOpen(with(aNonNull(CreateChannel.class)), with(aNonNull(HttpURI.class)));
                will(new CustomAction("will fire createCompleted") {

                    @Override
                    public Object invoke(Invocation invocation) throws URISyntaxException {
                        CreateHandlerListener listener = (CreateHandlerListener)lookup("createListener");
                        CreateChannel channel = (CreateChannel)invocation.getParameter(0);
                        listener.createFailed(channel, new IllegalStateException());
                        return null;
                    }
                });
                oneOf(listener).connectionFailed(with(aNonNull(WebSocketChannel.class)), with(aNonNull(Exception.class)));
            }
        });

        WSURI uri = new WSURI("ws://localhost:8001/echo");
        String[] protocols = new String[] {"foo"};
        WebSocketEmulatedChannel channel = new WebSocketEmulatedChannel(uri);

        //override CreateHandlerFactory
        WebSocketEmulatedHandler.createHandlerFactory = new CreateHandlerFactory() {

            @Override
            public CreateHandler createCreateHandler() {
               return createHandler;
            }
        };
        //override upstreamHandlerFactory
        WebSocketEmulatedHandler.upstreamHandlerFactory = new UpstreamHandlerFactory() {

            @Override
            public UpstreamHandler createUpstreamHandler() {
                return upstreamHandler;
            }
        };
        //override downstreamHandlerFactory
        WebSocketEmulatedHandler.downstreamHandlerFactory = new DownstreamHandlerFactory() {

            @Override
            public DownstreamHandler createDownstreamHandler() {
                return downstreamHandler;
            }
        };
        
        WebSocketEmulatedHandler handler = new WebSocketEmulatedHandler();
        handler.setListener(listener);

        handler.processConnect(channel, uri, protocols);
        context.assertIsSatisfied();
        
        WebSocketEmulatedHandler.createHandlerFactory = defaultCreateHandlerFactory;
        WebSocketEmulatedHandler.upstreamHandlerFactory = defaultUpstreamHandlerFactory;
        WebSocketEmulatedHandler.downstreamHandlerFactory = defaultDownstreamHandlerFactory;
    }

}
