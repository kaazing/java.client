/**
 * Copyright (c) 2007-2014, Kaazing Corporation. All rights reserved.
 */

package com.kaazing.net.ws.amqp;


/**
 * Convenience adapter for ConnectionListener
 */
public class ConnectionAdapter implements ConnectionListener {

    @Override
    public void onConnectionClose(ConnectionEvent e) {
    }

    @Override
    public void onConnectionOpen(ConnectionEvent e) {
    }

    @Override
    public void onConnecting(ConnectionEvent e) {
    }

    @Override
    public void onConnectionError(ConnectionEvent e) {
    }
}
