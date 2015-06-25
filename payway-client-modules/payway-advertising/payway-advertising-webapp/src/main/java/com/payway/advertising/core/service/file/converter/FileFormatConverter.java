/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.core.service.file.converter;

import com.payway.advertising.core.service.exception.FileFormatConverterException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * FileFormatConverter
 *
 * @author Sergey Kichenko
 * @created 25.06.15 00:00
 */
public interface FileFormatConverter {
    
    public enum FileFormat {
        
    }

    void convert(InputStream inputStream, OutputStream outputStream) throws FileFormatConverterException;
}
