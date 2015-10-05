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
package org.kaazing.gateway.client.impl.util;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.kaazing.gateway.client.util.StringUtils;

public class StringUtilsTest {
    @Test
    public void testStripCRLF() {
        String lineSep = System.getProperty("line.separator");
        String testStringWithLineSep = "foo" + lineSep + "bar";
        String testString = "abc\n\rdef";
        String anotherString = "abc\"def&hij<klm>";
        assertTrue(testString.indexOf('\n') > 0);
        assertTrue(testString.indexOf('\r') > 0);

        String processedString = StringUtils.stripControlCharacters(testString);
        String processedTest2 = StringUtils.stripControlCharacters(testStringWithLineSep);
        String processedAnotherString = StringUtils.stripControlCharacters(anotherString);
        assertTrue(processedString.indexOf('\n') == -1);
        assertTrue(processedString.indexOf('\r') == -1);
        assertTrue(processedTest2.indexOf(lineSep) == -1);
        assertTrue(processedAnotherString.indexOf('"') == -1);
        assertTrue(processedAnotherString.indexOf('<') == -1);
        assertTrue(processedAnotherString.indexOf('>') == -1);
    }
}