package com.payway.messaging.model.common;

import com.payway.messaging.model.AbstractDto;
import lombok.*;

/**
 * Created by mike on 09/06/15.
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class ChoiceDto extends AbstractDto {
    
    private static final long serialVersionUID = -7576783572000113001L;

    private String mnemonics;

    private String label;

}
