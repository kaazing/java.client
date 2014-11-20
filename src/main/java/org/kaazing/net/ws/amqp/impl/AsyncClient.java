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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.kaazing.net.ws.amqp.AmqpChannel;
import org.kaazing.net.ws.amqp.AmqpEvent;
import org.kaazing.net.ws.amqp.ChannelEvent;

public final class AsyncClient {
     
     private List<Action> actions = Collections.synchronizedList(new ArrayList<Action>());
     StateMachine stateMachine;        
     Object waitingAction;
   
     /**
      * AsyncClient provides base structure and functionality for asynchronous network clients
      */
     public AsyncClient()
     {
         this.stateMachine = null;
     }  

     public synchronized void processActions()
     {
    	 Action action = null;
         if (this.actions.size() > 0) {
        	 action = this.actions.remove(0);
         }
         
         if (action == null) {
        	 return;
         }
        
         try
         {
             Boolean movedSuccessfully = this.stateMachine.feedInput(action.actionName + "Action", action);            
             if (movedSuccessfully)
             {                 
                 if (action.func.equalsIgnoreCase("write")) {
                     ClientStateMachineListener csmListener = stateMachine.getClientStateMachineListener();
                     try {
                         csmListener.createAndSendFrame(csmListener.getClient(), action.args);
                     }
                     catch (Exception ex1) {
                         ex1.printStackTrace();
                         if (action.error != null) {
                             AmqpEvent e = new ChannelEvent(csmListener.getClient(), 
                                                            ChannelEvent.Kind.ERROR, 
                                                            ex1.getMessage());
                             action.error.error(e);
                         }
                     }
                 } 
                 else {
                     ChannelStateMachineListener chsmListener = stateMachine.getChannelStateMachineListener();
                     AmqpChannel channel = chsmListener.getChannel();

                     try {
                         chsmListener.createAndSendFrame(channel, action.args);
                     }
                     catch (Exception ex2) {
                         ex2.printStackTrace();
                         if (action.error != null) {
                             AmqpEvent e = new ChannelEvent(channel, ChannelEvent.Kind.ERROR, ex2.getMessage());
                             action.error.error(e);
                         }
                     }
                 }                
             }
             else
             {                
                 this.actions.add(0,action);                
             }
         }
         catch (Exception e)
         {    
             throw new IllegalStateException(e.getMessage());
         }
     }

    
     public synchronized void enqueueAction(String actionName, String func, Object[] args, Continuation callback, ErrorHandler error)
     {         
         Action action = new Action();
         action.actionName = actionName;
         action.func = func;
         action.args = args;
         if(callback != null){
             action.continuation=callback;
         }
         if(error != null){
             action.error=error;
         }
       
         this.actions.add(action);
                  
         this.processActions();
     }

     public void initAsyncClient()
     {
         this.stateMachine = new StateMachine(this);           
     }
     
     public StateMachine getStateMachine()
     {
         return this.stateMachine;
     }
     
     public Object getWaitingAction()
     {
         return this.waitingAction;
     }
     
     public synchronized void setWaitingAction(Object waitingAction)
     {
         this.waitingAction = waitingAction;
     }
}
