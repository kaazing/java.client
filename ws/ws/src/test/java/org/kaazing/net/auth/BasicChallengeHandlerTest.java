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

import java.net.PasswordAuthentication;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import org.kaazing.net.ws.WebSocketFactory;

public class BasicChallengeHandlerTest {

    BasicChallengeHandler basicHandler;
    LoginHandler loginHandler;
    DispatchChallengeHandler dispatchHandler;

    public static final String DEFAULT_LOCATION = "http://localhost:8000";

    @Before
    public void before() {
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        dispatchHandler = DispatchChallengeHandler.create();
        wsFactory.setDefaultChallengeHandler(dispatchHandler);
        basicHandler = BasicChallengeHandler.create();
        loginHandler = new LoginHandler() {
            @Override
            public PasswordAuthentication getCredentials() {
                return new PasswordAuthentication("joe", "welcome".toCharArray());
            }
        };

    }

    @Test
    public void shouldAlwaysHandleABasicRequestIfBasicChallengeHandlerIsRegistered() throws Exception {
        basicHandler.setLoginHandler(loginHandler);

        LoginHandler loginHandler2 = new LoginHandler() {
            @Override
            public PasswordAuthentication getCredentials() {
                return new PasswordAuthentication("joe2", "welcome2".toCharArray());
            }
        };

        basicHandler.setRealmLoginHandler("Test Realm", loginHandler2);
        dispatchHandler.register(DEFAULT_LOCATION, basicHandler);

        ChallengeRequest challengeRequest = new ChallengeRequest(DEFAULT_LOCATION, "Basic");
        Assert.assertTrue(dispatchHandler.canHandle(challengeRequest));
        ChallengeResponse response = dispatchHandler.handle(challengeRequest);
        Assert.assertEquals("Basic am9lOndlbGNvbWU=", String.valueOf(response.getCredentials()));

        challengeRequest = new ChallengeRequest(DEFAULT_LOCATION, "Basic realm=\"Not matching\"");
        Assert.assertTrue(dispatchHandler.canHandle(challengeRequest));
        response = dispatchHandler.handle(challengeRequest);
        Assert.assertEquals("Basic am9lOndlbGNvbWU=", String.valueOf(response.getCredentials()));

        challengeRequest = new ChallengeRequest(DEFAULT_LOCATION, "Basic realm=\"Test Realm\"");
        Assert.assertTrue(dispatchHandler.canHandle(challengeRequest));
        response = dispatchHandler.handle(challengeRequest);
        //NOTE: we should be encoding joe2/welcome2 because the realm handler is handling it
        //      using the loginHandler2.
        Assert.assertEquals("Basic am9lMjp3ZWxjb21lMg==", String.valueOf(response.getCredentials()));
    }

    @Test
    public void shouldOnlyHandleRealmSpecificRequestsWithRealmSpecificRegisteredHandler() throws Exception {

        LoginHandler loginHandler2 = new LoginHandler() {
            @Override
            public PasswordAuthentication getCredentials() {
                return new PasswordAuthentication("joe2", "welcome2".toCharArray());
            }
        };
        basicHandler.setLoginHandler(null);

        basicHandler.setRealmLoginHandler("Test Realm", loginHandler2);
        dispatchHandler.register(DEFAULT_LOCATION, basicHandler);

        ChallengeRequest challengeRequest = new ChallengeRequest(DEFAULT_LOCATION, "Basic");
        Assert.assertTrue(dispatchHandler.canHandle(challengeRequest));
        ChallengeResponse response = dispatchHandler.handle(challengeRequest);
        Assert.assertNull(response);


        challengeRequest = new ChallengeRequest(DEFAULT_LOCATION, "Basic realm=\"Not Matching\"");
        Assert.assertTrue(dispatchHandler.canHandle(challengeRequest));
        response = dispatchHandler.handle(challengeRequest);
        Assert.assertNull(response);

        challengeRequest = new ChallengeRequest(DEFAULT_LOCATION, "Basic realm=\"Test Realm\"");
        Assert.assertTrue(dispatchHandler.canHandle(challengeRequest));
        response = dispatchHandler.handle(challengeRequest);
        Assert.assertEquals("Basic am9lMjp3ZWxjb21lMg==", String.valueOf(response.getCredentials()));

        // Test for case insensitivity of realm parameter name
        challengeRequest = new ChallengeRequest(DEFAULT_LOCATION, "Basic Realm=\"Test Realm\"");
        Assert.assertTrue(dispatchHandler.canHandle(challengeRequest));
        response = dispatchHandler.handle(challengeRequest);
        Assert.assertEquals("Basic am9lMjp3ZWxjb21lMg==", String.valueOf(response.getCredentials()));
    }

}
