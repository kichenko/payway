/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.webapp.reporting.ui.service.content.impl;

import com.google.common.eventbus.Subscribe;
import com.payway.commons.webapp.bus.event.SessionDestroyedAppEventBus;
import com.payway.commons.webapp.config.SubscribeOnAppEventBus;
import com.payway.webapp.reporting.ui.service.content.ReportContentService;
import com.payway.webapp.reporting.utils.ZipFileUtil;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.net.URI;
import java.util.UUID;
import javax.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * SessionZipReportContentService
 *
 * @author Sergey Kichenko
 * @created 11.08.2015
 */
@Slf4j
@Getter
@Setter
@SubscribeOnAppEventBus
@Component(value = "app.reporting.SessionZipReportContentService")
public class SessionZipReportContentService implements ReportContentService {

    @Value("${app.config.reporting.root.path:file:///${user.home}/var/apps/reporting}")
    private String rootPath;

    @Subscribe
    public void onSessionDestroyedAppEventBus(SessionDestroyedAppEventBus event) {

        try {
            clear(event.getId());
        } catch (Exception ex) {
            //NOP
        }
    }

    @PostConstruct
    public void postConstruct() {
        if (!StringUtils.isBlank(getRootPath())) {
            setRootPath(getRootPath().replace("\\", "/"));
        }
    }

    @Override
    public ReportContentInfo save(String storageId, String fileName, byte[] content) throws Exception {

        final String reportFileName = fileName;
        final String localStorageId = UUID.randomUUID().toString();
        final String relativeUrlPath = new StringBuilder(localStorageId).append("/").append(reportFileName).toString();
        final File filePath = new File(new URI(new StringBuilder(getRootPath()).append("/").append(storageId).append("/").append(localStorageId).toString()));

        filePath.mkdirs();
        ZipFileUtil.unzip(new ByteArrayInputStream(content), filePath);

        return new ReportContentInfo() {

            @Override
            public String getRelativeURLPath() {
                return relativeUrlPath;
            }

            @Override
            public File getFilePath() {
                return new File(new StringBuilder(filePath.getAbsolutePath()).append("/").append(reportFileName).toString());
            }
        };
    }

    @Override
    public void clear(String uid) throws Exception {
        try {
            String path = new StringBuilder(getRootPath()).append("/").append(uid).toString();
            log.debug("Removing temp directory with report contents - [{}]", path);
            FileUtils.deleteDirectory(new File(new URI(path)));
        } catch (Exception ex) {
            log.error("Cannot remove temp directory with report contents", ex);
            throw ex;
        }
    }
}
