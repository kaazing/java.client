/**
 * Copyright (c) 2007-2012, Kaazing Corporation. All rights reserved.
 */

package com.kaazing.gateway.amqp.client;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.kaazing.gateway.amqp.client.impl.AmqpBuffer;
import com.kaazing.gateway.amqp.client.impl.AmqpBuffer.BasicProperties;

/**
 * AmqpProperties class is used to specify the pre-defined properties as per
 * AMQP 0-9-1 specification. This class provides type-safe convenience getters
 * and setters for the pre-defined or standard AMQP properties.
 * <p>
 * The value of the "headers" property is of type AmqpArguments. Kaazing
 * AMQP implementation uses AmqpArguments to encode the "table". Similarly,
 * Kaazing AMQP implementation decodes the "table" and constructs an instance
 * of AmqpArguments.
 *
 * @see AmqpArguments
 */
public final class AmqpProperties {
    public static final String TEXT_PLAIN = "text/plain";
    public static final String APPLICATION_OCTET_STREAM = "application/octet-stream";
    
    // Properties defined in AMQP 0-9-1 specification.
    public static final String AMQP_PROP_APP_ID = "appId";
    public static final String AMQP_PROP_CONTENT_TYPE = "contentType";
    public static final String AMQP_PROP_CONTENT_ENCODING = "contentEncoding";
    public static final String AMQP_PROP_CORRELATION_ID = "correlationId";
    public static final String AMQP_PROP_DELIVERY_MODE = "deliveryMode";
    public static final String AMQP_PROP_EXPIRATION = "expiration";
    public static final String AMQP_PROP_HEADERS = "headers";
    public static final String AMQP_PROP_MESSAGE_ID = "messageId";
    public static final String AMQP_PROP_PRIORITY = "priority";
    public static final String AMQP_PROP_REPLY_TO = "replyTo";
    public static final String AMQP_PROP_TIMESTAMP = "timestamp";
    public static final String AMQP_PROP_TYPE = "type";
    public static final String AMQP_PROP_USER_ID = "userId";

    private Map<String, Object>    _properties;
    
    public AmqpProperties() {
        _properties = new HashMap<String, Object>();
    }
    
    public AmqpProperties(Map<String, Object> properties) {
        _properties = properties;
        
        if (properties == null) {
            _properties = new HashMap<String, Object>();
        }
        else {
            // Confirm that the passed in map contains just the pre-defined
            // properties.
            boolean                    propFound = false;
            Set<Entry<String, Object>> props = _properties.entrySet();
            
            // For each property available in the set, we confirm whether it is
            // a valid pre-defined property by cross-checking with the
            // AmqpBuffer.basicProperties.
            for (Entry<String, Object> prop : props) {
                propFound = false;
                for (BasicProperties definedProp : AmqpBuffer.basicProperties) {
                    if (prop.getKey().equals(definedProp.name)) {
                        // Move to the next prop in the set.
                        propFound = true;
                        break;
                    }
                }
                
                if (!propFound) {
                    throw new IllegalStateException("Illegal property: '" + prop.getKey() + "' passed");
                }
            }
            
            // Validate the values specified in the map.
            for (Entry<String, Object> prop : props) {
                String propName = prop.getKey();
                Object propValue = prop.getValue();

                if (propValue == null) {
                    throw new IllegalStateException("Null value specified for " +
                                                    "property '" + propName + "'");
                }
                
                if (propName.equals(AMQP_PROP_APP_ID)           ||
                    propName.equals(AMQP_PROP_CONTENT_TYPE)     ||
                    propName.equals(AMQP_PROP_CONTENT_ENCODING) ||
                    propName.equals(AMQP_PROP_CORRELATION_ID)   ||
                    propName.equals(AMQP_PROP_EXPIRATION)       ||
                    propName.equals(AMQP_PROP_MESSAGE_ID)       ||
                    propName.equals(AMQP_PROP_REPLY_TO)         ||
                    propName.equals(AMQP_PROP_TYPE)             ||
                    propName.equals(AMQP_PROP_USER_ID))
                {
                    if (!(propValue instanceof String)) {
                        String s = "Invalid type: Value of '" + propName +
                                   "' should be of type String";
                        throw new IllegalStateException(s);
                    }
                }
                else if (propName.equals(AMQP_PROP_HEADERS)) {
                    if (!(propValue instanceof AmqpArguments)) {
                        String s = "Invalid type: Value of '" + propName +
                                   "' should be of type AmqpArguments";
                        throw new IllegalStateException(s);
                    }
                }
                else if (propName.equals(AMQP_PROP_TIMESTAMP)) {
                    if (!(propValue instanceof Timestamp)) {
                        String s = "Invalid type: Value of '" + propName +
                                   "' should be of type Timestamp";
                        throw new IllegalStateException(s);
                    }
                }
                else if (propName.equals(AMQP_PROP_DELIVERY_MODE)) {
                    if (!(propValue instanceof Integer)) {
                        String s = "Invalid type: Value of '" + propName +
                                   "' should be of type Integer";
                        throw new IllegalStateException(s);
                    }
                    
                    int value = ((Integer)propValue).intValue();
                    if ((value != 1) && (value != 2)) {
                        String s = "Invalid value: Value of '" + propName +
                                   "' should be either 1(non-persistent) or 2(persistent)";
                        throw new IllegalStateException(s);
                    }
                }
                else if (propName.equals(AMQP_PROP_PRIORITY)) {
                    if (!(propValue instanceof Integer)) {
                        String s = "Invalid type: Value of '" + propName +
                                   "' should be of type Integer";
                        throw new IllegalStateException(s);
                    }
                    
                    int value = ((Integer)propValue).intValue();
                    if ((value < 0) || (value > 9)) {
                        String s = "Invalid value: Value of property '" + propName +
                                   "' should be between 0 and 9";
                        throw new IllegalStateException(s);
                    }
                }
                else {
                    String s = "Illegal property '" + propName + "' specified";
                    throw new IllegalStateException(s);
                }
            }
        }
    }
    
    /**
     * Returns the value of "appId" property. A null is returned if the property
     * is not set.
     * 
     * @return String value for "appId" property
     */
    public String getAppId() {
        return (String) _properties.get(AMQP_PROP_APP_ID);
    }

    /**
     * Returns the value of "contentType" property. A null is returned if the
     * property is not set.
     * 
     * @return String value for "contentType" property
     */
    public String getContentType() {
        return (String) _properties.get(AMQP_PROP_CONTENT_TYPE);
    }
    
    /**
     * Returns the value of "contentEncoding" property. A null is returned if
     * the property is not set.
     * 
     * @return String value for "contentEncoding" property
     */
    public String getContentEncoding() {
        return (String) _properties.get(AMQP_PROP_CONTENT_ENCODING);
    }
    
    /**
     * Returns the value of "correlationId" property. A null is returned if the
     * property is not set.
     * 
     * @return String value for "correlationId" property
     */
    public String getCorrelationId() {
        return (String) _properties.get(AMQP_PROP_CORRELATION_ID);
    }

    /**
     * Returns the value of "deliveryMode" property. A null is returned if the
     * property is not set. If deliveryMode is 1, then it indicates 
     * non-persistent mode. If deliveryMode is 2, then it indicates a persistent
     * mode.
     * 
     * @return Integer value between 0 and 9 for "deliveryMode" property
     */
    public Integer getDeliveryMode() {
        return (Integer) _properties.get(AMQP_PROP_DELIVERY_MODE);
    }
    
    /**
     * Returns the value of "expiration" property. A null is returned if the
     * property is not set.
     * 
     * @return String value for "expiration" property
     */
    public String getExpiration() {
        return (String) _properties.get(AMQP_PROP_EXPIRATION);
    }

    /**
     * Returns the value of "headers" property. A null is returned if the
     * property is not set.
     * 
     * @return AmqpArguments as value for "headers" property
     */
    public AmqpArguments getHeaders() {
        return (AmqpArguments) _properties.get(AMQP_PROP_HEADERS);
    }
    
    /**
     * Returns the value of "messageId" property. A null is returned if the
     * property is not set.
     * 
     * @return String value for the "messageId" property
     */
    public String getMessageId() {
        return (String) _properties.get(AMQP_PROP_MESSAGE_ID);
    }

    /**
     * Returns the value of "priority" property. A null is returned if the
     * property is not set.
     * 
     * @return Integer value for "priority" property between 0 and 9
     */
    public Integer getPriority() {
        return (Integer) _properties.get(AMQP_PROP_PRIORITY);
    }
    
    /**
     * Returns a clone of the properties HashMap by shallow copying the values.
     * 
     * @return HashMap with the name-value pairs
     */
    public Map<String, Object> getProperties() {
        // Shallow copy entries to a newly instantiated HashMap.
        Map<String, Object> clone = new HashMap<String, Object>();
        Set<Entry<String, Object>> set = _properties.entrySet();
        
        for (Entry<String, Object> entry : set) {
            // There wouldn't be any entry with a null value.
            clone.put(entry.getKey(), entry.getValue());
        }
        
        return clone;
    }

    /**
     * Returns the value of "replyTo" property. A null is returned if the
     * property is not set.
     * 
     * @return String value for "replyTo" property
     */
    public String getReplyTo() {
        return (String) _properties.get(AMQP_PROP_REPLY_TO);
    }
    
    /**
     * Returns the value of "timestamp" property. A null is returned if the
     * property is not set.
     * 
     * @return Timestamp value for "timestamp" property
     */
    public Timestamp getTimestamp() {
        return (Timestamp) _properties.get(AMQP_PROP_TIMESTAMP);
    }
    
    /**
     * Returns the value of "type" property. A null is returned if the
     * property is not set.
     * 
     * @return String value for "type" property
     */
    public String getType() {
        return (String) _properties.get(AMQP_PROP_TYPE);
    }
    
    /**
     * Returns the value of "userId" property. A null is returned if the
     * property is not set.
     * 
     * @return String value for  "userId" property
     */
    public String getUserId() {
        return (String) _properties.get(AMQP_PROP_USER_ID);
    }
    
    /**
     * Sets the value of "appId" property. If a null value is passed in, it
     * indicates that the property is not set.
     * 
     * @param  appId    value of "appId" property
     */
    public void setAppId(String appId) {
        if (appId == null) {
            _properties.remove(AMQP_PROP_APP_ID);
            return;
        }
        _properties.put(AMQP_PROP_APP_ID, appId);
    }

    /**
     * Sets the value of "contentType" property. If a null value is passed in, it
     * indicates that the property is not set.
     * 
     * @param  contentType    value of "contentType" property
     */
    public void setContentType(String contentType) {
        if (contentType == null) {
            _properties.remove(AMQP_PROP_CONTENT_TYPE);
            return;
        }
        
        _properties.put(AMQP_PROP_CONTENT_TYPE, contentType);
    }
    
    /**
     * Sets the value of "contentEncoding" property. If a null value is passed 
     * in, it indicates that the property is not set.
     * 
     * @param  encoding    value of "contentEncoding" property
     */
    public void setContentEncoding(String encoding) {
        if (encoding == null) {
            _properties.remove(AMQP_PROP_CONTENT_ENCODING);
            return;
        }
        
        _properties.put(AMQP_PROP_CONTENT_ENCODING, encoding);
    }
    
    /**
     * Sets the value of "correlationId" property.  If a null value is passed 
     * in, it indicates that the property is not set.
     * 
     * @param  correlationId    value of "correlationId" property
     */
    public void setCorrelationId(String correlationId) {
        if (correlationId == null) {
            _properties.remove(AMQP_PROP_CORRELATION_ID);
            return;
        }
        
        _properties.put(AMQP_PROP_CORRELATION_ID, correlationId);
    }

    /**
     * Sets the value of "deliveryMode" property.  If a null value is passed 
     * in, it indicates that the property is not set.
     * 
     * @param  deliveryMode    value of "deliveryMode" property
     */
    public void setDeliveryMode(Integer deliveryMode) {
        if (deliveryMode == null) {
            _properties.remove(AMQP_PROP_DELIVERY_MODE);
            return;
        }
        
        // Perhaps, we could do an enum for deliveryMode. But, it will require
        // some major changes in encoding and decoding and we don't have much
        // time to do it across all the clients.
        int value = deliveryMode.intValue();
        if ((value != 1) && (value != 2)) {
            String s = "AMQP 0-9-1 spec mandates 'deliveryMode' value to be " +
                       "either 1(for non-persistent) or 2(for persistent)";
            throw new IllegalStateException(s);
        }
        
        _properties.put(AMQP_PROP_DELIVERY_MODE, deliveryMode);
    }
    
    /**
     * Sets the value of "expiration" property.  If a null value is passed 
     * in, it indicates that the property is not set.
     * 
     * @param  expiration    value of "expiration" property
     */
    public void setExpiration(String expiration) {
        if (expiration == null) {
            _properties.remove(AMQP_PROP_EXPIRATION);
            return;
        }
        
        _properties.put(AMQP_PROP_EXPIRATION, expiration);
    }

    /**
     * Sets the value of "headers" property.  If a null value is passed 
     * in, it indicates that the property is not set.
     * 
     * @param  headers    value of "headers" property
     */
    public void setHeaders(AmqpArguments headers) {
        if (headers == null) {
            _properties.remove(AMQP_PROP_HEADERS);
            return;
        }
        
        _properties.put(AMQP_PROP_HEADERS, headers);
    }
    
    /**
     * Sets the value of "messageId" property. If a null value is passed 
     * in, it indicates that the property is not set.
     * 
     * @param  messageId    value of "messageId" property
     */
    public void setMessageId(String messageId) {
        if (messageId == null) {
            _properties.remove(AMQP_PROP_MESSAGE_ID);
            return;
        }
        
        _properties.put(AMQP_PROP_MESSAGE_ID, messageId);
    }

    /**
     * Sets the value of "priority" property. If a null value is passed 
     * in, it indicates that the property is not set.
     * 
     * @param  priority    value of "priority" property
     */
    public void setPriority(Integer priority) {        
        if (priority == null) {
            _properties.remove(AMQP_PROP_PRIORITY);
            return;
        }
        
        int priorityValue = priority.intValue();
        if ((priorityValue < 0) || (priorityValue > 9)) {
            String s = "AMQP 0-9-1 spec mandates 'priority' value to be between 0 and 9";
            throw new IllegalStateException(s);
        }
        
        _properties.put(AMQP_PROP_PRIORITY, priority);
    }
    
    /**
     * Sets the value of "replyTo" property. If a null value is passed 
     * in, it indicates that the property is not set.
     * 
     * @param  replyTo    value of "replyTo" property
     */
    public void setReplyTo(String replyTo) {
        if (replyTo == null) {
            _properties.remove(AMQP_PROP_REPLY_TO);
            return;
        }
        
        _properties.put(AMQP_PROP_REPLY_TO, replyTo);
    }
    
    /**
     * Sets the value of "timestamp" property. If a null value is passed 
     * in, it indicates that the property is not set.
     * 
     * @param  timestamp    value of "timestamp" property
     */
    public void setTimestamp(Timestamp timestamp) {
        if (timestamp == null) {
            _properties.remove(AMQP_PROP_TIMESTAMP);
            return;
        }
        
        _properties.put(AMQP_PROP_TIMESTAMP, timestamp);
    }
    
    /**
     * Sets the value of "type" property. If a null value is passed 
     * in, it indicates that the property is not set.
     * 
     * @param  type    value of "type" property
     */
    public void setType(String type) {
        if (type == null) {
            _properties.remove(AMQP_PROP_TYPE);
            return;
        }
        
        _properties.put(AMQP_PROP_TYPE, type);
    }
    
    /**
     * Sets the value of "userId" property. If a null value is passed 
     * in, it indicates that the property is not set.
     * 
     * @param  userId    value of "userId" property
     */
    public void setUserId(String userId) {
        if (userId == null) {
            _properties.remove(AMQP_PROP_USER_ID);
            return;
        }
        
        _properties.put(AMQP_PROP_USER_ID, userId);
    }
    
    /**
     * Returns String representation of the properties.
     */
    public String toString() {
        if ((_properties == null) || (_properties.size() == 0)) {
            return "";
        }
        
        Set<Entry<String, Object>> set = _properties.entrySet();
        StringBuffer               buffer = new StringBuffer("{");
        
        for (Entry<String, Object> entry : set) {
            // There wouldn't be any entry with a null value.
            buffer.append(entry.getKey() + ":" + entry.getValue() + ", ");
        }
        
        // Strip the last redundant comma(,) and the space.
        String str = buffer.substring(0, buffer.length() - 2);
        return str + "}";
    }
}
