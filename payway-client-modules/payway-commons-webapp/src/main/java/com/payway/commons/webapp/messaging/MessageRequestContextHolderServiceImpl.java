/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.commons.webapp.messaging;

import java.util.Iterator;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * MessageRequestContextHolderServiceImpl
 *
 * @author Sergey Kichenko
 * @created 27.04.15 00:00
 */
@Slf4j
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MessageRequestContextHolderServiceImpl implements MessageRequestContextHolderService {

    /**
     * Карта, в которой хранится контекст отправленных сообщений
     */
    private Map<String, MessageContext> map;

    /**
     * Кладем данные запроса в карту
     *
     * @param id
     * @param context
     */
    @Override
    public void put(String id, MessageContext context) {
        map.put(id, context);
    }

    /**
     * Получаем данные запроса из карты по id
     *
     * @param id
     * @return данные запроса
     */
    @Override
    public MessageContext get(String id) {
        return map.get(id);
    }

    /**
     * Получаем и удаляем из карты данные запроса по id
     *
     * @param id
     * @return данные запроса
     */
    @Override
    public MessageContext remove(String id) {
        return map.remove(id);
    }

    @Override
    public void cleanup() {

        log.debug("Start cleanup message context...");

        Iterator<Map.Entry<String, MessageContext>> iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, MessageContext> entry = iterator.next();
            if (entry.getValue().isExpired()) {
                log.debug("Find expired message context, removing from map - [{}]", entry.getValue());
                if (entry.getValue().getCallback() != null) {
                    log.debug("Calling timeout event...");
                    entry.getValue().getCallback().onTimeout();
                }
                iterator.remove();
            }
        }

        log.debug("Stop cleanup message context...");
    }
}
