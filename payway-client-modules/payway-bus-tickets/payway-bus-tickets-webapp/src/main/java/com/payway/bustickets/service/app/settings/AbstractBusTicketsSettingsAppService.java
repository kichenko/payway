/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.bustickets.service.app.settings;

import com.payway.bustickets.service.app.settings.model.BusTicketsSessionSettings;
import com.payway.commons.webapp.service.app.settings.AbstractSettingsAppService;
import lombok.Getter;
import lombok.Setter;

/**
 * AbstractBusTicketsSettingsAppService
 *
 * @author Sergey Kichenko
 * @created 05.08.2015
 */
public abstract class AbstractBusTicketsSettingsAppService extends AbstractSettingsAppService<BusTicketsSessionSettings> {

    @Setter
    @Getter
    protected int baggageRatio;
}
