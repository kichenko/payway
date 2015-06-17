/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.bustickets.service.app;

import com.payway.bustickets.core.BusTicketsSettings;

/**
 * AppService
 *
 * @author Sergey Kichenko
 * @created 06.06.15 00:00
 */
public interface AppService {

    BusTicketsSettings getBusTicketsSettings();

    boolean setBusTicketsSettings(BusTicketsSettings settings);
}
