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
package org.kaazing.gateway.client.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.jmock.api.Action;
import org.jmock.api.Invocation;
import org.jmock.lib.action.CustomAction;
import org.kaazing.gateway.client.util.WrappedByteBuffer;


public class Expectations extends org.jmock.Expectations {

    private Map<String, Object>variables = new HashMap<String, Object>();

    public Object lookup(String variableName) {
        if (!variables.containsKey(variableName)) {
            return "<NOT-MATCHED>";
        }
        
        return variables.get(variableName);
    }

    public Action saveParameter(final String variableName) {
        return saveParameter(variableName, 0);
    }

    public Action saveParameter(final String variableName, final int parameterIndex) {
        return new CustomAction("save listener") {
            
            @Override
            public Object invoke(Invocation invocation) throws Throwable {
                int paramCount = invocation.getParameterCount();
                if (parameterIndex < 0 || parameterIndex >= paramCount) {
                    throw new IllegalArgumentException("Parameter index "+parameterIndex+" to save "+variableName+" is invalid: "+
                            "Should be in the range from 0-"+paramCount);
                }
                Object value = invocation.getParameter(parameterIndex);
                variables.put(variableName, value);
                return null;
            }
        };
    }

//    public Action open(final String description) {
//        return new OpenAction(description, "channelListener");
//    }
//    
//    class OpenAction extends CustomAction {
//        String variableName;
//        
//        private OpenAction(String description, String variableName) {
//            super(description);
//            this.variableName = variableName;
//        }
//
//        @Override
//        public Object invoke(Invocation invocation) throws Throwable {
//            GenericChannelListener channelListener = (GenericChannelListener)lookup(variableName);
//            channelListener.onOpen();
//            return null;
//        }
//    }
//
//    public Action receive(final String bytes, final String description) {
//        return new ReceiveAction(bytes, description, "channelListener");
//    }
//
//    private class ReceiveAction extends CustomAction {
//        String message; 
//        String variableName;
//
//        private ReceiveAction(String message, String description, String variableName) {
//            super(description);
//            this.message = message;
//            this.variableName = variableName;
//        }
//
//        @Override
//        public Object invoke(Invocation invocation) throws Throwable {
//            GenericChannelListener channelListener = (GenericChannelListener)lookup(variableName);
//            if (message.contains("$")) {
//                message = message.replace("$1", (String)lookup("$1"));
//                message = message.replace("$2", (String)lookup("$2"));
//                message = message.replace("$3", (String)lookup("$3"));
//                message = message.replace("$4", (String)lookup("$4"));
//            }
//            byte[] bytes = message.getBytes();
//            WrappedByteBuffer message = new WrappedByteBuffer(bytes);
//            channelListener.onMessage(message);
//            return null;
//        }
//    }
//
//    public Action close(final String description) {
//        return new CloseAction(description, "channelListener");
//    }
//    
//    class CloseAction extends CustomAction {
//        String variableName;
//        
//        private CloseAction(String description, String variableName) {
//            super(description);
//            this.variableName = variableName;
//        }
//
//        @Override
//        public Object invoke(Invocation invocation) throws Throwable {
//            GenericChannelListener channelListener = (GenericChannelListener)lookup(variableName);
//            channelListener.onClose();
//            return null;
//        }
//    }
//
//    public Action saveMatch(final String variableName, final int parameterIndex) {
//        return new CustomAction("save regexp match") {
//            
//            @Override
//            public Object invoke(Invocation invocation) throws Throwable {
//                // match variable saved by ByteBufferMatcher below
//                variables.put(variableName, lookup("$"+parameterIndex));
//                return null;
//            }
//        };
//    }

    public class ByteBufferMatcher extends BaseMatcher<WrappedByteBuffer> {
        Pattern pattern;
        Matcher matcher;
        String description;
        
        public ByteBufferMatcher(String regex, String description) {
            this.pattern = Pattern.compile(regex);
            this.description = description;
        }
        
        @Override
        public boolean matches(Object arg0) {
            WrappedByteBuffer buffer = (WrappedByteBuffer)arg0;
            byte bytes[] = buffer.array();
            String value = new String(bytes, buffer.position(), buffer.remaining());
            matcher = pattern.matcher(value);
            boolean matches = matcher.matches();
            if (matches) {
                for (int i=0; i<=matcher.groupCount(); i++) {
                    variables.put("$"+i, matcher.group(i));
                }
            }
            return matches;
        }

        @Override
        public void describeTo(Description desc) {
            desc.appendText(description);
        }
    }
}
