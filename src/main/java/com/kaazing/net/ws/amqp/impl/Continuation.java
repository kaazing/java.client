/**
 * Copyright (c) 2007-2014, Kaazing Corporation. All rights reserved.
 */

package com.kaazing.net.ws.amqp.impl;

import com.kaazing.net.ws.amqp.AmqpEvent;

/**
 * The Continuation class represents a callback when an asynchronous action is completed.  The callback passed
 * to an AMQP method is invoked to indicate that the action performed successfully.  When an error occurs while
 * performing the action, the error handler passed to the method is invoked instead.
 */
public interface Continuation {

    /**
     * Invoked when the associated action has been performed successfully.  
     * @param e
     */
    public void onCompleted(AmqpEvent e);
    
}
