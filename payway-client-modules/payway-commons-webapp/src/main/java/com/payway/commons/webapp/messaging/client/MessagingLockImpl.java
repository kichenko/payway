/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.commons.webapp.messaging.client;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import lombok.AccessLevel;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

/**
 * MessagingLockImpl
 *
 * @author Sergey Kichenko
 * @created 01.06.15 00:00
 */
@Slf4j
@ToString
public class MessagingLockImpl implements IMessagingLock {

    @Setter(AccessLevel.PRIVATE)
    private Lock lock;

    @Setter(AccessLevel.PRIVATE)
    private IMessagingClient messagingClient;

    public MessagingLockImpl() {
        //
    }

    public MessagingLockImpl(Lock lock, IMessagingClient messagingClient) {
        setLock(lock);
        setMessagingClient(messagingClient);
    }

    @Override
    public void lock() {
        if (messagingClient != null && IMessagingClient.State.Connected.equals(messagingClient.getState())) {
            try {
                lock.lock();
            } catch (Exception ex) {
                log.error("Bad lock to messaging lock", ex);
            }
        } else {
            log.error("Bad lock to messaging lock, invalid state");
        }
    }

    @Override
    public void unlock() {
        if (messagingClient != null && IMessagingClient.State.Connected.equals(messagingClient.getState())) {
            try {
                lock.unlock();
            } catch (Exception ex) {
                log.error("Bad unlock to messaging lock", ex);
            }
        } else {
            log.error("Bad unlock to messaging lock, invalid state");
        }
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) {
        if (messagingClient != null && IMessagingClient.State.Connected.equals(messagingClient.getState())) {
            try {
                return lock.tryLock(time, unit);
            } catch (Exception ex) {
                log.error("Bad tryLock to messaging queue", ex);
            }
        } else {
            log.error("Bad tryLock to messaging lock, invalid state");
        }

        return false;
    }

    @Override
    public IMessagingClient getMessagingClient() {
        return messagingClient;
    }

    @Override
    public String getName() {
        return "";
    }

}
