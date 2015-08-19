/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.messaging.model.reporting.ui;

import java.util.Collection;

/**
 * ListBoxStateDto
 *
 * @author Sergey Kichenko
 * @created 30.07.15 00:00
 */
public class ListBoxStateDto extends EagerDatasourceComponentStateDto {

    private static final long serialVersionUID = -5531889667232365891L;

    public ListBoxStateDto(String name, String caption, Object value, Collection<SimpleComponentModelStateDto> values) {
        setName(name);
        setCaption(caption);
        setValue(value);
        setDatasource(values);
    }

}
