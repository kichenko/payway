/*
 * (c) Sergey Kichenko, 2015. All right reserved.
 */
package com.payway.messaging.core;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Заголовок конверта (Envelope). Конверт может содержать 0...n заголовков.
 *
 * @author Sergey Kichenko
 * @param <K>
 * @param <V>
 * @created 23.04.15 00:00
 */
@Getter
@Setter
@AllArgsConstructor
@ToString(exclude = {"value"})
@EqualsAndHashCode(exclude = {"value"})
public abstract class AbstractHeader<K extends Serializable, V extends Serializable> implements Header<K, V> {

    private K key;
    private V value;

    @Override
    public K key() {
        return key;
    }

    @Override
    public V value() {
        return value;
    }

}
