/**
 * Copyright (c) 2007-2012, Kaazing Corporation. All rights reserved.
 */

package com.kaazing.gateway.amqp.client.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import com.kaazing.gateway.amqp.client.AmqpArguments;
import com.kaazing.gateway.amqp.client.AmqpArguments.AmqpTableEntry;
import com.kaazing.gateway.client.html5.ByteBuffer;

/**
 * Internal class
 * @private
 */
public final class AmqpBuffer extends ByteBuffer {
    
    /**
     * Data and framing layer for the AMQP protocol
     */
    private byte bitCount;
    
    public enum Type{
        BIT, SHORTSTRING, LONGSTRING, TABLE, INT, UNSIGNEDINT, UNSIGNEDSHORT, 
        UNSIGNED, SHORT, LONG, PATH, LONGLONG, VOID, OCTET, FIELDTABLE,
        TIMESTAMP
    }
    
    public enum InType{
        CLASSID, CONSUMERTAG, DELIVERYTAG, EXCHANGENAME, METHODID, NOACK, NOLOCAL, NOWAIT, PATH, PEERPROPERTIES, 
        QUEUENAME, REDELIVERED, MESSAGECOUNT, REPLYCODE, REPLYTEXT, BIT, OCTET, SHORT, LONG, LONGLONG, SHORTSTR,
        LONGSTR, TIMESTAMP, TABLE, VOID, FIELDTABLE, INT
    }
    private HashMap<Character, String> typeIdentifierMap = new HashMap<Character, String>();
    private HashMap<String, String> typeNameMap = new HashMap<String, String>();
    private HashMap<String, Object> contentProperties = new HashMap<String, Object>();

    public static ArrayList<BasicProperties> basicProperties;

   /**
    * Internal class
    * Data and framing layer for the AMQP protocol
    * @param bytes
    */
    private AmqpBuffer(byte[] bytes)
    {        
        // consecutive bit counter for bit packing        
        super(bytes);        
        this.bitCount = 0;
        this.init();
    }

    public AmqpBuffer()
    {        
        this.init();
    }

    private void init()
    {            
        typeIdentifierMap.put('S', "Longstr");
        typeIdentifierMap.put('I', "int");
        typeIdentifierMap.put('F', "FieldTable");
        typeIdentifierMap.put('V', "Void");

        typeNameMap.put("Longstr", "S");
        typeNameMap.put("int", "I");
        typeNameMap.put("Void", "V");
    }
           

    /**
     * Encodes the AMQPLAIN authentication response as an AmqpArguments
     */
    public String encodeAuthAmqPlain(String username, String password)
    {

        AmqpBuffer bytes = new AmqpBuffer();       
        bytes.putShortString("LOGIN");
        bytes.putTypeIdentifier("Longstr");
        bytes.putLongString(username);

        bytes.putShortString("PASSWORD");
        bytes.putTypeIdentifier("Longstr");
        bytes.putLongString(password);
        bytes.flip();

        int len = bytes.remaining();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < len; i++)
        {
            builder.append((char)(bytes.getUnsigned()));
        }
        String s = builder.toString();
        return s;
    }

    /**
     * getLongString returns an AMQP long String, which is a String prefixed
     * by a unsigned 32 bit integer.
     *
     * @public
     * @function
     * @name getLongString
     * @memberOf AmqpBuffer
     */

    private String getLongString()
    {
        long len = this.getUnsignedInt();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < len; i++)
        {
            builder.append((char)(this.getUnsigned()));
        }
        String s = builder.toString();
        return s;
    }

    /**
     * getShortString returns an AMQP Short String
     * @return
     */
    private String getShortString()
    {
        int len = this.getUnsigned();       
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < len; i++)
        {
            builder.append((char)(this.getUnsigned()));
        }
        String s = builder.toString();
        return s;
    }

    /**
     * putUnsignedLong puts an AMQP unsigned Long
     * @param v
     * @return ByteBuffer
     */
    private ByteBuffer putUnsignedLong(long v)
    {
        this.putInt(0);
        return this.putUnsignedInt((int)v);
    }
    
    /**
     * getUnsignedLong gets an AMQP unsigned Long
     * @return long
     */
    private long getUnsignedLong()
    {
        // For unsigned longs (8 byte integers)
        // throw away the first word, then read the next
        // word as an unsigned int
        this.getInt();
        return this.getUnsignedInt();
    }

    private long getMilliseconds() {
        return this.getLong();
    }
    
    private ByteBuffer putMilliseconds(long millis) {
        return this.putLong(millis);
    }
    
    private String getTypeIdentifier()
    {
        char c = (char)(this.getUnsigned());
        return typeIdentifierMap.get(c);
    }

    private void putTypeIdentifier(String type)
    {
        String ti = typeNameMap.get(type);
        char c = ti.charAt(0);
        byte charCode = (byte)c;
        this.putUnsigned(charCode);
    }

    private Object getFieldValue() {
        int typeCode = getUnsigned();
        switch(typeCode) {
        case 116:   // 't'
            boolean b = getUnsigned() != 0;
            return b;
        default:
            throw new IllegalStateException("Decoding Error in AmqpBuffer: cannot decode field value");
        }
    }

    private Map<String,Object> getFieldTable() {
        Map<String,Object> t = new HashMap<String,Object>();
        int len = (int)getUnsignedInt();
        int initial = position();
        while (len > (position()-initial)) {
            String key = getShortString();
            Object value = getFieldValue();
            t.put(key,value);
        }
        return t;
    }

    
    private AmqpArguments getTable()
    {        
        long len = (long)getUnsignedInt();     
        long end = this.position() + len;  

        /*
        ArrayList<TableEntry> table = new ArrayList<TableEntry>();
        ArrayList<TableEntry> tables =null;
        while (position() < end)
        {            
            TableEntry kv1 = new TableEntry();
            kv1.key = getShortString();   
            String ti = getTypeIdentifier();
            String typeCodec = getMappedType(ti);
            kv1.value = getObjectOfType(typeCodec);

            table.add(kv1);
            tables=table;
        }
        return tables;
        */

        ArrayList<AmqpTableEntry> entries = new ArrayList<AmqpTableEntry>();
        while (position() < end) {
            String key = getShortString();
            String ti = getTypeIdentifier();
            String typeCodec = getMappedType(ti);
            Object value = getObjectOfType(typeCodec);

            AmqpArguments.AmqpTableEntry entry = 
                      new AmqpArguments.AmqpTableEntry(key, value, ti);
            entries.add(entry);
        }
        
        if (entries.isEmpty()) {
            return null;
        }
        
        AmqpArguments args = new AmqpArguments();
        args.tableEntryArray = entries;
        return args;
    }

     private int getBit()
    {
        return this.getUnsigned();
    }

    /**
     * Packs one (of possibly several) boolean values as bits into a single 8-bit
     * field.
     *
     * If the last value written was a bit, the buffer's bit flag is false.
     * If the buffer's bit flag is set, putBit will try to pack the current bit
     * value into the same byte.
     */

     private AmqpBuffer putBit(Boolean v)
    {       
        byte value = 0;
        if (v)
        {
            value = 1;
        }
        if (this.bitCount > 0)
        {
            byte lastByte = this.getAt(this.position() - 1);
            lastByte = (byte)((value << this.bitCount) | lastByte);
            this.putAt(this.position() - 1, lastByte);
        }
        else
        {
            this.putUnsigned(value);
        }
        return this;
    }

     private void putLongString(String s)
    {
        this.putUnsignedInt(s.length());
        for (int i = 0; i < s.length(); i++)
        {
            char c = s.charAt(i);
            byte charCode = (byte)c;

            this.putUnsigned(charCode);
        }
    }

     private void putShortString(String s)
    {
        this.putUnsigned((byte)s.length());
        for (int i = 0; i < s.length(); i++)
        {
            char c = s.charAt(i);
            byte charCode = (byte)c;
            this.putUnsigned(charCode);
        }
    }

    private void putTable(Object table)
    {       
        if (table == null)
        {            
            this.putUnsignedInt(0);               
        }
        else
        {            
            AmqpArguments arg = (AmqpArguments)table;            
            List<AmqpTableEntry> entries = (List<AmqpTableEntry>)arg.tableEntryArray;            
            AmqpTableEntry entry = null;
            AmqpBuffer bytes = new AmqpBuffer();
            for (int i = 0; i < entries.size(); i++)
            {                
                entry = (AmqpTableEntry)entries.get(i);
                
                bytes.putShortString(entry.getKey());
                
                bytes.putTypeIdentifier(entry.getType());
                
                String type1 = getMappedType(entry.getType());
                
                bytes.putObjectOfType(type1, entry.getValue());
                
            }
            bytes.flip();
            this.putUnsignedInt(bytes.remaining());
            this.putBuffer(bytes);
        }
    }

    ////////////////////////////////////////////////////////////////////////////////
    // Frame types
    ////////////////////////////////////////////////////////////////////////////////

    private FrameHeader getFrameHeader()
    {        
        int frameType = this.getUnsigned();        
        int channel = this.getUnsignedShort();        
        long size =  this.getUnsignedInt();        

        FrameHeader header = new FrameHeader();
        header.frameType = frameType;
        header.size = size;
        header.channel = channel;

        return header;
    }

    /**
     * Decodes arguments given a method's parameter list
     */

     private Arg[] getMethodArguments(List<Parameter> param)
    {
        
         Arg[] val = new Arg[param.size()];
        for (int i = 0; i < param.size(); i++)
        {
            String type = param.get(i).type;

            Arg arg = new Arg();
            arg.type = type;
            arg.name = param.get(i).name;
            Object v = null;
            String type1 = null;
            try
            {            
                type1 = this.getMappedType(type);              
                v = getObjectOfType(type1);
            }
            catch (Exception e)
            {
                throw new IllegalStateException("type codec failed for type " + type + " for domain " + e.getMessage());
            }

            // support unpacking of consecutive bits from a single byte
            this.bitCount = (byte)((type1 == "Bit") ? this.bitCount + 1 : 0);

            arg.value = v;
            val[i]=arg;            
        }

        return val;
    }

     /**
      * Parses the ByteBuffer and returns an AMQP frame
      * @return AmqpFrame
      */
     public AmqpFrame getFrame()
    {    
        int pos = this.position();       
        AmqpMethod method = new AmqpMethod();
        int classIndex;
        AmqpFrame frame = new AmqpFrame(); 
                 
        // If there is at least one Frame in the buffer, attempt to decode it.
        if (this.remaining() > 7)
        {            
            frame.setHeader(this.getFrameHeader());           
            frame.setChannelId((short) frame.getHeader().channel);           
            frame.setType((short) frame.getHeader().frameType);           
            // the buffer must have an additional byte for the Frame end marker 
            
            if (this.remaining() >= frame.getHeader().size + 1)
            {                
                switch (frame.getType())
                {                
                    case AmqpConstants.FRAME_BODY:                        
                        // get the body sliced out;
                        ByteBuffer body = new AmqpBuffer();
                        //ByteBuffer body = new ByteBuffer();
                        // copy body into new Buffer
                        int end = this.position() + (int)frame.getHeader().size;
                        while (position() < end)
                        {
                            body.put(get());
                        }
                        frame.setBody(new AmqpBuffer(body.array()));
                        //frame1.body =new ByteBuffer(body.array());
                        frame.setMethodName("body");                        
                        break;
                    case AmqpConstants.FRAME_HEADER:                        
                        classIndex = this.getUnsignedShort();
                        @SuppressWarnings("unused")
                        short weight = (short)this.getUnsignedShort();
                        @SuppressWarnings("unused")
                        long bodySize = this.getUnsignedLong();                            
                        
                        frame.setContentProperties(this.getContentProperties());
                        frame.setMethodName("header");                        
                        break;
                    case AmqpConstants.FRAME_METHOD:
                         classIndex = this.getUnsignedShort();
                        int methodIndex = this.getUnsignedShort();
                        String indexes = String.valueOf(classIndex) + String.valueOf(methodIndex);                      
                        method = MethodLookup.LookupMethod(indexes);
                        
                        // pull parameter list off of method
                        List<Parameter> params = method.allParameters;

                        frame.setMethodName(method.name);
                        frame.setArgs(this.getMethodArguments(params));                        
                        break;
                    default:
                       throw new IllegalStateException("getFrame: AMQP Frame type '" + frame.getType() + "' is unknown or has not been implemented");
                }
                
                short frameEnd = (short) this.getUnsigned();                
                if (frameEnd != AmqpConstants.FRAME_END)
                {                    
                    throw new IllegalStateException("Invalid end of AMQP method frame");
                }
            }
            else
            {                
                this.position(pos);
                return null;
            }
            return frame;
        }
        return null;
    }


     public AmqpBuffer putMethodFrame(AmqpMethod method, short channel, Object[] args)
    {
        
        short classIndex = method.classIndex;
        short methodIndex = method.index;
        
        AmqpBuffer payload = new AmqpBuffer();
        payload.putUnsignedShort(classIndex);
        
        payload.putUnsignedShort(methodIndex);
        
        payload.putMethodArguments(args, method.allParameters);
        
        // compute size from payload bytes
        payload.flip();

        return this.putFrame((byte)AmqpConstants.FRAME_METHOD, channel, payload);

    }

     private AmqpBuffer putFrame(byte type, short channel, ByteBuffer payload)
    {
        this.putUnsigned(type);
        this.putUnsignedShort(channel);

        // compute size from payload bytes
        int size = payload.remaining();
        this.putUnsignedInt(size);
        this.putBuffer(payload);

        // write terminator
        this.putUnsigned((byte)AmqpConstants.FRAME_END);
        return this;
    }

    public AmqpBuffer putHeaderFrame(short channel, short classIndex, short weight, long bodySize, HashMap<String, Object> contentProperties)
    {
        AmqpBuffer payload = new AmqpBuffer();
        payload.putUnsignedShort(classIndex);
        payload.putUnsignedShort(weight);
        payload.putUnsignedLong(bodySize);            
        payload.putContentProperties(contentProperties);       

        payload.flip();        
        return this.putFrame((byte)AmqpConstants.FRAME_HEADER, channel, payload);
    }

    public AmqpBuffer putBodyFrame(short channel, ByteBuffer payload)
    {
        return this.putFrame((byte)AmqpConstants.FRAME_BODY, channel, payload);
    }

    /**
     * Encodes arguments given a method's parameter list and the provided arguments
     */
    AmqpBuffer putMethodArguments(Object[] args, List<Parameter> formalParams)
    {
        
        for (int i = 0; i < formalParams.size(); i++)
        {
        
            String type = formalParams.get(i).type;
        
            if (type == null)
            {
                throw new IllegalStateException("Unknown AMQP domain " + type);
            }
            String typeMap = getMappedType(type);
        
            putObjectOfType(typeMap, args[i]);
            // support packing of consecutive bits into a single byte
            this.bitCount = (byte)((typeMap == "Bit") ? this.bitCount + 1 : 0);
        }
        return this;
    }

   

    private Object getObjectOfType(String type)
    {
        Object value = null;
        switch (Type.valueOf(type.toUpperCase()))
        {
            case BIT:
                value = this.getBit();
                break;
            case SHORTSTRING:
                value = this.getShortString();
                break;
            case LONGSTRING:
                value = this.getLongString();
                break;
            case FIELDTABLE:
                value = this.getFieldTable();
                break;
            case TABLE:                
                value = this.getTable();
                break;
            case INT:
                value = this.getInt();
                break;
            case UNSIGNEDINT:
                value = this.getUnsignedInt();
                break;
            case UNSIGNEDSHORT:
                value = this.getUnsignedShort();
                break;
            case UNSIGNED:                
                value = this.getUnsigned();
                break;
            case SHORT:
                value = this.getUnsignedShort();
                break;
            case LONG:
                value = this.getUnsignedInt();
                break;
            case OCTET:                
                value = this.getUnsigned();
                break;
            case PATH:
                value = this.getShortString();
                break;
            case LONGLONG:
                value = this.getUnsignedLong();
                break;
            case TIMESTAMP:
                long millis = this.getMilliseconds();
                value = new Timestamp(millis);
                break;
            case VOID:
                value = null;
                break;
            default:
                String s = "Invalid type: '" + type + "' not found in _typeCodeMap";
                throw new IllegalStateException(s);
        }
        return value;
    }

    
    private void putObjectOfType(String type, Object value)
    {        
          switch (Type.valueOf(type.toUpperCase()))
          {
                case BIT:
                    this.putBit((Boolean)value);
                    break;
                case SHORTSTRING:
                    this.putShortString((String)value);
                    break;
                case LONGSTRING:
                    this.putLongString((String)value);
                    break;
                case TABLE:
                    this.putTable(value);
                    break;
                case INT:
                    this.putInt((Integer)value);
                    break;
                case UNSIGNEDINT:
                    this.putUnsignedInt((Integer)value);
                    break;
                case UNSIGNEDSHORT:
                    this.putUnsignedShort((Short)value);
                    break;
                case UNSIGNED:
                    this.putUnsigned((Integer)value);
                    break;
                case SHORT:
                    int channelmax = (Integer)value;
                    this.putUnsignedShort((short)channelmax);
                    break;
                case LONG:
                    this.putUnsignedInt((Integer)value);
                    break;
                case PATH:
                    this.putShortString((String)value);
                    break;
                case LONGLONG:
                    int val = (Integer)value;
                    this.putUnsignedLong((long)val);
                    break;
                case TIMESTAMP:
                    long millis = ((Timestamp)value).getTime();
                    this.putMilliseconds(millis);
                    break;
                case VOID:
                    // We do not encode the null value on the wire. The type 'V'
                    // indicates a null value.
                    break;
                default:
                    String s = "Invalid type: '" + type + "' not found in _typeCodeMap";
                    throw new IllegalStateException(s);
          }           
    }

    private HashMap<String, Object> getContentProperties()
    {
        int packedPropertyFlags = this.getUnsignedShort();
        Stack<Integer> propertyFlags = new Stack<Integer>();
        int k=0;
        for (int i = 0; i <= 15; i++)
        {
            int bit = (packedPropertyFlags >> (i)) & 0x01;
            if (bit != 0)
            {
                propertyFlags.push(i+1);
            }
        }
        // int len = basicProperties.size();
        int sz = propertyFlags.size();

        for (int i = 0; i < sz; i++)
        {
            k = 16 - propertyFlags.pop();

            String propertyName = basicProperties.get(k).name;
            String propertyDomain = basicProperties.get(k).domain;
            String propertyType = this.getMappedType(propertyDomain);

            contentProperties.put(propertyName, getObjectOfType(propertyType));
        }
        return contentProperties;
    }

    private AmqpBuffer putContentProperties(HashMap<String, Object> contentProperties)
    {
        if (contentProperties.size() == 0) {
            this.putUnsignedShort((short) 0);
            return this;
        }
        
        int tempPackedPropertyFlags=0;
        short packedPropertyFlags = 0x00;
        List<Properties> properties = new ArrayList<Properties>();

        for (BasicProperties property : basicProperties) {
            Object propertyValue = contentProperties.get(property.name);
            if (propertyValue != null)
            {
                properties.add(new Properties(property.name, propertyValue, property.domain));
                tempPackedPropertyFlags = tempPackedPropertyFlags << 1 | 0x1;
            }
            else
            {
                tempPackedPropertyFlags = tempPackedPropertyFlags << 1;
            }                
        }
        
        packedPropertyFlags = (short)(tempPackedPropertyFlags << 2);
        this.putUnsignedShort(packedPropertyFlags);

        for (int i = 0; i < properties.size(); i++)
        {
            Properties property = new Properties();
            property = properties.get(i);
            String propertyDomain = property.domain;
            String type = getMappedType(propertyDomain);
            this.putObjectOfType(type, property.propertyValue);
        }
        return this;
    }
    
   
    private String getMappedType(String inType)
    {
        switch (InType.valueOf(inType.toUpperCase()))
        {
            case CLASSID:
                return "Short";
            case CONSUMERTAG:
                return "ShortString";
            case DELIVERYTAG:
                return "LongLong";
            case EXCHANGENAME:
                return "ShortString";
            case METHODID:
                return "Short";
            case NOACK:
                return "Bit";
            case NOLOCAL:
                return "Bit";
            case NOWAIT:
                return "Bit";
            case PATH:
                return "ShortString";
            case PEERPROPERTIES:
                return "Table";
            case QUEUENAME:
                return "ShortString";
            case REDELIVERED:
                return "Bit";
            case MESSAGECOUNT:
                return "Long";
            case REPLYCODE:
                return "Short";
            case REPLYTEXT:
                return "ShortString";
            case BIT:
                return "Bit";
            case OCTET:
                return "Unsigned";
            case SHORT:
                return "Short";
            case LONG:
                return "Long";
            case LONGLONG:
                return "LongLong";
            case SHORTSTR:
                return "ShortString";
            case LONGSTR:
                return "LongString";
            case TIMESTAMP:
                return "Timestamp";
            case TABLE:
                return "Table";
            case FIELDTABLE:
                return "FieldTable";
            case INT:
                return "Int";
            case VOID:
                return "Void";
            default:
                throw new IllegalStateException("Error in getMappedType()");
        }
    }
        
    ByteBuffer readBuffer = ByteBuffer.allocate(1024);
    
    public void appendBuffer(ByteBuffer in) {
        int p = readBuffer.position();
        readBuffer.skip(readBuffer.remaining());
        readBuffer.putBuffer(in);
        readBuffer.flip();
        readBuffer.position(p);
    }
    
    public AmqpFrame readFrame() {
        return null;
    }
        
    public static class BasicProperties
    {
         public String name;
         public String domain;
         public String label;        
    }
    
    public class Arg
    {
        public String name;
        public String type;
        public Object value;
    }    
}

class FrameHeader
{
     int frameType;
     long size;
     int channel;
}

 class Entry
{
     String key;
     String type;
     String value;
}

class Properties
{
     String propertyName;
     Object propertyValue;
     String domain;

     Properties(String propertyName, Object propertyValue, String domain)
    {
        this.propertyName = propertyName;
        this.propertyValue = propertyValue;
        this.domain = domain;
    }
     Properties()
    {
    }
}
