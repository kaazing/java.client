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
package org.kaazing.net.ws.amqp;

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
