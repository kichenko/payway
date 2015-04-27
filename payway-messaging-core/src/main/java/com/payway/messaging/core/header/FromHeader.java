/*
 * (c) Sergey Kichenko, 2015. All right reserved.
 */
package com.payway.messaging.core.header;

import com.payway.messaging.core.AbstractHeader;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Заголовок From конверта (Envelope).
 *
 * @author Sergey Kichenko
 * @created 24.04.15 00:00
 */
@Getter
@Setter
@ToString
public class FromHeader extends AbstractHeader<String, String> {

    private static final long serialVersionUID = -5067799951276570660L;

    public FromHeader() {
        super("From", "");
    }

    public FromHeader(String value) {
        super("From", value);
    }
}
