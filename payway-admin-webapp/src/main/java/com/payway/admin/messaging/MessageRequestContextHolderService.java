/*
 * (c) Sergey Kichenko, 2015. All right reserved.
 */
package com.payway.admin.messaging;

/**
 * Интерфейс сервиса хранения контекста запросов.
 *
 * @author Sergey Kichenko
 * @created 27.04.15 00:00
 */
public interface MessageRequestContextHolderService {

   

    /**
     * Кладем данные запроса в карту
     *
     * @param id
     * @param ui
     */
    void put(String id, MessageContext context);

    /**
     * Получаем данные запроса из карты по id
     *
     * @param id
     * @return данные запроса
     */
    MessageContext get(String id);

    /**
     * Получаем и удаляем из карты данные запроса по id
     *
     * @param id
     * @return данные запроса
     */
    MessageContext remove(String id);
}
