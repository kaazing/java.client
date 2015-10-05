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
package org.kaazing.net.auth;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import org.kaazing.net.auth.ChallengeHandler;
import org.kaazing.net.impl.auth.SampleChallengeHandler;

public class ChallengeHandlersTest {
    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }



    @Test
    public void testChallengeHandlerFactoryLoading() throws IllegalAccessException, InstantiationException {
        // Should load a TestChallengeHandler

        ChallengeHandler handler = SampleChallengeHandler.create();
        Assert.assertNotNull(handler);
        Assert.assertEquals(SampleChallengeHandler.class, handler.getClass());
    }

    @Test
    public void testChallengeHandlerFactoryClassLoading() {
        // Should load a TestChallengeHandler
        ChallengeHandler handler = SampleChallengeHandler.create(ClassLoader.getSystemClassLoader());
        Assert.assertNotNull(handler);
        Assert.assertEquals(SampleChallengeHandler.class, handler.getClass());
    }

    @Test
    public void testFindByClassObject() throws Exception {
        ChallengeHandler chf1 = SampleChallengeHandler.create();
        Assert.assertNotNull(chf1);
    }
}
