/*
 * (c) Sergey Kichenko, 2015. All right reserved.
 */
package com.payway.advertising.messaging;

import com.payway.messaging.core.response.ExceptionResponse;
import com.payway.messaging.core.response.SuccessResponse;

/**
 * Интерфес обратного вызова для взаимодействия с UI.
 *
 * @param <S>
 * @param <R>
 * @param <E>
 * @author Sergey Kichenko
 * @created 25.04.15 00:00
 */
public interface ResponseCallBack<R extends SuccessResponse, E extends ExceptionResponse> {

    /**
     * Любой корректный ответ от сервера, например успешная/неуспешная
     * аутентификация/авторизация
     *
     */
    void onServerResponse(final R response);

    /**
     * Информирование об ошибке на сервере, например ошибка обращения к БД при
     * выполнении бизнес-логики на сервере порождает подобное исключение.
     *
     */
    void onServerException(final E exception);

    /**
     * Информирование о локальной ошибке, происходит до отправки сообщения на
     * сервер.
     *
     */
    void onLocalException();

    /**
     * Если ответ от сервера не приходит в течении определенного времени -
     * генерируется таймаут.
     *
     */
    void onTimeout();

}
