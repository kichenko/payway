
/*
 * (c) Sergey Kichenko, 2015. All right reserved.
 */
package com.payway.messaging.core.header;

import com.payway.messaging.core.AbstractHeader;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Заголовок CorrelationID конверта (Envelope).
 *
 * @author Sergey Kichenko
 * @created 24.04.15 00:00
 */
@Getter
@Setter
@ToString
public class CorrelationIDHeader extends AbstractHeader<String, UUID> {

    private static final long serialVersionUID = -8835165738726706881L;

    public CorrelationIDHeader() {
        super("CorrelationID", UUID.randomUUID());
    }

    public CorrelationIDHeader(UUID value) {
        super("CorrelationID", value);
    }
}
