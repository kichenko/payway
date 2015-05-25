/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.messaging.core.service;

/**
 * Интерфейс сервиса для работы с распределенными объектами.
 *
 * @author Sergey Kichenko
 * @created 27.04.15 00:00
 */
public interface DistributedObjectService {

    /**
     * Создать очередь по имени
     *
     * @param name имя очереди
     * @return
     */
    Object createQueueByName(String name);

    /**
     * Получить очередь по имени
     *
     * @param name уникальное имя очереди
     * @return
     */
    Object getQueueByName(String name);

    /**
     * Получить уникальное имя очереди по шаблону
     *
     * @param template
     * @return
     */
    String generateQueueName(String template);
}
