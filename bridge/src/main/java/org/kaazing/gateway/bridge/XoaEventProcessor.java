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

package org.kaazing.gateway.bridge;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

class XoaEventProcessor implements Runnable {
    private static final String CLASS_NAME = XoaEventProcessor.class.getName();

    static final Logger LOG = Logger.getLogger(CLASS_NAME);

    private final XoaEventListener listener;
    
    private LinkedBlockingQueue<XoaEvent> xoaEventQueue = new LinkedBlockingQueue<XoaEvent>();

    static final int STARTED = 0;
    static final int STOPPING = 1;
    static final int STOPPED = 2;

    final AtomicInteger state = new AtomicInteger(STOPPED);
    
    XoaEventProcessor(XoaEventListener listener) {
        this.listener = listener;
        LOG.entering(CLASS_NAME, "<init>");
    }
    
    public void offer(XoaEvent event) {
        xoaEventQueue.offer(event);
        
        // Start Event Handler thread if necessary
        start();
    }

    public void start() {
        // TODO: it's possible to race with run() below where the queue was empty
        // before the call to offer, but the state is set to STOPPED
        LOG.entering(CLASS_NAME, "start");
        if (state.compareAndSet(STOPPING, STARTED)) {
            // Keep same thread running if STOPPING
        }
        else if (state.compareAndSet(STOPPED, STARTED)) {
            new Thread(this, "XoaEventProcessor").start();
        }
    }
    
    public void stop() {
        LOG.entering(CLASS_NAME, "stop");
        state.compareAndSet(STARTED, STOPPING);
    }

    public void run() {
        LOG.entering(CLASS_NAME, "run");
        for (;;) {
            if (xoaEventQueue.size() == 0) {
                if (state.compareAndSet(STOPPING, STOPPED)) {
                    break;
                }
            }

            try {
                // call to take blocks till the next element is available
                XoaEvent ev = xoaEventQueue.poll(1000, TimeUnit.MILLISECONDS);
                if (ev != null) {
                    listener.handleEvent(ev);
                }
            } catch (Exception e) {
                LOG.log(Level.FINE, "While processing bridge events: "+e.getMessage(), e);
            }
        }
    }
}