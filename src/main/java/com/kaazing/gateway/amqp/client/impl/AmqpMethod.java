/**
 * Copyright (c) 2007-2012, Kaazing Corporation. All rights reserved.
 */

package com.kaazing.gateway.amqp.client.impl;

import java.util.List;

import com.kaazing.gateway.amqp.client.impl.Parameter;

public final class AmqpMethod {
    
    public String          name;
    public short           classIndex;
    public short           index;
    public String          returnType;
    public Boolean         synchronous;
    public Boolean         hasContent;
    public List<Parameter> allParameters;
}
