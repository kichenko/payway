/*
 * (c) Sergey Kichenko, 2015. All right reserved.
 */
package com.payway.server.messaging;

import com.payway.messaging.core.Envelope;
import java.util.concurrent.BlockingQueue;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Обработчик входящих сообщений. Прототип т.к. хранит в себе конверт сообщения.
 * Вопрос, а нужно ли создавать объекты через spring ioc, т.к. доп. нагрузка?
 *
 * @author Sergey Kichenko
 * @created 24.04.15 00:00
 */
@NoArgsConstructor
@AllArgsConstructor
@Component(value = "messageServerRequestHandler")
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class MessageServerRequestHandler implements Runnable {

    @Autowired
    @Qualifier("blockingQueue")
    private BlockingQueue<Envelope> blockingQueue;

    /**
     * Конверт с сообшением
     */
    private Envelope envelope;

    public MessageServerRequestHandler(Envelope envelope) {
        this.envelope = envelope;
    }

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
