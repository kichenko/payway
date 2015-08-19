/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.core.handlers;

import com.payway.advertising.core.utils.Helpers;
import com.payway.advertising.ui.upload.FileUploadedProcessorTaskListener;
import java.io.File;
import java.net.URI;
import java.util.List;
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

    @Setter
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

        int i = 0;
        FileHandlerArgs args = new FileHandlerArgs(srcFilePath, srcFileName, dstFilePath, dstFileName, null, 0);

        if (callback != null) {
            callback.onStart(handlers.size());
        }

        try {
            for (FileHandler handler : getHandlers()) {
                i = i + 1;
                if (callback != null) {
                    if (callback.isInterrupt()) {
                        callback.onInterrupt();
                        break;
                    }
                    callback.onProcess(i, getHandlers().size());
                }

                if (handler.handle(args)) {
                    log.debug("File [{}], successful processed", args.getSrcFileName());
                } else {
                    log.debug("File [{}], skipped", args.getSrcFileName());
                }
            }

            if (callback != null && !callback.isInterrupt()) {
                callback.onFinish(args);
            }
        } catch (Exception ex) {
            log.error("File handler executed with error [{}]", ex);
            try {
                if (callback != null) {
                    callback.onFail(i, getHandlers().size());
                }
            } catch (Exception e) {
                log.error("Could not call callback onFail - ", e);
            }
        } finally {
            //delete uploaded file
            try {
                new File(new URI(Helpers.addEndSeparator(args.getSrcFilePath()) + args.getSrcFileName())).delete();
            } catch (Exception e) {
                log.error("Could not delete uploaded file [{}] on crush file handler - {}", Helpers.addEndSeparator(args.getSrcFilePath()) + args.getSrcFileName(), e);
            }
        }
    }

    @Override
    public List<FileHandler> getHandlers() {
        return handlers;
    }
}
