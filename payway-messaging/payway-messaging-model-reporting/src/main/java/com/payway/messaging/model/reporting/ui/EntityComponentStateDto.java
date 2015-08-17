/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.messaging.model.reporting.ui;

import java.util.Collection;

/**
 * EntityComponentStateDto
 *
 * @author Sergey Kichenko
 * @created 30.07.15 00:00
 */
public class EntityComponentStateDto extends EagerDatasourceComponentStateDto {

    private static final long serialVersionUID = -2271105383177151197L;

    public enum ViewStyle {
        Combobox,
        Listbox
    }

    protected ViewStyle style;

    public ViewStyle getStyle() {
        return style;
    }

    public void setStyle(ViewStyle style) {
        this.style = style;
    }

    public EntityComponentStateDto() {
        style = ViewStyle.Combobox;
    }

    public EntityComponentStateDto(String name, String caption, ViewStyle style, Object value, Collection<SimpleComponentModelStateDto> values) {
        setName(name);
        setCaption(caption);
        setStyle(style);
        setValue(value);
        setDatasource(values);
    }

}
