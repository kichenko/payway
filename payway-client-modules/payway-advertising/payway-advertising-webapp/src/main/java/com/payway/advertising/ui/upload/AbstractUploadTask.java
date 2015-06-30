/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.ui.upload;

import com.googlecode.flyway.core.util.StringUtils;
import com.payway.advertising.core.utils.Helpers;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

/**
 * AbstractUploadTask
 *
 * @author Sergey Kichenko
 * @created 23.05.15 00:00
 */
@Slf4j
public abstract class AbstractUploadTask implements UploadTask {

    @Getter(onMethod = @_({
        @Override}))
    @Setter(AccessLevel.PROTECTED)
    protected UUID taskId;

    @Getter(onMethod = @_({
        @Override}))
    @Setter(onMethod = @_({
        @Override}))
    protected int bufferSize;

    @Getter(onMethod = @_({
        @Override}))
    @Setter(onMethod = @_({
        @Override}))
    protected long fileSize;

    @Getter(onMethod = @_({
        @Override}))
    @Setter(onMethod = @_({
        @Override}))
    protected String fileName;

    @Getter(onMethod = @_({
        @Override}))
    @Setter(onMethod = @_({
        @Override}))
    protected String tmpFileExt;

    @Getter(onMethod = @_({
        @Override}))
    protected String uploadPath;

    @Getter(onMethod = @_({
        @Override}))
    @Setter(onMethod = @_({
        @Override}))
    protected String uploadTempFileName;

    @Getter(onMethod = @_({
        @Override}))
    protected boolean interrupted;

    @Getter(onMethod = @_({
        @Override}))
    @Setter(onMethod = @_({
        @Override}))
    protected FileUploadedProcessorTaskListener fileUploadedProcessorTaskListener;

    @Getter(onMethod = @_({
        @Override}))
    @Setter(onMethod = @_({
        @Override}))
    protected String destFilePath;

    protected final List<UploadListener> listeners = new ArrayList<>();

    public AbstractUploadTask() {
        setBufferSize(2048);
        setTaskId(UUID.randomUUID());
    }

    public AbstractUploadTask(String uploadPath, String destFilePath, int bufferSize) {
        setUploadPath(uploadPath);
        setBufferSize(bufferSize);
        setTaskId(UUID.randomUUID());
        setDestFilePath(destFilePath);
    }

    protected boolean removeTmpUploadedFile() {
        boolean isOk = false;
        try {
            isOk = new File(new URI(Helpers.addEndSeparator(getUploadPath()) + getUploadTempFileName())).delete();
        } catch (Exception ex) {
            log.error("Error then delete failed uploaded file - {}", ex);
        }
        return isOk;
    }

    protected String createUploadTempFileName() {
        uploadTempFileName = UUID.randomUUID().toString() + "_" + getFileName();
        return uploadTempFileName;
    }

    @Override
    public void setUploadPath(String path) {
        uploadPath = StringUtils.replaceAll(path, "\\", "/");
    }

    protected OutputStream createOutputStream() {
        if (!isInterrupted()) {

            try {
                File uploadTmpFolder = new File(new URI(getUploadPath()));
                if (!uploadTmpFolder.exists()) {
                    FileUtils.forceMkdir(uploadTmpFolder);
                }
                return new BufferedOutputStream(new FileOutputStream(new File(new URI(Helpers.addEndSeparator(getUploadPath()) + createUploadTempFileName()))), getBufferSize());
            } catch (Exception ex) {
                interrupt();
                log.error("Error create output stream - {}", ex);
            }
        }

        return new NullOutputStream();
    }

    protected void uploadFinished() {
        if (listeners != null) {
            for (UploadListener l : listeners) {
                l.uploadSucceeded(this);
            }
        } else {
            for (UploadListener l : listeners) {
                l.uploadFailed(this, false);
            }
        }
    }

    @Override
    public void addListener(UploadListener listener) {
        listeners.add(listener);
    }

    @Override
    public void interrupt() {
        interrupted = true;
        if (getFileUploadedProcessorTaskListener() != null) {
            getFileUploadedProcessorTaskListener().interrupt();
        }
    }
}
