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
import org.kaazing.gateway.client.impl.WebSocketHandlerListener;
import org.kaazing.gateway.client.impl.ws.WebSocketCompositeChannel;
import org.kaazing.gateway.client.impl.ws.WebSocketCompositeHandler;
import org.kaazing.gateway.client.impl.ws.WebSocketSelectedChannel;
import org.kaazing.gateway.client.impl.ws.WebSocketSelectedHandler;
import org.kaazing.gateway.client.impl.ws.WebSocketSelectedHandlerImpl;
import org.kaazing.gateway.client.impl.wseb.WebSocketEmulatedChannel;
import org.kaazing.gateway.client.impl.wsn.WebSocketNativeChannel;
import org.kaazing.gateway.client.impl.util.WSCompositeURI;
import org.kaazing.gateway.client.impl.util.WSURI;


public class WebSocketCompositeHandlerTest {

    /*
     * test java:ws successful connection
     */
    @Test
    public void testJavaWs() throws URISyntaxException {
        Mockery context = new Mockery();
        final WebSocketSelectedHandler nativeHandler = context.mock(WebSocketSelectedHandler.class, "nativeHandler");
        final WebSocketSelectedHandler emulatedHandler = context.mock(WebSocketSelectedHandler.class, "emulatedHandler");
        final WebSocketHandlerListener listener = context.mock(WebSocketHandlerListener.class);
        final String[] requestedProtocols = new String[] { "foo" };

        context.checking(new Expectations() {
            {
                oneOf(nativeHandler).setListener(with(aNonNull(WebSocketHandlerListener.class)));
                will(saveParameter("nativeListener", 0));
                
                oneOf(emulatedHandler).setListener(with(aNonNull(WebSocketHandlerListener.class)));
                will(saveParameter("emulatedListener", 0));

                oneOf(nativeHandler).processConnect(with(aNonNull(WebSocketSelectedChannel.class)), with(aNonNull(WSURI.class)), with(equal(requestedProtocols)));
                will(new CustomAction("check handler") {
                    @Override
                    public Object invoke(Invocation invocation) {
                        WebSocketNativeChannel channel = (WebSocketNativeChannel)invocation.getParameter(0);
                        Assert.assertEquals(channel.getLocation().toString(), "ws://localhost:8001/echo");
                        Assert.assertEquals(channel.getProtocol(), null); // Not set yet!
                        Assert.assertEquals( channel.getLocation().getHttpEquivalentScheme(), "http");
                        
                        //now raise connection Opened event
                        WebSocketHandlerListener listener = (WebSocketHandlerListener)lookup("nativeListener");
                        listener.connectionOpened(channel, "foo");
                        return null;
                    }
                });
                
                oneOf(listener).connectionOpened(with(aNonNull(WebSocketCompositeChannel.class)), with(equal("foo")));
            }
        });

        //override WebSocketSelectHandlerFactory to create mock object
        WebSocketCompositeHandler.WEBSOCKET_NATIVE_HANDLER_FACTORY = new WebSocketSelectedHandlerImpl.WebSocketSelectedHandlerFactory() {
            @Override
            public WebSocketSelectedHandler createSelectedHandler() {
                   return nativeHandler; 
            }
        };
        
        WebSocketCompositeHandler.WEBSOCKET_EMULATED_HANDLER_FACTORY = new WebSocketSelectedHandlerImpl.WebSocketSelectedHandlerFactory() {
            @Override
            public WebSocketSelectedHandler createSelectedHandler() {
                   return emulatedHandler; 
            }
        };
        
        WSCompositeURI uri = new WSCompositeURI("java:ws://localhost:8001/echo");
        WebSocketCompositeChannel channel = new WebSocketCompositeChannel(uri);

        WebSocketCompositeHandler handler = new WebSocketCompositeHandler();
        handler.setListener(listener);
        handler.processConnect(channel, uri.getWSEquivalent(), new String[] {"foo"});
        
        context.assertIsSatisfied();
    }
    
    /*
     * test java:wss successful connection
     */
    @Test
    public void testJavaWss() throws URISyntaxException {
        Mockery context = new Mockery();
        final WebSocketSelectedHandler nativeHandler = context.mock(WebSocketSelectedHandler.class, "nativeHandler");
        final WebSocketSelectedHandler emulatedHandler = context.mock(WebSocketSelectedHandler.class, "emulatedHandler");
        final WebSocketHandlerListener listener = context.mock(WebSocketHandlerListener.class);
        final String[] requestedProtocols = new String[] { "foo" };

        context.checking(new Expectations() {
            {
                oneOf(nativeHandler).setListener(with(aNonNull(WebSocketHandlerListener.class)));
                will(saveParameter("nativeListener", 0));
                
                oneOf(emulatedHandler).setListener(with(aNonNull(WebSocketHandlerListener.class)));
                will(saveParameter("emulatedListener", 0));

                oneOf(nativeHandler).processConnect(with(aNonNull(WebSocketSelectedChannel.class)), with(aNonNull(WSURI.class)), with(equal(requestedProtocols)));
                will(new CustomAction("check handler") {
                    @Override
                    public Object invoke(Invocation invocation) {
                        WebSocketNativeChannel channel = (WebSocketNativeChannel)invocation.getParameter(0);
                        Assert.assertEquals(channel.getLocation().toString(), "wss://localhost:8001/echo");
                        Assert.assertEquals(channel.getProtocol(), null); // Not set yet!
                        Assert.assertEquals( channel.getLocation().getHttpEquivalentScheme(), "https");
                        //now raise connection Opened event
                        WebSocketHandlerListener listener = (WebSocketHandlerListener)lookup("nativeListener");
                        listener.connectionOpened(channel, "foo");
                        return null;
                    }
                });

                oneOf(listener).connectionOpened(with(aNonNull(WebSocketCompositeChannel.class)), with(equal("foo")));
            }
        });

        //override WebSocketSelectHandlerFactory to create mock object
        WebSocketCompositeHandler.WEBSOCKET_NATIVE_HANDLER_FACTORY = new WebSocketSelectedHandlerImpl.WebSocketSelectedHandlerFactory() {
            @Override
            public WebSocketSelectedHandler createSelectedHandler() {
                   return nativeHandler; 
            }
        };
        
        WebSocketCompositeHandler.WEBSOCKET_EMULATED_HANDLER_FACTORY = new WebSocketSelectedHandlerImpl.WebSocketSelectedHandlerFactory() {
            @Override
            public WebSocketSelectedHandler createSelectedHandler() {
                   return emulatedHandler; 
            }
        };

        WSCompositeURI uri = new WSCompositeURI("java:wss://localhost:8001/echo");
        WebSocketCompositeChannel channel = new WebSocketCompositeChannel(uri);

        WebSocketCompositeHandler handler = new WebSocketCompositeHandler();
        handler.setListener(listener);
        handler.processConnect(channel, uri.getWSEquivalent(), new String[] {"foo"});
        
        context.assertIsSatisfied();
    }
    
    /*
     * test java:wse successful connection
     */
    @Test
    public void testJavaWse() throws URISyntaxException {
        Mockery context = new Mockery();
        final WebSocketSelectedHandler nativeHandler = context.mock(WebSocketSelectedHandler.class, "nativeHandler");
        final WebSocketSelectedHandler emulatedHandler = context.mock(WebSocketSelectedHandler.class, "emulatedHandler");
        final WebSocketHandlerListener listener = context.mock(WebSocketHandlerListener.class);
        final String[] requestedProtocols = new String[] { "foo" };

        context.checking(new Expectations() {
            {
                oneOf(nativeHandler).setListener(with(aNonNull(WebSocketHandlerListener.class)));
                will(saveParameter("nativeListener", 0));
                
                oneOf(emulatedHandler).setListener(with(aNonNull(WebSocketHandlerListener.class)));
                will(saveParameter("emulatedListener", 0));

                oneOf(emulatedHandler).processConnect(with(aNonNull(WebSocketSelectedChannel.class)), with(aNonNull(WSURI.class)), with(equal(requestedProtocols)));
                will(new CustomAction("check handler") {
                    @Override
                    public Object invoke(Invocation invocation) {
                        WebSocketEmulatedChannel channel = (WebSocketEmulatedChannel)invocation.getParameter(0);
                        Assert.assertEquals(channel.getLocation().toString(), "ws://localhost:8001/echo");
                        Assert.assertNull(channel.getProtocol());
                        Assert.assertEquals(channel.getLocation().getHttpEquivalentScheme(), "http");
                        
                        //now raise connection Opened event
                        WebSocketHandlerListener listener = (WebSocketHandlerListener)lookup("emulatedListener");
                        listener.connectionOpened(channel, "foo");
                        return null;
                    }
                });
                
                oneOf(listener).connectionOpened(with(aNonNull(WebSocketCompositeChannel.class)), with(equal("foo")));
            }
        });

        //override WebSocketSelectHandlerFactory to create mock object
        WebSocketCompositeHandler.WEBSOCKET_NATIVE_HANDLER_FACTORY = new WebSocketSelectedHandlerImpl.WebSocketSelectedHandlerFactory() {
            @Override
            public WebSocketSelectedHandler createSelectedHandler() {
                   return nativeHandler; 
            }
        };
        
        WebSocketCompositeHandler.WEBSOCKET_EMULATED_HANDLER_FACTORY = new WebSocketSelectedHandlerImpl.WebSocketSelectedHandlerFactory() {
            @Override
            public WebSocketSelectedHandler createSelectedHandler() {
                   return emulatedHandler; 
            }
        };
        
        WSCompositeURI uri = new WSCompositeURI("java:wse://localhost:8001/echo");
        WebSocketCompositeChannel channel = new WebSocketCompositeChannel(uri);

        WebSocketCompositeHandler handler = new WebSocketCompositeHandler();
        handler.setListener(listener);
        handler.processConnect(channel, uri.getWSEquivalent(), requestedProtocols);
        
        context.assertIsSatisfied();
    }
    
    /*
     * test java:wse successful connection
     */
    @Test
    public void testJavaWseSsl() throws URISyntaxException {
        Mockery context = new Mockery();
        final WebSocketSelectedHandler nativeHandler = context.mock(WebSocketSelectedHandler.class, "nativeHandler");
        final WebSocketSelectedHandler emulatedHandler = context.mock(WebSocketSelectedHandler.class, "emulatedHandler");
        final WebSocketHandlerListener listener = context.mock(WebSocketHandlerListener.class);

        context.checking(new Expectations() {
            {
                oneOf(nativeHandler).setListener(with(aNonNull(WebSocketHandlerListener.class)));
                will(saveParameter("nativeListener", 0));
                
                oneOf(emulatedHandler).setListener(with(aNonNull(WebSocketHandlerListener.class)));
                will(saveParameter("emulatedListener", 0));

                oneOf(emulatedHandler).processConnect(with(aNonNull(WebSocketSelectedChannel.class)), with(aNonNull(WSURI.class)),
                                                  with(equal(new String[] { "foo" })));
                will(new CustomAction("check handler") {
                    @Override
                    public Object invoke(Invocation invocation) {
                        WebSocketEmulatedChannel channel = (WebSocketEmulatedChannel)invocation.getParameter(0);
                        Assert.assertEquals(channel.getLocation().toString(), "wss://localhost:8001/echo");
                        Assert.assertNull(channel.getProtocol());
                        Assert.assertEquals(channel.getLocation().getHttpEquivalentScheme(), "https");
                        
                        //now raise connection Opened event
                        WebSocketHandlerListener listener = (WebSocketHandlerListener)lookup("emulatedListener");
                        listener.connectionOpened(channel, "foo");
                        return null;
                    }
                });
                
                oneOf(listener).connectionOpened(with(aNonNull(WebSocketCompositeChannel.class)), with(equal("foo")));
            }
        });

        //override WebSocketSelectHandlerFactory to create mock object
        WebSocketCompositeHandler.WEBSOCKET_NATIVE_HANDLER_FACTORY = new WebSocketSelectedHandlerImpl.WebSocketSelectedHandlerFactory() {
            @Override
            public WebSocketSelectedHandler createSelectedHandler() {
                   return nativeHandler; 
            }
        };
        
        WebSocketCompositeHandler.WEBSOCKET_EMULATED_HANDLER_FACTORY = new WebSocketSelectedHandlerImpl.WebSocketSelectedHandlerFactory() {
            @Override
            public WebSocketSelectedHandler createSelectedHandler() {
                   return emulatedHandler; 
            }
        };

        WSCompositeURI uri = new WSCompositeURI("java:wse+ssl://localhost:8001/echo");
        WebSocketCompositeChannel channel = new WebSocketCompositeChannel(uri);

        WebSocketCompositeHandler handler = new WebSocketCompositeHandler();
        handler.setListener(listener);
        handler.processConnect(channel, uri.getWSEquivalent(), new String[] {"foo"});
        
        context.assertIsSatisfied();
    }
    
    /*
     * test ws connection fallback function
     */
    @Test
    public void testWseFallback() throws URISyntaxException {
        Mockery context = new Mockery();
        final WebSocketSelectedHandler nativeHandler = context.mock(WebSocketSelectedHandler.class, "nativeHandler");
        final WebSocketSelectedHandler emulatedHandler = context.mock(WebSocketSelectedHandler.class, "emulatedHandler");
        final WebSocketHandlerListener listener = context.mock(WebSocketHandlerListener.class);
        context.checking(new Expectations() {
            {
                oneOf(nativeHandler).setListener(with(aNonNull(WebSocketHandlerListener.class)));
                will(saveParameter("nativeListener", 0));
                
                oneOf(emulatedHandler).setListener(with(aNonNull(WebSocketHandlerListener.class)));
                will(saveParameter("emulatedListener", 0));

                //try native first
                oneOf(nativeHandler).processConnect(with(aNonNull(WebSocketSelectedChannel.class)), with(aNonNull(WSURI.class)), with(equal(new String[] { "foo" })));
                will(new CustomAction("check handler") {
                    @Override
                    public Object invoke(Invocation invocation) {
                        WebSocketNativeChannel channel = (WebSocketNativeChannel)invocation.getParameter(0);
                        Assert.assertEquals(channel.getLocation().toString(), "ws://localhost:8001/echo");
                        Assert.assertNull(channel.getProtocol());
                        Assert.assertEquals( channel.getLocation().getHttpEquivalentScheme(), "http");

                        //now raise connection failed event
                        WebSocketHandlerListener listener = (WebSocketHandlerListener)lookup("nativeListener");
                        listener.connectionFailed(channel, null);
                        return null;
                    }
                });
                //fallback to emulated
                oneOf(emulatedHandler).processConnect(with(aNonNull(WebSocketSelectedChannel.class)), with(aNonNull(WSURI.class)), with(equal(new String[] { "foo" })));
                will(new CustomAction("check handler") {
                    @Override
                    public Object invoke(Invocation invocation) {
                        WebSocketEmulatedChannel channel = (WebSocketEmulatedChannel)invocation.getParameter(0);
                        Assert.assertEquals(channel.getLocation().toString(), "ws://localhost:8001/echo");
                        Assert.assertNull(channel.getProtocol());
                        Assert.assertEquals(channel.getLocation().getHttpEquivalentScheme(), "http");
                        
                        //now raise connection failed event
                        WebSocketHandlerListener listener = (WebSocketHandlerListener)lookup("emulatedListener");
                        listener.connectionFailed(channel, null);
                        return null;
                    }
                });
                oneOf(listener).connectionClosed(with(aNonNull(WebSocketCompositeChannel.class)), with(false), with(1006), with(aNull(String.class)));
            }
        });

        //override WebSocketSelectHandlerFactory to create mock object
        WebSocketCompositeHandler.WEBSOCKET_NATIVE_HANDLER_FACTORY = new WebSocketSelectedHandlerImpl.WebSocketSelectedHandlerFactory() {
            @Override
            public WebSocketSelectedHandler createSelectedHandler() {
                   return nativeHandler; 
            }
        };
        
        WebSocketCompositeHandler.WEBSOCKET_EMULATED_HANDLER_FACTORY = new WebSocketSelectedHandlerImpl.WebSocketSelectedHandlerFactory() {
            @Override
            public WebSocketSelectedHandler createSelectedHandler() {
                   return emulatedHandler; 
            }
        };

        WSCompositeURI uri = new WSCompositeURI("ws://localhost:8001/echo");
        WebSocketCompositeChannel channel = new WebSocketCompositeChannel(uri);

        WebSocketCompositeHandler handler = new WebSocketCompositeHandler();
        handler.setListener(listener);
        handler.processConnect(channel, uri.getWSEquivalent(), new String[] { "foo" });
        
        context.assertIsSatisfied();
    }
    /*
     * test wss connection fallback function
     */
    @Test
    public void testWsFallback() throws URISyntaxException {
        Mockery context = new Mockery();
        final WebSocketSelectedHandler nativeHandler = context.mock(WebSocketSelectedHandler.class, "nativeHandler");
        final WebSocketSelectedHandler emulatedHandler = context.mock(WebSocketSelectedHandler.class, "emulatedHandler");
        final WebSocketHandlerListener listener = context.mock(WebSocketHandlerListener.class);
        final String[] requestedProtocols = new String[] { "foo" };

        context.checking(new Expectations() {
            {
                oneOf(nativeHandler).setListener(with(aNonNull(WebSocketHandlerListener.class)));
                will(saveParameter("nativeListener", 0));
                
                oneOf(emulatedHandler).setListener(with(aNonNull(WebSocketHandlerListener.class)));
                will(saveParameter("emulatedListener", 0));

                //try native first
                oneOf(nativeHandler).processConnect(with(aNonNull(WebSocketSelectedChannel.class)), with(aNonNull(WSURI.class)), with(equal(requestedProtocols)));
                will(new CustomAction("check handler") {
                    @Override
                    public Object invoke(Invocation invocation) {
                        WebSocketNativeChannel channel = (WebSocketNativeChannel)invocation.getParameter(0);
                        Assert.assertEquals(channel.getLocation().toString(), "ws://localhost:8001/echo");
                        Assert.assertNull(channel.getProtocol());
                        Assert.assertEquals( channel.getLocation().getHttpEquivalentScheme(), "http");

                        //now raise connection failed event
                        WebSocketHandlerListener listener = (WebSocketHandlerListener)lookup("nativeListener");
                        listener.connectionFailed(channel, null);
                        return null;
                    }
                });
                
                //fallback to emulated
                oneOf(emulatedHandler).processConnect(with(aNonNull(WebSocketSelectedChannel.class)), with(aNonNull(WSURI.class)), with(equal(requestedProtocols)));
                will(new CustomAction("check handler") {
                    @Override
                    public Object invoke(Invocation invocation) {
                        WebSocketEmulatedChannel channel = (WebSocketEmulatedChannel)invocation.getParameter(0);
                        Assert.assertEquals(channel.getLocation().toString(), "ws://localhost:8001/echo");
                        Assert.assertNull(channel.getProtocol());
                        Assert.assertEquals( channel.getLocation().getHttpEquivalentScheme(), "http");
                        
                        //now raise connection failed event
                        WebSocketHandlerListener listener = (WebSocketHandlerListener)lookup("emulatedListener");
                        listener.connectionFailed(channel, null);
                        return null;
                    }
                });
                oneOf(listener).connectionClosed(with(aNonNull(WebSocketCompositeChannel.class)), with(false), with(1006), with(aNull(String.class)));
            }
        });

        //override WebSocketSelectHandlerFactory to create mock object
        WebSocketCompositeHandler.WEBSOCKET_NATIVE_HANDLER_FACTORY = new WebSocketSelectedHandlerImpl.WebSocketSelectedHandlerFactory() {
            @Override
            public WebSocketSelectedHandler createSelectedHandler() {
                   return nativeHandler; 
            }
        };
        
        WebSocketCompositeHandler.WEBSOCKET_EMULATED_HANDLER_FACTORY = new WebSocketSelectedHandlerImpl.WebSocketSelectedHandlerFactory() {
            @Override
            public WebSocketSelectedHandler createSelectedHandler() {
                   return emulatedHandler; 
            }
        };
        
        WSCompositeURI uri = new WSCompositeURI("ws://localhost:8001/echo");
        WebSocketCompositeChannel channel = new WebSocketCompositeChannel(uri);

        WebSocketCompositeHandler handler = new WebSocketCompositeHandler();
        handler.setListener(listener);
        handler.processConnect(channel, uri.getWSEquivalent(), new String[] {"foo"});
        
        context.assertIsSatisfied();
    }
    
    /*
     * test wss connection fallback function
     */
    @Test
    public void testWssFallback() throws URISyntaxException {
        Mockery context = new Mockery();
        final WebSocketSelectedHandler nativeHandler = context.mock(WebSocketSelectedHandler.class, "nativeHandler");
        final WebSocketSelectedHandler emulatedHandler = context.mock(WebSocketSelectedHandler.class, "emulatedHandler");
        final WebSocketHandlerListener listener = context.mock(WebSocketHandlerListener.class);
        final String[] requestedProtocols = new String[] { "foo" };

        context.checking(new Expectations() {
            {
                oneOf(nativeHandler).setListener(with(aNonNull(WebSocketHandlerListener.class)));
                will(saveParameter("nativeListener", 0));
                
                oneOf(emulatedHandler).setListener(with(aNonNull(WebSocketHandlerListener.class)));
                will(saveParameter("emulatedListener", 0));

                //try native first
                oneOf(nativeHandler).processConnect(with(aNonNull(WebSocketSelectedChannel.class)), with(aNonNull(WSURI.class)), with(equal(requestedProtocols)));
                will(new CustomAction("check handler") {
                    @Override
                    public Object invoke(Invocation invocation) {
                        WebSocketNativeChannel channel = (WebSocketNativeChannel)invocation.getParameter(0);
                        Assert.assertEquals(channel.getLocation().toString(), "wss://localhost:8001/echo");
                        Assert.assertNull(channel.getProtocol());
                        Assert.assertEquals( channel.getLocation().getHttpEquivalentScheme(), "https");
                        //now raise connection failed event
                        WebSocketHandlerListener listener = (WebSocketHandlerListener)lookup("nativeListener");
                        listener.connectionFailed(channel, null);
                        return null;
                    }
                });
                
                //fallback to emulated
                oneOf(emulatedHandler).processConnect(with(aNonNull(WebSocketSelectedChannel.class)), with(aNonNull(WSURI.class)), with(equal(requestedProtocols)));
                will(new CustomAction("check handler") {
                    @Override
                    public Object invoke(Invocation invocation) {
                        WebSocketEmulatedChannel channel = (WebSocketEmulatedChannel)invocation.getParameter(0);
                        Assert.assertEquals(channel.getLocation().toString(), "wss://localhost:8001/echo");
                        Assert.assertNull(channel.getProtocol());
                        Assert.assertEquals( channel.getLocation().getHttpEquivalentScheme(), "https");
                        //now raise connection failed event
                        WebSocketHandlerListener listener = (WebSocketHandlerListener)lookup("emulatedListener");
                        listener.connectionFailed(channel, null);
                        return null;
                    }
                });
                oneOf(listener).connectionClosed(with(aNonNull(WebSocketCompositeChannel.class)), with(false), with(1006), with(aNull(String.class)));
            }
        });

        //override WebSocketSelectHandlerFactory to create mock object
        WebSocketCompositeHandler.WEBSOCKET_NATIVE_HANDLER_FACTORY = new WebSocketSelectedHandlerImpl.WebSocketSelectedHandlerFactory() {
            @Override
            public WebSocketSelectedHandler createSelectedHandler() {
                   return nativeHandler; 
            }
        };
        
        WebSocketCompositeHandler.WEBSOCKET_EMULATED_HANDLER_FACTORY = new WebSocketSelectedHandlerImpl.WebSocketSelectedHandlerFactory() {
            @Override
            public WebSocketSelectedHandler createSelectedHandler() {
                   return emulatedHandler; 
            }
        };

        WSCompositeURI uri = new WSCompositeURI("wss://localhost:8001/echo");
        WebSocketCompositeChannel channel = new WebSocketCompositeChannel(uri);

        WebSocketCompositeHandler handler = new WebSocketCompositeHandler();
        handler.setListener(listener);
        handler.processConnect(channel, uri.getWSEquivalent(), new String[] {"foo"});
        
        context.assertIsSatisfied();
    }

    /*
     * test java:ws connection failed
     */
    @Test
    public void testJavaWsFailed() throws URISyntaxException {
        Mockery context = new Mockery();
        final WebSocketSelectedHandler nativeHandler = context.mock(WebSocketSelectedHandler.class, "nativeHandler");
        final WebSocketSelectedHandler emulatedHandler = context.mock(WebSocketSelectedHandler.class, "emulatedHandler");
        final WebSocketHandlerListener listener = context.mock(WebSocketHandlerListener.class);
        final String[] requestedProtocols = new String[] { "foo" };

        context.checking(new Expectations() {
            {
                oneOf(nativeHandler).setListener(with(aNonNull(WebSocketHandlerListener.class)));
                will(saveParameter("nativeListener", 0));
                
                oneOf(emulatedHandler).setListener(with(aNonNull(WebSocketHandlerListener.class)));
                will(saveParameter("emulatedListener", 0));

                oneOf(nativeHandler).processConnect(with(aNonNull(WebSocketSelectedChannel.class)), with(aNonNull(WSURI.class)), with(equal(requestedProtocols)));
                will(new CustomAction("check handler") {
                    @Override
                    public Object invoke(Invocation invocation) {
                        WebSocketNativeChannel channel = (WebSocketNativeChannel)invocation.getParameter(0);
                        Assert.assertEquals(channel.getLocation().toString(), "ws://localhost:8001/echo");
                        Assert.assertNull(channel.getProtocol());
                        Assert.assertEquals( channel.getLocation().getHttpEquivalentScheme(), "http");
                        //now raise connection failed event
                        WebSocketHandlerListener listener = (WebSocketHandlerListener)lookup("nativeListener");
                        listener.connectionFailed(channel, null);
                        return null;
                    }
                });
                oneOf(listener).connectionClosed(with(aNonNull(WebSocketCompositeChannel.class)), with(false), with(1006), with(aNull(String.class)));
            }
        });

        //override WebSocketSelectHandlerFactory to create mock object
        WebSocketCompositeHandler.WEBSOCKET_NATIVE_HANDLER_FACTORY = new WebSocketSelectedHandlerImpl.WebSocketSelectedHandlerFactory() {
            @Override
            public WebSocketSelectedHandler createSelectedHandler() {
                   return nativeHandler; 
            }
        };
        
        WebSocketCompositeHandler.WEBSOCKET_EMULATED_HANDLER_FACTORY = new WebSocketSelectedHandlerImpl.WebSocketSelectedHandlerFactory() {
            @Override
            public WebSocketSelectedHandler createSelectedHandler() {
                   return emulatedHandler; 
            }
        };
        
        WSCompositeURI uri = new WSCompositeURI("java:ws://localhost:8001/echo");
        WebSocketCompositeChannel channel = new WebSocketCompositeChannel(uri);

        WebSocketCompositeHandler handler = new WebSocketCompositeHandler();
        handler.setListener(listener);
        handler.processConnect(channel, uri.getWSEquivalent(), new String[] {"foo"});
        
        context.assertIsSatisfied();
    }
    /*
     * test java:wse connection failed
     */
    @Test
    public void testJavaWseFailed() throws URISyntaxException {
        Mockery context = new Mockery();
        final WebSocketSelectedHandler nativeHandler = context.mock(WebSocketSelectedHandler.class, "nativeHandler");
        final WebSocketSelectedHandler emulatedHandler = context.mock(WebSocketSelectedHandler.class, "emulatedHandler");
        final WebSocketHandlerListener listener = context.mock(WebSocketHandlerListener.class);
        context.checking(new Expectations() {
            {
                oneOf(nativeHandler).setListener(with(aNonNull(WebSocketHandlerListener.class)));
                will(saveParameter("nativeListener", 0));
                
                oneOf(emulatedHandler).setListener(with(aNonNull(WebSocketHandlerListener.class)));
                will(saveParameter("emulatedListener", 0));

                oneOf(emulatedHandler).processConnect(with(aNonNull(WebSocketSelectedChannel.class)), with(aNonNull(WSURI.class)),
                                                      with(equal(new String[] { "foo" })));
                will(new CustomAction("check emulated handler") {
                    @Override
                    public Object invoke(Invocation invocation) {
                        WebSocketEmulatedChannel channel = (WebSocketEmulatedChannel)invocation.getParameter(0);
                        Assert.assertEquals(channel.getLocation().toString(), "ws://localhost:8001/echo");
                        Assert.assertNull(channel.getProtocol());
                        Assert.assertEquals(channel.getLocation().getHttpEquivalentScheme(), "http");
                        
                        //now raise connection failed event
                        WebSocketHandlerListener listener = (WebSocketHandlerListener)lookup("emulatedListener");
                        listener.connectionFailed(channel, null);
                        return null;
                    }
                });
                
                oneOf(listener).connectionClosed(with(aNonNull(WebSocketCompositeChannel.class)), with(false), with(1006), with(aNull(String.class)));
            }
        });

        //override WebSocketSelectHandlerFactory to create mock object
        WebSocketCompositeHandler.WEBSOCKET_NATIVE_HANDLER_FACTORY = new WebSocketSelectedHandlerImpl.WebSocketSelectedHandlerFactory() {
            @Override
            public WebSocketSelectedHandler createSelectedHandler() {
                return nativeHandler; 
            }
        };
        
        WebSocketCompositeHandler.WEBSOCKET_EMULATED_HANDLER_FACTORY = new WebSocketSelectedHandlerImpl.WebSocketSelectedHandlerFactory() {
            @Override
            public WebSocketSelectedHandler createSelectedHandler() {
                return emulatedHandler; 
            }
        };

        WSCompositeURI uri = new WSCompositeURI("java:wse://localhost:8001/echo");
        WebSocketCompositeChannel channel = new WebSocketCompositeChannel(uri);

        WebSocketCompositeHandler handler = new WebSocketCompositeHandler();
        handler.setListener(listener);
        handler.processConnect(channel, uri.getWSEquivalent(), new String[] {"foo"});
        
        context.assertIsSatisfied();
    }
    /*
     * test invalid scheme
     */
    @Test (expected = URISyntaxException.class)
    public void testInvalidScheme() throws URISyntaxException {
        Mockery context = new Mockery();
        final WebSocketSelectedHandler nativeHandler = context.mock(WebSocketSelectedHandler.class, "nativeHandler");
        final WebSocketSelectedHandler emulatedHandler = context.mock(WebSocketSelectedHandler.class, "emulatedHandler");
        final WebSocketHandlerListener listener = context.mock(WebSocketHandlerListener.class);
        final String[] requestedProtocols = new String[] { "foo" };

        context.checking(new Expectations() {
            {
                oneOf(nativeHandler).setListener(with(aNonNull(WebSocketHandlerListener.class)));
                will(saveParameter("nativeListener", 0));
                
                oneOf(emulatedHandler).setListener(with(aNonNull(WebSocketHandlerListener.class)));
                will(saveParameter("emulatedListener", 0));
            }
        });

        //override WebSocketSelectHandlerFactory to create mock object
        WebSocketCompositeHandler.WEBSOCKET_NATIVE_HANDLER_FACTORY = new WebSocketSelectedHandlerImpl.WebSocketSelectedHandlerFactory() {
            @Override
            public WebSocketSelectedHandler createSelectedHandler() {
                return nativeHandler; 
            }
        };
        
        WebSocketCompositeHandler.WEBSOCKET_EMULATED_HANDLER_FACTORY = new WebSocketSelectedHandlerImpl.WebSocketSelectedHandlerFactory() {
            @Override
            public WebSocketSelectedHandler createSelectedHandler() {
                   return emulatedHandler; 
            }
        };
        
        WSCompositeURI uri = new WSCompositeURI("http://localhost:8001/echo");
        WebSocketCompositeChannel channel = new WebSocketCompositeChannel(uri);

        WebSocketCompositeHandler handler = new WebSocketCompositeHandler();
        handler.setListener(listener);
        handler.processConnect(channel, uri.getWSEquivalent(), requestedProtocols);
        
        context.assertIsSatisfied();
    }
}
