/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.ui.view.workspace.content;

import java.io.BufferedOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.VFS;

/**
 * AbstractUploadTask
 *
 * @author Sergey Kichenko
 * @created 23.05.15 00:00
 */
@Slf4j
public abstract class AbstractUploadTask implements UploadTask {

    protected UUID taskId;
    protected int bufSize;
    protected String path;
    protected long fileSize;
    protected String fileName;
    protected String fileTmpExt;
    protected boolean isInterrupted;
    protected final List<UploadListener> listeners = new ArrayList<>();

    public AbstractUploadTask() {
        taskId = UUID.randomUUID();
        bufSize = 2048;
    }

    public AbstractUploadTask(String path, int bufSize) {
        taskId = UUID.randomUUID();
        bufSize = 2048;
        this.path = path;
        this.bufSize = bufSize;
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
    public UUID getTaskId() {
        return taskId;
    }

    @Override
    public void addListener(UploadListener listener) {
        listeners.add(listener);
    }

    @Override
    public void interrupt() {
        isInterrupted = true;
    }

    @Override
    public long getFileSize() {
        return fileSize;
    }

    @Override
    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    @Override
    public String getPath() {
        return path;
    }

    @Override
    public int getBufferSize() {
        return bufSize;
    }

    @Override
    public void setBufferSize(int bufSize) {
        this.bufSize = bufSize;
    }

    @Override
    public String getFileName() {
        return fileName;
    }

    @Override
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public void setTmpFileExt(String ext) {
        fileTmpExt = ext;
    }

    @Override
    public String getTmpFileExt() {
        return fileTmpExt;
    }

    @Override
    public boolean isInterrupted() {
        return isInterrupted;
    }

}
