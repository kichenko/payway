/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.core.service;

import java.util.concurrent.TimeUnit;
import org.springframework.stereotype.Component;

/**
 * ConfigurationApplyLockServiceImpl
 *
 * @author Sergey Kichenko
 * @created 19.05.15 00:00
 */
@Component(value = "configurationApplyLockService")
public class ConfigurationApplyLockServiceImpl implements ConfigurationApplyLockService {

    private volatile boolean busy;

    @Override
    public void lock() {
        busy = true;
    }

    @Override
    public void unlock() {
        busy = false;
    }

    @Override
    public boolean isLock() {
        return busy = true;
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) {
        return (busy = true);
    }
}
