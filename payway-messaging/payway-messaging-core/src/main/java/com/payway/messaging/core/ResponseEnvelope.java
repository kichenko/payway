/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.messaging.core;

import lombok.Getter;
import lombok.ToString;

/**
 * Конверт ответа, содержит заголовки (Header) и тело (Body).
 *
 * @author Sergey Kichenko
 * @created 23.04.15 00:00
 */
@Getter
@ToString(callSuper = true)
public class ResponseEnvelope extends AbstractEnvelope {

    private static final long serialVersionUID = 6918309089564715542L;

    private String requestId;

    public ResponseEnvelope(String requestId, String origin, Message body) {
        super(origin, body);
        this.requestId = requestId;
    }

}
