/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.messaging.model.reporting.ui;

import java.util.Collection;
import lombok.Getter;
import lombok.Setter;

/**
 * DatasourceComponentStateDto
 *
 * @author Sergey Kichenko
 * @created 30.07.15 00:00
 */
@Getter
@Setter
public abstract class DatasourceComponentStateDto extends FieldComponentStateDto {

    private static final long serialVersionUID = -3818585365329901422L;

    protected Collection<? extends AbstractModelStateDto> datasource;
}
