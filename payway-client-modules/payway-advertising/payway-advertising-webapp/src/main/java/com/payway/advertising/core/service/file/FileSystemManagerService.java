/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.core.service.file;

import com.payway.advertising.core.service.exception.FileSystemManagerServiceException;
import java.io.InputStream;
import java.util.List;

/**
 * FileSystemManagerService
 *
 * @author Sergey Kichenko
 * @created 07.10.15 00:00
 */
public interface FileSystemManagerService {
    
    String getSchema(FileSystemObject uri);
    
    String getUri(FileSystemObject uri);

    void create(FileSystemObject srcUri) throws FileSystemManagerServiceException;

    void rename(FileSystemObject srcUri, FileSystemObject destUri) throws FileSystemManagerServiceException;

    void delete(FileSystemObject srcUri) throws FileSystemManagerServiceException;

    void move(FileSystemObject srcUri, FileSystemObject destUri) throws FileSystemManagerServiceException;

    void copy(FileSystemObject srcUri, FileSystemObject destUri) throws FileSystemManagerServiceException;

    List<FileSystemObject> list(FileSystemObject srcUri, boolean addFolders, boolean recursive) throws FileSystemManagerServiceException;

    boolean exist(FileSystemObject uri) throws FileSystemManagerServiceException;

    InputStream getInputStream(FileSystemObject uri) throws FileSystemManagerServiceException;
}
