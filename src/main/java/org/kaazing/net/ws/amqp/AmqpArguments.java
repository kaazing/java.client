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

package org.kaazing.net.ws.amqp;

import java.util.ArrayList;
import java.util.List;

/**
 * AmqpArguments class is used to send arguments and custom headers.
 *
 */
public final class AmqpArguments {
    /**
     * AmqpTableEntry represents a single entry in the table containing
     * custom headers.
     */
    public static class AmqpTableEntry {
        private String key;
        private Object value;
        private String type;
        
        public AmqpTableEntry(String key, Object value, String type) {
            if (!(type.equals("int"))        && 
                !(type.equals("Longstr"))    &&
                !(type.equals("FieldTable")) &&
                !(type.equals("Void"))) {
                String s = "Invalid entry type '" + type + "'. " + 
                           "Legal values are 'int', 'Longstr', and 'FieldTable', and 'Void'";

                throw new IllegalStateException(s);
            }
            
            // If value is null, we will use Void as the type so that the
            // encoding and decoding can work properly.
            if (value == null) {
                type = "Void";
            }
            
            this.key = key;
            this.value = value;
            this.type = type;
        }
        
        /**
         * Returns the key for the entry.
         * 
         * @return
         */
        public String getKey() {
            return key;
        }
        
        /**
         * Returns the type for the entry.
         * 
         * @return
         */
        public String getType() {
            return type;
        }
        
        /**
         * Returns the value for the entry.
         * 
         * @return
         */
        public Object getValue() {
            return value;
        }
        
        /**
         * Returns the string representation of the entry.
         * 
         * @return
         */
        public String toString() {
            return "{\"key\":\"" + key + "\",\"value\":\"" + value + "\",\"type\":\"" + type + "\"}";
        }
    }

    public ArrayList<AmqpTableEntry> tableEntryArray = new ArrayList<AmqpTableEntry>();
    
    /**
     * Returns the string/JSON representation of the table of entries.
     * 
     * @return String/JSON representation of the table of entries
     */
    public String toString() {
        String s = "{";
        int    i = 0;
        for (AmqpTableEntry entry : tableEntryArray) {
            s = s + "\"" + i + "\":" + entry.toString() + ",";
            i++;
        }
        
        // i has been incremented earlier so no need to
        // increment it again.
        s = s + "\"length\":" + i;
        
        return s + "}";
    }

    private void add(String key, Object value, String type) {
        AmqpTableEntry table = new AmqpTableEntry(key, value, type);
 
        if (tableEntryArray == null) {
            tableEntryArray = new ArrayList<AmqpTableEntry>();
        }
        
        this.tableEntryArray.add(table);
    }

    /**
     * Adds an integer entry to the AmqpArguments table.
     *
     * @param key    name of an entry
     * @param value  integer value of an entry
     * @return AmqpArguments object that holds the table of entries
     */
    public AmqpArguments addInteger(String key, int value) {
        this.add(key, value, "int");
        return this;
    }

    /**
     * Adds a long string entry to the AmqpArguments table.
     *
     * @param key    name of an entry
     * @param value  long string value of an entry
     * @return AmqpArguments object that holds the table of entries
     */
    public AmqpArguments addLongString(String key, String value) {
        this.add(key, value, "Longstr");
        return this;
    }
    
    /**
     * Returns a list of AmqpTableEntry objects that matches the specified key.
     * If a null key is passed in, then a null is returned. Also, if the internal
     * structure is null, then a null is returned.
     * 
     * @param key    name of the entry
     * @return List<AmqpTableEntry> object with matching key
     */
    public List<AmqpTableEntry> getEntries(String key) {
        if ((key == null) || (tableEntryArray == null)) {
            return null;
        }
        
        List<AmqpTableEntry> entries = new ArrayList<AmqpTableEntry>();
        for (AmqpTableEntry entry : tableEntryArray) {
            if (entry.key.equals(key)) {
                entries.add(entry);
            }
        }
        return entries;
    }
}
