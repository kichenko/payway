package com.payway.messaging.model.common;

import com.payway.messaging.model.AbstractDto;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * Created by mike on 11/06/15.
 */
@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class RetailerTerminalDto extends AbstractDto {

    private static final long serialVersionUID = 8662939658365643130L;

    final private String terminalName;

    final private String terminalLocation;

    final private String retailerName;

    public RetailerTerminalDto(long id, String terminalName, String terminalLocation, String retailerName) {
        super(id);
        this.terminalName = terminalName;
        this.terminalLocation = terminalLocation;
        this.retailerName = retailerName;
    }

}
