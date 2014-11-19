/**
 * Copyright (c) 2007-2014, Kaazing Corporation. All rights reserved.
 */

package com.kaazing.net.ws.amqp;

import java.util.EventListener;

/**
 * Listener for AMQP messages from the server
 */
public interface ConnectionListener extends EventListener {
    
    /**
     * Invoked when AMQP WebSocket Connection is opened. 
     * 
     * @param e  ConnectionEvent payload
     */
    public void onConnectionOpen(ConnectionEvent e);

    /**
     * Invoked when AMQP WebSocket Connection is attempted. 
     * 
     * @param e  ConnectionEvent payload
     */
    public void onConnecting(ConnectionEvent e);
    
    /**
     * Invoked when AMQP WebSocket Connection is closed.
     *  
     * @param e  ConnectionEvent payload
     */
    public void onConnectionClose(ConnectionEvent e);

    /**
     * Invoked when an error has been encountered. This will
     * be followed by onConnectionClose() as all errors in AMQP
     * are hard-errors which mean that the connection has to
     * be dropped.
     * 
     * @param e  ConnectionEvent payload
     */
    public void onConnectionError(ConnectionEvent e);
}
