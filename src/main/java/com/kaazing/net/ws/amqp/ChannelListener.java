/**
 * Copyright (c) 2007-2014, Kaazing Corporation. All rights reserved.
 */

package com.kaazing.net.ws.amqp;

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
