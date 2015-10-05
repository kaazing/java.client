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

import org.junit.Assert;
import org.junit.Test;

import org.kaazing.net.auth.BasicChallengeHandler;
import org.kaazing.net.auth.ChallengeResponse;

public class ChallengeResponseTest {



    ChallengeResponse response;


    @Test
    public void testConstruction() throws Exception {
        response = new ChallengeResponse(null, null);
        Assert.assertNull(response.getCredentials());
        Assert.assertNull(response.getNextChallengeHandler());

        BasicChallengeHandler handler = BasicChallengeHandler.create();
        response = new ChallengeResponse(null, handler);
        Assert.assertSame(handler, response.getNextChallengeHandler());

        response = new ChallengeResponse("Basic fjksdlafjs".toCharArray(), handler);
        Assert.assertArrayEquals("Basic fjksdlafjs".toCharArray(), response.getCredentials());
        Assert.assertSame(handler, response.getNextChallengeHandler());
    }


}
