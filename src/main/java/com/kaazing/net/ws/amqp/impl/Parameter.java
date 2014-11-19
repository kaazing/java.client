/**
 * Copyright (c) 2007-2014, Kaazing Corporation. All rights reserved.
 */

package com.kaazing.net.ws.amqp.impl;

public final class Parameter {
    public String name;
    public String type;

    public Parameter(String name, String type)
    {
        this.name = name;
        this.type = type;
    }
}
