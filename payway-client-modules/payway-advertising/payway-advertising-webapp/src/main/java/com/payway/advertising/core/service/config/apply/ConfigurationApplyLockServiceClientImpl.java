/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.core.service.config.apply;

import java.util.concurrent.TimeUnit;
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
public class ConfigurationApplyLockServiceClientImpl implements ConfigurationApplyLockService {

    @Getter
    @Setter
    private volatile boolean lock;

    @Override
    public void lock() {
        lock = true;
    }

    @Override
    public void unlock() {
        lock = false;
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) {
        if (!lock) {
            lock = true;
        }
        return lock;
    }
}
