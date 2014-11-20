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

import org.kaazing.net.ws.WebSocketFactory;

/**
 * AmqClientFactory is used to create instances of {@link AmqpClient}. Also, it 
 * is used to access the {@link WebSocketFactory} used to create the underlying
 * {@link WebSocket} connection over which AMQP 0-9-1 protocol will be run.
 * The {@link WebSocketFactory} can be used to set connection-timeout, 
 * challenge handlers, redirect policy, etc. These properties/characteristics 
 * set on the {@link WebSocketFactory} will be inherited by the 
 * {@link WebSocket} instances that are created using the factory.
 */
public class AmqpClientFactory {
	private WebSocketFactory    wsFactory;

	private AmqpClientFactory() {
		wsFactory = WebSocketFactory.createWebSocketFactory();
	}
	
	/**
	 * Creates and returns a new instance of AmqpClientFactory every time the
	 * method is invoked.
	 * 
	 * @return AmqpClientFactory instance
	 */
	public static AmqpClientFactory createAmqpClientFactory() {
		return new AmqpClientFactory();
	}
	
	/**
	 * Creates and returns a new instance of {@link AmqpClient} class every time
	 * this method is invoked.
	 * 
	 * @return instance of {@link AmqpClient}
	 */
	public AmqpClient createAmqpClient() {
		return new AmqpClient(this);
	}

	/**
	 * Returns the {@link WebSocketFactory} instance that will be eventually
	 * used to create a {@link WebSocket} connection to run AMQP 0-9-1 
	 * protocol.
	 * 
	 * @return instance of {@link WebSocketFactory}
	 */
	public WebSocketFactory getWebSocketFactory() {
		return wsFactory;
	}

	/**
	 * Sets the {@link WebSocketFactory} to be used to create the underlying
	 * {@link WebSocket} connection to run AMQP 0-9-1 protocol.
	 * 
	 * @param wsFactory    instance of WebSocketFactory
	 */
	public void setWebSocketFactory(WebSocketFactory wsFactory) {
		this.wsFactory = wsFactory;
	}
	
}
