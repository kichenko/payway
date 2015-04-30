/*
 * (c) Sergey Kichenko, 2015. All right reserved.
 */
package com.payway.messaging.core.request;

import com.payway.messaging.core.Message;

/**
 * Абстрактный базовый класс сообщения-запроса, хранится в теле (Body) конверта.
 *
 * @author Sergey Kichenko
 * @created 23.04.15 00:00
 */
public abstract class Request implements Message {

    private static final long serialVersionUID = -9014705301031179506L;

}
