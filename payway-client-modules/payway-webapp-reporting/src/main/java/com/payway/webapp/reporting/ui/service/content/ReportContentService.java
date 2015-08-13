/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.webapp.reporting.ui.service.content;

import java.io.File;

/**
 * ReportContentService
 *
 * @author Sergey Kichenko
 * @created 11.08.2015
 */
public interface ReportContentService {

    public interface ReportContentInfo {

        String getRelativeURLPath();

        File getFilePath();
    }

    ReportContentInfo save(String storageId, String fileName, byte[] content) throws Exception;

    void clear(String uid) throws Exception;
}
