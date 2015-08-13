/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.webapp.reporting.components.model;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * SimpleSelectionModel
 *
 * @author Sergey Kichenko
 * @created 06.08.2015
 */
@Setter
@Getter
@AllArgsConstructor
public class SimpleSelectionModel implements Serializable {

    private static final long serialVersionUID = -3450452158031872467L;

    private Object id;
    private String caption;
    private boolean selected;
}
