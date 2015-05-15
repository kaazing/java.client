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

package org.kaazing.net.ws.amqp;

import org.kaazing.net.ws.amqp.impl.AmqpBuffer.Arg;

public final class ConnectionEvent extends AmqpEvent {
    
    /**
     * Kind of event
     */
    public enum Kind {
        /**
         * The client is attempting to connect to the AMQP broker
         */
        CONNECTING,
        
        /**
         * The client connection to the AMQP broker has been opened
         */
        OPEN,
        
        /**
         * The client connection to the AMQP broker has been closed
         */
        CLOSE,
        
        /**
         * The client has encountered an error with the connection.
         */
        ERROR
    }

    private AmqpClient client;
    private Kind kind;
    private String errorMessage;

    public ConnectionEvent(AmqpClient client, Kind kind) {
        this(client, kind, null);
    }

    public ConnectionEvent(AmqpClient client, Kind kind, Arg[] args) {
        super(args);
        this.client = client;
        this.kind = kind;
        
        if (kind == Kind.ERROR) {
        	errorMessage = (String) super.getArgument("replyText");
        }
    }

    /**
     * Returns the AMQPClient associated with the event
     * @return AmqpClient client
     */
    public AmqpClient getClient() {
        return client;
    }

    /**
     * Returns the kind of event
     * @return Kind kind
     */
    public Kind getKind() {
        return kind;
    }
    
    /**
     * Returns the error message when it's an Kind.ERROR event.
     * @return
     */
    public String getMessage() {
    	return errorMessage;
    }
}
