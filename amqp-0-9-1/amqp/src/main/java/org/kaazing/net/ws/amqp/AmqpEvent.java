/**
 * Copyright 2007-2015, Kaazing Corporation. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kaazing.net.ws.amqp;

import java.util.HashMap;

import org.kaazing.net.ws.amqp.impl.AmqpBuffer.Arg;

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
