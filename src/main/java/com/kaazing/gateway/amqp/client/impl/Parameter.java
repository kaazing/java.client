/**
 * Copyright (c) 2007-2012, Kaazing Corporation. All rights reserved.
 */

package com.kaazing.gateway.amqp.client.impl;

public final class Parameter {
    public String name;
    public String type;

    public Parameter(String name, String type)
    {
        this.name = name;
        this.type = type;
    }
}
