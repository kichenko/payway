/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.messaging.model;

import lombok.*;

import java.io.Serializable;

/**
 * AbstractDto
 *
 * @author Sergey Kichenko
 * @created 19.05.15 00:00
 */
@Setter
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
public abstract class AbstractDto implements Serializable {

    protected long id = 0;

}
