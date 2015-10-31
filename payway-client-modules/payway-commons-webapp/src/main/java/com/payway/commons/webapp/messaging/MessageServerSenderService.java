/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.commons.webapp.messaging;

import com.payway.messaging.core.request.Request;

/**
 * Интрефейс сервиса отправки сообщений на сервер.
 *
 * @author Sergey Kichenko
 * @created 27.04.15 00:00
 */
public interface MessageServerSenderService {

    void auth(String userName, String password, String remoteAddress, ResponseCallBack callback);

    void auth(String token, String remoteAddress, ResponseCallBack callback);

    void sendMessage(Request request, ResponseCallBack callback);

}
