/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.webapp.reporting.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import org.apache.commons.io.IOUtils;

/**
 * ZipFileUtil
 *
 * @author Sergey Kichenko
 * @created 11.08.2015
 */
public final class ZipFileUtil {

    private ZipFileUtil() {
        //
    }

    public static void unzip(InputStream input, File dir) throws Exception {

        ZipEntry ze;
        String path = dir.getPath() + "/";
        ZipInputStream zin = new ZipInputStream(input);

        while ((ze = zin.getNextEntry()) != null) {
            if (ze.isDirectory()) {
                new File(path + ze.getName()).mkdirs();
            } else {
                try (FileOutputStream fos = new FileOutputStream(path + ze.getName())) {
                    IOUtils.copy(zin, fos);
                    zin.closeEntry();
                }
            }
        }
    }
}
