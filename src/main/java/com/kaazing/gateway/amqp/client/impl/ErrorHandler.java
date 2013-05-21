/**
 * Copyright (c) 2007-2012, Kaazing Corporation. All rights reserved.
 */

package com.kaazing.gateway.amqp.client.impl;

import com.kaazing.gateway.amqp.client.AmqpEvent;

/**
 * The ErrorHandler class represents a callback when an error occurs during an asynchronous action.  A Continuation callback
 * passed to an AMQP method is invoked to indicate that the action performed successfully.  When an error occurs while
 * performing the action, the error handler passed to the method is invoked instead.
 */
public interface ErrorHandler {
    
    /**
     * Invoked when an error occurs while performing the associated action.  
     * @param e
     */
    public void error(AmqpEvent e);
}
