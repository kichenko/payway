/*
 * (c) Sergey Kichenko, 2015. All right reserved.
 */
package com.payway.messaging.core;

import com.payway.messaging.core.header.CorrelationIDHeader;
import com.payway.messaging.core.header.DateExpiredHeader;
import com.payway.messaging.core.header.DateHeader;
import com.payway.messaging.core.header.MessageIDHeader;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
public class ResponseEnvelope extends AbstractEnvelope {

    private static final long serialVersionUID = 6918309089564715542L;

    /**
     * Набор стандартных заголовков
     */
    private CorrelationIDHeader correlationID = new CorrelationIDHeader();

     public ResponseEnvelope(MessageIDHeader messageID, DateHeader date, DateExpiredHeader dateExpired, CorrelationIDHeader correlationID, Body body) {
        super(messageID, date, dateExpired, body);
        setCorrelationID(correlationID);
    }
}
