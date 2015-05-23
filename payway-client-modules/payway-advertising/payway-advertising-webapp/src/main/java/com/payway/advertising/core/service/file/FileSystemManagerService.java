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
 * @created 07.05.15 00:00
 */
public interface FileSystemManagerService {

    void create(FileSystemObject src) throws FileSystemManagerServiceException;

    void rename(FileSystemObject src, FileSystemObject dst) throws FileSystemManagerServiceException;

    void delete(FileSystemObject src) throws FileSystemManagerServiceException;

    void move(FileSystemObject src, FileSystemObject dst) throws FileSystemManagerServiceException;

    void copy(FileSystemObject src, FileSystemObject dst) throws FileSystemManagerServiceException;

    List<FileSystemObject> list(FileSystemObject src, boolean addFolders, boolean recursive) throws FileSystemManagerServiceException;

    boolean exist(FileSystemObject src) throws FileSystemManagerServiceException;

    InputStream getInputStream(FileSystemObject src) throws FileSystemManagerServiceException;
}
