package com.payway.messaging.model.common;

import com.payway.messaging.model.AbstractDto;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by mike on 15/06/15.
 */
@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class RetailerTerminalsDto extends AbstractDto {

    private static final long serialVersionUID = -8894982584836559568L;

    final List<RetailerTerminalDto> retailerTerminals = new LinkedList<>();

    public void add(Long id, String terminalName, String terminalLocation, String retailerName) {
        retailerTerminals.add(new RetailerTerminalDto(id, terminalName, terminalLocation, retailerName));
    }

    public boolean isEmpty() {
        return retailerTerminals.isEmpty();
    }

}
