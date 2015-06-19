/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.core.service.config.apply;

import com.payway.advertising.core.service.file.FileSystemObject;
import com.vaadin.ui.UI;

/**
 * ConfigurationApplyService
 *
 * @author Sergey Kichenko
 * @created 19.05.15 00:00
 */
public interface ConfigurationApplyService {

    void apply(final UI currentUI, final String userName, final FileSystemObject localPath, final FileSystemObject serverPath, ApplyConfigRunCallback result);

    boolean cancel();

    ApplyConfigurationStatus getStatus();
}
