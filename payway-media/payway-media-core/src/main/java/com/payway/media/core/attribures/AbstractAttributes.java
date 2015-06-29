/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.media.core.attribures;

import java.io.Serializable;
import java.util.Properties;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * AbstractAttributes
 *
 * @author Sergey Kichenko
 * @created 27.06.15 00:00
 */
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@Setter(AccessLevel.PROTECTED)
public abstract class AbstractAttributes implements Attributes, Serializable {

    private static final long serialVersionUID = 8684881654073193666L;

    protected Properties cpreset;

}
