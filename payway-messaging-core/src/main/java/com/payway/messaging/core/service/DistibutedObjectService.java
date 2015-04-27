/*
 * (c) Sergey Kichenko, 2015. All right reserved.
 */
package com.payway.messaging.core.service;

/**
 * Интерфейс сервиса для работы с распределенными объектами.
 *
 * @author Sergey Kichenko
 * @created 27.04.15 00:00
 */
public interface DistibutedObjectService {

    public enum DistibutedObjectKind {

        IdGenerator,
        Queue
    }

    Object get(DistibutedObjectKind kind, String name);

    Object create(DistibutedObjectKind kind, String prefix);
}
