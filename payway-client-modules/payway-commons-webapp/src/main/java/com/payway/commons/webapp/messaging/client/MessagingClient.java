/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.commons.webapp.messaging.client;

import com.payway.commons.webapp.messaging.client.exception.MessagingException;
import java.io.Serializable;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.locks.Lock;

/**
 * MessagingClient
 *
 * @author Sergey Kichenko
 * @created 01.06.15 00:00
 */
public interface MessagingClient {

    public enum State {

        Started,
        Stopped,
        Connected,
        Disconnected,
    }

    State getState();

    void start() throws MessagingException;

    void startAsync() throws MessagingException;

    void stop();

    <E extends Serializable> BlockingQueue<E> getQueue(String name) throws MessagingException;

    Lock getLock(String name) throws MessagingException;

    <E extends Serializable> BlockingQueue<E> getClientQueue() throws MessagingException;

    <E extends Serializable> BlockingQueue<E> getServerQueue() throws MessagingException;

    String getClientQueueName();

    String getServerQueueName();

    boolean isConnected();
}
