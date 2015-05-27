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

package org.kaazing.net.auth;

import java.net.PasswordAuthentication;

import org.junit.Test;

import org.kaazing.net.impl.auth.SampleChallengeHandler;
import org.kaazing.net.ws.WebSocketFactory;

public class TestExamples {

    @Test
    public void EXAMPLE_clientWantsToUseOneGlobalChallengeHandler() throws Exception {
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        BasicChallengeHandler basicChallengeHandler = BasicChallengeHandler.create();
        basicChallengeHandler.setLoginHandler(new LoginHandler() {
            @Override
            public PasswordAuthentication getCredentials() {
                return new PasswordAuthentication("global", "credentials".toCharArray());
            }
        });
        wsFactory.setDefaultChallengeHandler(basicChallengeHandler);
    }

    @Test
    public void EXAMPLE_clientWantsToUseOneGlobalChallengeHandlerDeclaratively() throws Exception {
        LoginHandler loginHandler = new LoginHandler() {
            @Override
            public PasswordAuthentication getCredentials() {
                return new PasswordAuthentication("global", "credentials".toCharArray());
            }
        };
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        wsFactory.setDefaultChallengeHandler(BasicChallengeHandler.create().setLoginHandler(loginHandler));
    }

    @Test
    public void EXAMPLE_clientWantsToRegisterOneLocationSpecificChallengeHandler() throws Exception {
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        DispatchChallengeHandler dispatchChallengeHandler = DispatchChallengeHandler.create();
        BasicChallengeHandler basicChallengeHandler = BasicChallengeHandler.create();
        basicChallengeHandler.setLoginHandler(new LoginHandler() {
            @Override
            public PasswordAuthentication getCredentials() {
                return new PasswordAuthentication("global", "credentials".toCharArray());
            }
        });
        dispatchChallengeHandler.register("ws://my.server.com", basicChallengeHandler);
        wsFactory.setDefaultChallengeHandler(dispatchChallengeHandler);
    }

    @Test
    public void EXAMPLE_clientWantsToRegisterOneLocationSpecificChallengeHandlerDeclaratively() throws Exception {
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        wsFactory.setDefaultChallengeHandler(DispatchChallengeHandler.create().register("ws://my.server.com", 
                                                                                 BasicChallengeHandler.create().setLoginHandler(new LoginHandler() {
                                                                                    @Override
                                                                                    public PasswordAuthentication getCredentials() {
                                                                                        return new PasswordAuthentication("global", "credentials".toCharArray());
                                                                                    }
                                                                                  })
                                                                                )
                                    );
    }


    @Test
    public void EXAMPLE_clientWantsToRegisterMultipleLocationSpecificChallengeHandlersDeclaratively() throws Exception {

        // Client can declare their login handlers....
        LoginHandler myServerLoginHandler = new LoginHandler() {
            @Override
            public PasswordAuthentication getCredentials() {
                return new PasswordAuthentication("myserver", "credentials".toCharArray());
            }
        };


        LoginHandler anotherServerLoginHandler = new LoginHandler() {
            @Override
            public PasswordAuthentication getCredentials() {
                return new PasswordAuthentication("another.server", "credentials".toCharArray());
            }
        };

        // Client can then declaratively associated those login handlers with locations
        WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
        wsFactory.setDefaultChallengeHandler(DispatchChallengeHandler.create().register("ws://my.server.com",
                                                                                 BasicChallengeHandler.create().setLoginHandler(myServerLoginHandler)
                                                                                ).register("ws://another.server.com",
                                                                                           SampleChallengeHandler.create().setLoginHandler(anotherServerLoginHandler)
                                                                                  )
                                      );
    }
}
