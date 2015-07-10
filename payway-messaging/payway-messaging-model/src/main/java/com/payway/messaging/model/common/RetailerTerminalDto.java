package com.payway.messaging.model.common;

import com.payway.messaging.model.IdentifiableDto;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * Created by mike on 11/06/15.
 */
@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public final class RetailerTerminalDto extends IdentifiableDto {

    private static final long serialVersionUID = 8662939658365643130L;

    private final String terminalName;

    private final String terminalLocation;

    private final String retailerName;

    public RetailerTerminalDto(long id, String terminalName, String terminalLocation, String retailerName) {
        super(id);
        this.terminalName = terminalName;
        this.terminalLocation = terminalLocation;
        this.retailerName = retailerName;
    }

}
