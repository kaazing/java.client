/**
 * Copyright (c) 2007-2014 Kaazing Corporation. All rights reserved.
 * 
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.kaazing.gateway.bridge;

import java.io.Serializable;
import java.util.logging.Logger;

class XoaEvent implements Serializable {
    private static final String CLASS_NAME = "org.kaazing.gateway.client.bridge.XoaEvent";
    private static final Logger LOG = Logger.getLogger(CLASS_NAME);

    private static final long serialVersionUID = 1L;
    public static final String[] EMPTY_ARGS = new String[] {};

    public static XoaEvent createErrorEvent(Integer id) {
        return new XoaEvent(id, XoaEvent.XoaEventKind.ERROR, EMPTY_ARGS);
    }

    public XoaEvent(Integer handlerId, XoaEventKind event, Object[] params) {
        LOG.entering(CLASS_NAME, "<init>", new Object[] { handlerId, event, params });
        this.handlerId = handlerId;
        this.event = event;
        this.params = params;
    }

    public Integer getHandlerId() {
        return handlerId;
    }

    public XoaEventKind getEvent() {
        return event;
    }

    public Object[] getParams() {
        return params;
    }

    public String toString() {
        String out = "EventID:" + getHandlerId() + "," + getEvent().name() + "[";
        for (int i = 0; i < params.length; i++) {
            out += params[i] + ",";
        }
        return out + "]";
    }

    private Integer handlerId;
    private XoaEventKind event;
    private Object[] params;

    public enum XoaEventKind {

        // Entries for WebSocket and ByteSocket events
        OPEN("open"), // handlerid
        MESSAGE("message"), // handlerid, message (string)
        CLOSED("closed"), // handlerid
        REDIRECT("redirect"), // handlerid, location
        AUTHENTICATE("authenticate"), // handlerid, challenge
        AUTHORIZE("authorize"), // handlerid, authroize

        // Entries for StreamingHttpRequest events
        LOAD("load"), // handlerid
        PROGRESS("progress"), // handlerid, ???
        READYSTATECHANGE("readystatechange"), // handlerid, state
        ERROR("error"), // handlerid
        ABORT("abort"), // handlerid

        // Entries for WebSocket and ByteSocket methods
        CREATE("create"), // handlerid, type, wsurl, originurl
        POSTMESSAGE("postMessage"), // handlerid, message (string)
        DISCONNECT("disconnect"), // handlerid

        // OPEN("open"), // handlerid //*** Also an event name
        SEND("send"), // handlerid
        GETRESPONSEHEADER("getResponseHeader"), // handlerid, header
        GEALLRESPONSEHEADERS("getAllResponseHeaders"), // handlerid
        SETREQUESTHEADER("setRequestHeader"), // handlerid, header, value
        // ABORT("abort"), // handlerid // *** also an event name
        UNDEFINED("");

        String name;

        XoaEventKind(String in) {
            name = in;
        }

        public static XoaEventKind getName(String in) {
            final XoaEventKind[] v = values();
            for (int i = 0; i < v.length; i++) {
                if (v[i].name.equals(in)) {
                    return v[i];
                }
            }
            return UNDEFINED;
        }

        public String toString() {
            return name;
        }
    }
}
