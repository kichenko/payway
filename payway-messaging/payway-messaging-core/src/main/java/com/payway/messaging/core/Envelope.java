/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.messaging.core;

import java.io.Serializable;

/**
 * Базовый интерфейс конверта
 *
 * @author Sergey Kichenko
 * @created 23.04.15 00:00
 */
public interface Envelope extends Serializable {

    String getMessageId();

    String getOrigin();

    long getTimestamp();

    Message getBody();

}
