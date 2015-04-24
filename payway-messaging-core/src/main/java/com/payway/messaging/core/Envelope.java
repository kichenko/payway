/*
 * (c) Sergey Kichenko, 2015. All right reserved.
 */
package com.payway.messaging.core;

import java.io.Serializable;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Конверт, содержит заголовки (Header) и тело (Body).
 *
 * @author Sergey Kichenko
 * @created 23.04.15 00:00
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class Envelope implements Serializable {

    private static final long serialVersionUID = 6682385096379731333L;

    /**
     * Набор заголовков конверта от 0...n. Определён стандартный набор
     * заголовков, см. @see com.payway.messaging.core.header
     */
    private Set<Header> headers;

    /**
     * Тело конверта, содержит полезные данные.
     */
    private Body body;

}
