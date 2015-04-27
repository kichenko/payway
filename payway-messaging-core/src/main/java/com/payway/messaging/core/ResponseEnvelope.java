/*
 * (c) Sergey Kichenko, 2015. All right reserved.
 */
package com.payway.messaging.core;

import com.payway.messaging.core.header.CorrelationIDHeader;
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
}
