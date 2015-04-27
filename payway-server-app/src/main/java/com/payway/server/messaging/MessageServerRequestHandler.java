/*
 * (c) Sergey Kichenko, 2015. All right reserved.
 */
package com.payway.server.messaging;

import com.payway.messaging.core.RequestEnvelope;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * Обработчик входящих сообщений. Прототип т.к. хранит в себе конверт сообщения.
 * Вопрос, а нужно ли создавать объекты через spring ioc, т.к. доп. нагрузка?
 *
 * @author Sergey Kichenko
 * @created 24.04.15 00:00
 */
@NoArgsConstructor
@AllArgsConstructor
public class MessageServerRequestHandler implements Runnable {

    /**
     * Конверт с сообшением
     */
    private RequestEnvelope envelope;

    /**
     * Обработка, выполнение бизнес-логики и отправка ответного сообщения.
     */
    @Override
    public void run() {
        if (envelope != null) {
            //
        }
    }

}
