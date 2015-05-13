/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.ui.view.workspace.content;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * ContentConfigurationView
 *
 * @author Sergey Kichenko
 * @created 22.04.15 00:00
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FileExplorerItemData implements Serializable {

    public enum FileType {

        File,
        Folder
    }

    public enum FileKind {

        Unknown,
        Logo,
        Banner,
        Archive,
        Clip,
        Popup
    }

    private static final long serialVersionUID = -3077409807450078907L;

    private FileType fileType;
    private FileKind kind;
    private String name;
    private String path;
    private Long size;
}
