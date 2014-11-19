/**
 * Copyright (c) 2007-2014, Kaazing Corporation. All rights reserved.
 */

package com.kaazing.net.ws.amqp;

import java.util.HashMap;

import com.kaazing.net.ws.amqp.impl.AmqpBuffer.Arg;

public abstract class AmqpEvent {
 
    private HashMap<String, Object> arguments = null;
    
    /**
     * Fetches the value of the named argument from the AMQP protocol frame causing the event.
     * 
     * @param name The name of the argument.
     * @return Object The value of the argument, or null if not found.
     */
    public Object getArgument(String name) {
        return arguments.get(name);
    }

    /**
     * Creates a new AmqpEvent with the arguments specified.
     * 
     * @param args
     */
    AmqpEvent(Arg[] args) {
        arguments = new HashMap<String, Object>();
        if (args != null) {
            for (int i = 0; i < args.length; i++) {
                arguments.put(args[i].name, args[i].value);
            }
        }
    }
}
