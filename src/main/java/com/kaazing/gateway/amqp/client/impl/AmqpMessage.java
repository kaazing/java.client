/**
 * Copyright (c) 2007-2012, Kaazing Corporation. All rights reserved.
 */

package com.kaazing.gateway.amqp.client.impl;

import com.kaazing.gateway.client.html5.ByteBuffer;

public final class AmqpMessage {
    
    private ByteBuffer content;
    private String routingKey;
    private String deliveryTag;

    public AmqpMessage(ByteBuffer contents) {
        this.content = contents;
    }

    public ByteBuffer getContent() {
        return content;
    }

    public String getRoutingKey() {
        return routingKey;
    }

    public String getDeliveryTag() {
        return deliveryTag;
    }
    
}
