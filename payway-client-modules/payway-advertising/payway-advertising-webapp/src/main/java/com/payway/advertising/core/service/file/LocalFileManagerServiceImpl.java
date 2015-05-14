/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.core.service.file;

import java.io.InputStream;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileSystemManager;
import org.apache.commons.vfs2.FileType;
import org.apache.commons.vfs2.Selectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * LocalFileManagerServiceImpl
 *
 * @author Sergey Kichenko
 * @created 08.05.15 00:00
 */
@Component(value = "localFileManagerService")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class LocalFileManagerServiceImpl implements FileSystemManagerService {

    private final static String SCHEMA = "file:///";

    @Autowired
    @Qualifier(value = "fileSystemManager")
    private FileSystemManager fileSystemManager;

    @Autowired
    @Qualifier(value = "md5MessageDigest")
    private MessageDigest messageDigest;

    @Override
    public void create(FileSystemObject srcUri) throws FileSystemManagerServiceException {
        try {
            FileObject fo = fileSystemManager.resolveFile(SCHEMA + srcUri.getPath());

            if (!exist(srcUri)) {
                if (FileSystemObject.FileSystemObjectType.FILE.equals(srcUri.getType())) {
                    fo.createFile();
                } else if (FileSystemObject.FileSystemObjectType.FOLDER.equals(srcUri.getType())) {
                    fo.createFolder();
                } else {
                    throw new FileSystemManagerServiceException("Can't create file system object, unknown type");
                }
            } else {
                throw new FileSystemManagerServiceException("Can't create file system object, already exist");
            }
        } catch (FileSystemException ex) {
            throw new FileSystemManagerServiceException("Error rename file system object", ex);
        }
    }

    @Override
    public void rename(FileSystemObject srcUri, FileSystemObject destUri) throws FileSystemManagerServiceException {
        try {
            FileObject foOld = fileSystemManager.resolveFile(SCHEMA + srcUri.getPath());
            FileObject foNew = fileSystemManager.resolveFile(SCHEMA + destUri.getPath());

            if (foOld.canRenameTo(foNew)) {
                foOld.moveTo(foNew);
            } else {
                throw new FileSystemManagerServiceException("Can't rename file system object");
            }
        } catch (FileSystemException ex) {
            throw new FileSystemManagerServiceException("Error rename file system object", ex);
        }
    }

    @Override
    public void delete(FileSystemObject srcUri) throws FileSystemManagerServiceException {
        try {
            FileObject fo = fileSystemManager.resolveFile(SCHEMA + srcUri.getPath());
            fo.delete(Selectors.SELECT_ALL);
        } catch (Exception ex) {
            throw new FileSystemManagerServiceException("Error delete file system object", ex);
        }
    }

    @Override
    public void move(FileSystemObject srcUri, FileSystemObject destUri) throws FileSystemManagerServiceException {

        if (srcUri.getPath().equals(destUri.getPath())) {
            throw new FileSystemManagerServiceException("Can't move to the same file system object");
        }

        rename(srcUri, destUri);
    }

    @Override
    public void copy(FileSystemObject srcUri, FileSystemObject destUri) throws FileSystemManagerServiceException {

        if (srcUri.getPath().equals(destUri.getPath())) {
            throw new FileSystemManagerServiceException("Can't copy to the same file system object");
        }

        try {
            FileObject src = fileSystemManager.resolveFile(SCHEMA + srcUri.getPath());
            FileObject dst = fileSystemManager.resolveFile(SCHEMA + destUri.getPath());
            dst.copyFrom(src, Selectors.SELECT_ALL);
        } catch (FileSystemException ex) {
            throw new FileSystemManagerServiceException("Error rename file system object", ex);
        }
    }

    @Override
    public List<FileSystemObject> list(FileSystemObject srcUri, boolean isRecursive) throws FileSystemManagerServiceException {

        List<FileSystemObject> list = new ArrayList<>(0);

        if (isRecursive) {
            throw new FileSystemManagerServiceException("Recursive parameter is not supported");
        }

        try {
            FileObject fo = fileSystemManager.resolveFile(SCHEMA + srcUri.getPath());
            if (FileType.FOLDER.equals(fo.getType())) {
                FileObject[] childs = fo.getChildren();
                if (childs != null) {
                    for (FileObject f : childs) {
                        list.add(new FileSystemObject(StringUtils.substring(f.getName().getURI(), SCHEMA.length()),
                          FileType.FILE.equals(f.getType()) ? FileSystemObject.FileSystemObjectType.FILE : FileSystemObject.FileSystemObjectType.FOLDER,
                          FileType.FILE.equals(f.getType()) ? f.getContent().getSize() : 0,
                          new ArrayList<FileSystemObject>(0),
                          srcUri
                        ));
                    }
                }
            } else {
                throw new FileSystemManagerServiceException("Can list only folder file system object");
            }
        } catch (FileSystemException ex) {
            throw new FileSystemManagerServiceException("Error list file system object", ex);
        }

        return list;
    }

    @Override
    public boolean exist(FileSystemObject uri) throws FileSystemManagerServiceException {
        try {
            FileObject fo = fileSystemManager.resolveFile(SCHEMA + uri.getPath());
            return fo.exists();
        } catch (FileSystemException ex) {
            throw new FileSystemManagerServiceException("Error exist file system object", ex);
        }
    }

    @Override
    public InputStream getInputStream(FileSystemObject uri) throws FileSystemManagerServiceException {
        InputStream is = null;
        try {
            FileObject fo = fileSystemManager.resolveFile(SCHEMA + uri.getPath());
            if (FileType.FILE.equals(fo.getType())) {
                is = fo.getContent().getInputStream();
            }
        } catch (Exception ex) {
            throw new FileSystemManagerServiceException("Error receive file system object input stream", ex);
        }

        return is;
    }
}
