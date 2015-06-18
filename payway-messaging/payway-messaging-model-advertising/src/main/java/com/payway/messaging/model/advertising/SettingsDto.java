/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.messaging.model.advertising;

import com.payway.messaging.model.AbstractDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * AdvertisingSettingsDto
 *
 * @author Sergey Kichenko
 * @created 19.05.15 00:00
 */
@Getter
@AllArgsConstructor
@ToString(callSuper = true)
public final class SettingsDto extends AbstractDto {

    private static final long serialVersionUID = -2060521815298015163L;

    /**
     * Path to server config, ex. 1/2/3/server-config-folder-name
     */
    private final String configPath;

}
