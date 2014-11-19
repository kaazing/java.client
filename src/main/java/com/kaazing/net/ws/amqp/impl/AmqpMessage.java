/**
 * Copyright (c) 2007-2014, Kaazing Corporation. All rights reserved.
 */

package com.kaazing.net.ws.amqp.impl;

import com.kaazing.gateway.client.common.util.WrappedByteBuffer;


public final class AmqpMessage {
    
    private WrappedByteBuffer content;
    private String routingKey;
    private String deliveryTag;

    public AmqpMessage(WrappedByteBuffer contents) {
        this.content = contents;
    }

    public WrappedByteBuffer getContent() {
        return content;
    }

    public String getRoutingKey() {
        return routingKey;
    }

    public String getDeliveryTag() {
        return deliveryTag;
    }
    
}
