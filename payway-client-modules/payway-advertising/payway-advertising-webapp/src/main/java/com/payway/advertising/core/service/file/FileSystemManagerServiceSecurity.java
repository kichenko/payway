/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.core.service.file;

import java.io.InputStream;

/**
 * FileSystemManagerServiceSecurity
 *
 * @author Sergey Kichenko
 * @created 14.10.15 00:00
 */
public interface FileSystemManagerServiceSecurity {

    String digestMD5Hex(InputStream is) throws FileSystemManagerServiceException;
}
