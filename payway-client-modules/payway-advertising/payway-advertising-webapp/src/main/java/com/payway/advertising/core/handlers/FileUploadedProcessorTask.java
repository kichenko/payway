/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.core.handlers;

import com.payway.advertising.ui.upload.FileUploadedProcessorTaskListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * FileUploadedProcessorTask
 *
 * @author Sergey Kichenko
 * @created 29.06.15 00:00
 */
@Slf4j
public class FileUploadedProcessorTask implements FileProcessorTask, Runnable {

    private List<FileHandler> handlers;

    @Setter(AccessLevel.PRIVATE)
    @Getter(AccessLevel.PRIVATE)
    private String srcFilePath;

    @Setter(AccessLevel.PRIVATE)
    @Getter(AccessLevel.PRIVATE)
    private String srcFileName;

    @Setter(AccessLevel.PRIVATE)
    @Getter(AccessLevel.PRIVATE)
    private String dstFilePath;

    @Setter(AccessLevel.PRIVATE)
    @Getter(AccessLevel.PRIVATE)
    private String dstFileName;

    @Setter(AccessLevel.PRIVATE)
    @Getter(AccessLevel.PRIVATE)
    private FileUploadedProcessorTaskListener callback;

    public FileUploadedProcessorTask(String srcFilePath, String srcFileName, String dstFilePath, String dstFileName, FileUploadedProcessorTaskListener callback) {

        setSrcFilePath(srcFilePath);
        setSrcFileName(srcFileName);
        setDstFilePath(dstFilePath);
        setDstFileName(dstFileName);
        setCallback(callback);
    }

    @Override
    public void run() {

        try {
            process();
        } catch (Exception ex) {
            log.error("File processor [{}] executed with error [{}]", this, ex);
        }
    }

    @Override
    public void process() throws FileProcessorException {

        Map<String, Object> params = new HashMap<>();

        params.put("dstFilePath", dstFilePath);
        params.put("dstFileName", dstFileName);

        if (callback != null) {
            callback.onStart(handlers.size());
        }

        int i = 0;
        for (FileHandler handler : handlers) {

            try {
                i = i + 1;
                if (callback != null) {
                    if (callback.isInterrupt()) {
                        callback.onInterrupt();
                        break;
                    }
                    callback.onProcess(i, handlers.size());
                }

                if (handler.handle(srcFilePath, srcFileName, params)) {
                    log.debug("File [{}], successful processed", srcFileName);
                } else {
                    log.debug("File [{}], skipped", srcFileName);
                }
            } catch (Exception ex) {
                log.error("File handler [{}] executed with error [{}]", handler, ex);
                if (callback != null) {
                    callback.onFail(i, handlers.size());
                    break;
                }
            }
        }

        if (callback != null && !callback.isInterrupt()) {
            callback.onFinish();
        }
    }

    @Override
    public List<FileHandler> getHandlers() {
        return handlers;
    }
}
