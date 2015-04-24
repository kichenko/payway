/*
 * (c) Sergey Kichenko, 2015. All right reserved.
 */
package com.payway.messaging.core;

import java.io.Serializable;

/**
 * Интерфейс заголовка конверта (Envelope). Конверт может содержать 0...n
 * заголовков.
 *
 * @author Sergey Kichenko
 * @param <K>
 * @param <V>
 * @created 24.04.15 00:00
 */
public interface Header<K extends Serializable, V extends Serializable> extends Serializable {

    K key();

    V value();
}
