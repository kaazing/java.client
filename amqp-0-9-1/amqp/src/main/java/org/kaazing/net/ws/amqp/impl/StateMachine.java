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

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;

import org.kaazing.net.ws.amqp.AmqpClient;

/**
 * State machine class for representing state graphs with behavior and
 * conditional transitions on input objects.
 */
public final class StateMachine {

    private ClientStateMachineListener     clientStateMachineListener;
    private ChannelStateMachineListener    channelStateMachineListener;
    private Object                         context;
    private State                          state;
    private State                          currentState;
    private HashMap<String, State>         states = new HashMap<String, State>();

    public StateMachine(Object context) {
        state = new State();
        this.context = context;
        this.currentState = new State();
    }

    public ClientStateMachineListener getClientStateMachineListener() {
        return clientStateMachineListener;
    }

    public void setClientStateMachineListener(ClientStateMachineListener listener) {
        this.clientStateMachineListener = listener;
    }

    public ChannelStateMachineListener getChannelStateMachineListener() {
        return channelStateMachineListener;
    }

    public void setChannelStateMachineListener(ChannelStateMachineListener listener) {
        this.channelStateMachineListener = listener;
    }

    public void enterState(String stateName, String input, Object args) {
        State tempStateName = this.currentState;

        // change to next state
        State state = this.states.get(stateName);
        this.currentState = state;

        if (tempStateName.stateName != null) {
            this.executeEntryOrExitBehaviour(tempStateName.exitBehavior,
                                             stateName, input, args);
        }
        try {
            this.executeEntryOrExitBehaviour(state.entryBehavior, stateName,
                                             input, args);
        } catch (Exception e) {
            throw new IllegalStateException(
                    "Could not call behavior for state " + this.state.stateName
                            + "\n\n" + e.getMessage());
        }
    }

    public void addState(String                   stateName, 
                         List<Rules>              ruleList,
                         ClientChannelBehaviours  entryBehavior,
                          ClientChannelBehaviours exitBehavior) {
        State state = new State();
        state.stateName = stateName;

        if (entryBehavior != null) {
            state.entryBehavior = entryBehavior;
        }

        if (exitBehavior != null) {
            state.exitBehavior = exitBehavior;
        }

        this.states.put(stateName, state);

        // build associative lookup of states by name
        for (int i = 0; i < ruleList.size(); i++) {
            Rules rules = ruleList.get(i);
            for (int j = 0; j < rules.inputs.length; j++) {
                String input = rules.inputs[j];
                state.rules.put(input, rules.targetState);
            }
        }
    }

    public Boolean feedInput(String input, Object args) {
        state.stateName = this.currentState.stateName;
        state.rules = this.currentState.rules;
        state.entryBehavior = this.currentState.entryBehavior;
        state.exitBehavior = this.currentState.exitBehavior;

        if (state.rules.containsKey(input)) {

            StateMachine sm = this;
            sm.enterState(state.rules.get(input), input, args);
            return true;
        } else {
            return false;
        }
    }

    private void executeEntryOrExitBehaviour(ClientChannelBehaviours behaviour,
                                             String                  stateName, 
                                             String                  input, 
                                             Object                  args) {

        Class<AmqpClient> cls = AmqpClient.class;
        boolean flagMethodExecute = false;

        try {
            Method[] methods = cls.getDeclaredMethods();

            for (int i = 0; i < methods.length; i++) {
                String methodName = methods[i].getName();
                if (methodName.equals(mapConstantsToFunctions(behaviour)) &&
                    (clientStateMachineListener != null)) {
                    callClientBehav(behaviour, this.context, input, args,
                                    stateName);
                    flagMethodExecute = true;
                    break;
                }
            }
            if (!flagMethodExecute) {
                callChannelBehav(behaviour, this.context, input, args,
                                 stateName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void callClientBehav(ClientChannelBehaviours behaviour,
                                 Object                  context, 
                                 String                  input,
                                 Object                  args, 
                                 String                  stateName) {
        switch (behaviour) {
            case HANDSHAKE_START_HANDLER:
                clientStateMachineListener.startHandshake(context, input, args, stateName);
                break;
            case DEFAULT_BEHAVIOR_HANDLER:
                clientStateMachineListener.performDefaultBehavior(context, input, args, stateName);
                break;
            case STARTING_HANDLER:
                clientStateMachineListener.startConnection(context, input, args, stateName);
                break;
            case TUNE_CONNECTION_HANDLER:
                clientStateMachineListener.tuneConnection(context, input, args, stateName);
                break;
            case ADVANCE_ACTIONS_HANDLER:
                clientStateMachineListener.performAdvancedActions(context, input, args, stateName);
                break;
            case REGISTER_SYNCHRONOUS_REQUEST:
                clientStateMachineListener.registerSynchronousRequest(context, input, args, stateName);
                break;
            case GENERIC_RESPONSE_HANDLER:
                clientStateMachineListener.handleGenericResponse(context, input, args, stateName);
                break;
            case IDLING_HANDLER:
                clientStateMachineListener.handleIdling(context, input, args, stateName);
                break;
            case CLOSED_HANDLER:
                clientStateMachineListener.closeConnection(context, input, args, stateName);
                break;
            case DEFAULT:
                throw new IllegalStateException("Error in callClientBehav");
        }
    }

    private void callChannelBehav(ClientChannelBehaviours behaviour,
                                  Object                  context, 
                                  String                  input,
                                  Object                  args, 
                                  String                  stateName) {
        switch (behaviour) {
            case ADVANCE_ACTIONS_CHANNEL_HANDLER:
                channelStateMachineListener.handleAdvancedActions(context, input, args, stateName);
                break;
            case TX_CHECKING_HANDLER:
                channelStateMachineListener.checkTransactions(context, input, args, stateName);
                break;
            case CHANNEL_REGISTER_SYNCHRONOUS_REQUEST:
                channelStateMachineListener.registerSynchronousRequest(context, input, args, stateName);
                break;
            case GET_EMPTY_RESPONSE_HANDLER:
                channelStateMachineListener.handleEmptyResponse(context, input, args, stateName);
                break;
            case CHANNEL_GENERIC_RESPONSE_HANDLER:
                channelStateMachineListener.handleGenericResponse(context, input, args, stateName);
                break;
            case MESSAGE_DELIVERY_HANDLER:
                channelStateMachineListener.handleDelivery(context, input, args, stateName);
                break;
            case CONTENT_HEADER_HANDLER:
                channelStateMachineListener.handleContentHeader(context, input, args, stateName);
                break;
            case DEFAULT_BEHAVIOR_HANDLER:
                channelStateMachineListener.handleDefaultBehavior(context, input, args, stateName);
                break;
            case MESSAGE_BODY_HANDLER:
                channelStateMachineListener.handleMessageBody(context, input, args, stateName);
                break;
            case GENERIC_ERROR_HANDLER:
                channelStateMachineListener.handleGenericError(context, input, args, stateName);
                break;
            case CHANNEL_CLOSED_HANDLER:
                channelStateMachineListener.closeChannel(context, input, args, stateName);
                break;
            case DEFAULT:
                throw new IllegalStateException("Error in callChannelBehav");
        }
    }

    private String mapConstantsToFunctions(ClientChannelBehaviours behaviour) {
        switch (behaviour) {
            case HANDSHAKE_START_HANDLER:
                return "handshakeStartHandler";
            case DEFAULT_BEHAVIOR_HANDLER:
                return "defaultBehaviorHandler";
            case STARTING_HANDLER:
                return "startingHandler";
            case TUNE_CONNECTION_HANDLER:
                return "tuneConnectionHandler";
            case ADVANCE_ACTIONS_HANDLER:
                return "advanceActionsHandler";
            case REGISTER_SYNCHRONOUS_REQUEST:
                return "registerSynchronousRequest";
            case GENERIC_RESPONSE_HANDLER:
                return "genericResponseHandler";
            case IDLING_HANDLER:
                return "idlingHandler";
            case CLOSED_HANDLER:
                return "closedHandler";
            case DEFAULT:
                throw new IllegalStateException("Error in mapConstantsToFunctions");
        }
        return null;
    }
}

class State {
    public String stateName;
    public ClientChannelBehaviours entryBehavior;
    public ClientChannelBehaviours exitBehavior;
    public HashMap<String, String> rules = new HashMap<String, String>();

}


