/*
 * (c) Sergey Kichenko, 2015. All right reserved.
 */
package com.payway.messaging.core.header;

import com.payway.messaging.core.AbstractHeader;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Заголовок Date конверта (Envelope).
 *
 * @author Sergey Kichenko
 * @created 24.04.15 00:00
 */
@Getter
@Setter
@ToString
public class DateHeader extends AbstractHeader<String, LocalDateTime> {

    private static final long serialVersionUID = -5067799951276570660L;

    public DateHeader(LocalDateTime value) {
        super("Date", value);
    }
}
