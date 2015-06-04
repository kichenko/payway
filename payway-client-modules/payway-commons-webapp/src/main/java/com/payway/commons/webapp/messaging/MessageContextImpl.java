/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.commons.webapp.messaging;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * Реализация интерфейса контекста сообщения
 *
 * @author Sergey Kichenko
 * @created 29.04.15 00:00
 */
@NoArgsConstructor
@AllArgsConstructor
public class MessageContextImpl implements MessageContext {

    private static final long serialVersionUID = 7871864510583276539L;

    private String messageId;
    private ResponseCallBack callback;

    @Override
    public ResponseCallBack getCallback() {
        return callback;
    }
}
