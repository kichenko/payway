/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.core.service.file;

import com.payway.advertising.core.service.exception.FileSystemManagerServiceException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemManager;
import org.apache.commons.vfs2.FileType;
import org.apache.commons.vfs2.Selectors;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * FileManagerServiceImpl
 *
 * @author Sergey Kichenko
 * @created 18.05.15 00:00
 */
@Slf4j
@Component(value = "app.advertising.FileManagerService")
public class FileManagerServiceImpl implements FileSystemManagerService {

    @Autowired
    private FileSystemManager fileSystemManager;

    @Override
    public void create(FileSystemObject src) throws FileSystemManagerServiceException {
        try {
            FileObject fo = fileSystemManager.resolveFile(src.getPath());

            if (FileSystemObject.FileType.FILE.equals(src.getFileType())) {
                fo.createFile();
            } else if (FileSystemObject.FileType.FOLDER.equals(src.getFileType())) {
                fo.createFolder();
            } else {
                throw new Exception("Can't create file system object, unknown type");
            }
        } catch (Exception ex) {
            log.error("Bad create", ex);
            throw new FileSystemManagerServiceException("Error create file system object", ex);
        }
    }

    @Override
    public void rename(FileSystemObject src, FileSystemObject dst) throws FileSystemManagerServiceException {
        try {
            FileObject foOld = fileSystemManager.resolveFile(src.getPath());
            FileObject foNew = fileSystemManager.resolveFile(dst.getPath());

            if (exist(dst)) {
                throw new Exception("Can't rename file system object");
            }
            foOld.moveTo(foNew);
        } catch (Exception ex) {
            log.error("Bad file rename", ex);
            throw new FileSystemManagerServiceException("Error rename file system object", ex);
        }
    }

    @Override
    public void delete(FileSystemObject src) throws FileSystemManagerServiceException {
        try {
            FileObject fo = fileSystemManager.resolveFile(src.getPath());
            fo.delete(Selectors.SELECT_ALL);
        } catch (Exception ex) {
            log.error("Bad file delete", ex);
            throw new FileSystemManagerServiceException("Error delete file system object", ex);
        }
    }

    @Override
    public void move(FileSystemObject src, FileSystemObject dst) throws FileSystemManagerServiceException {
        if (src.getPath().equals(dst.getPath())) {
            throw new FileSystemManagerServiceException("Can't move to the same file system object");
        }

        rename(src, dst);
    }

    @Override
    public void copy(FileSystemObject src, FileSystemObject dst) throws FileSystemManagerServiceException {
        try {
            if (src.getPath().equals(dst.getPath())) {
                throw new Exception("Can't copy to the same file system object");
            }

            FileObject srcFo = fileSystemManager.resolveFile(src.getPath());
            FileObject dstFo = fileSystemManager.resolveFile(dst.getPath());
            dstFo.copyFrom(srcFo, Selectors.SELECT_ALL);

        } catch (Exception ex) {
            log.error("Bad file copy", ex);
            throw new FileSystemManagerServiceException("Error rename file system object", ex);
        }
    }

    private void recursiveFilesAndFolders(FileObject file, List<FileSystemObject> list, boolean addFolders) throws Exception {
        if (file != null) {
            if ((addFolders && FileType.FOLDER.equals(file.getType())) || (FileType.FILE.equals(file.getType()))) {
                list.add(new FileSystemObject(
                        file.getURL().toString(),
                        FileType.FILE.equals(file.getType()) ? FileSystemObject.FileType.FILE : FileSystemObject.FileType.FOLDER,
                        FileType.FILE.equals(file.getType()) ? file.getContent().getSize() : 0,
                        new LocalDateTime(file.getContent().getLastModifiedTime())));
            }

            if (FileType.FOLDER.equals(file.getType())) {
                FileObject[] childs = file.getChildren();
                if (childs != null) {
                    for (FileObject f : childs) {
                        recursiveFilesAndFolders(f, list, addFolders);
                    }
                }
            }
        }
    }

    @Override
    public List<FileSystemObject> list(FileSystemObject src, boolean addFolders, boolean recursive) throws FileSystemManagerServiceException {

        List<FileSystemObject> list = new ArrayList<>(0);

        try {
            FileObject fo = fileSystemManager.resolveFile(src.getPath());
            if (recursive) {
                recursiveFilesAndFolders(fo, list, addFolders);
            } else {
                if (FileType.FOLDER.equals(fo.getType())) {
                    FileObject[] childs = fo.getChildren();
                    if (childs != null) {
                        for (FileObject f : childs) {
                            if ((addFolders && FileType.FOLDER.equals(f.getType())) || (FileType.FILE.equals(f.getType()))) {
                                list.add(new FileSystemObject(
                                        f.getURL().toString(),
                                        FileType.FILE.equals(f.getType()) ? FileSystemObject.FileType.FILE : FileSystemObject.FileType.FOLDER,
                                        FileType.FILE.equals(f.getType()) ? f.getContent().getSize() : 0,
                                        new LocalDateTime(f.getContent().getLastModifiedTime())));
                            }
                        }
                    }
                } else {
                    throw new Exception("Can list only folder file system object");
                }
            }
        } catch (Exception ex) {
            log.error("Bad file list", ex);
            throw new FileSystemManagerServiceException("Error list file system object", ex);
        }

        return list;
    }

    @Override
    public boolean exist(FileSystemObject src) throws FileSystemManagerServiceException {
        try {
            FileObject fo = fileSystemManager.resolveFile(src.getPath());
            return fo.exists();
        } catch (Exception ex) {
            log.error("Bad file exist check", ex);
            throw new FileSystemManagerServiceException("Error exist file system object", ex);
        }
    }

    @Override
    public InputStream getInputStream(FileSystemObject src) throws FileSystemManagerServiceException {
        InputStream is = null;
        try {
            FileObject fo = fileSystemManager.resolveFile(src.getPath());
            if (FileType.FILE.equals(fo.getType())) {
                is = fo.getContent().getInputStream();
            }
        } catch (Exception ex) {
            log.error("Bad file input stream", ex);
            throw new FileSystemManagerServiceException("Error receive file system object input stream", ex);
        }

        return is;
    }

    @Override
    public OutputStream getOutputStream(FileSystemObject src) throws FileSystemManagerServiceException {
        OutputStream is = null;
        try {
            FileObject fo = fileSystemManager.resolveFile(src.getPath());
            if (FileType.FILE.equals(fo.getType())) {
                is = fo.getContent().getOutputStream();
            }
        } catch (Exception ex) {
            log.error("Bad file input stream", ex);
            throw new FileSystemManagerServiceException("Error receive file system object input stream", ex);
        }

        return is;
    }

    @Override
    public FileSystemObject resolve(FileSystemObject src) throws FileSystemManagerServiceException {

        try {
            FileObject fo = fileSystemManager.resolveFile(src.getPath());
            return new FileSystemObject(src.getPath(), fo.isFile() ? FileSystemObject.FileType.FILE : FileSystemObject.FileType.FOLDER, fo.getContent().getSize(), new LocalDateTime(fo.getContent().getLastModifiedTime()));
        } catch (Exception ex) {
            log.error("Bad file input stream", ex);
            throw new FileSystemManagerServiceException("Error resolving file system object", ex);
        }
    }

    @Override
    public String canonicalization(String path) {

        try {
            FileObject fo = fileSystemManager.resolveFile(path);
            return fo.getURL().toString();
        } catch (Exception ex) {
            log.error("Bad file uri canonicalization", ex);
        }

        return "";
    }

    @Override
    public String digestMD5Hex(FileSystemObject in) throws FileSystemManagerServiceException {

        try {
            return DigestUtils.md5Hex(getInputStream(in));
        } catch (Exception ex) {
            log.error("Error generate md5 hex digest", ex);
            throw new FileSystemManagerServiceException("Error generate md5 hex digest", ex);
        }
    }
}
