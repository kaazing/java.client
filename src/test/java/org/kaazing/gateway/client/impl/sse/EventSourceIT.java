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

package org.kaazing.gateway.client.impl.sse;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.Assert.fail;
import static org.junit.rules.RuleChain.outerRule;

import java.net.URI;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.DisableOnDebug;
import org.junit.rules.TestRule;
import org.junit.rules.Timeout;
import org.kaazing.k3po.junit.annotation.Specification;
import org.kaazing.k3po.junit.rules.K3poRule;
import org.kaazing.net.sse.SseEventReader;
import org.kaazing.net.sse.SseEventSource;
import org.kaazing.net.sse.SseEventSourceFactory;
import org.kaazing.net.sse.SseEventType;

public class EventSourceIT {
    boolean success;

	private final K3poRule k3po = new K3poRule();

	private final TestRule timeout = new DisableOnDebug(new Timeout(5, SECONDS));

	@Rule
	public final TestRule chain = outerRule(k3po).around(timeout);

    @Specification("connect.and.get.data")
    @Test(timeout = 1500)
    public void testConnect() throws Exception {

        SseEventSource eventSource = null;
        SseEventSourceFactory factory = SseEventSourceFactory.createEventSourceFactory();
        eventSource = factory.createEventSource(URI.create("http://localhost:7788"));
        eventSource.connect();
        SseEventReader reader = eventSource.getEventReader();
        SseEventType type = null;
        while ((type = reader.next()) != SseEventType.EOS) {
            switch (type) {
                case DATA:
                    if (reader.getData().equals("Lakers Rule!!")){
                        success = true;
                        break;
                    }
                    break;
                case EMPTY:
                    System.out.println("Empty");
                    break;
                case EOS:
                    break;
            }

            if (success){
                break;
            }
        } 

        if (!success) {
            fail("Did not receive message on SSE");
        }
        k3po.finish();

    }

    /**
     * Sets the test up @Test
     */
    @Before
    public void setUp() throws Exception {

    }

    /**
     * Tears the test up after each @Test
     */
    @After
    public void tearDown() throws Exception {

    }
}
