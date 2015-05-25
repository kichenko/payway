/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.messaging.model.message.configuration;

import com.payway.messaging.model.AbstractDto;
import java.util.Collection;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * ConfigurationDto
 *
 * @author Sergey Kichenko
 * @created 19.05.15 00:00
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApplyConfigurationDto extends AbstractDto {

    private static final long serialVersionUID = 3476919207290685928L;

    private Collection<AgentFileDto> agentFile;
    private Collection<AgentFileOwnerDto> agentFileOwner;
}
