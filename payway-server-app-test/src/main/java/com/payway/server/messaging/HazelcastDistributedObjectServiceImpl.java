/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.server.messaging;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IdGenerator;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * Реализация сервиса для работы с распределенными объектами чз Hazelcast.
 *
 * @author Sergey Kichenko
 * @created 27.04.15 00:00
 */
@Slf4j
@Getter
@Setter
public class HazelcastDistributedObjectServiceImpl implements DistributedObjectService {

    private IdGenerator idGenerator;

    private HazelcastInstance hazelcastInstance;

    /**
     * Создать очередь по имени
     *
     * @param name имя очереди
     * @return
     */
    @Override
    public Object createQueueByName(String name) {
        return getHazelcastInstance().getQueue(name);
    }

    /**
     * Получить очередь по имени
     *
     * @param name имя очереди
     * @return
     */
    @Override
    public Object getQueueByName(String name) {
        return getHazelcastInstance().getQueue(name);
    }

    @Override
    public String generateQueueName(String template) {
        return String.format("%s%d", template, idGenerator.newId());
    }

}
