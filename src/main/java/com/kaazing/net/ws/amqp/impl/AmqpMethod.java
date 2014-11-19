/**
 * Copyright (c) 2007-2014, Kaazing Corporation. All rights reserved.
 */

package com.kaazing.net.ws.amqp.impl;

import java.util.List;

import com.kaazing.net.ws.amqp.impl.Parameter;

public final class AmqpMethod {
    
    public String          name;
    public short           classIndex;
    public short           index;
    public String          returnType;
    public Boolean         synchronous;
    public Boolean         hasContent;
    public List<Parameter> allParameters;
}
