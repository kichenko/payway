/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.commons.webapp.messaging.client;

import java.io.Serializable;

/**
 * IMessagingClient
 *
 * @author Sergey Kichenko
 * @created 01.06.15 00:00
 */
public interface IMessagingClient {

    public enum State {

        Disconnected,
        Connecting,
        Connected,
        Shutdown
    }

    State getState();

    void shutdown();

    void construct() throws Exception;

    <E extends Serializable> IMessagingQueue getClientQueue() throws MessagingException;

    <E extends Serializable> IMessagingQueue getServerQueue() throws MessagingException;

    <E extends Serializable> IMessagingQueue<E> getQueue(String name) throws MessagingException;

    IMessagingLock getLock(String name) throws MessagingException;
}
