/**
 * Copyright (c) 2007-2012, Kaazing Corporation. All rights reserved.
 */

package com.kaazing.gateway.amqp.client.impl;

import com.kaazing.gateway.amqp.client.AmqpClient;

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
