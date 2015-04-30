/*
 * (c) Sergey Kichenko, 2015. All right reserved.
 */
package com.payway.messaging.core;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.joda.time.LocalDateTime;

/**
 * Конверт ответа, содержит заголовки (Header) и тело (Body).
 *
 * @author Sergey Kichenko
 * @created 23.04.15 00:00
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class ResponseEnvelope extends AbstractEnvelope {

    private static final long serialVersionUID = 6918309089564715542L;

    /**
     * Набор стандартных заголовков
     */
    private String correlationID;

    public ResponseEnvelope(String messageID, LocalDateTime date, LocalDateTime dateExpired, String correlationID, Body body) {
        super(messageID, date, dateExpired, body);
        setCorrelationID(correlationID);
    }
}
