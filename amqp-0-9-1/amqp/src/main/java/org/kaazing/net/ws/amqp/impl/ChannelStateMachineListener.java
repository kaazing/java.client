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
package org.kaazing.net.ws.amqp.impl;

import org.kaazing.net.ws.amqp.AmqpChannel;

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
