/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.core.service.file;

import com.payway.advertising.core.service.exception.FileSystemManagerServiceException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemManager;
import org.apache.commons.vfs2.FileType;
import org.apache.commons.vfs2.Selectors;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * FileManagerServiceImpl
 *
 * @author Sergey Kichenko
 * @created 18.05.15 00:00
 */
@Slf4j
@Component(value = "fileManagerService")
public class FileManagerServiceImpl implements FileSystemManagerService {

    @Autowired
    @Qualifier(value = "fileSystemManager")
    private FileSystemManager fileSystemManager;

    @Value("file:///")
    private String fileSchema;

    @Value("webdav://")
    private String webDavSchema;

    @Value("${webdav.username}")
    private String webDavUsername;

    @Value("${webdav.password}")
    private String webDavPassword;

    @Value("${webdav.host}")
    private String webDavHost;

    @Value("${webdav.port}")
    private Integer webDavPort;

    //webdav://[ username[: password]@] hostname[: port]/[ absolute-path]
    private String getWebDavUri(FileSystemObject uri) {

        //webdav://[ username[: password]@] hostname[: port]/[ absolute-path]
        StringBuilder sb = new StringBuilder(getSchema(uri));

        //user & pswd
        if (!StringUtils.isBlank(webDavUsername)) {
            sb.append(webDavUsername);
        }

        if (!org.codehaus.plexus.util.StringUtils.isBlank(webDavPassword)) {
            sb.append(":").append(webDavPassword).append("@");
        }

        //host
        sb.append(ObjectUtils.defaultIfNull(webDavHost, ""));

        //port
        if (webDavPort != null) {
            sb.append(":").append(Integer.toString(webDavPort));
        }

        sb.append("/");

        //with absolutePath
        return sb.append(ObjectUtils.defaultIfNull(uri.getPath(), "")).toString();
    }

    //[file:///] absolute-path
    private String getFileUri(FileSystemObject uri) {
        return fileSchema + uri.getPath();
    }

    private String getSchemaWithoutAbsolutePath(FileSystemObject.FileSystemType fileSystemType) {
        return getSchema(new FileSystemObject("", fileSystemType, null, 0L, null));
    }

    @Override
    public String getSchema(FileSystemObject uri) {
        return FileSystemObject.FileSystemType.WEBDAV.equals(uri.getFileSystemType()) ? webDavSchema : fileSchema;
    }

    @Override
    public String getUri(FileSystemObject uri) {
        return FileSystemObject.FileSystemType.WEBDAV.equals(uri.getFileSystemType()) ? getWebDavUri(uri) : getFileUri(uri);
    }

    @Override
    public void create(FileSystemObject srcUri) throws FileSystemManagerServiceException {
        try {
            FileObject fo = fileSystemManager.resolveFile(getUri(srcUri));

            if (FileSystemObject.FileType.FILE.equals(srcUri.getFileType())) {
                fo.createFile();
            } else if (FileSystemObject.FileType.FOLDER.equals(srcUri.getFileType())) {
                fo.createFolder();
            } else {
                throw new Exception("Can't create file system object, unknown type");
            }
        } catch (Exception ex) {
            throw new FileSystemManagerServiceException("Error rename file system object", ex);
        }
    }

    @Override
    public void rename(FileSystemObject srcUri, FileSystemObject destUri) throws FileSystemManagerServiceException {
        try {
            FileObject foOld = fileSystemManager.resolveFile(getUri(srcUri));
            FileObject foNew = fileSystemManager.resolveFile(getUri(destUri));

            if (foOld.canRenameTo(foNew)) {
                foOld.moveTo(foNew);
            } else {
                throw new Exception("Can't rename file system object");
            }
        } catch (Exception ex) {
            throw new FileSystemManagerServiceException("Error rename file system object", ex);
        }
    }

    @Override
    public void delete(FileSystemObject srcUri) throws FileSystemManagerServiceException {
        try {
            FileObject fo = fileSystemManager.resolveFile(getUri(srcUri));
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
        try {

            if (srcUri.getPath().equals(destUri.getPath())) {
                throw new Exception("Can't copy to the same file system object");
            }

            FileObject src = fileSystemManager.resolveFile(getUri(srcUri));
            FileObject dst = fileSystemManager.resolveFile(getUri(destUri));
            dst.copyFrom(src, Selectors.SELECT_ALL);

        } catch (Exception ex) {
            throw new FileSystemManagerServiceException("Error rename file system object", ex);
        }
    }

    private void recursiveFilesAndFolders(FileSystemObject.FileSystemType fileSystemType, FileObject file, List<FileSystemObject> list, boolean addFolders) throws Exception {
        if (file != null) {
            if ((addFolders && FileType.FOLDER.equals(file.getType())) || (FileType.FILE.equals(file.getType()))) {
                list.add(new FileSystemObject(
                  StringUtils.substring(file.getName().getURI(), getSchemaWithoutAbsolutePath(fileSystemType).length()),
                  fileSystemType,
                  FileType.FILE.equals(file.getType()) ? FileSystemObject.FileType.FILE : FileSystemObject.FileType.FOLDER,
                  FileType.FILE.equals(file.getType()) ? file.getContent().getSize() : 0,
                  new LocalDateTime(file.getContent().getLastModifiedTime())));
            }

            if (FileType.FOLDER.equals(file.getType())) {
                FileObject[] childs = file.getChildren();
                if (childs != null) {
                    for (FileObject f : childs) {
                        recursiveFilesAndFolders(fileSystemType, f, list, addFolders);
                    }
                }
            }
        }
    }

    @Override
    public List<FileSystemObject> list(FileSystemObject srcUri, boolean addFolders, boolean recursive) throws FileSystemManagerServiceException {

        List<FileSystemObject> list = new ArrayList<>(0);

        try {
            FileObject fo = fileSystemManager.resolveFile(getUri(srcUri));
            if (recursive) {
                recursiveFilesAndFolders(srcUri.getFileSystemType(), fo, list, addFolders);
            } else {
                if (FileType.FOLDER.equals(fo.getType())) {
                    FileObject[] childs = fo.getChildren();
                    if (childs != null) {
                        for (FileObject f : childs) {
                            if ((addFolders && FileType.FOLDER.equals(f.getType())) || (FileType.FILE.equals(f.getType()))) {
                                list.add(new FileSystemObject(
                                  StringUtils.substring(f.getName().getURI(), getSchemaWithoutAbsolutePath(srcUri.getFileSystemType()).length()),
                                  srcUri.getFileSystemType(),
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
            throw new FileSystemManagerServiceException("Error list file system object", ex);
        }

        return list;
    }

    @Override
    public boolean exist(FileSystemObject uri) throws FileSystemManagerServiceException {
        try {
            FileObject fo = fileSystemManager.resolveFile(getUri(uri));
            return fo.exists();
        } catch (Exception ex) {
            throw new FileSystemManagerServiceException("Error exist file system object", ex);
        }
    }

    @Override
    public InputStream getInputStream(FileSystemObject uri) throws FileSystemManagerServiceException {
        InputStream is = null;
        try {
            FileObject fo = fileSystemManager.resolveFile(getUri(uri));
            if (FileType.FILE.equals(fo.getType())) {
                is = fo.getContent().getInputStream();
            }
        } catch (Exception ex) {
            throw new FileSystemManagerServiceException("Error receive file system object input stream", ex);
        }

        return is;
    }
}
