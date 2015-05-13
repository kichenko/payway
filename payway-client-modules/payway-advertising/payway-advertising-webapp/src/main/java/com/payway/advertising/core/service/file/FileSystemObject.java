/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.payway.advertising.core.service.file;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author sergey
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FileSystemObject {

    public enum FileSystemObjectType {

        FILE,
        FOLDER
    }

    protected String path;
    protected FileSystemObjectType type;
    protected Long size;
    protected List<FileSystemObject> child;
    protected FileSystemObject parent;
}
