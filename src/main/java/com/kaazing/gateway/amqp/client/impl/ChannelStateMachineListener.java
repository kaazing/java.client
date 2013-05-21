/**
 * Copyright (c) 2007-2012, Kaazing Corporation. All rights reserved.
 */

package com.kaazing.gateway.amqp.client.impl;

import com.kaazing.gateway.amqp.client.AmqpChannel;

// Listener used to avoid exposing additional public APIs on AmqpClient. This
// interface will allow classes in the impl package such as AsynClient and 
// StateMachine to invoke methods on AmqpClient.
public interface ChannelStateMachineListener {
	
	public void checkTransactions(Object   context,
                                  String   input,
                                  Object   args, 
                                  String   stateName);

	public void closeChannel(Object   context,
                             String   input,
                             Object   args, 
                             String   stateName);
	
	public void createAndSendFrame(Object context, Object[] args);
	
	public void handleAdvancedActions(Object   context,
                                      String   input,
                                      Object   args, 
                                      String   stateName);

	public void handleContentHeader(Object   context,
                                    String   input,
                                    Object   args, 
                                    String   stateName);

    public void handleDelivery(Object   context,
                               String   input,
                               Object   args, 
                               String   stateName);
	
	public void handleDefaultBehavior(Object   context,
                                      String   input,
                                      Object   args, 
                                      String   stateName);
	
	public void handleEmptyResponse(Object   context,
                                    String   input,
                                    Object   args, 
                                    String   stateName);

	public void handleGenericError(Object   context,
                                   String   input,
                                   Object   args, 
                                   String   stateName);

	public void handleGenericResponse(Object   context,
                                      String   input,
                                      Object   args, 
                                      String   stateName);
	
	public void handleMessageBody(Object   context,
                                  String   input,
                                  Object   args, 
                                  String   stateName);

	public AmqpChannel getChannel();
	
	public void registerSynchronousRequest(Object   context,
                                           String   input,
                                           Object   args, 
                                           String   stateName);
}
