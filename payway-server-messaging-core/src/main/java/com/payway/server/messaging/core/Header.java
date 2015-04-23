/*
 * (c) Sergey Kichenko, 2015. All right reserved.
 */
package com.payway.server.messaging.core;

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
 * @param <T>
 * @created 23.04.15 00:00
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
public class Header<T extends Serializable> implements Serializable {

    private static final long serialVersionUID = 3712121384752983273L;

    private T data;
}
