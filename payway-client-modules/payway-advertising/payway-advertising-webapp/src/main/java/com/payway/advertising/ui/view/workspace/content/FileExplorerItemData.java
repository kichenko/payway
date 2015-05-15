/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.ui.view.workspace.content;

import com.payway.advertising.model.DbAgentFile;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.joda.time.LocalDateTime;

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

    private static final long serialVersionUID = -3077409807450078907L;
    private FileType fileType;
    private String name;
    private String path;
    private Long size;
    private DbAgentFile property;
    private LocalDateTime lastModifiedTime;
}
