/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.commons.webapp.messaging;

import java.io.Serializable;
import org.joda.time.LocalDateTime;

/**
 * Интерфейс контекста сообщения
 *
 * @author Sergey Kichenko
 * @created 29.04.15 00:00
 */
public interface MessageContext extends Serializable {

    long getLifeTime();

    LocalDateTime getCreated();

    ResponseCallBack getCallback();

    boolean isExpired();
}
