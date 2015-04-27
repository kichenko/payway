/*
 * (c) Sergey Kichenko, 2015. All right reserved.
 */
package com.payway.admin.messaging.client;

import com.payway.messaging.core.response.ExceptionResponse;
import com.payway.messaging.core.response.SuccessResponse;

/**
 * Обработчик получения клиентских пакетов. Всего будет один объект, больше не
 * нужно.
 *
 * @author Sergey Kichenko
 * @param <S>
 * @param <E>
 * @created 25.04.15 00:00
 */
public interface ResponseCallBack<S extends SuccessResponse, E extends ExceptionResponse> {

    void onResponse(S response);

    void onException(E exception);

    void onTimeout();

}
