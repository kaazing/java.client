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

package org.kaazing.net.ws.impl.io;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.kaazing.net.impl.util.BlockingQueueImpl;
import org.kaazing.net.ws.WebSocketMessageType;
import org.kaazing.net.ws.WebSocketException;
import org.kaazing.net.ws.WebSocketMessageReader;
import org.kaazing.net.ws.impl.WebSocketImpl;

public class WsMessageReaderImpl extends WebSocketMessageReader {
    private static final String _CLASS_NAME = WsMessageReaderImpl.class.getName();
    private static final Logger _LOG = Logger.getLogger(_CLASS_NAME);

    private final BlockingQueueImpl<Object>    _sharedQueue;
    private final WebSocketImpl                _webSocket;
    private       Object                       _payload;
    private       WebSocketMessageType         _messageType;
    private       boolean                      _closed;

    public WsMessageReaderImpl(WebSocketImpl             webSocket,
                               BlockingQueueImpl<Object> sharedQueue) {
        if (webSocket == null) {
            String s = "Null webSocket passed in";
            throw new IllegalArgumentException(s);
        }

        if (sharedQueue == null) {
            String s = "Null sharedQueue passed in";
            throw new IllegalArgumentException(s);
        }

        _webSocket = webSocket;
        _sharedQueue = sharedQueue;
    }

    // --------------------- WebSocketMessageReader Implementation -----------
    @Override
    public ByteBuffer getBinary() throws IOException {
        if (_messageType == null) {
            return null;
        }

        if (_messageType == WebSocketMessageType.EOS){
            String s = "End of stream has reached as the connection has been closed";
            throw new WebSocketException(s);
        }

        if (_messageType != WebSocketMessageType.BINARY) {
            String s = "Invalid WebSocketMessageType: Cannot decode the payload " +
                       "as a binary message";
            throw new WebSocketException(s);
        }

        return ByteBuffer.wrap(((ByteBuffer)_payload).array());
    }

    @Override
    public CharSequence getText() throws IOException {
        if (_messageType == null) {
            return null;
        }

        if (_messageType == WebSocketMessageType.EOS){
            String s = "End of stream has reached as the connection has been closed";
            throw new WebSocketException(s);
        }

        if (_messageType != WebSocketMessageType.TEXT) {
            String s = "Invalid WebSocketMessageType: Cannot decode the payload " +
                       "as a text message";
            throw new WebSocketException(s);
        }

        return String.valueOf(((String)_payload).toCharArray());
    }

    @Override
    public WebSocketMessageType getType() {
        return _messageType;
    }

    @Override
    public WebSocketMessageType next() throws IOException {
        synchronized (this) {
            if (_sharedQueue.isEmpty() && isClosed()) {
                String s = "Cannot read as the connection is closed";
                throw new WebSocketException(s);
            }

            try {
                _payload = null;
                _webSocket.setException(null);
                _payload = _sharedQueue.take();
            }
            catch (InterruptedException ex) {
                System.out.println(String.format("put(): ThreadId: %d: Caught InterruptedException",
                                                 Thread.currentThread().getId()));
                _LOG.log(Level.INFO, ex.getMessage());
            }

            if (_payload == null) {
                String s = "MessageReader has been interrupted maybe the connection is closed";
                // throw new WebSocketException(s);
                _LOG.log(Level.INFO, _CLASS_NAME, s);
                System.out.println(String.format("put(): ThreadId: %d: Setting _messageType to EOS",
                                                 Thread.currentThread().getId()));

                _messageType = WebSocketMessageType.EOS;
                return _messageType;
            }

            if (_payload.getClass() == String.class) {
                _messageType = WebSocketMessageType.TEXT;
            }
            else {
                _messageType = WebSocketMessageType.BINARY;
            }
        }

        return _messageType;
    }

    // ------------------ Package-Private Implementation ----------------------
    // These methods are called from other classes in this package. They are
    // not part of the public API.
    public void close() throws IOException {
        if (isClosed()) {
            return;
        }

        if (!_webSocket.isDisconnected()) {
            String s = "Can't close the MessageReader if the WebSocket is still connected";
            throw new WebSocketException(s);
        }

        _closed = true;
        _sharedQueue.done();
    }

    public boolean isClosed() {
        return _closed;
    }
}
