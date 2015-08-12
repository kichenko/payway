/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.commons.webapp.bus.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * AbstractSessionAppEventBus
 *
 * @author Sergey Kichenko
 * @created 11.08.2015
 */
@Getter
@AllArgsConstructor
public abstract class AbstractSessionAppEventBus extends AbstractAppEventBus {

    private static final long serialVersionUID = -5260240786100244537L;

    private final String id;
}
