/**
 * Copyright (c) 2007-2012, Kaazing Corporation. All rights reserved.
 */

package com.kaazing.gateway.amqp.client.impl;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public final class EventTargetSupport {
    private static final String CLASS_NAME = EventTargetSupport.class.getName();
    private static final Logger LOG = Logger.getLogger(CLASS_NAME);

    public EventTargetSupport() {
        LOG.entering(CLASS_NAME, "<init>");
    }

    public void addEventListener(String name, EventListener listener) {
        LOG.entering(CLASS_NAME, "addEventListener", listener);
        if (listener == null || name == null || (name.length() == 0)) {
            throw new IllegalArgumentException("Invalid name or listener for addEventListener" + name + " " + listener);
        }
        List<EventListener> list = getListenerList(name, true);
        list.add(listener);
    }

    public void removeEventListener(String name, EventListener listener) {
        LOG.entering(CLASS_NAME, "removeEventListener", listener);
        List<EventListener> list = getListenerList(name, true);
        if (list == null || !list.contains(listener)) {
            throw new IllegalArgumentException("Trying to remove an unregistered listener " + name + " " + listener);
        }
        list.remove(listener);
    }

    public List<EventListener> getListenerList(String name) {
        LOG.entering(CLASS_NAME, "getListenerList", name);
        List<EventListener> ll = getListenerList(name, false);
        LOG.exiting(CLASS_NAME, "getListenerList", ll);
        return ll;
    }

    public List<EventListener> getListenerList(String name, boolean createIfNotPresent) {
        LOG.entering(CLASS_NAME, "getListenerList", new Object[] { name, createIfNotPresent });
        if (name == null || (name.length()== 0)) {
            return null;
        }

        List<EventListener> listenersList = listenersMap.get(name);
        if (listenersList != null) {
            LOG.exiting(CLASS_NAME, "getListenerList", listenersList);
            return listenersList;
        }
        if (!createIfNotPresent) {
            return null;
        }
        listenersList = new ArrayList<EventListener>();
        listenersMap.put(name, listenersList);
        LOG.exiting(CLASS_NAME, "getListenerList", listenersList);
        return listenersList;
    }

    Map<String, List<EventListener>> listenersMap = new HashMap<String, List<EventListener>>();
}
