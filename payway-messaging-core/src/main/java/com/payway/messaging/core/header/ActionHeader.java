
/*
 * (c) Sergey Kichenko, 2015. All right reserved.
 */
package com.payway.messaging.core.header;

import com.payway.messaging.core.AbstractHeader;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Заголовок Action конверта (Envelope).
 *
 * @author Sergey Kichenko
 * @created 24.04.15 00:00
 */
@Getter
@Setter
@ToString
public class ActionHeader extends AbstractHeader<String, String> {

    private static final long serialVersionUID = -7122284599817582869L;

    public ActionHeader() {
        super("Action", "");
    }

    public ActionHeader(String value) {
        super("Action", value);
    }
}
