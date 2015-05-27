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

package org.kaazing.gateway.client.transport.ws;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.ByteBuffer;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.concurrent.Synchroniser;
import org.jmock.api.Invocation;
import org.jmock.lib.action.CustomAction;
import org.junit.Test;
import org.kaazing.gateway.client.transport.Expectations;
import org.kaazing.gateway.client.transport.LoadEvent;
import org.kaazing.gateway.client.transport.OpenEvent;
import org.kaazing.gateway.client.transport.http.HttpRequestDelegate;
import org.kaazing.gateway.client.transport.http.HttpRequestDelegateFactory;
import org.kaazing.gateway.client.transport.http.HttpRequestDelegateListener;

public class WebSocketDelegateTest {

    @Test
    public void testEncodeGetRequest() throws URISyntaxException {
        URI url = new URI("ws://localhost:8000/echo");
        WebSocketDelegateImpl wsDelegate = new WebSocketDelegateImpl(url, new URI("http://localhost:8000"), null, 0L);
        String[] names = {"one", "two"};
        String[] values = {"onevalue", "twovalue"};
        byte[] encoded = wsDelegate.encodeGetRequest(url, names, values);

        assertEquals(encoded.length, 52);
    }

    class BridgeOutputStream extends OutputStream {
        @Override
        public void write(int b) throws IOException {
        }
    }

    class BridgeInputStream extends InputStream {
        ByteBuffer buf;

        public BridgeInputStream() {
            buf = ByteBuffer.allocate(512);
            buf.put("HTTP/1.1 101 Web Socket Protocol Handshake\r\nUpgrade: WebSocket\r\nConnection: Upgrade\r\nSec-WebSocket-Accept: Q5rvnGjrVmNaRE/6DmLRKTDcmi4=\r\n\r\n"
                    .getBytes());
            // buf.put((byte)0x00);
            // buf.put("FOO".getBytes());
            // buf.put((byte)0xff);
            buf.put(new byte[]{(byte) 0x81, 0x03, 0x46, 0x4f, 0x4f});
            buf.flip();
        }

        @Override
        public int read() throws IOException {
            // first call return handshake headers
            if (buf.remaining() > 0) {
                return buf.get() & 0xff;
            }
            // prepare for send call, return binary frame
            // buf = ByteBuffer.wrap(new byte[] {(byte)0x81, 0x03, 0x46, 0x4f, 0x4f});
            return -1;
        }
    }

    @Test
    public void testHandshake() throws Exception {
    	Mockery context = new JUnit4Mockery() {{
  		  setThreadingPolicy(new Synchroniser());
  	    }};

  	    final WebSocketDelegateListener listener = context.mock(WebSocketDelegateListener.class);
        final HttpRequestDelegate httpRequestDelegate = context.mock(HttpRequestDelegate.class);
        final BridgeSocket socket = context.mock(BridgeSocket.class);

        final ByteBuffer responseBuffer = ByteBuffer.wrap(new byte[]{});
        final OutputStream outputStream = new BridgeOutputStream();
        final InputStream inputStream = new BridgeInputStream();

        final Latch openLatch = new Latch();

        context.checking(new Expectations() {
            {
                /* Cookies request expectations */
                oneOf(httpRequestDelegate).setListener(with(any(HttpRequestDelegateListener.class)));
                will(saveParameter("cookiesRequestListener"));

                oneOf(httpRequestDelegate).processOpen(with("GET"),
                        with(new URLPrefixMatcher("http://localhost:8001/echo/;e/cookies?.krn=")),
                        with(new StringPrefixMatcher("privileged://localhost:8000")), with(false), with(0L));
                oneOf(httpRequestDelegate).processSend(null);
                will(new CustomAction("Respond") {
                    @Override
                    public Object invoke(Invocation invocation) throws Throwable {
                        HttpRequestDelegateListener cookiesRequestListener = (HttpRequestDelegateListener) lookup("cookiesRequestListener");
                        cookiesRequestListener.loaded(new LoadEvent(responseBuffer));
                        return null;
                    }
                });
                oneOf(httpRequestDelegate).getStatusCode();
                will(returnValue(new Integer(200)));
                oneOf(httpRequestDelegate).getResponseText();
                will(returnValue(responseBuffer));

                /* Native Bridge Socket */
                oneOf(socket).connect(with(new InetAddressMatcher("localhost", 8001)), with(0L));
                oneOf(socket).setKeepAlive(true);
                oneOf(socket).setSoTimeout(0);

                allowing(socket).getOutputStream();
                will(returnValue(outputStream));
                allowing(socket).getInputStream();
                will(returnValue(inputStream));

                // oneOf(socket).close();

                /* Listeners */
                oneOf(listener).opened(with(any(OpenEvent.class)));
                will(new CustomAction("Release Open latch") {
                    @Override
                    public Object invoke(Invocation invocation) throws Throwable {
                        System.out.println("OPEN");
                        openLatch.unlatch();
                        return null;
                    }
                });
            }
        });

        URI url = new URI("ws://localhost:8001/echo?.kl=Y");
        WebSocketDelegate wsDelegate = new WebSocketDelegateImpl(url, new URI("privileged://localhost:8000"), null, 0L);
        ((WebSocketDelegateImpl) wsDelegate).websocketKey = "TjlI2MTGZ0i6wEYq7nnglg==";

        ((WebSocketDelegateImpl) wsDelegate).HTTP_REQUEST_DELEGATE_FACTORY = new HttpRequestDelegateFactory() {
            @Override
            public HttpRequestDelegate createHttpRequestDelegate() {
                System.out.println("http request delegate");
                return httpRequestDelegate;
            }
        };
        ((WebSocketDelegateImpl) wsDelegate).BRIDGE_SOCKET_FACTORY = new BridgeSocketFactory() {
            @Override
            public BridgeSocket createSocket(boolean secure) throws IOException {
                return socket;
            }
        };

        wsDelegate.setListener(listener);
        wsDelegate.processOpen();

        openLatch.await(5000);

        context.assertIsSatisfied();
    }

    public class StringPrefixMatcher extends BaseMatcher<String> {
        private String prefix;

        /**
         * Create a new StringPrefixMatcher matching prefix specified
         * @param prefix prefix to match
         */
        public StringPrefixMatcher(String prefix) {
            this.prefix = prefix;
        }

        @Override
        public boolean matches(Object obj) {
            return (obj instanceof String && ((String) obj).startsWith(prefix));
        }

        @Override
        public void describeTo(Description desc) {
            desc.appendText("matches prefix: " + prefix);
        }
    }

    public final class URLPrefixMatcher extends BaseMatcher<URL> {
        private String prefix;

        /**
         * Create a new URLPrefixMatcher matching prefix specified
         * @param prefix prefix to match
         */
        public URLPrefixMatcher(String prefix) {
            this.prefix = prefix;
        }

        @Override
        public boolean matches(Object obj) {
            if (obj instanceof URL) {
                URL url = (URL) obj;
                return url.toString().startsWith(prefix);
            }
            return false;
        }

        @Override
        public void describeTo(Description desc) {
            desc.appendText("matches prefix: " + prefix);
        }
    }

    public class InetAddressMatcher extends BaseMatcher<InetSocketAddress> {

        String host;
        int port;

        public InetAddressMatcher(String string, int port) {
            this.host = string;
            this.port = port;
        }

        @Override
        public boolean matches(Object obj) {
            if (obj instanceof InetSocketAddress) {
                InetSocketAddress addr = (InetSocketAddress) obj;
                if (addr.getHostName().equals(host) && addr.getPort() == port) {
                    return true;
                }
            }

            return false;
        }

        @Override
        public void describeTo(Description desc) {
            desc.appendText("matches inet address: " + host + ":" + port);
        }
    }

    public class Latch {

        private boolean locked = true;

        public Latch() {
        }

        public void unlatch() {
            synchronized (this) {
                locked = false;
                notifyAll();
            }
        }

        public void await(long time) {
            synchronized (this) {
                if (locked) {
                    try {
                        wait(time);
                    } catch (InterruptedException e) {
                        throw new Error("Latch broken");
                    }

                    if (locked) {
                        throw new Error("Latch broken");
                    }
                }
            }
        }

        public synchronized boolean isLocked() {
            return locked;
        }
    }

}
