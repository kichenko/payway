/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.core.handlers;

import com.payway.advertising.model.DbAgentFile;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * FileHandlerArgs
 *
 * @author Sergey Kichenko
 * @created 29.06.15 00:00
 */
@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class FileHandlerArgs {

    private String srcFilePath;
    private String srcFileName;

    private String dstFilePath;
    private String dstFileName;
    private DbAgentFile agentFile;

    private long length;
}
