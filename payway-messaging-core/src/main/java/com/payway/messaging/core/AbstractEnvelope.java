/*
 * (c) Sergey Kichenko, 2015. All right reserved.
 */
package com.payway.messaging.core;

import com.payway.messaging.core.header.DateExpiredHeader;
import com.payway.messaging.core.header.DateHeader;
import com.payway.messaging.core.header.MessageIDHeader;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Базовый конверт, содержит набор стандартных заголовков (Header) и тело
 * (Body).
 *
 * @author Sergey Kichenko
 * @created 23.04.15 00:00
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public abstract class AbstractEnvelope implements Envelope {

    /**
     * Набор базовых заголовков
     */
    private MessageIDHeader messageID = new MessageIDHeader();
    private DateHeader date = new DateHeader();
    private DateExpiredHeader dateExpired = new DateExpiredHeader();

    /**
     * Тело конверта, содержит полезные данные.
     */
    private Body body = new Body();

}
