/*
 * (c) Sergey Kichenko, 2015. All right reserved.
 */
package com.payway.messaging.core.header;

import com.payway.messaging.core.AbstractHeader;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Заголовок To конверта (Envelope).
 *
 * @author Sergey Kichenko
 * @created 24.04.15 00:00
 */
@Getter
@Setter
@ToString
public class ToHeader extends AbstractHeader<String, String> {

    private static final long serialVersionUID = -1832473408629910582L;

    public ToHeader(String value) {
        super("To", value);
    }
}
