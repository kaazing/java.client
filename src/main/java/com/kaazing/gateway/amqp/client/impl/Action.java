/**
 * Copyright (c) 2007-2012, Kaazing Corporation. All rights reserved.
 */

package com.kaazing.gateway.amqp.client.impl;


public final class Action {
     public String          actionName;
     public String          func;
     public Object[]        args;
     public Continuation    continuation;
     public ErrorHandler    error;
}
