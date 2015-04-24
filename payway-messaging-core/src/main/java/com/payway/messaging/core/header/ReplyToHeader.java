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
 * Заголовок ReplyTo конверта (Envelope).
 *
 * @author Sergey Kichenko
 * @created 24.04.15 00:00
 */
@Getter
@Setter
@ToString
public class ReplyToHeader extends AbstractHeader<String, UUID> {

    private static final long serialVersionUID = 2135589792479848041L;

    public ReplyToHeader(UUID value) {
        super("ReplyTo", value);
    }
}
