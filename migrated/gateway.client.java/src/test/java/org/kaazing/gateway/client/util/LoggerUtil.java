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

package org.kaazing.gateway.client.util;

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class LoggerUtil {

    public static Logger getLogger() {
        return getLogger(Level.WARNING);
    }

    public static Logger getLogger(Level loggerLevel) {
        return getLogger(loggerLevel, "org.kaazing.gateway.client");
    }
    
    public static Logger getLogger(Level level, String loggerName) {
        Logger logger = Logger.getLogger(loggerName);
        logger.setLevel(level);
        logger.addHandler(new Handler() {
            @Override
            public void publish(LogRecord record) {
                String msg = record.getMillis()+": "+record.getSourceClassName()+"."+record.getSourceMethodName()+": "+record.getMessage();
                if (record.getParameters() != null) {
                    for (int i=0; i<record.getParameters().length; i++) {
                        if (i > 0) {
                            msg += ", ";
                        }
                        msg += record.getParameters()[i];
                    }
                }
                System.out.println(msg);
            }
            @Override
            public void flush() {}
            @Override
            public void close() throws SecurityException {
            }
        });
        
        return logger;
    }
}
