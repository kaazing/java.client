/**
 * Copyright (c) 2007-2014, Kaazing Corporation. All rights reserved.
 */

package com.kaazing.net.ws.amqp.impl;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Internal class
 */
public final class MethodLookup {
        
    static HashMap<String, AmqpMethod> methodTable=null;
    public static AmqpMethod LookupMethod(String s)
    {        
        AmqpMethod meth = new AmqpMethod();        
        meth=methodTable.get(s);
        return meth;
    }
   
    static
    {
        methodTable = new HashMap<String, AmqpMethod>(); 
        AmqpMethod method = null;        
        String classIndex, index, methodId2;
        method = new AmqpMethod();
        method.name = "startConnection";
        method.classIndex = 10;
        method.index = 10;
        method.returnType = "StartOk";
        method.synchronous = true;
        method.hasContent = false;
        method.allParameters = new ArrayList<Parameter>();
        method.allParameters.add(new Parameter("versionMajor", "Octet")); method.allParameters.add(new Parameter("versionMinor", "Octet")); method.allParameters.add(new Parameter("serverProperties", "PeerProperties")); method.allParameters.add(new Parameter("mechanisms", "Longstr")); method.allParameters.add(new Parameter("locales", "Longstr"));

        
        classIndex = String.valueOf(method.classIndex);
        index = String.valueOf(method.index);
        methodId2 = classIndex+index;            
        methodTable.put(methodId2, method);
        
        method = new AmqpMethod();
        method.name = "startOkConnection";
        method.classIndex = 10;
        method.index = 11;
        method.returnType = "void";
        method.synchronous = true;
        method.hasContent = false;
        method.allParameters = new ArrayList<Parameter>();
        method.allParameters.add(new Parameter("clientProperties", "PeerProperties")); method.allParameters.add(new Parameter("mechanism", "Shortstr")); method.allParameters.add(new Parameter("response", "Longstr")); method.allParameters.add(new Parameter("locale", "Shortstr"));
        classIndex = String.valueOf(method.classIndex);
        index = String.valueOf(method.index);
        methodId2 = classIndex + index;
        methodTable.put(methodId2, method);            
        

        
        method = new AmqpMethod();
        method.name = "secureConnection";
        method.classIndex = 10;
        method.index = 20;
        method.returnType = "SecureOk";
        method.synchronous = true;
        method.hasContent = false;
        method.allParameters = new ArrayList<Parameter>();
        method.allParameters.add(new Parameter("challenge", "Longstr"));
        classIndex = String.valueOf(method.classIndex);
        index = String.valueOf(method.index);
        methodId2 = classIndex + index;
        methodTable.put(methodId2, method); 
        
        method = new AmqpMethod();
        method.name = "secureOkConnection";
        method.classIndex = 10;
        method.index = 21;
        method.returnType = "void";
        method.synchronous = true;
        method.hasContent = false;
        method.allParameters = new ArrayList<Parameter>();
        method.allParameters.add(new Parameter("response", "Longstr"));
        classIndex = String.valueOf(method.classIndex);
        index = String.valueOf(method.index);
        methodId2 = classIndex + index;
        methodTable.put(methodId2, method); 
       

        method = new AmqpMethod();
        method.name = "tuneConnection";
        method.classIndex = 10;
        method.index = 30;
        method.returnType = "TuneOk";
        method.synchronous = true;
        method.hasContent = false;
        method.allParameters = new ArrayList<Parameter>();
        method.allParameters.add(new Parameter("channelMax", "Short")); method.allParameters.add(new Parameter("frameMax", "Long")); method.allParameters.add(new Parameter("heartbeat", "Short"));
        classIndex = String.valueOf(method.classIndex);
        index = String.valueOf(method.index);
        methodId2 = classIndex + index;
        methodTable.put(methodId2, method); 
        
        method = new AmqpMethod();
        method.name = "tuneOkConnection";
        method.classIndex = 10;
        method.index = 31;
        method.returnType = "void";
        method.synchronous = true;
        method.hasContent = false;
        method.allParameters = new ArrayList<Parameter>();
        method.allParameters.add(new Parameter("channelMax", "Short")); method.allParameters.add(new Parameter("frameMax", "Long")); method.allParameters.add(new Parameter("heartbeat", "Short"));
        classIndex = String.valueOf(method.classIndex);
        index = String.valueOf(method.index);
        methodId2 = classIndex + index;
        methodTable.put(methodId2, method); 
       
        method = new AmqpMethod();
        method.name = "openConnection";
        method.classIndex = 10;
        method.index = 40;
        method.returnType = "OpenOk";
        method.synchronous = true;
        method.hasContent = false;
        method.allParameters = new ArrayList<Parameter>();
        method.allParameters.add(new Parameter("virtualHost", "Path")); method.allParameters.add(new Parameter("reserved1", "Shortstr")); method.allParameters.add(new Parameter("reserved2", "Bit"));
        classIndex = String.valueOf(method.classIndex);
        index = String.valueOf(method.index);
        methodId2 = classIndex + index;
        methodTable.put(methodId2, method); 
        
        method = new AmqpMethod();
        method.name = "openOkConnection";
        method.classIndex = 10;
        method.index = 41;
        method.returnType = "void";
        method.synchronous = true;
        method.hasContent = false;
        method.allParameters = new ArrayList<Parameter>();
        method.allParameters.add(new Parameter("reserved1", "Shortstr"));
        classIndex = String.valueOf(method.classIndex);
        index = String.valueOf(method.index);
        methodId2 = classIndex + index;
        methodTable.put(methodId2, method); 
       

        method = new AmqpMethod();
        method.name = "closeConnection";
        method.classIndex = 10;
        method.index = 50;
        method.returnType = "CloseOk";
        method.synchronous = true;
        method.hasContent = false;
        method.allParameters = new ArrayList<Parameter>();
        method.allParameters.add(new Parameter("replyCode", "ReplyCode")); method.allParameters.add(new Parameter("replyText", "ReplyText")); method.allParameters.add(new Parameter("classId", "ClassId")); method.allParameters.add(new Parameter("methodId", "MethodId"));
        classIndex = String.valueOf(method.classIndex);
        index = String.valueOf(method.index);
        methodId2 = classIndex + index;
        methodTable.put(methodId2, method); 
       
        method = new AmqpMethod();
        method.name = "closeOkConnection";
        method.classIndex = 10;
        method.index = 51;
        method.returnType = "void";
        method.synchronous = true;
        method.hasContent = false;
        method.allParameters = new ArrayList<Parameter>();

        classIndex = String.valueOf(method.classIndex);
        index = String.valueOf(method.index);
        methodId2 = classIndex + index;
        methodTable.put(methodId2, method); 


        method = new AmqpMethod();
        method.name = "openChannel";
        method.classIndex = 20;
        method.index = 10;
        method.returnType = "OpenOk";
        method.synchronous = true;
        method.hasContent = false;
        method.allParameters = new ArrayList<Parameter>();
        method.allParameters.add(new Parameter("reserved1", "Shortstr"));

        classIndex = String.valueOf(method.classIndex);
        index = String.valueOf(method.index);
        methodId2 = classIndex + index;
        methodTable.put(methodId2, method); 


        method = new AmqpMethod();
        method.name = "openOkChannel";
        method.classIndex = 20;
        method.index = 11;
        method.returnType = "void";
        method.synchronous = true;
        method.hasContent = false;
        method.allParameters = new ArrayList<Parameter>();
        method.allParameters.add(new Parameter("reserved1", "Longstr"));

        classIndex = String.valueOf(method.classIndex);
        index = String.valueOf(method.index);
        methodId2 = classIndex + index;
        methodTable.put(methodId2, method); 


        method = new AmqpMethod();
        method.name = "flowChannel";
        method.classIndex = 20;
        method.index = 20;
        method.returnType = "FlowOk";
        method.synchronous = true;
        method.hasContent = false;
        method.allParameters = new ArrayList<Parameter>();
        method.allParameters.add(new Parameter("active", "Bit"));

        classIndex = String.valueOf(method.classIndex);
        index = String.valueOf(method.index);
        methodId2 = classIndex + index;
        methodTable.put(methodId2, method); 


        method = new AmqpMethod();
        method.name = "flowOkChannel";
        method.classIndex = 20;
        method.index = 21;
        method.returnType = "void";
        method.synchronous = false;
        method.hasContent = false;
        method.allParameters = new ArrayList<Parameter>();
        method.allParameters.add(new Parameter("active", "Bit"));

        classIndex = String.valueOf(method.classIndex);
        index = String.valueOf(method.index);
        methodId2 = classIndex + index;
        methodTable.put(methodId2, method); 


        method = new AmqpMethod();
        method.name = "closeChannel";
        method.classIndex = 20;
        method.index = 40;
        method.returnType = "CloseOk";
        method.synchronous = true;
        method.hasContent = false;
        method.allParameters = new ArrayList<Parameter>();
        method.allParameters.add(new Parameter("replyCode", "ReplyCode")); method.allParameters.add(new Parameter("replyText", "ReplyText")); method.allParameters.add(new Parameter("classId", "ClassId")); method.allParameters.add(new Parameter("methodId", "MethodId"));

        classIndex = String.valueOf(method.classIndex);
        index = String.valueOf(method.index);
        methodId2 = classIndex + index;
        methodTable.put(methodId2, method); 


        method = new AmqpMethod();
        method.name = "closeOkChannel";
        method.classIndex = 20;
        method.index = 41;
        method.returnType = "void";
        method.synchronous = true;
        method.hasContent = false;
        method.allParameters = new ArrayList<Parameter>();

        classIndex = String.valueOf(method.classIndex);
        index = String.valueOf(method.index);
        methodId2 = classIndex + index;
        methodTable.put(methodId2, method); 


        method = new AmqpMethod();
        method.name = "declareExchange";
        method.classIndex = 40;
        method.index = 10;
        method.returnType = "DeclareOk";
        method.synchronous = true;
        method.hasContent = false;
        method.allParameters = new ArrayList<Parameter>();
        method.allParameters.add(new Parameter("reserved1", "Short")); method.allParameters.add(new Parameter("exchange", "ExchangeName")); method.allParameters.add(new Parameter("type", "Shortstr")); method.allParameters.add(new Parameter("passive", "Bit")); method.allParameters.add(new Parameter("durable", "Bit")); method.allParameters.add(new Parameter("reserved2", "Bit")); method.allParameters.add(new Parameter("reserved3", "Bit")); method.allParameters.add(new Parameter("noWait", "NoWait")); method.allParameters.add(new Parameter("arguments", "Table"));

        classIndex = String.valueOf(method.classIndex);
        index = String.valueOf(method.index);
        methodId2 = classIndex + index;
        methodTable.put(methodId2, method); 


        method = new AmqpMethod();
        method.name = "declareOkExchange";
        method.classIndex = 40;
        method.index = 11;
        method.returnType = "void";
        method.synchronous = true;
        method.hasContent = false;
        method.allParameters = new ArrayList<Parameter>();

        classIndex = String.valueOf(method.classIndex);
        index = String.valueOf(method.index);
        methodId2 = classIndex + index;
        methodTable.put(methodId2, method); 


        method = new AmqpMethod();
        method.name = "deleteExchange";
        method.classIndex = 40;
        method.index = 20;
        method.returnType = "DeleteOk";
        method.synchronous = true;
        method.hasContent = false;
        method.allParameters = new ArrayList<Parameter>();
        method.allParameters.add(new Parameter("reserved1", "Short")); method.allParameters.add(new Parameter("exchange", "ExchangeName")); method.allParameters.add(new Parameter("ifUnused", "Bit")); method.allParameters.add(new Parameter("noWait", "NoWait"));

        classIndex = String.valueOf(method.classIndex);
        index = String.valueOf(method.index);
        methodId2 = classIndex + index;
        methodTable.put(methodId2, method); 


        method = new AmqpMethod();
        method.name = "deleteOkExchange";
        method.classIndex = 40;
        method.index = 21;
        method.returnType = "void";
        method.synchronous = true;
        method.hasContent = false;
        method.allParameters = new ArrayList<Parameter>();

        classIndex = String.valueOf(method.classIndex);
        index = String.valueOf(method.index);
        methodId2 = classIndex + index;
        methodTable.put(methodId2, method); 



        method = new AmqpMethod();
        method.name = "declareQueue";
        method.classIndex = 50;
        method.index = 10;
        method.returnType = "DeclareOk";
        method.synchronous = true;
        method.hasContent = false;
        method.allParameters = new ArrayList<Parameter>();
        method.allParameters.add(new Parameter("reserved1", "Short")); method.allParameters.add(new Parameter("queue", "QueueName")); method.allParameters.add(new Parameter("passive", "Bit")); method.allParameters.add(new Parameter("durable", "Bit")); method.allParameters.add(new Parameter("exclusive", "Bit")); method.allParameters.add(new Parameter("autoDelete", "Bit")); method.allParameters.add(new Parameter("noWait", "NoWait")); method.allParameters.add(new Parameter("arguments", "Table"));

        classIndex = String.valueOf(method.classIndex);
        index = String.valueOf(method.index);
        methodId2 = classIndex + index;
        methodTable.put(methodId2, method); 


        method = new AmqpMethod();
        method.name = "declareOkQueue";
        method.classIndex = 50;
        method.index = 11;
        method.returnType = "void";
        method.synchronous = true;
        method.hasContent = false;
        method.allParameters = new ArrayList<Parameter>();
        method.allParameters.add(new Parameter("queue", "QueueName")); method.allParameters.add(new Parameter("messageCount", "MessageCount")); method.allParameters.add(new Parameter("consumerCount", "Long"));

        classIndex = String.valueOf(method.classIndex);
        index = String.valueOf(method.index);
        methodId2 = classIndex + index;
        methodTable.put(methodId2, method); 


        method = new AmqpMethod();
        method.name = "bindQueue";
        method.classIndex = 50;
        method.index = 20;
        method.returnType = "BindOk";
        method.synchronous = true;
        method.hasContent = false;
        method.allParameters = new ArrayList<Parameter>();
        method.allParameters.add(new Parameter("reserved1", "Short")); method.allParameters.add(new Parameter("queue", "QueueName")); method.allParameters.add(new Parameter("exchange", "ExchangeName")); method.allParameters.add(new Parameter("routingKey", "Shortstr")); method.allParameters.add(new Parameter("noWait", "NoWait")); method.allParameters.add(new Parameter("arguments", "Table"));

       classIndex = String.valueOf(method.classIndex);
        index = String.valueOf(method.index);
        methodId2 = classIndex + index;
        methodTable.put(methodId2, method); 


        method = new AmqpMethod();
        method.name = "bindOkQueue";
        method.classIndex = 50;
        method.index = 21;
        method.returnType = "void";
        method.synchronous = true;
        method.hasContent = false;
        method.allParameters = new ArrayList<Parameter>();

        classIndex = String.valueOf(method.classIndex);
        index = String.valueOf(method.index);
        methodId2 = classIndex + index;
        methodTable.put(methodId2, method); 


        method = new AmqpMethod();
        method.name = "unbindQueue";
        method.classIndex = 50;
        method.index = 50;
        method.returnType = "UnbindOk";
        method.synchronous = true;
        method.hasContent = false;
        method.allParameters = new ArrayList<Parameter>();
        method.allParameters.add(new Parameter("reserved1", "Short")); method.allParameters.add(new Parameter("queue", "QueueName")); method.allParameters.add(new Parameter("exchange", "ExchangeName")); method.allParameters.add(new Parameter("routingKey", "Shortstr")); method.allParameters.add(new Parameter("arguments", "Table"));

        classIndex = String.valueOf(method.classIndex);
        index = String.valueOf(method.index);
        methodId2 = classIndex + index;
        methodTable.put(methodId2, method); 


        method = new AmqpMethod();
        method.name = "unbindOkQueue";
        method.classIndex = 50;
        method.index = 51;
        method.returnType = "void";
        method.synchronous = true;
        method.hasContent = false;
        method.allParameters = new ArrayList<Parameter>();

        classIndex = String.valueOf(method.classIndex);
        index = String.valueOf(method.index);
        methodId2 = classIndex + index;
        methodTable.put(methodId2, method); 


        method = new AmqpMethod();
        method.name = "purgeQueue";
        method.classIndex = 50;
        method.index = 30;
        method.returnType = "PurgeOk";
        method.synchronous = true;
        method.hasContent = false;
        method.allParameters = new ArrayList<Parameter>();
        method.allParameters.add(new Parameter("reserved1", "Short")); method.allParameters.add(new Parameter("queue", "QueueName")); method.allParameters.add(new Parameter("noWait", "NoWait"));

       classIndex = String.valueOf(method.classIndex);
        index = String.valueOf(method.index);
        methodId2 = classIndex + index;
        methodTable.put(methodId2, method); 


        method = new AmqpMethod();
        method.name = "purgeOkQueue";
        method.classIndex = 50;
        method.index = 31;
        method.returnType = "void";
        method.synchronous = true;
        method.hasContent = false;
        method.allParameters = new ArrayList<Parameter>();
        method.allParameters.add(new Parameter("messageCount", "MessageCount"));

        classIndex = String.valueOf(method.classIndex);
        index = String.valueOf(method.index);
        methodId2 = classIndex + index;
        methodTable.put(methodId2, method); 


        method = new AmqpMethod();
        method.name = "deleteQueue";
        method.classIndex = 50;
        method.index = 40;
        method.returnType = "DeleteOk";
        method.synchronous = true;
        method.hasContent = false;
        method.allParameters = new ArrayList<Parameter>();
        method.allParameters.add(new Parameter("reserved1", "Short")); method.allParameters.add(new Parameter("queue", "QueueName")); method.allParameters.add(new Parameter("ifUnused", "Bit")); method.allParameters.add(new Parameter("ifEmpty", "Bit")); method.allParameters.add(new Parameter("noWait", "NoWait"));

        classIndex = String.valueOf(method.classIndex);
        index = String.valueOf(method.index);
        methodId2 = classIndex + index;
        methodTable.put(methodId2, method); 


        method = new AmqpMethod();
        method.name = "deleteOkQueue";
        method.classIndex = 50;
        method.index = 41;
        method.returnType = "void";
        method.synchronous = true;
        method.hasContent = false;
        method.allParameters = new ArrayList<Parameter>();
        method.allParameters.add(new Parameter("messageCount", "MessageCount"));

        classIndex = String.valueOf(method.classIndex);
        index = String.valueOf(method.index);
        methodId2 = classIndex + index;
        methodTable.put(methodId2, method); 



        method = new AmqpMethod();
        method.name = "qosBasic";
        method.classIndex = 60;
        method.index = 10;
        method.returnType = "QosOk";
        method.synchronous = true;
        method.hasContent = false;
        method.allParameters = new ArrayList<Parameter>();
        method.allParameters.add(new Parameter("prefetchSize", "Long")); method.allParameters.add(new Parameter("prefetchCount", "Short")); method.allParameters.add(new Parameter("global", "Bit"));

        classIndex = String.valueOf(method.classIndex);
        index = String.valueOf(method.index);
        methodId2 = classIndex + index;
        methodTable.put(methodId2, method); 


        method = new AmqpMethod();
        method.name = "qosOkBasic";
        method.classIndex = 60;
        method.index = 11;
        method.returnType = "void";
        method.synchronous = true;
        method.hasContent = false;
        method.allParameters = new ArrayList<Parameter>();

       classIndex = String.valueOf(method.classIndex);
        index = String.valueOf(method.index);
        methodId2 = classIndex + index;
        methodTable.put(methodId2, method); 


        method = new AmqpMethod();
        method.name = "consumeBasic";
        method.classIndex = 60;
        method.index = 20;
        method.returnType = "ConsumeOk";
        method.synchronous = true;
        method.hasContent = false;
        method.allParameters = new ArrayList<Parameter>();
        method.allParameters.add(new Parameter("reserved1", "Short")); method.allParameters.add(new Parameter("queue", "QueueName")); method.allParameters.add(new Parameter("consumerTag", "ConsumerTag")); method.allParameters.add(new Parameter("noLocal", "NoLocal")); method.allParameters.add(new Parameter("noAck", "NoAck")); method.allParameters.add(new Parameter("exclusive", "Bit")); method.allParameters.add(new Parameter("noWait", "NoWait")); method.allParameters.add(new Parameter("arguments", "Table"));

        classIndex = String.valueOf(method.classIndex);
        index = String.valueOf(method.index);
        methodId2 = classIndex + index;
        methodTable.put(methodId2, method); 


        method = new AmqpMethod();
        method.name = "consumeOkBasic";
        method.classIndex = 60;
        method.index = 21;
        method.returnType = "void";
        method.synchronous = true;
        method.hasContent = false;
        method.allParameters = new ArrayList<Parameter>();
        method.allParameters.add(new Parameter("consumerTag", "ConsumerTag"));

       classIndex = String.valueOf(method.classIndex);
        index = String.valueOf(method.index);
        methodId2 = classIndex + index;
        methodTable.put(methodId2, method); 


        method = new AmqpMethod();
        method.name = "cancelBasic";
        method.classIndex = 60;
        method.index = 30;
        method.returnType = "CancelOk";
        method.synchronous = true;
        method.hasContent = false;
        method.allParameters = new ArrayList<Parameter>();
        method.allParameters.add(new Parameter("consumerTag", "ConsumerTag")); method.allParameters.add(new Parameter("noWait", "NoWait"));

       classIndex = String.valueOf(method.classIndex);
        index = String.valueOf(method.index);
        methodId2 = classIndex + index;
        methodTable.put(methodId2, method); 


        method = new AmqpMethod();
        method.name = "cancelOkBasic";
        method.classIndex = 60;
        method.index = 31;
        method.returnType = "void";
        method.synchronous = true;
        method.hasContent = false;
        method.allParameters = new ArrayList<Parameter>();
        method.allParameters.add(new Parameter("consumerTag", "ConsumerTag"));

        classIndex = String.valueOf(method.classIndex);
        index = String.valueOf(method.index);
        methodId2 = classIndex + index;
        methodTable.put(methodId2, method); 


        method = new AmqpMethod();
        method.name = "publishBasic";
        method.classIndex = 60;
        method.index = 40;
        method.returnType = "void";
        method.synchronous = false;
        method.hasContent = true;
        method.allParameters = new ArrayList<Parameter>();
        method.allParameters.add(new Parameter("reserved1", "Short")); method.allParameters.add(new Parameter("exchange", "ExchangeName")); method.allParameters.add(new Parameter("routingKey", "Shortstr")); method.allParameters.add(new Parameter("mandatory", "Bit")); method.allParameters.add(new Parameter("immediate", "Bit"));

        classIndex = String.valueOf(method.classIndex);
        index = String.valueOf(method.index);
        methodId2 = classIndex + index;
        methodTable.put(methodId2, method); 


        method = new AmqpMethod();
        method.name = "returnBasic";
        method.classIndex = 60;
        method.index = 50;
        method.returnType = "void";
        method.synchronous = false;
        method.hasContent = true;
        method.allParameters = new ArrayList<Parameter>();
        method.allParameters.add(new Parameter("replyCode", "ReplyCode")); method.allParameters.add(new Parameter("replyText", "ReplyText")); method.allParameters.add(new Parameter("exchange", "ExchangeName")); method.allParameters.add(new Parameter("routingKey", "Shortstr"));

        classIndex = String.valueOf(method.classIndex);
        index = String.valueOf(method.index);
        methodId2 = classIndex + index;
        methodTable.put(methodId2, method); 


        method = new AmqpMethod();
        method.name = "deliverBasic";
        method.classIndex = 60;
        method.index = 60;
        method.returnType = "void";
        method.synchronous = false;
        method.hasContent = true;
        method.allParameters = new ArrayList<Parameter>();
        method.allParameters.add(new Parameter("consumerTag", "ConsumerTag")); method.allParameters.add(new Parameter("deliveryTag", "DeliveryTag")); method.allParameters.add(new Parameter("redelivered", "Redelivered")); method.allParameters.add(new Parameter("exchange", "ExchangeName")); method.allParameters.add(new Parameter("routingKey", "Shortstr"));

        classIndex = String.valueOf(method.classIndex);
        index = String.valueOf(method.index);
        methodId2 = classIndex + index;
        methodTable.put(methodId2, method); 


        method = new AmqpMethod();
        method.name = "getBasic";
        method.classIndex = 60;
        method.index = 70;
        method.returnType = "GetOk";
        method.synchronous = true;
        method.hasContent = false;
        method.allParameters = new ArrayList<Parameter>();
        method.allParameters.add(new Parameter("reserved1", "Short")); method.allParameters.add(new Parameter("queue", "QueueName")); method.allParameters.add(new Parameter("noAck", "NoAck"));

       classIndex = String.valueOf(method.classIndex);
        index = String.valueOf(method.index);
        methodId2 = classIndex + index;
        methodTable.put(methodId2, method); 


        method = new AmqpMethod();
        method.name = "getOkBasic";
        method.classIndex = 60;
        method.index = 71;
        method.returnType = "void";
        method.synchronous = true;
        method.hasContent = true;
        method.allParameters = new ArrayList<Parameter>();
        method.allParameters.add(new Parameter("deliveryTag", "DeliveryTag")); method.allParameters.add(new Parameter("redelivered", "Redelivered")); method.allParameters.add(new Parameter("exchange", "ExchangeName")); method.allParameters.add(new Parameter("routingKey", "Shortstr")); method.allParameters.add(new Parameter("messageCount", "MessageCount"));

        classIndex = String.valueOf(method.classIndex);
        index = String.valueOf(method.index);
        methodId2 = classIndex + index;
        methodTable.put(methodId2, method); 


        method = new AmqpMethod();
        method.name = "getEmptyBasic";
        method.classIndex = 60;
        method.index = 72;
        method.returnType = "void";
        method.synchronous = true;
        method.hasContent = false;
        method.allParameters = new ArrayList<Parameter>();
        method.allParameters.add(new Parameter("reserved1", "Shortstr"));

        classIndex = String.valueOf(method.classIndex);
        index = String.valueOf(method.index);
        methodId2 = classIndex + index;
        methodTable.put(methodId2, method); 


        method = new AmqpMethod();
        method.name = "ackBasic";
        method.classIndex = 60;
        method.index = 80;
        method.returnType = "void";
        method.synchronous = false;
        method.hasContent = false;
        method.allParameters = new ArrayList<Parameter>();
        method.allParameters.add(new Parameter("deliveryTag", "DeliveryTag")); method.allParameters.add(new Parameter("multiple", "Bit"));

        classIndex = String.valueOf(method.classIndex);
        index = String.valueOf(method.index);
        methodId2 = classIndex + index;
        methodTable.put(methodId2, method); 


        method = new AmqpMethod();
        method.name = "rejectBasic";
        method.classIndex = 60;
        method.index = 90;
        method.returnType = "void";
        method.synchronous = false;
        method.hasContent = false;
        method.allParameters = new ArrayList<Parameter>();
        method.allParameters.add(new Parameter("deliveryTag", "DeliveryTag")); method.allParameters.add(new Parameter("requeue", "Bit"));

        classIndex = String.valueOf(method.classIndex);
        index = String.valueOf(method.index);
        methodId2 = classIndex + index;
        methodTable.put(methodId2, method); 


        method = new AmqpMethod();
        method.name = "recoverAsyncBasic";
        method.classIndex = 60;
        method.index = 100;
        method.returnType = "void";
        method.synchronous = false;
        method.hasContent = false;
        method.allParameters = new ArrayList<Parameter>();
        method.allParameters.add(new Parameter("requeue", "Bit"));

        classIndex = String.valueOf(method.classIndex);
        index = String.valueOf(method.index);
        methodId2 = classIndex + index;
        methodTable.put(methodId2, method); 


        method = new AmqpMethod();
        method.name = "recoverBasic";
        method.classIndex = 60;
        method.index = 110;
        method.returnType = "void";
        method.synchronous = false;
        method.hasContent = false;
        method.allParameters = new ArrayList<Parameter>();
        method.allParameters.add(new Parameter("requeue", "Bit"));

        classIndex = String.valueOf(method.classIndex);
        index = String.valueOf(method.index);
        methodId2 = classIndex + index;
        methodTable.put(methodId2, method); 


        method = new AmqpMethod();
        method.name = "recoverOkBasic";
        method.classIndex = 60;
        method.index = 111;
        method.returnType = "void";
        method.synchronous = true;
        method.hasContent = false;
        method.allParameters = new ArrayList<Parameter>();

        classIndex = String.valueOf(method.classIndex);
        index = String.valueOf(method.index);
        methodId2 = classIndex + index;
        methodTable.put(methodId2, method); 


        method = new AmqpMethod();
        method.name = "selectTx";
        method.classIndex = 90;
        method.index = 10;
        method.returnType = "SelectOk";
        method.synchronous = true;
        method.hasContent = false;
        method.allParameters = new ArrayList<Parameter>();

        classIndex = String.valueOf(method.classIndex);
        index = String.valueOf(method.index);
        methodId2 = classIndex + index;
        methodTable.put(methodId2, method); 


        method = new AmqpMethod();
        method.name = "selectOkTx";
        method.classIndex = 90;
        method.index = 11;
        method.returnType = "void";
        method.synchronous = true;
        method.hasContent = false;
        method.allParameters = new ArrayList<Parameter>();

        classIndex = String.valueOf(method.classIndex);
        index = String.valueOf(method.index);
        methodId2 = classIndex + index;
        methodTable.put(methodId2, method); 


        method = new AmqpMethod();
        method.name = "commitTx";
        method.classIndex = 90;
        method.index = 20;
        method.returnType = "CommitOk";
        method.synchronous = true;
        method.hasContent = false;
        method.allParameters = new ArrayList<Parameter>();

       classIndex = String.valueOf(method.classIndex);
        index = String.valueOf(method.index);
        methodId2 = classIndex + index;
        methodTable.put(methodId2, method); 


        method = new AmqpMethod();
        method.name = "commitOkTx";
        method.classIndex = 90;
        method.index = 21;
        method.returnType = "void";
        method.synchronous = true;
        method.hasContent = false;
        method.allParameters = new ArrayList<Parameter>();

        classIndex = String.valueOf(method.classIndex);
        index = String.valueOf(method.index);
        methodId2 = classIndex + index;
        methodTable.put(methodId2, method); 


        method = new AmqpMethod();
        method.name = "rollbackTx";
        method.classIndex = 90;
        method.index = 30;
        method.returnType = "RollbackOk";
        method.synchronous = true;
        method.hasContent = false;
        method.allParameters = new ArrayList<Parameter>();

        classIndex = String.valueOf(method.classIndex);
        index = String.valueOf(method.index);
        methodId2 = classIndex + index;
        methodTable.put(methodId2, method); 


        method = new AmqpMethod();
        method.name = "rollbackOkTx";
        method.classIndex = 90;
        method.index = 31;
        method.returnType = "void";
        method.synchronous = true;
        method.hasContent = false;
        method.allParameters = new ArrayList<Parameter>();

        classIndex = String.valueOf(method.classIndex);
        index = String.valueOf(method.index);
        methodId2 = classIndex + index;
        methodTable.put(methodId2, method); 

    }
}
