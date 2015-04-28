/*
 * (c) Sergey Kichenko, 2015. All right reserved.
 */
package com.payway.admin.messaging;

import com.payway.messaging.core.response.ExceptionResponse;
import com.payway.messaging.core.response.SuccessResponse;

/**
 * Интерфес обратного вызова для взаимодействия с UI.
 *
 * @param <S>
 * @param <E>
 * @author Sergey Kichenko
 * @created 25.04.15 00:00
 */
public interface ResponseCallBack<S extends SuccessResponse, E extends ExceptionResponse> {

    void onResponse(S response);

    void onException(E exception);

    void onTimeout();

}
