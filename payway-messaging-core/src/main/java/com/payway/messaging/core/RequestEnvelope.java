/*
 * (c) Sergey Kichenko, 2015. All right reserved.
 */
package com.payway.messaging.core;

import com.payway.messaging.core.header.DateExpiredHeader;
import com.payway.messaging.core.header.DateHeader;
import com.payway.messaging.core.header.MessageIDHeader;
import com.payway.messaging.core.header.ReplyToHeader;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Конверт запроса, содержит заголовки (Header) и тело (Body).
 *
 * @author Sergey Kichenko
 * @created 23.04.15 00:00
 */
@Getter
@Setter
@NoArgsConstructor
public class RequestEnvelope extends AbstractEnvelope {

    private static final long serialVersionUID = -5394044189519318966L;

    /**
     * Набор стандартных заголовков
     */
    private ReplyToHeader replyTo = new ReplyToHeader();

    public RequestEnvelope(MessageIDHeader messageID, DateHeader date, DateExpiredHeader dateExpired, ReplyToHeader replyTo, Body body) {
        super(messageID, date, dateExpired, body);
        setReplyTo(replyTo);
    }
}
