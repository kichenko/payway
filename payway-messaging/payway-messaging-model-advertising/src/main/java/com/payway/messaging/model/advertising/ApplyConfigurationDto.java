/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.messaging.model.advertising;

import com.payway.messaging.model.AbstractDto;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

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
@ToString(callSuper = true)
public class ApplyConfigurationDto extends AbstractDto {

    private static final long serialVersionUID = 3476919207290685928L;

    private Set<AgentFileDto> agentFile;
    private Set<AgentFileOwnerDto> agentFileOwner;
}
