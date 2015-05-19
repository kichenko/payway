/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.core.service;

import java.util.concurrent.TimeUnit;

/**
 * ConfigurationApplyLockService
 *
 * @author Sergey Kichenko
 * @created 19.05.15 00:00
 */
public interface ConfigurationApplyLockService {

    void lock();

    void unlock();

    boolean tryLock(long time, TimeUnit unit);
}
