/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.messaging.model.bustickets;

import com.payway.messaging.model.AbstractDto;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * SettingsDto
 *
 * @author Sergey Kichenko
 * @created 18.06.15 00:00
 */
@Getter
@Setter
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public final class SettingsDto extends AbstractDto {

    private static final long serialVersionUID = -8936272452512462276L;

}
