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
package org.kaazing.net.sse.impl.url;

import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableList;

import java.net.URLStreamHandler;
import java.util.Collection;

import org.kaazing.net.URLStreamHandlerFactorySpi;

public class SseURLStreamHandlerFactorySpiImpl extends URLStreamHandlerFactorySpi {
    private static final Collection<String> _supportedProtocols = unmodifiableList(asList("sse"));

    @Override
    public URLStreamHandler createURLStreamHandler(String protocol) {
        if (!_supportedProtocols.contains(protocol)) {
            String s = String.format("Protocol not supported '%s'", protocol);
            throw new IllegalArgumentException(s);
        }
 
        return new SseURLStreamHandlerImpl(protocol);
    }

    @Override
    public Collection<String> getSupportedProtocols() {
        return _supportedProtocols;
    }

}
