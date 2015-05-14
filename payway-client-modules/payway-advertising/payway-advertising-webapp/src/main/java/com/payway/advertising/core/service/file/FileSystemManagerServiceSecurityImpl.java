/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.core.service.file;

import java.io.InputStream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * FileSystemManagerServiceSecurityImpl
 *
 * @author Sergey Kichenko
 * @created 14.10.15 00:00
 */
@Slf4j
@Component(value = "fileSystemManagerServiceSecurityImpl")
public class FileSystemManagerServiceSecurityImpl implements FileSystemManagerServiceSecurity {

    @Override
    public String digestMD5Hex(InputStream is) throws FileSystemManagerServiceException {
        String digest = null;
        try {
            digest = org.apache.commons.codec.digest.DigestUtils.md5Hex(is);
        } catch (Exception ex) {
            throw new FileSystemManagerServiceException("Error generate md5 hex digest", ex);
        }
        return digest;
    }
}