/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.messaging.model.reporting.ui;

import lombok.Getter;
import lombok.Setter;

/**
 * EntityComponent
 *
 * @author Sergey Kichenko
 * @created 30.07.15 00:00
 */
@Getter
@Setter
public class EntityComponent extends EagerDatasourceComponent {

    private static final long serialVersionUID = -2271105383177151197L;

    public enum ViewStyle {

        Combobox,
        Listbox
    }

    protected ViewStyle style;

    public EntityComponent() {
        style = ViewStyle.Combobox;
    }
}
