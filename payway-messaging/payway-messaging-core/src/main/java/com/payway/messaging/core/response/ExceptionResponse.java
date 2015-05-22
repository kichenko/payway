/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.messaging.core.response;

/**
 * Базовый интерфейс ошибки-ответа.
 *
 * @author Sergey Kichenko
 * @created 23.04.15 00:00
 */
public interface ExceptionResponse extends Response {

    String getCode();

    String getMessage();

    String getDescription();

}
