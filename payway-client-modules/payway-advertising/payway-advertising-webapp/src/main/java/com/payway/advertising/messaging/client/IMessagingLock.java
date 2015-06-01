/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.messaging.client;

import java.util.concurrent.TimeUnit;

/**
 * IMessagingLock
 *
 * @author Sergey Kichenko
 * @created 01.06.15 00:00
 */
public interface IMessagingLock extends IMessagingObject {
    
    void lock();
    
    void unlock();

    boolean tryLock(long time, TimeUnit unit);
}
