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
 * Заголовок MessageID конверта (Envelope).
 *
 * @author Sergey Kichenko
 * @created 24.04.15 00:00
 */
@Getter
@Setter
@ToString
public class MessageIDHeader extends AbstractHeader<String, UUID> {

    private static final long serialVersionUID = -1606615635803210638L;

    public MessageIDHeader(UUID value) {
        super("MessageID", value);
    }
}
