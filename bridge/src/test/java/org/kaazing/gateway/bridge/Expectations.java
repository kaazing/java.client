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
package org.kaazing.gateway.bridge;

import java.util.HashMap;
import java.util.Map;

import org.jmock.api.Action;
import org.jmock.api.Invocation;
import org.jmock.lib.action.CustomAction;

public class Expectations extends org.jmock.Expectations {

    private Map<String, Object>variables = new HashMap<String, Object>();

    Action doNothing = new CustomAction("do nothing") {
        @Override
        public Object invoke(Invocation invocation) throws Throwable {
            return null;
        }
    };

    public Object lookup(String variableName) {
        if (!variables.containsKey(variableName)) {
            throw new IllegalArgumentException("lookup of "+variableName+" not found");
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
}
