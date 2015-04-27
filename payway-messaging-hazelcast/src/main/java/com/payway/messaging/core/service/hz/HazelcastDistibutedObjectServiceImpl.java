/*
 * (c) Sergey Kichenko, 2015. All right reserved.
 */
package com.payway.messaging.core.service.hz;

import com.hazelcast.core.HazelcastInstance;
import com.payway.messaging.core.service.DistibutedObjectService;

/**
 * Реализация сервиса для работы с распределенными объектами.
 *
 * @author Sergey Kichenko
 * @created 27.04.15 00:00
 */
public class HazelcastDistibutedObjectServiceImpl implements DistibutedObjectService {

    private HazelcastInstance hazelcastInstance;

    @Override
    public Object get(DistibutedObjectKind kind, String name) {

        if (hazelcastInstance != null) {
            if (kind.equals(DistibutedObjectKind.Queue)) {
                return hazelcastInstance.getQueue(name);
            } else if (kind.equals(DistibutedObjectKind.IdGenerator)) {
                return hazelcastInstance.getIdGenerator(name);
            }
        }

        return null;
    }

    @Override
    public Object create(DistibutedObjectKind kind, String prefix) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
