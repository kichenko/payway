/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.messaging.model.reporting;

import com.payway.messaging.model.AbstractDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * ReportParameterDto
 *
 * @author Sergey Kichenko
 * @created 06.08.2015
 */
@ToString(callSuper = true)
public class ReportParameterDto extends AbstractDto {

    private static final long serialVersionUID = 4727781243558162322L;

    private final String name;

    private final Object param;

    public ReportParameterDto(String name, Object param) {
        this.name = name;
        this.param = param;
    }

    public String getName() {
        return name;
    }

    public Object getParam() {
        return param;
    }

}
