/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.ui.upload;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.VFS;

import java.io.BufferedOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
    protected String path;

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
    protected boolean interrupted;

    protected final List<UploadListener> listeners = new ArrayList<>();

    public AbstractUploadTask() {
        setBufferSize(2048);
        setTaskId(UUID.randomUUID());
    }

    public AbstractUploadTask(String path, int bufferSize) {
        setPath(path);
        setBufferSize(bufferSize);
        setTaskId(UUID.randomUUID());
    }

    protected OutputStream createOutputStream() {
        if (!isInterrupted()) {
            try {
                FileObject fo = VFS.getManager().resolveFile(getPath() + getFileName() + getTmpFileExt());
                if (!fo.exists()) {
                    return new BufferedOutputStream(fo.getContent().getOutputStream(), getBufferSize());
                } else {
                    interrupt();
                }
            } catch (Exception ex) {
                log.error("Error create output stream", ex);
            }
        }

        return new NullOutputStream();
    }

    protected void uploadFinished() {
        //then upload is success - rename uploaded file (remove tmp file ext)
        boolean isOk = renameUploadedFile();
        if (!isOk) {
            log.error("Error then rename uploaded file");
        }

        if (listeners != null) {
            if (isOk) {
                for (UploadListener l : listeners) {
                    l.uploadSucceeded(this);
                }
            } else {
                for (UploadListener l : listeners) {
                    l.uploadFailed(this, false);
                }
            }
        }
    }

    protected boolean renameUploadedFile() {
        boolean isOk = false;
        try {
            FileObject foOld = VFS.getManager().resolveFile(getPath() + getFileName() + getTmpFileExt());
            FileObject foNew = VFS.getManager().resolveFile(getPath() + getFileName());

            if (foOld.canRenameTo(foNew)) {
                foOld.moveTo(foNew);
            } else {
                throw new Exception("Can't rename file system object");
            }
            isOk = true;
        } catch (Exception ex) {
            log.error("Error create output stream", ex);
        }
        return isOk;
    }

    protected boolean removeTmpUploadedFile() {
        boolean isOk = false;
        try {
            FileObject fo = VFS.getManager().resolveFile(getPath() + getFileName() + getTmpFileExt());
            isOk = fo.delete();
        } catch (Exception ex) {
            log.error("Error then delete failed uploaded file");
        }
        return isOk;
    }

    @Override
    public void addListener(UploadListener listener) {
        listeners.add(listener);
    }

    @Override
    public void interrupt() {
        interrupted = true;
    }
}
