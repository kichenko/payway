/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.messaging.model.message.settings;

import com.payway.messaging.model.AbstractDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * SettingsDto
 *
 * @author Sergey Kichenko
 * @created 19.05.15 00:00
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SettingsDto extends AbstractDto {

    /**
     * Path to server config, ex. 1/2/3/server-config-folder-name
     */
    private String configPath;

}
