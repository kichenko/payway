/*
 * (c) Sergey Kichenko, 2015. All right reserved.
 */
package com.payway.messaging.core.response.exception;

/**
 * Базовый интерфейс ошибки-ответа.
 *
 * @author Sergey Kichenko
 * @created 23.04.15 00:00
 */
public interface ExceptionResponse {

    Integer code();

    String message();

    String description();

}
