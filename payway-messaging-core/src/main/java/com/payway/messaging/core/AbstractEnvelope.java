/*
 * (c) Sergey Kichenko, 2015. All right reserved.
 */
package com.payway.messaging.core;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.joda.time.LocalDateTime;

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
    private String messageID;
    private LocalDateTime date;
    private LocalDateTime dateExpired;

    /**
     * Тело конверта, содержит полезные данные.
     */
    private Body body = new Body();

}
