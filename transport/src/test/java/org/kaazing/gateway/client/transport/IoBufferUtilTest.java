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
package org.kaazing.gateway.client.transport;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.nio.ByteBuffer;

import org.junit.Test;

import org.kaazing.gateway.client.transport.IoBufferUtil;

public class IoBufferUtilTest {

    @Test
    public void testIoBufferUtilCanAccomodate() {
        ByteBuffer buf = ByteBuffer.allocate(10);
        buf.put("012345".getBytes());
        assertTrue(IoBufferUtil.canAccomodate(buf, 4));
        assertFalse(IoBufferUtil.canAccomodate(buf, 5));
        buf.put("6789".getBytes());
    }

    @Test
    public void testIoBufferUtilExpandRetainSize() {
        ByteBuffer buf = ByteBuffer.allocate(10);
        buf.put("012345".getBytes());
        ByteBuffer newBuf = IoBufferUtil.expandBuffer(buf, 4);
        assertEquals(buf, newBuf);
        assertEquals(buf.position(), 6);
        assertEquals(buf.position(), newBuf.position());
    }

    @Test
    public void testIoBufferUtilExpand() {
        ByteBuffer buf = ByteBuffer.allocate(10);
        buf.put("12345".getBytes());
        ByteBuffer newBuf = IoBufferUtil.expandBuffer(buf, 10);
        assertEquals(newBuf.capacity(), 20);
        newBuf.put("abcdefghij".getBytes());
        newBuf.flip();
        
        assertEquals(newBuf.remaining(), 15);
        assertEquals(newBuf.get(0), (byte)0x31);
        assertEquals(newBuf.get(5), (byte)0x61);
        assertEquals(newBuf.get(14), (byte)0x6A);
    }
}
