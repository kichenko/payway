/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.media.core.container;

import java.io.Serializable;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * AbstractFormatContainer
 *
 * @author Sergey Kichenko
 * @created 27.06.15 00:00
 */
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public abstract class AbstractFormatContainer implements FormatContainer, Serializable {

    private static final long serialVersionUID = -4128684700126919715L;

    protected String id;
    protected String name;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }
}
