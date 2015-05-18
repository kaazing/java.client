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

package org.kaazing.gateway.client.impl;

import static org.junit.Assert.assertEquals;

import org.junit.Assert;
import org.junit.Test;
import org.kaazing.gateway.client.util.Base64Util;
import org.kaazing.gateway.client.util.WrappedByteBuffer;

public class Base64UtilTest {

    @Test
    public void mapped() throws Exception {
        String input = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
        byte [] expectedBytes = {(byte)0,(byte)16,(byte)131,(byte)16,(byte)81,(byte)135,(byte)32,(byte)146,(byte)139,(byte)48,(byte)211,(byte)143,(byte)65,(byte)20,(byte)147,(byte)81,(byte)85,(byte)151,(byte)97,(byte)150,(byte)155,(byte)113,(byte)215,(byte)159,(byte)130,(byte)24,(byte)163,(byte)146,(byte)89,(byte)167,(byte)162,(byte)154,(byte)171,(byte)178,(byte)219,(byte)175,(byte)195,(byte)28,(byte)179,(byte)211,(byte)93,(byte)183,(byte)227,(byte)158,(byte)187,(byte)243,(byte)223,(byte)191};
        WrappedByteBuffer expected = WrappedByteBuffer.wrap(expectedBytes); 
        WrappedByteBuffer out = Base64Util.decode(input);
        assertEquals(out, expected);
    }

    @Test
    public void testEncodeDecode() throws Exception {
        byte[] inBytes =
                           //NegTokenTarg (0xa1), length 0x1e
                new byte[]{(byte)0xa1, (byte)0x82, (byte)0x00, (byte)0x1c,
                           //Constructed Sequence, length
                           (byte)0x30, (byte)0x82, (byte)0x00, (byte)0x18,
                           //Seq. Element 0, negResult, length 3
                           (byte)0xA0, (byte)0x03,
                           //ENUMERATED, length 1, accept_incomplete
                           (byte)0x0A, (byte)0x01, (byte)0x01,
                           // Seq. Element 1, supportedMech length 0x0b
                           (byte)0xa1, (byte)0x0b,
                           // Microsoft Kerberos OID
                           (byte)0x06, (byte)0x09, (byte)0x2a, (byte)0x86, (byte)0x48, (byte)0x82, (byte)0xf7, (byte)0x12, (byte)0x01, (byte)0x02, (byte)0x02,
                           // Seq. Element 2, responseToken, length 0x03
                           (byte)0xA2, (byte)0x81, (byte)0x03,
                           // OCTET STRING length 0x00
                           (byte)0x04, (byte)0x81, (byte)0x00};

        //
        // KG-1542:
        // inBytes is a kerberos NegResponseTarg token, length 32.
        // this is how this was found, but the issue happens for any
        // input array where array.length %3 == 2.
        //
        WrappedByteBuffer in = WrappedByteBuffer.wrap(inBytes);
        WrappedByteBuffer codec = Base64Util.decode(Base64Util.encode(in));

        byte[] codecBytes = new byte[codec.remaining()];
        for (int i = 0; i < codecBytes.length; i++) {
            codecBytes[i] = codec.get();
        }
        org.junit.Assert.assertArrayEquals(inBytes, codecBytes);

    }


    @Test
    public void testEncodeDecode2() throws Exception {
        byte[] inBytes =
                           //NegTokenTarg (0xa1), length 0x1e
                new byte[]{(byte)0xa1, (byte)0x82, (byte)0x00, (byte)0x1c,
                           //Constructed Sequence, length
                           (byte)0x30, (byte)0x82, (byte)0x00, (byte)0x18,
                           //Seq. Element 0, negResult, length 3
                           (byte)0xA0, (byte)0x03,
                           //ENUMERATED, length 1, accept_incomplete
                           (byte)0x0A, (byte)0x01, (byte)0x01,
                           // Seq. Element 1, supportedMech length 0x0b
                           (byte)0xa1, (byte)0x0b,
                           // Microsoft Kerberos OID
                           (byte)0x06, (byte)0x09, (byte)0x2a, (byte)0x86, (byte)0x48, (byte)0x82, (byte)0xf7, (byte)0x12, (byte)0x01, (byte)0x02, (byte)0x02,
                           // Seq. Element 2, responseToken, length 0x03
                           (byte)0xA2, (byte)0x81, (byte)0x03,
                           // OCTET STRING length 0x00
                           (byte)0x04, (byte)0x81, (byte)0x00};

        //
        // KG-1542:
        // inBytes is a kerberos NegResponseTarg token, length 32.
        // this is how this was found, but the issue happens for any
        // input array where array.length %3 == 2.
        //
        WrappedByteBuffer in = WrappedByteBuffer.wrap(inBytes);
        WrappedByteBuffer codec = Base64Util.decode(Base64Util.encode(in));

        byte[] codecBytes = new byte[codec.remaining()];
        org.junit.Assert.assertEquals(inBytes.length, codecBytes.length);

        for (int i = 0; i < codecBytes.length; i++) {
            codecBytes[i] = codec.get();
            if ( codecBytes[i] != inBytes[i]) {
                Assert.fail("Encoded/decoded array differs from input at position " + i);
            }
        }


    }
}
