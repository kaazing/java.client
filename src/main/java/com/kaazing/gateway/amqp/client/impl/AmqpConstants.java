/**
 * Copyright (c) 2007-2012, Kaazing Corporation. All rights reserved.
 */

package com.kaazing.gateway.amqp.client.impl;

class AmqpConstants {
    public static final  short FRAME_METHOD = 1;
    public static final  short FRAME_HEADER = 2;
    public static final  short FRAME_BODY = 3;
    public static final  short FRAME_HEARTBEAT = 8;
    public static final  short FRAME_MIN_SIZE = 4096;
    public static final  short FRAME_END = 206;
    public static final  short REPLY_SUCCESS = 200;
    public static final  short CONTENT_TOO_LARGE = 311;
    public static final  short NO_CONSUMERS = 313;
    public static final  short CONNECTION_FORCED = 320;
    public static final  short INVALID_PATH = 402;
    public static final  short ACCESS_REFUSED = 403;
    public static final  short NOT_FOUND = 404;
    public static final  short RESOURCE_LOCKED = 405;
    public static final  short PRECONDITION_FAILED = 406;
    public static final  short FRAME_ERROR = 501;
    public static final  short SYNTAX_ERROR = 502;
    public static final  short COMMAND_INVALID = 503;
    public static final  short CHANNEL_ERROR = 504;
    public static final  short UNEXPECTED_FRAME = 505;
    public static final  short RESOURCE_ERROR = 506;
    public static final  short NOT_ALLOWED = 530;
    public static final  short NOT_IMPLEMENTED = 540;
    public static final  short INTERNAL_ERROR = 541;
}
