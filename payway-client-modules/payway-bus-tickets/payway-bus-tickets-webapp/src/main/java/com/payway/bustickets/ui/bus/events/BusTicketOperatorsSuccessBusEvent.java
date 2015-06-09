/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.bustickets.ui.bus.events;

import com.payway.commons.webapp.ui.bus.events.AbstractSessionBusEvent;
import com.payway.messaging.model.common.OperatorDto;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * BusTicketOperatorsSuccessBusEvent
 *
 * @author Sergey Kichenko
 * @created 09.06.15 00:00
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BusTicketOperatorsSuccessBusEvent extends AbstractSessionBusEvent {

    private List<OperatorDto> operators;
}
