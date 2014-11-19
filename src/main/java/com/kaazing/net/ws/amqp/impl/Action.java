/**
 * Copyright (c) 2007-2014, Kaazing Corporation. All rights reserved.
 */

package com.kaazing.net.ws.amqp.impl;


public final class Action {
     public String          actionName;
     public String          func;
     public Object[]        args;
     public Continuation    continuation;
     public ErrorHandler    error;
}
