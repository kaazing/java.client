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

package org.kaazing.net.ws.amqp.impl;

import org.kaazing.net.ws.amqp.AmqpClient;

// Listener used to avoid exposing additional public APIs on AmqpClient. This 
// interface will allow classes in the impl package such as AsynClient and 
// StateMachine to indirectly invoke methods on AmqpClient.
public interface ClientStateMachineListener {

    public void closeConnection(Object   context,
                                String   input,
                                Object   args, 
                                String   stateName);
    
    public void createAndSendFrame(Object   context,
                                   Object[] args);

    public void handleGenericResponse(Object   context,
                                      String   input,
                                      Object   args, 
                                      String   stateName);

    public void handleIdling(Object   context,
                             String   input,
                             Object   args, 
                             String   stateName);

    public AmqpClient getClient();

    public void startHandshake(Object   context,
                               String   input,
                               Object   args, 
                               String   stateName);
    
    public void performDefaultBehavior(Object   context,
                                       String   input,
                                       Object   args, 
                                       String   stateName);

    public void performAdvancedActions(Object   context,
                                       String   input,
                                       Object   args, 
                                       String   stateName);

    public void registerSynchronousRequest(Object   context,
                                           String   input,
                                           Object   args, 
                                           String   stateName);

    public void startConnection(Object   context,
                                String   input,
                                Object   args, 
                                String   stateName);
    
    public void tuneConnection(Object   context,
                               String   input,
                               Object   args, 
                               String   stateName);
}
