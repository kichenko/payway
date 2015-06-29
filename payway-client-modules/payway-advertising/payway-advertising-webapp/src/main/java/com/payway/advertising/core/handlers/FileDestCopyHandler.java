/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.core.handlers;

import com.payway.advertising.core.service.app.settings.SettingsAppService;
import com.payway.advertising.core.service.file.FileSystemManagerService;
import com.payway.advertising.core.service.file.FileSystemObject;
import com.payway.advertising.core.utils.Helpers;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;

/**
 * FileCopyHandler
 *
 * @author Sergey Kichenko
 * @created 29.06.15 00:00
 */
@Slf4j
public class FileDestCopyHandler implements FileHandler {

    private FileSystemManagerService fileSystemManagerService;
    private SettingsAppService settingsAppService;

    @Override
    public boolean handle(String srcFilePath, String srcFileName, Map<String, Object> params) throws FileHandlerException {

        try {

            if (params == null || params.isEmpty() || params.get("dstFilePath") == null || params.get("dstFileName") == null) {
                throw new Exception("Empty params");
            }

            String input = Helpers.addEndSeparator(srcFilePath) + srcFileName;
            String output = Helpers.addEndSeparator((String) params.get("dstFilePath")) + (String) params.get("dstFileName");
            String outputTmp = Helpers.addEndSeparator((String) params.get("dstFilePath")) + (String) params.get("dstFileName") + settingsAppService.getTemporaryFileExt();

            //settingsAppService.getTemporaryFileExt()
            FileSystemObject src = new FileSystemObject(input, FileSystemObject.FileType.FILE, 0L, null);
            FileSystemObject dst = new FileSystemObject(output, FileSystemObject.FileType.FILE, 0L, null);
            FileSystemObject dstTmp = new FileSystemObject(outputTmp, FileSystemObject.FileType.FILE, 0L, null);

            fileSystemManagerService.copy(src, dstTmp);
            fileSystemManagerService.rename(dstTmp, dst);

        } catch (Exception ex) {
            log.error("Could not copy src to dst file - {}", ex);
            throw new FileHandlerException("Could not finish file handler (copy src to dst file)", ex);
        }

        return true;
    }
}
