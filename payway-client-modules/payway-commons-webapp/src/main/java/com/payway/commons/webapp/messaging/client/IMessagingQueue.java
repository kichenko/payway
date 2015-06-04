/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.commons.webapp.messaging.client;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

/**
 * IMessagingQueue
 *
 * @author Sergey Kichenko
 * @param <E>
 * @created 01.06.15 00:00
 */
public interface IMessagingQueue<E extends Serializable> extends IMessagingObject {

    void put(E e) throws MessagingException;

    boolean offer(E e, long timeout, TimeUnit unit) throws MessagingException;

    E poll(long timeout, TimeUnit unit) throws MessagingException;
}
