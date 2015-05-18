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

package org.kaazing.net.http;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.net.URI;

import org.junit.Test;

public class HttpRedirectPolicyTest {

    @Test
    public void testAlways() {
        URI current = URI.create("ws://gateway.kaazing.test:8001/echo");
        URI redirect = URI.create("ws://node.kaazing.test:8001/echo/;e/cbm");
        int status = HttpRedirectPolicy.ALWAYS.compare(current, redirect);
        assertTrue(status == 0);
    }
    
    @Test
    public void testNever() {
        URI current = URI.create("ws://gateway.kaazing.test:8001/echo");
        URI redirect = URI.create("ws://node.kaazing.test:8001/echo/;e/cbm");
        int status = HttpRedirectPolicy.NEVER.compare(current, redirect);
        assertFalse(status == 0);
    }
    
    @Test
    public void testSameOrigin() {
        URI current = URI.create("ws://gateway.kaazing.test:8001/echo");
        URI redirect = URI.create("ws://gateway.kaazing.test:8001/echo/;e/cbm");
        int status = HttpRedirectPolicy.SAME_ORIGIN.compare(current, redirect);
        assertTrue(status == 0);
    }
    
    @Test
    public void testSameOriginWithDifferentPorts() {
        URI current = URI.create("ws://gateway.kaazing.test:8001/echo");
        URI redirect = URI.create("ws://gateway.kaazing.test:8002/echo/;e/cbm");
        int status = HttpRedirectPolicy.SAME_ORIGIN.compare(current, redirect);
        assertFalse(status == 0);
    }
    
    @Test
    public void testSameOriginWithDifferentHost() {
        URI current = URI.create("ws://gateway.kaazing.test:8001/echo");
        URI redirect = URI.create("ws://node.kaazing.test:8001/echo/;e/cbm");
        int status = HttpRedirectPolicy.SAME_ORIGIN.compare(current, redirect);
        assertFalse(status == 0);
    }
    
    @Test
    public void testSameDomain() {
        URI current = URI.create("ws://gateway.kaazing.test:8001/echo");
        URI redirect = URI.create("ws://gateway.kaazing.test:8002/echo/;e/cbm");
        int status = HttpRedirectPolicy.SAME_DOMAIN.compare(current, redirect);
        assertTrue(status == 0);
    }

    @Test
    public void testSameDomainWithIdenticalAuthority() {
        URI current = URI.create("ws://gateway.kaazing.test:8001/echo");
        URI redirect = URI.create("ws://gateway.kaazing.test:8001/echo/;e/cbm");
        int status = HttpRedirectPolicy.SAME_DOMAIN.compare(current, redirect);
        assertTrue(status == 0);
    }
    
    @Test
    public void testSameDomainWithDifferentHosts() {
        URI current = URI.create("ws://kaazing.test:8001/echo");
        URI redirect = URI.create("ws://gateway.kaazing.test:8001/echo/;e/cbm");
        int status = HttpRedirectPolicy.SAME_DOMAIN.compare(current, redirect);
        assertFalse(status == 0);
    }
    
    @Test
    public void testPeerDomain() {
        URI current = URI.create("ws://gateway.kaazing.test:8001/echo");
        URI redirect = URI.create("ws://node.kaazing.test:8001/echo/;e/cbm");
        int status = HttpRedirectPolicy.PEER_DOMAIN.compare(current, redirect);
        assertTrue(status == 0);
    }
    
    @Test
    public void testPeerDomainSameAuthority() {
        URI current = URI.create("ws://gateway.kaazing.test:8001/echo");
        URI redirect = URI.create("ws://gateway.kaazing.test:8001/echo/;e/cbm");
        int status = HttpRedirectPolicy.PEER_DOMAIN.compare(current, redirect);
        assertTrue(status == 0);
    }
    
    @Test
    public void testPeerDomainSameHostname() {
        URI current = URI.create("ws://localhost:8001/echo");
        URI redirect = URI.create("ws://localhost:8002/echo/;e/cbm");
        int status = HttpRedirectPolicy.PEER_DOMAIN.compare(current, redirect);
        assertTrue(status == 0);
    }
    
    @Test
    public void testPeerDomainNegative() {
        URI current = URI.create("ws://hr.benefits.example.com:8001/echo");
        URI redirect = URI.create("ws://campaign.marketing.example.com:8002/echo/;e/cbm");
        int status = HttpRedirectPolicy.PEER_DOMAIN.compare(current, redirect);
        assertFalse(status == 0);
    }
    
    @Test
    public void testSubDomain() {
        URI current = URI.create("ws://benefits.example.com:8001/echo");
        URI redirect = URI.create("ws://hr.benefits.example.com:8002/echo/;e/cbm");
        int status = HttpRedirectPolicy.SUB_DOMAIN.compare(current, redirect);
        assertTrue(status == 0);
    }
    
    @Test
    public void testSubDomainSameAuthority() {
        URI current = URI.create("ws://hr.benefits.example.com:8001/echo");
        URI redirect = URI.create("ws://hr.benefits.example.com:8001/echo/;e/cbm");
        int status = HttpRedirectPolicy.SUB_DOMAIN.compare(current, redirect);
        assertTrue(status == 0);
    }
    
    @Test
    public void testSubDomainSameHostname() {
        URI current = URI.create("ws://hr.benefits.example.com:8001/echo");
        URI redirect = URI.create("ws://hr.benefits.example.com:8002/echo/;e/cbm");
        int status = HttpRedirectPolicy.SUB_DOMAIN.compare(current, redirect);
        assertTrue(status == 0);
    }
    
    @Test
    public void testChildDomain() {
        URI current = URI.create("ws://example.com:8001/echo");
        URI redirect = URI.create("ws://hr.benefits.example.com:8001/echo/;e/cbm");
        int status = HttpRedirectPolicy.SUB_DOMAIN.compare(current, redirect);
        assertTrue(status == 0);
    }
    
    @Test
    public void testSubDomainNegative() {
        URI current = URI.create("ws://marketing.example.com:8001/echo");
        URI redirect = URI.create("ws://hr.benefits.example.com:8002/echo/;e/cbm");
        int status = HttpRedirectPolicy.SUB_DOMAIN.compare(current, redirect);
        assertFalse(status == 0);
    }
}
