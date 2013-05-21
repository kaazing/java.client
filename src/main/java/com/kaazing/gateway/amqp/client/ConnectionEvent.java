/**
 * Copyright (c) 2007-2012, Kaazing Corporation. All rights reserved.
 */

package com.kaazing.gateway.amqp.client;

import com.kaazing.gateway.amqp.client.impl.AmqpBuffer.Arg;

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
