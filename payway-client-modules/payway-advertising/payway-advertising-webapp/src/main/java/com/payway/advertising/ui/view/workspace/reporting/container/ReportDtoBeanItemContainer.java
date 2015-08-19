/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.ui.view.workspace.reporting.container;

import com.payway.messaging.model.reporting.ReportDto;
import com.vaadin.data.util.BeanItemContainer;

/**
 * ReportDtoBeanItemContainer
 *
 * @author Sergey Kichenko
 * @created 12.08.2015
 */
public class ReportDtoBeanItemContainer extends BeanItemContainer< ReportDto> {

    private static final long serialVersionUID = -5299501747511585644L;

    public ReportDtoBeanItemContainer() {
        super(ReportDto.class);
    }
}
