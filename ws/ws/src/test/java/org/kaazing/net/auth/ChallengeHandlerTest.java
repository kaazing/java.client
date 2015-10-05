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
import org.junit.Test;

import org.kaazing.net.ws.WebSocketFactory;

public class ChallengeHandlerTest {

    @Test
    public void shouldCorrectlyRetrieveAnEstablishedDefaultChallengeHandler() throws Exception {
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        BasicChallengeHandler basicChallengeHandler = BasicChallengeHandler.create();
        basicChallengeHandler.setLoginHandler(new LoginHandler() {
            @Override
            public PasswordAuthentication getCredentials() {
                return new PasswordAuthentication("global", "credentials".toCharArray());
            }
        });
        wsFactory.setDefaultChallengeHandler(basicChallengeHandler);
        Assert.assertSame(basicChallengeHandler, wsFactory.getDefaultChallengeHandler());
    }


    @Test
    public void clientShouldBeAbleToRegisterAndUseABasicChallengeHandler() throws Exception {

        DispatchChallengeHandler dispatchChallengeHandler = DispatchChallengeHandler.create();

        // From the loader find a challenge handler that can handle that challenge.
        BasicChallengeHandler challengeHandler = BasicChallengeHandler.create();

        // Establish a login handler on the challenge handler factory (required)
        challengeHandler.setLoginHandler(new LoginHandler() {
            @Override
            public PasswordAuthentication getCredentials() {
                return new PasswordAuthentication("joe", "welcome".toCharArray());
            }
        });

        // Register the challenge handler at a location.
        dispatchChallengeHandler.register("ws://*/", challengeHandler);

        // Up until now, we are doing one-time setup.  Now let us imagine the server challenges us
        // for authentication (i.e. returns a 401 status code).  The developer does not need to
        // know about the code below - it simulates what happens.

        // Imagine we get this string back from the server as a www-authenticate (401) challenge.
        String challenge = "Basic realm=\"Example realm\"";

        // The web socket code will lookup the challenge handler factory by location.
        ChallengeRequest challengeRequest = new ChallengeRequest("ws://my.server.com", challenge);

        // ...and then use the challenge handler to respond to the server challenge
        ChallengeResponse response = dispatchChallengeHandler.handle(challengeRequest);
        Assert.assertEquals("Basic am9lOndlbGNvbWU=", String.valueOf(response.getCredentials()));
    }

}
