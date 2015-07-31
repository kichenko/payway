/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.messaging.model.reporting;

import com.payway.messaging.model.AbstractDto;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * ReportUIDto
 *
 * @author Sergey Kichenko
 * @created 30.07.15 00:00
 */
@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public final class ReportUIDto extends AbstractDto {

    private static final long serialVersionUID = 4353900234907820219L;
}
