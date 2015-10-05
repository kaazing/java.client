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
package org.kaazing.net.ws.amqp.impl;

class AmqpConstants {
    public static final  short FRAME_METHOD = 1;
    public static final  short FRAME_HEADER = 2;
    public static final  short FRAME_BODY = 3;
    public static final  short FRAME_HEARTBEAT = 4;
    public static final  short FRAME_RABBITMQ_HEARTBEAT = 8;
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
