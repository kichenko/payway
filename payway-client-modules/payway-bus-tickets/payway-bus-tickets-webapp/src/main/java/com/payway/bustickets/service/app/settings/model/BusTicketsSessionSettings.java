/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.bustickets.service.app.settings.model;

import com.payway.commons.webapp.service.app.settings.model.AbstractAppSessionSettings;
import com.payway.messaging.model.common.OperatorDto;
import com.payway.messaging.model.common.RetailerTerminalDto;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * BusTicketsSessionSettings
 *
 * @author Sergey Kichenko
 * @created 15.06.15 00:00
 */
@Getter
@AllArgsConstructor
public final class BusTicketsSessionSettings extends AbstractAppSessionSettings {

    private static final long serialVersionUID = 5356465285068553304L;

    private final List<OperatorDto> operators;
    private final List<RetailerTerminalDto> terminals;
    private final RetailerTerminalDto currentRetailerTerminal;
}
