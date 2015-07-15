/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.core.handlers;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * FileHandlerArgs
 *
 * @author Sergey Kichenko
 * @created 29.06.15 00:00
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FileHandlerArgs {

    private String srcFilePath;
    private String srcFileName;

    private String dstFilePath;
    private String dstFileName;

    private long length;
}
