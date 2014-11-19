/**
 * Copyright (c) 2007-2010, Kaazing Corporation. All rights reserved.
 */

/**
 * Creates a new AmqpClient instance.
 *
 * AmqpClient provides a socket-based Java client API to communicate
 *         with any compatible AMQP server
 */


public class AmqpClient {
        
$amqp.constants:{constant|
    /**
     * $constant.doc$
     */
    public static int $constant.name$ = $constant.value$;
        }$ 

$amqp.classes:{clazz|
/**
 * $clazz.doc$
 */
public static class $clazz.name$() {
    $clazz.methods:{method|$methodjava(m=method)$}$
}}$

};
