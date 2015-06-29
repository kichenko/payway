/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.core.service.app.settings;

import com.payway.advertising.core.service.exception.ServiceException;

/**
 * DatabaseAppSettings
 *
 * @author Sergey Kichenko
 * @created 29.06.15 00:00
 */
public interface DatabaseAppSettings {

    void save() throws ServiceException;

    void load() throws ServiceException;
}
