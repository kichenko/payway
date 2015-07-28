/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.commons.webapp.messaging;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * MessageRequestContextHolderServiceImpl
 *
 * @author Sergey Kichenko
 * @created 27.04.15 00:00
 */
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

}
