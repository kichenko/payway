/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.core.service.file;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.joda.time.LocalDateTime;

/**
 * FileSystemObject
 *
 * @author Sergey Kichenko
 * @created 10.05.15 00:00
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FileSystemObject {

    public enum FileSystemType {

        FILE,
        WEBDAV
    }

    public enum FileType {

        FILE,
        FOLDER
    }

    protected String path;
    protected FileSystemType fileSystemType;
    protected FileType fileType;
    protected Long size;
    protected LocalDateTime lastModifiedTime;
}
