/**
 * Copyright (c) 2007-2014, Kaazing Corporation. All rights reserved.
 */

package com.kaazing.net.ws.amqp.impl;

public final class AmqpClientException extends Exception{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    /**
     * AMQP client exception
     * 
     * @param message
     */
    public AmqpClientException(String message) {
        super(message);
    }
    
    /**
     * AMQP client exception
     * 
     * @param message 
     * @param e nested exception
     */
    public AmqpClientException(String message, Exception e) {
        super(message, e);
    }

}
