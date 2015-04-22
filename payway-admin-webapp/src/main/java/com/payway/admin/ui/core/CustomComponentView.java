/*
 * (c) Sergey Kichenko, 2015. All right reserved.
 */
package com.payway.admin.ui.core;

import com.payway.admin.core.service.event.AdminEventBusService;
import com.vaadin.ui.CustomComponent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * AdminCustomComponentView
 *
 * @author Sergey Kichenko
 * @created 22.04.15 00:00
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public abstract class CustomComponentView extends CustomComponent {

    protected AdminEventBusService adminEventBusService;
}
