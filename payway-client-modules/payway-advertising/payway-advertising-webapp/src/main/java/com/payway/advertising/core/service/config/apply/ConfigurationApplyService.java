/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.core.service.config.apply;

import com.payway.advertising.core.service.file.FileSystemObject;

/**
 * ConfigurationApplyService
 *
 * @author Sergey Kichenko
 * @created 19.05.15 00:00
 */
public interface ConfigurationApplyService {

    void apply(final String userName, final String configurationName, final FileSystemObject localPath, final FileSystemObject serverPath, ApplyConfigRunCallback result);

    boolean cancel();

    ApplyConfigurationStatus getStatus();
}
