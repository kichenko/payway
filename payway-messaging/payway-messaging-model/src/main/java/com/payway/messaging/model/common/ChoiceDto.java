package com.payway.messaging.model.common;

import com.payway.messaging.model.AbstractDto;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * Created by mike on 09/06/15.
 */
@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class ChoiceDto extends AbstractDto {
    
    private static final long serialVersionUID = -7576783572000113001L;

    private String mnemonics;

    private String label;

    public ChoiceDto(String mnemonics, String label) {
        this.mnemonics = mnemonics;
        this.label = label;
    }

}
