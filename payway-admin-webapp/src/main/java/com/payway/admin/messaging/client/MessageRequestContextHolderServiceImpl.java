/*
 * (c) Sergey Kichenko, 2015. All right reserved.
 */
package com.payway.admin.messaging.client;

import java.io.Serializable;
import java.util.Map;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Реализация сервиса хранения контекста запросов.
 *
 * @author Sergey Kichenko
 * @created 27.04.15 00:00
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MessageRequestContextHolderServiceImpl {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MessageContext implements Serializable {

        private static final long serialVersionUID = 7871864510583276539L;

        private UUID messageId;
        private ResponseCallBack callback;
    }

    private Map<UUID, MessageContext> map;

    /**
     * Кладем данные запроса в карту
     *
     * @param id
     * @param ui
     */
    public void put(UUID id, MessageContext ui) {
        map.put(id, ui);
    }

    /**
     * Получаем данные запроса из карты по id
     *
     * @param id
     * @return данные запроса
     */
    public MessageContext get(UUID id) {
        return map.get(id);
    }

    /**
     * Получаем и удаляем из карты данные запроса по id
     *
     * @param id
     * @return данные запроса
     */
    public MessageContext remove(UUID id) {
        return map.remove(id);
    }

}
