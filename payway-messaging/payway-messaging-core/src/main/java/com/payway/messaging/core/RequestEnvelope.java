/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.messaging.core;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.joda.time.LocalDateTime;

/**
 * Конверт запроса, содержит заголовки (Header) и тело (Body).
 *
 * @author Sergey Kichenko
 * @created 23.04.15 00:00
 */
@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
public class RequestEnvelope extends AbstractEnvelope {

    private static final long serialVersionUID = -5394044189519318966L;

    /**
     * Набор стандартных заголовков
     */
    private String replyTo;

    public RequestEnvelope(String messageID, LocalDateTime date, LocalDateTime dateExpired, String replyTo, Body body) {
        super(messageID, date, dateExpired, body);
        setReplyTo(replyTo);
    }
}
