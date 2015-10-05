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

import static org.junit.Assert.assertEquals;

import java.nio.BufferOverflowException;
import java.nio.BufferUnderflowException;
import java.nio.charset.Charset;

import org.junit.Test;
import org.kaazing.gateway.client.util.WrappedByteBuffer;


public class ByteBufferTest {

    @Test
    public void testByteBuffer() {
        /* Invariant: 0 <= mark <= position <= limit <= capacity */
        WrappedByteBuffer buf = new WrappedByteBuffer();
        //assertEquals(0, buf.mark());
        assertEquals(0, buf.position());
        assertEquals(128, buf.limit());
        assertEquals(128, buf.capacity());
    }

    @Test
    public void testByteBufferAllocate() {
        /* Invariant: 0 <= mark <= position <= limit <= capacity */
        WrappedByteBuffer buf = WrappedByteBuffer.allocate(0);
        //assertEquals(0, buf.mark());
        assertEquals(0, buf.position());
        assertEquals(0, buf.limit());
        assertEquals(0, buf.capacity());
    }

    @Test
    public void testGetAndSetBytes() throws Exception {
        WrappedByteBuffer buf = WrappedByteBuffer.allocate(2);
        buf.put((byte)1);
        buf.put((byte)2);
        buf.flip();
        assertEquals((byte)1, buf.get());
        assertEquals((byte)2, buf.get());
        buf.flip();
        assertEquals((byte)1, buf.getAt(0));
        assertEquals((byte)2, buf.getAt(1));
        buf.flip();
    }

    @Test
    public void testGetAndSetShorts() throws Exception {
        //tests reallocation of buffer automatically.
        WrappedByteBuffer buf = WrappedByteBuffer.allocate(2);
        buf.putShort((short) 1);
        buf.putShort((short) 2);
        buf.putShort((short) 3);
        buf.putShort((short) 4);
        buf.putShort((short) 5);
        buf.flip();
        assertEquals((short)1, buf.getShort());
        assertEquals((short)1, buf.getShortAt(0));
        assertEquals((short)2, buf.getShortAt(2));
        assertEquals((short)3, buf.getShortAt(4));
        assertEquals((short)2, buf.getShort());
        assertEquals((short)3, buf.getShort());
        assertEquals((short)4, buf.getShort());
        assertEquals((short)5, buf.getShort());
    }

    @Test
    public void testGetAndSetVarious() throws Exception {
        //tests reallocation of buffer automatically.
        WrappedByteBuffer buf = WrappedByteBuffer.allocate(2);
        buf.put((byte) 1);
        buf.putShort((short) 2);
        buf.putInt(3);
        buf.putLong((long) 4);
        String test = "this";
        buf.putString(test, Charset.defaultCharset());
        buf.put((byte)0);
        buf.putPrefixedString(1, "abcde", Charset.defaultCharset());
        buf.flip();
        assertEquals((byte)1, buf.get());
        assertEquals((short)2, buf.getShort());
        assertEquals(3, buf.getInt());
        assertEquals((long)4, buf.getLong());
        assertEquals(test, buf.getString(Charset.defaultCharset()));
        assertEquals("abcde", buf.getPrefixedString(1,Charset.defaultCharset()));
    }

    @Test
    public void testGetAndSetPrefixStringsInUTF16() throws Exception {
        //tests reallocation of buffer automatically.
        WrappedByteBuffer buf = WrappedByteBuffer.allocate(2);
        Charset cs = Charset.forName("UTF-16");
        String test = "this";
        String test1 = "is";
        String test2 = "a";
        String test3 = "story";
        buf.putPrefixedString(2, test, cs);
        buf.putPrefixedString(2, test1, cs);
        buf.putPrefixedString(2, test2, cs);
        buf.putPrefixedString(2, test3, cs);
        buf.put((byte)0);
        buf.putInt(100);
        buf.flip();
        assertEquals(test, buf.getPrefixedString(2,cs));
        assertEquals(test1, buf.getPrefixedString(2,cs));
        assertEquals(test2, buf.getPrefixedString(2,cs));
        assertEquals(test3, buf.getPrefixedString(2,cs));
        assertEquals((byte)0, buf.get());
        assertEquals((int)100, buf.getInt());
    }

    @Test
    public void testGetAndSetStringsInUTF8() throws Exception {
        //tests reallocation of buffer automatically.
        WrappedByteBuffer buf = WrappedByteBuffer.allocate(2);
        String test = "this";
        String test1 = "is";
        String test2 = "a";
        String test3 = "story";
        Charset cs = Charset.forName("UTF-8");
        buf.putString(test, cs);
        buf.put((byte)0);
        buf.putPrefixedString(2, "abcde", cs);
        buf.putPrefixedString(2, test1, cs);
        buf.putPrefixedString(2, test2, cs);
        buf.putPrefixedString(2, test3, cs);
        buf.flip();
        assertEquals(test, buf.getString(cs));
        assertEquals("abcde", buf.getPrefixedString(2,cs));
        assertEquals(test1, buf.getPrefixedString(2,cs));
        assertEquals(test2, buf.getPrefixedString(2,cs));
        assertEquals(test3, buf.getPrefixedString(2,cs));
    }
    
    @Test
    public void testGetAndSetStringsInASCII() throws Exception {
        //tests reallocation of buffer automatically.
        WrappedByteBuffer buf = WrappedByteBuffer.allocate(2);
        String test = "this";
        String test1 = "is";
        String test2 = "a";
        String test3 = "story";
        Charset cs = Charset.forName("ASCII");
        buf.putString(test, cs);
        buf.put((byte)0);
        buf.putPrefixedString(2, "abcde", cs);
        buf.putPrefixedString(2, test1, cs);
        buf.putPrefixedString(2, test2, cs);
        buf.putPrefixedString(2, test3, cs);
        buf.flip();
        assertEquals(test, buf.getString(cs));
        assertEquals("abcde", buf.getPrefixedString(2,cs));
        assertEquals(test1, buf.getPrefixedString(2,cs));
        assertEquals(test2, buf.getPrefixedString(2,cs));
        assertEquals(test3, buf.getPrefixedString(2,cs));
    }
    
    @Test
    public void testHexDump() {
        // TODO: should test actual HEX values, 1 and 2 digits
        WrappedByteBuffer buf = new WrappedByteBuffer();
        buf.put((byte)1); 
        buf.put((byte)2);
        buf.put((byte)3);
        buf.flip();
        assertEquals("1 2 3 ", buf.getHexDump());
    }

    @Test
    public void testBuffers() throws Exception {
        //tests reallocation of buffer automatically.
        String test1 = "Test1";
        String test2 = "Test2";
        WrappedByteBuffer buf1 = WrappedByteBuffer.wrap(test1.getBytes());
        WrappedByteBuffer buf2 = WrappedByteBuffer.wrap(test2.getBytes());
        buf1.get();
        buf1.get();

        int p = buf1.position();
        buf1.skip(buf1.remaining());
        buf1.putBuffer(buf2);
        buf1.flip();
        buf1.position(p);
        buf1 = buf1.slice();

        // TODO: should test actual contents of these buffers
        assertEquals(8, buf1.remaining());
    }    

    /*
     * byte array tests
     */
    @Test
    public void testGetAndSetByteArray() throws Exception{
        // TODO: test should include NULL, MIN and MAX bytes
        WrappedByteBuffer buf = new WrappedByteBuffer();
        byte b1[] = new byte[6];
        b1[0] = (byte)1;
        b1[1] = (byte)-1;
        b1[2] = (byte)-1;
        b1[3] = (byte)4;
        b1[4] = (byte)1;
        b1[5] = (byte)-2;
        buf.putBytes(b1); 
        buf.flip();

        byte res[] = buf.getBytes(2);
        byte res1[] = buf.getBytesAt(4,2);
        assertEquals((byte)1, res[0]);
        assertEquals((byte)-1, res[1]);
        assertEquals((byte)1, res1[0]); 
        assertEquals((byte)-2, res1[1]);
        
        byte[] b2 = new byte[2];
        b2[0] = (byte)121;
        b2[1] = (byte)122;
        buf.putBytesAt(0,b2);
        buf.flip();
        res1 = buf.getBytesAt(0,2);
        assertEquals((byte)121, res1[0]); 
        assertEquals((byte)122, res1[1]);
    }

    @Test
    public void testGetAndSetPut() throws Exception {
        WrappedByteBuffer buf = new WrappedByteBuffer();
        byte b1= (byte) -128;
        byte b2= (byte) 127;
        byte b3= (byte) 128;
        byte b4= (byte) 12;
        byte b5= (byte) 13;
        buf.put(b1);
        buf.put(b2);
        buf.put(b3);
        buf.flip();

        assertEquals(b1, buf.get());
        assertEquals(b2, buf.get());
        assertEquals(b3, buf.get());

        buf.putAt(0,b4);
        buf.putAt(1,b5);
        assertEquals(b4, buf.getAt(0));
        assertEquals(b5, buf.getAt(1));
    }

    @Test (expected=BufferOverflowException.class)
    public void testGetAndSetPutException() {
        WrappedByteBuffer buf = WrappedByteBuffer.allocate(0);
        buf.setAutoExpand(false);
        buf.put((byte)121);
    }

    @Test (expected=IndexOutOfBoundsException.class)
    public void testGetAndSetPutAtException2() {
        WrappedByteBuffer buf = new WrappedByteBuffer();
        buf.putAt(129, (byte)121);
    }
    
    @Test
    public void testGetAndSetUnsigned() throws Exception {
        WrappedByteBuffer buf = new WrappedByteBuffer();
        buf.putUnsigned(0);
        buf.putUnsigned(255);
        buf.putUnsigned(256);
        buf.putUnsigned(-1);
        buf.flip();

        assertEquals(buf.position(), 0);
        assertEquals(buf.limit(), 4);

        assertEquals(0, buf.getUnsigned());
        assertEquals(255, buf.getUnsigned()); 
        assertEquals(0, buf.getUnsigned());
        assertEquals(255, buf.getUnsigned());

        assertEquals(buf.position(), 4);

        buf.putUnsignedAt(0, 121);
        buf.putUnsignedAt(1, 122);
        assertEquals(121, buf.getUnsignedAt(0));
        assertEquals(122, buf.getUnsignedAt(1)); 
        
        assertEquals(buf.position(), 4);
    }

    @Test (expected=BufferOverflowException.class)
    public void testGetAndSetPutUnsignedException() {
        WrappedByteBuffer buf = WrappedByteBuffer.allocate(0);
        buf.setAutoExpand(false);
        buf.putUnsigned(121);
    }

    @Test (expected=IndexOutOfBoundsException.class)
    public void testGetAndSetPutUnsignedAtException2() {
        WrappedByteBuffer buf = new WrappedByteBuffer();
        buf.putUnsignedAt(129, 121);
    }
    
    /*
     * Short Tests
     */
    @Test
    public void testGetAndSetShort() throws Exception{
        WrappedByteBuffer buf = new WrappedByteBuffer();
        buf.putShort((short)0);
        buf.putShort((short)1);
        buf.putShort((short)32767);
        buf.putShort((short)-32768);
        buf.flip();

        assertEquals(buf.position(), 0);
        assertEquals(buf.limit(), 8);

        assertEquals((short)0, buf.getShort());
        assertEquals((short)1, buf.getShort());
        assertEquals((short)32767, buf.getShort());
        assertEquals((short)-32768, buf.getShort());

        assertEquals(buf.position(), 8);

        buf.putShortAt(0, (short)121);
        buf.putShortAt(2, (short)1000);
        assertEquals((short)121, buf.getShortAt(0));
        assertEquals((short)1000, buf.getShortAt(2));
        
        assertEquals((int)buf.position(), 8);
    }

    @Test (expected=BufferOverflowException.class)
    public void testGetAndSetPutShortException() {
        WrappedByteBuffer buf = WrappedByteBuffer.allocate(0);
        buf.setAutoExpand(false);
        buf.putShort((short)121);
    }

    @Test (expected=IndexOutOfBoundsException.class)
    public void testGetAndSetPutShortAtException2() {
        WrappedByteBuffer buf = new WrappedByteBuffer();
        buf.putShortAt(129, (short)121);
    }

    @Test (expected=BufferUnderflowException.class)
    public void testGetAndSetGetShortException3() {
        WrappedByteBuffer buf = WrappedByteBuffer.allocate(0);
        buf.getShort();
    }

    @Test (expected=IndexOutOfBoundsException.class)
    public void testGetAndSetGetShortAtException4() {
        WrappedByteBuffer buf = new WrappedByteBuffer();
        buf.getShortAt(128);
    }
    
    @Test
    public void testGetAndSetUnsignedShort() throws Exception{
        WrappedByteBuffer buf = new WrappedByteBuffer();
        buf.putUnsignedShort(0);
        buf.putUnsignedShort(65535);
        buf.putUnsignedShort(65536);
        buf.putUnsignedShort(-1);

        buf.flip();
        assertEquals(0, buf.getUnsignedShort());
        assertEquals(65535, buf.getUnsignedShort());
        assertEquals(0, buf.getUnsignedShort());
        assertEquals(65535, buf.getUnsignedShort());

        buf.rewind();
        buf.putUnsignedShortAt(0,121);
        buf.putUnsignedShortAt(2,122);
        assertEquals(121, buf.getUnsignedShortAt(0));
        assertEquals(122, buf.getUnsignedShortAt(2));
    }

    @Test (expected=BufferOverflowException.class)
    public void testGetAndSetPutUnsignedShortException() throws Exception {
        WrappedByteBuffer buf = WrappedByteBuffer.allocate(0);
        buf.setAutoExpand(false);
        buf.putUnsignedShort(121);
    }

    @Test (expected=IndexOutOfBoundsException.class)
    public void testGetAndSetPutUnsignedShortAtException2() {
        WrappedByteBuffer buf = new WrappedByteBuffer();
        buf.putUnsignedShortAt(129, 121);
    }

    @Test (expected=BufferUnderflowException.class)
    public void testGetAndSetGetUnsignedShortException3() {
        WrappedByteBuffer buf = WrappedByteBuffer.allocate(0);
        buf.getUnsignedShort();
    }

    @Test (expected=IndexOutOfBoundsException.class)
    public void testGetAndSetGetUnsignedShortAtException4() {
        WrappedByteBuffer buf = new WrappedByteBuffer();
        buf.getUnsignedShortAt(128);
    }
    
    /*
     * Int tests
     */
    @Test
    public void testGetAndSetInt() throws Exception{
        // cant test for numbers outside range as putint accepts int and 
        // hence java doesnt allow values outside range -2147483648 to 2147483647 to be set to valriables
        // hence no test case for assertFalse for numbers outside -2147483648 to 2147483647
        WrappedByteBuffer buf = new WrappedByteBuffer();
        int b1= -2147483648  ;
        int b2= 2147483647 ;
        int b3= 32768;
        int b4= 128;
        int b5= 128;
        buf.putInt(b1);
        buf.putInt(b2);
        buf.putInt(b3);
        
        buf.flip();
        assertEquals(b1, buf.getInt());
        assertEquals(b2, buf.getInt());
        buf.rewind();
        buf.putIntAt(0,b4);
        buf.putIntAt(4,b5);
        assertEquals(b4, buf.getIntAt(0));
        assertEquals(b5, buf.getIntAt(4));
    }

    @Test (expected=BufferOverflowException.class)
    public void testGetAndSetPutIntException() {
        WrappedByteBuffer buf = WrappedByteBuffer.allocate(0);
        buf.setAutoExpand(false);
        buf.putInt(121);
    }

    @Test (expected=IndexOutOfBoundsException.class)
    public void testGetAndSetPutIntAtException2() {
        WrappedByteBuffer buf = new WrappedByteBuffer();
        buf.putIntAt(129, 121);
    }

    @Test (expected=BufferUnderflowException.class)
    public void testGetAndSetGetIntException3() {
        WrappedByteBuffer buf = WrappedByteBuffer.allocate(0);
        buf.getInt();
    }

    @Test (expected=IndexOutOfBoundsException.class)
    public void testGetAndSetGetIntAtException4() {
        WrappedByteBuffer buf = new WrappedByteBuffer();
        buf.getIntAt(128);
    }
    
    @Test
    public void testGetAndSetUnsignedInt() throws Exception{
        WrappedByteBuffer buf = new WrappedByteBuffer();
        long MAX_UNSIGNED_INT = 4294967295L;

        buf.putUnsignedInt(MAX_UNSIGNED_INT);
        buf.putUnsignedInt(1);
        buf.putUnsignedInt(0);
        buf.putUnsignedInt(-1);
        buf.putUnsignedInt(MAX_UNSIGNED_INT+1);

        assertEquals(buf.position(), 20);

        buf.flip();

        assertEquals(buf.position(), 0);
        assertEquals(buf.limit(), 20);

        assertEquals("test 1", MAX_UNSIGNED_INT, buf.getUnsignedInt());
        assertEquals("test 2", 1L, buf.getUnsignedInt());
        assertEquals("test 3", 0L, buf.getUnsignedInt());
        assertEquals("test 4", MAX_UNSIGNED_INT, buf.getUnsignedInt());
        assertEquals("test 5", 0L, buf.getUnsignedInt());

        assertEquals(buf.position(), 20);

        buf.putUnsignedIntAt(0,121);
        buf.putUnsignedIntAt(4,4000000000L);
        assertEquals(121L, (long)buf.getUnsignedIntAt(0));
        assertEquals(4000000000L, (long)buf.getUnsignedIntAt(4));

        assertEquals(buf.position(), 20);
    }

    @Test (expected=BufferOverflowException.class)
    public void testGetAndSetPutUnsignedIntException() throws Exception {
        WrappedByteBuffer buf = WrappedByteBuffer.allocate(0);
        buf.setAutoExpand(false);
        buf.putUnsignedInt(121);
    }

    @Test (expected=IndexOutOfBoundsException.class)
    public void testGetAndSetPutUnsignedIntAtException2() {
        WrappedByteBuffer buf = new WrappedByteBuffer();
        buf.putUnsignedIntAt(129, 121);
    }

    @Test (expected=BufferUnderflowException.class)
    public void testGetAndSetGetUnsignedIntException3() {
        WrappedByteBuffer buf = WrappedByteBuffer.allocate(0);
        buf.getUnsignedInt();
    }

    @Test (expected=IndexOutOfBoundsException.class)
    public void testGetAndSetGetUnsignedIntAtException4() {
        WrappedByteBuffer buf = new WrappedByteBuffer();
        buf.getUnsignedIntAt(128);
    }
    
    /*
     * Unsigned Medium Int tests
     */
    @Test
    public void testGetAndSetMediumInt() throws Exception{
        WrappedByteBuffer buf = new WrappedByteBuffer();
        buf.putUnsigned(0x12);
        buf.putUnsigned(0x34);
        buf.putUnsigned(0x56);
        
        buf.flip();

        assertEquals(0x123456, buf.getUnsignedMediumInt());
    }

    @SuppressWarnings("deprecation")
    @Test
    public void testGetAndSetMediumInt2() throws Exception{
        WrappedByteBuffer buf = new WrappedByteBuffer();
        buf.order(java.nio.ByteOrder.LITTLE_ENDIAN);

        buf.putUnsigned(0x12);
        buf.putUnsigned(0x34);
        buf.putUnsigned(0x56);
        
        buf.flip();

        assertEquals(0x563412, buf.getUnsignedMediumInt());
    }

    @Test (expected=BufferUnderflowException.class)
    public void testGetAndSetGetMediumIntException3() {
        WrappedByteBuffer buf = WrappedByteBuffer.allocate(0);
        buf.getUnsignedMediumInt();
    }
    
    // Long tests
    @Test
    public void testGetAndSetLong() throws Exception{
        WrappedByteBuffer buf = new WrappedByteBuffer();
        long b1= -9223372036854775808L;
        long b2= 9223372036854775807L;
        long b3= 32768;
        long b4= 543210987654321L;
        long b5= 123456789012345L;
        buf.putLong(b1);
        buf.putLong(b2);
        buf.putLong(b3);
        buf.flip();

        assertEquals(b1, buf.getLong());
        assertEquals(b2, buf.getLong());

        buf.rewind();
        buf.putLongAt(0, b4);
        buf.putLongAt(8, b5);
        assertEquals(b4, buf.getLongAt(0));
        assertEquals(b5, buf.getLongAt(8));
    }

    @Test (expected=BufferOverflowException.class)
    public void testGetAndSetPutLongException() {
        WrappedByteBuffer buf = WrappedByteBuffer.allocate(0);
        buf.setAutoExpand(false);
        buf.putLong(121L);
    }

    @Test (expected=IndexOutOfBoundsException.class)
    public void testGetAndSetPutLongAtException2() {
        WrappedByteBuffer buf = new WrappedByteBuffer();
        buf.putLongAt(129, 121L);
    }

    @Test (expected=BufferUnderflowException.class)
    public void testGetAndSetGetLongException3() {
        WrappedByteBuffer buf = WrappedByteBuffer.allocate(0);
        buf.getLong();
    }

    @Test (expected=IndexOutOfBoundsException.class)
    public void testGetAndSetGetLongAtException4() {
        WrappedByteBuffer buf = new WrappedByteBuffer();
        buf.getLongAt(128);
    }
    
    @Test
    public void testGetAndSetSegment() throws Exception {
        //tests reallocation of buffer automatically.
        byte[] source = new byte[] {(byte)1,(byte)2,(byte)3,(byte)4,(byte)5,(byte)6 };
        WrappedByteBuffer buf = WrappedByteBuffer.allocate(2);
        buf.put(source, 1, 4); //put 2,3,4,5 into buffer
        buf.flip();
        assertEquals(4, buf.remaining());
        byte[] dest = new byte[3];
        buf.get(dest, 1, 2); //get 3,4 into dest, start position at 1
        assertEquals((byte)0, dest[0]);
        assertEquals((byte)2, dest[1]);
        assertEquals((byte)3, dest[2]);
    }
}