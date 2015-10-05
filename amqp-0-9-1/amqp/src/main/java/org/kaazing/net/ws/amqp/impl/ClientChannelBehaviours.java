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

public enum ClientChannelBehaviours {
    // Client Handlers
    HANDSHAKE_START_HANDLER, 
    DEFAULT_BEHAVIOR_HANDLER,
    STARTING_HANDLER, 
    TUNE_CONNECTION_HANDLER, 
    ADVANCE_ACTIONS_HANDLER, 
    REGISTER_SYNCHRONOUS_REQUEST, 
    GENERIC_RESPONSE_HANDLER, 
    IDLING_HANDLER, 
    CLOSED_HANDLER, 
    DEFAULT,

    // Channel Handlers
    ADVANCE_ACTIONS_CHANNEL_HANDLER, 
    TX_CHECKING_HANDLER, 
    CHANNEL_REGISTER_SYNCHRONOUS_REQUEST, 
    GET_EMPTY_RESPONSE_HANDLER,
    CHANNEL_GENERIC_RESPONSE_HANDLER, 
    MESSAGE_DELIVERY_HANDLER,
    CONTENT_HEADER_HANDLER, 
    MESSAGE_BODY_HANDLER, 
    GENERIC_ERROR_HANDLER, 
    CHANNEL_CLOSED_HANDLER
}
