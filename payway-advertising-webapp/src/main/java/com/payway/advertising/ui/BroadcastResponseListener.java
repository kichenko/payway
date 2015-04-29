/*
 * (c) Sergey Kichenko, 2015. All right reserved.
 */
package com.payway.advertising.ui;

import com.payway.messaging.core.ResponseEnvelope;

/**
 * Слушатель событий-ответов от сервера. Имплементируется UI.
 *
 * @author Sergey Kichenko
 * @created 27.04.15 00:00
 */
public interface BroadcastResponseListener {

    void receiveResponse(ResponseEnvelope envelope);
}
