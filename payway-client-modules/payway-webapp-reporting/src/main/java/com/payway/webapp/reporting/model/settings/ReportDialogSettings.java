/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.webapp.reporting.model.settings;

import com.payway.messaging.model.reporting.ReportExportFormatTypeDto;
import com.payway.webapp.settings.convertor.annotation.JsonWebAppSettingsConvert;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * ReportDialogSettings
 *
 * @author Sergey Kichenko
 * @created 24.08.2015
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonWebAppSettingsConvert
public final class ReportDialogSettings implements Serializable {

    private static final long serialVersionUID = -3067682670108103178L;

    private Boolean ignorePagination;
    private ReportExportFormatTypeDto format;
}
