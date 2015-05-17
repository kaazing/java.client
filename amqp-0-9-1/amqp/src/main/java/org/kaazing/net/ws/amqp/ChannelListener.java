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

import java.util.EventListener;

public interface ChannelListener extends EventListener {
    
    public void onOpen(ChannelEvent e);
    
    public void onClose(ChannelEvent e);
    
    public void onDeliver(ChannelEvent e);

    public void onError(ChannelEvent e);

    public void onDeclareExchange(ChannelEvent e);
    
    public void onDeclareQueue(ChannelEvent e);
    
    public void onConsumeBasic(ChannelEvent e);
    
    public void onFlow(ChannelEvent e);
    
    public void onDeleteExchange(ChannelEvent e);
    
    public void onDeleteQueue(ChannelEvent e);    
    
    public void onBindQueue(ChannelEvent e);
    
    public void onUnbind(ChannelEvent e);
    
    public void onPurgeQueue(ChannelEvent e);
    
    public void onQos(ChannelEvent e);
    
    public void onCancelBasic(ChannelEvent e);
    
    public void onGetBasic(ChannelEvent e);
    
    public void onGetEmpty(ChannelEvent e);

    public void onRecoverBasic(ChannelEvent e);
    
    public void onSelect(ChannelEvent e);
    
    public void onCommit(ChannelEvent e);
    
    public void onRollback(ChannelEvent e);
    
    public void onMessage(ChannelEvent e);
    
    public void onRejectBasic(ChannelEvent e);
}
