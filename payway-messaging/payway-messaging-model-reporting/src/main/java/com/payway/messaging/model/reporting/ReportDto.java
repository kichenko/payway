/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.messaging.model.reporting;

import com.payway.messaging.model.IdentifiableDto;
import lombok.Getter;
import lombok.ToString;

/**
 * ReportDto
 *
 * @author Sergey Kichenko
 * @created 12.08.2015
 */
@ToString(callSuper = true)
public final class ReportDto extends IdentifiableDto {

    private static final long serialVersionUID = 1359806820505842395L;

    private final String name;

    private final String description;

    public ReportDto(long id, String name, String description) {
        super(id);
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

}
