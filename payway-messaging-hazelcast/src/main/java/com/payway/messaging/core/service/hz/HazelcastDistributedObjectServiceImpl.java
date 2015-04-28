/*
 * (c) Sergey Kichenko, 2015. All right reserved.
 */
package com.payway.messaging.core.service.hz;

import com.hazelcast.core.HazelcastInstance;
import com.payway.messaging.core.service.DistributedObjectService;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * Реализация сервиса для работы с распределенными объектами.
 *
 * @author Sergey Kichenko
 * @created 27.04.15 00:00
 */
@Getter
@Setter
@Slf4j
public class HazelcastDistributedObjectServiceImpl implements DistributedObjectService {

    private HazelcastInstance hazelcastInstance;
    private String idGeneratorName;
    private String prefixName;

    /**
     * Создать новую очередь. Имя очериди = префикс+id
     *
     * @return
     */
    @Override
    public Object createQueue() {
        return hazelcastInstance.getQueue(getPrefixName() + getHazelcastInstance().getIdGenerator(getIdGeneratorName()).newId());
    }

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
     * Получить генератор идентификаторов
     *
     * @return
     */
    @Override
    public Object getIdGenerator() {
        return getHazelcastInstance().getIdGenerator(getIdGeneratorName());
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
}
