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
package org.kaazing.gateway.client.transport.ws;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.Test;

import org.kaazing.gateway.client.transport.ws.FrameProcessor;
import org.kaazing.gateway.client.transport.ws.FrameProcessorListener;

public class FrameProcessorTest {

    @Test
    public void testTextFrame() throws IOException {
        Mockery context = new Mockery();
        
        final FrameProcessorListener listener = context.mock(FrameProcessorListener.class);
        
        context.checking(new Expectations() {
            {
                oneOf(listener).messageReceived(with(new BaseMatcher<ByteBuffer>() {

                    @Override
                    public boolean matches(Object arg0) {
                        ByteBuffer buffer = (ByteBuffer)arg0;
                        return buffer.get(0) == 'F' && buffer.get(1) == 'O' && buffer.get(2) == 'O'; 
                    }

                    @Override
                    public void describeTo(Description desc) {
                        desc.appendText("fires onMessage");
                    }
                    
                }), with("TEXT"));
            }
        });
        
        FrameProcessor frameProcessor = new FrameProcessor(listener);
        byte[] bytes = new byte[] { (byte)0x81, 0x03, 'F','O','O' };
        frameProcessor.process(new ByteArrayInputStream(bytes));
        
        context.assertIsSatisfied();
    }
    
    @Test
    public void testTextMultipleFrames() throws IOException {
        Mockery context = new Mockery();
        
        final FrameProcessorListener listener = context.mock(FrameProcessorListener.class);
        
        context.checking(new Expectations() {
            {
                exactly(1000).of(listener).messageReceived(with(new BaseMatcher<ByteBuffer>() {
                    int i=1;

                    @Override
                    public boolean matches(Object arg0) {
                        ByteBuffer buffer = (ByteBuffer)arg0;
                        int actual = Integer.valueOf(new String(buffer.array()));
                        int expected = i++;
                        if (actual != expected) {
                            System.out.println("Expected: "+expected+"  Actual:"+actual);
                        }
                        return actual == expected;
                    }

                    @Override
                    public void describeTo(Description desc) {
                        desc.appendText("fires onMessage");
                    }
                    
                }), with("TEXT"));
            }
        });
        
        FrameProcessor frameProcessor = new FrameProcessor(listener);

        ByteBuffer byteBuffer = ByteBuffer.allocate(50000);
        for (int i=1; i<=1000; i++) {
            byteBuffer.put((byte)0x81);
            byte[] s = (""+i).getBytes();
            byteBuffer.put((byte)s.length);
            byteBuffer.put(s);
            
        }
        frameProcessor.process(new ByteArrayInputStream(byteBuffer.array(), 0, byteBuffer.position()));
        
        context.assertIsSatisfied();
    }

    @Test
    public void testTextLargeFrames() throws IOException {
        Mockery context = new Mockery();
        
        final FrameProcessorListener listener = context.mock(FrameProcessorListener.class);
        
        context.checking(new Expectations() {
            {
                oneOf(listener).messageReceived(with(new BaseMatcher<ByteBuffer>() {

                    @Override
                    public boolean matches(Object arg0) {
                        ByteBuffer buffer = (ByteBuffer)arg0;
                        int n = buffer.remaining();
                        for (int i=0; i<n; i++) {
                            if (buffer.get(i) != (byte)(32+i%37)) {
                                return false;
                            }
                        }
                        return true;
                    }

                    @Override
                    public void describeTo(Description desc) {
                        desc.appendText("fires onMessage");
                    }
                    
                }), with("TEXT"));
            }
        });
        
        FrameProcessor frameProcessor = new FrameProcessor(listener);

        byte[] bytes = new byte[10000010];
        bytes[0] = (byte)0x81;
        bytes[1] = 127; //64 bit length
        bytes[2] = 0x00; //hex code for 10,000,000
        bytes[3] = 0x00;
        bytes[4] = 0x00;
        bytes[5] = 0x00;
        bytes[6] = 0x00;
        bytes[7] = (byte)0x98;
        bytes[8] = (byte) 0x96;
        bytes[9] = (byte) 0x80;
        for (int i=0; i<10000000; i++) {
            bytes[i+10] = (byte)(32+i%37);
        }
        
        frameProcessor.process(new ByteArrayInputStream(bytes));
        
        context.assertIsSatisfied();
    }
    
    @Test
    public void testBinaryLargeFrames() throws IOException {
        Mockery context = new Mockery();
        
        final FrameProcessorListener listener = context.mock(FrameProcessorListener.class);
        
        context.checking(new Expectations() {
            {
                oneOf(listener).messageReceived(with(new BaseMatcher<ByteBuffer>() {

                    @Override
                    public boolean matches(Object arg0) {
                        ByteBuffer buffer = (ByteBuffer)arg0;
                        int n = buffer.remaining();
                        for (int i=0; i<n; i++) {
                            if (buffer.get(i) != (byte)(i%256)) {
                                return false;
                            }
                        }
                        return true; 
                    }

                    @Override
                    public void describeTo(Description desc) {
                        desc.appendText("fires onMessage");
                    }
                    
                }), with("BINARY"));
            }
        });
        
        FrameProcessor frameProcessor = new FrameProcessor(listener);

        byte[] bytes = new byte[10000010];
        bytes[0] = (byte)0x82;
        bytes[1] = 127; //64 bit length
        bytes[2] = 0x00; //hex code for 10,000,000
        bytes[3] = 0x00;
        bytes[4] = 0x00;
        bytes[5] = 0x00;
        bytes[6] = 0x00;
        bytes[7] = (byte)0x98;
        bytes[8] = (byte) 0x96;
        bytes[9] = (byte) 0x80;
        for (int i=0; i<10000000; i++) {
            bytes[i+10] = (byte)(i%256);
        }

        frameProcessor.process(new ByteArrayInputStream(bytes));
        
        context.assertIsSatisfied();
    }

    @Test
    public void testBinaryFrames() throws IOException {
        Mockery context = new Mockery();
        
        final FrameProcessorListener listener = context.mock(FrameProcessorListener.class);
        
        context.checking(new Expectations() {
            {
                oneOf(listener).messageReceived(with(new BaseMatcher<ByteBuffer>() {

                    @Override
                    public boolean matches(Object arg0) {
                        ByteBuffer buffer = (ByteBuffer)arg0;
                        int n = buffer.remaining();
                        for (int i=0; i<n; i++) {
                            if (buffer.get(i) != (byte)(i%256)) {
                                return false;
                            }
                        }
                        return true; 
                    }

                    @Override
                    public void describeTo(Description desc) {
                        desc.appendText("fires onMessage");
                    }
                    
                }), with("BINARY"));
            }
        });
        
        FrameProcessor frameProcessor = new FrameProcessor(listener);

        final byte[] bytes = new byte[10000010];
        bytes[0] = (byte)0x82;
        bytes[1] = 127; //64 bit length
        bytes[2] = 0x00; //hex code for 10,000,000
        bytes[3] = 0x00;
        bytes[4] = 0x00;
        bytes[5] = 0x00;
        bytes[6] = 0x00;
        bytes[7] = (byte)0x98;
        bytes[8] = (byte) 0x96;
        bytes[9] = (byte) 0x80;
        for (int i=0; i<10000000; i++) {
            bytes[i+10] = (byte)(i%256);
        }

        frameProcessor.process(new InputStream() {
            int i=0;
            
            @Override
            public int read() throws IOException {
                if (i >= bytes.length) {
                    return -1;
                }
                
                int ret = bytes[i++];
                if (ret < 0) {
                    ret += 256;
                }
                return ret;
            }

            @Override
            public int read(byte b[], int offset, int len) throws IOException {
                if (len > 1000) {
                    len = 1000;
                }
                if (offset + len > bytes.length) {
                    len = bytes.length - offset;
                }
                return super.read(b, offset, len);
            }
        });
        
        context.assertIsSatisfied();
    }

    @Test(expected=IOException.class)
    public void testException() throws IOException {
        Mockery context = new Mockery();
        
        final FrameProcessorListener listener = context.mock(FrameProcessorListener.class);
        
        context.checking(new Expectations() {
            {
                never(listener).messageReceived(with(any(ByteBuffer.class)), with(any(String.class)));
            }
        });
        
        FrameProcessor frameProcessor = new FrameProcessor(listener);
        frameProcessor.process(new InputStream() {
            @Override
            public int read() throws IOException {
                throw new IOException("Unexpected exception");
            }
        });
        
        context.assertIsSatisfied();
    }
    
    @Test
    public void testCommandFrames() throws IOException {
        Mockery context = new Mockery();
        
        final FrameProcessorListener listener = context.mock(FrameProcessorListener.class);
        
        context.checking(new Expectations() {
            {
                oneOf(listener).messageReceived(with(any(ByteBuffer.class)), with("PING"));
            }
        });
        
        FrameProcessor frameProcessor = new FrameProcessor(listener);

        byte[] bytes = new byte[18];
        bytes[0] = (byte)0x89; //unmasked ping message with body "Hello"
        bytes[1] = 0x05; 
        bytes[2] = 0x48;
        bytes[3] = 0x65;
        bytes[4] = 0x6c;
        bytes[5] = 0x6c;
        bytes[6] = 0x6f;
        
        bytes[7] = (byte)0x8a; //masked pong message with body "Hello" -0x8a 0x85 0x37 0xfa 0x21 0x3d 0x7f 0x9f 0x4d 0x51 0x58
        bytes[8] = (byte)0x85; 
        bytes[9] = 0x37;
        bytes[10] = (byte) 0xfa;
        bytes[11] = 0x21;
        bytes[12] = 0x3d;
        bytes[13] = 0x7f;
        bytes[14] = (byte) 0x9f;
        bytes[15] = 0x4d;
        bytes[16] = 0x51;
        bytes[17] = 0x58;
        
        
        frameProcessor.process(new ByteArrayInputStream(bytes));
        
        context.assertIsSatisfied();
    }
    
    @Test(expected=IOException.class)
    public void testException2() throws IOException {
        Mockery context = new Mockery();
        
        final FrameProcessorListener listener = context.mock(FrameProcessorListener.class);
        
        context.checking(new Expectations() {
            {
                never(listener).messageReceived(with(any(ByteBuffer.class)), with(any(String.class)));
            }
        });
        
        FrameProcessor frameProcessor = new FrameProcessor(listener);

        final byte[] bytes = new byte[10000010];
        bytes[0] = (byte)0x82;
        bytes[1] = 127; //64 bit length
        bytes[2] = 0x00; //hex code for 10,000,000
        bytes[3] = 0x00;
        bytes[4] = 0x00;
        bytes[5] = 0x00;
        bytes[6] = 0x00;
        bytes[7] = (byte)0x98;
        bytes[8] = (byte) 0x96;
        bytes[9] = (byte) 0x80;
        for (int i=0; i<10000000; i++) {
            bytes[i+10] = (byte)(i%256);
        }

        frameProcessor.process(new InputStream() {
            int i=0;
            
            @Override
            public int read() throws IOException {
                if (i == 1000) {
                    throw new IOException("Unexpected exception");
                }
                int ret = bytes[i++];
                if (ret < 0) {
                    ret += 256;
                }
                return ret;
            }
        });
        
        context.assertIsSatisfied();
    }
}
