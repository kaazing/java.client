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

import org.kaazing.net.auth.ChallengeRequest;
import org.kaazing.net.impl.auth.RealmUtils;

import org.junit.Assert;
import org.junit.Test;

public class ChallengeRequestTest {


    private static final String DEFAULT_LOCATION = "http://host.example.com/foo";

    @Test(expected = NullPointerException.class)
    public void testNullLocation() throws Exception {
        new ChallengeRequest(null, null);
    }

    @Test
    public void testNullChallenge() throws Exception {
        ChallengeRequest challengeRequest = new ChallengeRequest(DEFAULT_LOCATION, null);
        Assert.assertNull(challengeRequest.getAuthenticationScheme());
        Assert.assertNull(challengeRequest.getAuthenticationParameters());
    }

    @Test
    public void testEmptyChallenge() throws Exception {
        ChallengeRequest challengeRequest = new ChallengeRequest(DEFAULT_LOCATION, "");
        Assert.assertEquals("", challengeRequest.getAuthenticationScheme());
        Assert.assertNull(challengeRequest.getAuthenticationParameters());
    }

    @Test
    public void testBasicChallenge() throws Exception {
        ChallengeRequest challengeRequest = new ChallengeRequest(DEFAULT_LOCATION, "Basic");
        Assert.assertEquals("Basic", challengeRequest.getAuthenticationScheme());
        Assert.assertNull(challengeRequest.getAuthenticationParameters());

        challengeRequest = new ChallengeRequest(DEFAULT_LOCATION, "Basic ");
        Assert.assertEquals("Basic", challengeRequest.getAuthenticationScheme());
        Assert.assertNull(challengeRequest.getAuthenticationParameters());

        challengeRequest = new ChallengeRequest(DEFAULT_LOCATION, "Basic AuthData");
        Assert.assertEquals("Basic", challengeRequest.getAuthenticationScheme());
        Assert.assertEquals("AuthData", challengeRequest.getAuthenticationParameters());
    }

    @Test
    public void testRealmParameter() throws Exception {
        ChallengeRequest challengeRequest = new ChallengeRequest(DEFAULT_LOCATION, "Basic");
        Assert.assertNull(RealmUtils.getRealm(challengeRequest));

        challengeRequest = new ChallengeRequest(DEFAULT_LOCATION, "Basic realm=missingQuotes");
        Assert.assertNull(RealmUtils.getRealm(challengeRequest));

        challengeRequest = new ChallengeRequest(DEFAULT_LOCATION, "Basic realm=");
        Assert.assertNull(RealmUtils.getRealm(challengeRequest));

        challengeRequest = new ChallengeRequest(DEFAULT_LOCATION, "Basic realm=\"\"");
        Assert.assertEquals("", RealmUtils.getRealm(challengeRequest));

        challengeRequest = new ChallengeRequest(DEFAULT_LOCATION, "Basic realm=\"realmValue\"");
        Assert.assertEquals("realmValue", RealmUtils.getRealm(challengeRequest));
    }
}
