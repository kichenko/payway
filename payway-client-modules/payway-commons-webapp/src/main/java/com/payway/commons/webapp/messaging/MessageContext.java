/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.commons.webapp.messaging;

import java.io.Serializable;

/**
 * Интерфейс контекста сообщения
 *
 * @author Sergey Kichenko
 * @created 29.04.15 00:00
 */
public interface MessageContext extends Serializable {

    ResponseCallBack getCallback();
}
