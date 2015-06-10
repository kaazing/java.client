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

package org.kaazing.net.impl.util;

import static java.lang.String.format;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * ArrayBlockingQueue extension with ability to interrupt or end-of-stream.
 * This will be used by producer(ie. the listener) and the consumer(ie. the
 * WebSocketMessageReader). To match the 3.X event-listener behavior, the
 * capacity of the queue can be set to 1.
 *
 * @param <E>   element type
 */
public class BlockingQueueImpl<E> extends ArrayBlockingQueue<E> {
    private static final long serialVersionUID = 1L;
    private static final String _CLASS_NAME = BlockingQueueImpl.class.getName();
    private static final Logger _LOG = Logger.getLogger(_CLASS_NAME);


    // ### TODO: Maybe expose an API on WebSocket/WsURLConnection for developers
    //           to specify the number of incoming messages that can be held
    //           before we start pushing on the network.
    private static final int  _QUEUE_CAPACITY = 32;

    private boolean _done = false;

    public BlockingQueueImpl() {
        super(_QUEUE_CAPACITY, true);
    }

    public synchronized void done() {
        _LOG.log(Level.FINE, format("BlockingQueueImpl.done(): ThreadId: %d: Connection closed; size = '%d'",
                                     Thread.currentThread().getId(), size()));
        _done = true;
        notifyAll();
    }

    public synchronized boolean isDone() {
        _LOG.log(Level.FINE, format("BlockingQueueImpl.isDone(): ThreadId: %d: size = '%d'",
                                    Thread.currentThread().getId(), size()));
        return _done;
    }

    public synchronized void reset() {
        // Wake up threads that maybe blocked to retrieve data.
        notifyAll();
        clear();
        _done = false;
    }

    // Override to make peek() a blocking call.
    @Override
    public E peek() {
        E el;

        while (((el = super.peek()) == null) && !isDone()) {
            synchronized (this) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    String s = "Reader has been interrupted maybe the connection is closed";
                    throw new RuntimeException(s);
                }
            }
        }

        if ((el == null) && isDone()) {
            String s = "Reader has been interrupted maybe the connection is closed";
            throw new RuntimeException(s);
        }

        return el;
    }

    @Override
    public void put(E el) throws InterruptedException {
        _LOG.log(Level.FINE, format("BlockingQueueImpl.put(): ThreadId: %d: Adding a message - current size = '%d'",
                                         Thread.currentThread().getId(), size()));
        synchronized (this) {
            while ((size() == _QUEUE_CAPACITY) && !isDone()) {
                // Push on the network as the messages are not being retrieved.
                wait();
            }

            if (isDone() && (size() == _QUEUE_CAPACITY)) {
                notifyAll();
                return;
            }
        }

        super.put(el);

        synchronized (this) {
            _LOG.log(Level.FINE, format("BlockingQueueImpl.put(): ThreadId: %d: Added a message - current size = '%d'",
                    Thread.currentThread().getId(), size()));
            notifyAll();
        }
    }

    @Override
    public E take() throws InterruptedException {
        E el = null;
        _LOG.log(Level.FINE, format("BlockingQueueImpl.take(): ThreadId: %d: Retrieving a message - current size = '%d'",
                                    Thread.currentThread().getId(), size()));

        synchronized (this) {
            while (isEmpty() && !isDone()) {
                wait();
            }

            if (isDone() && (size() == 0)) {
                notifyAll();
                _LOG.log(Level.FINE,
                        format("BlockingQueueImpl.take(): ThreadId: %d: Throwing InterruptedException - current size = '%d'",
                               Thread.currentThread().getId(), size()));
                String s = "Reader has been interrupted maybe the connection is closed";
                throw new InterruptedException(s);
            }
        }

        el = super.take();

        synchronized (this) {
            notifyAll();
        }

        _LOG.log(Level.FINE, format("BlockingQueueImpl.take(): ThreadId: %d: Retrieved a message - current size = '%d'",
                                    Thread.currentThread().getId(), size()));
        return el;
    }
}

