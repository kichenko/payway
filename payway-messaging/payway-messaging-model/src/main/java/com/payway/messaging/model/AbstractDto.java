/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.messaging.model;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * AbstractDto
 *
 * @author Sergey Kichenko
 * @created 19.05.15 00:00
 */
@Setter
@Getter
@ToString
public abstract class AbstractDto implements Serializable {

    protected long id;
}
