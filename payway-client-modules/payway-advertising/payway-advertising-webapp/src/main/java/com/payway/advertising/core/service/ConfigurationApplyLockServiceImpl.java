/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.core.service;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * ConfigurationApplyLockServiceImpl
 *
 * @author Sergey Kichenko
 * @created 19.05.15 00:00
 */
@Slf4j
public class ConfigurationApplyLockServiceImpl implements ConfigurationApplyLockService {

    @Getter
    @Setter
    private Lock lock;

    @Override
    public void lock() {
        lock.lock();
    }

    @Override
    public void unlock() {
        lock.unlock();
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) {

        boolean buzy = false;

        try {
            buzy = lock.tryLock(time, unit);
        } catch (Exception ex) {
            log.error("Error try to lock", ex);
        }
        
        return buzy;
    }
}