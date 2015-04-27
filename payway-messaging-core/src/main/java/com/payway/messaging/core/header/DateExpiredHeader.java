/*
 * (c) Sergey Kichenko, 2015. All right reserved.
 */
package com.payway.messaging.core.header;

import com.payway.messaging.core.AbstractHeader;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.joda.time.LocalDateTime;

/**
 * Заголовок Date конверта (Envelope).
 *
 * @author Sergey Kichenko
 * @created 24.04.15 00:00
 */
@Getter
@Setter
@ToString
public class DateExpiredHeader extends AbstractHeader<String, LocalDateTime> {

    private static final long serialVersionUID = -5067799951276570660L;

    public DateExpiredHeader() {
        super("DateExpired", new LocalDateTime());
    }

    public DateExpiredHeader(LocalDateTime value) {
        super("DateExpired", value);
    }
}
