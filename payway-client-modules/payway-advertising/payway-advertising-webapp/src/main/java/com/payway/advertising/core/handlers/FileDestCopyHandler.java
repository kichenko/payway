/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.core.handlers;

import com.payway.advertising.core.service.app.settings.SettingsAppService;
import com.payway.advertising.core.service.file.FileSystemManagerService;
import com.payway.advertising.core.service.file.FileSystemObject;
import com.payway.advertising.core.utils.Helpers;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * FileCopyHandler
 *
 * @author Sergey Kichenko
 * @created 29.06.15 00:00
 */
@Slf4j
@Setter
@Getter
public class FileDestCopyHandler implements FileHandler {

    private FileSystemManagerService fileSystemManagerService;
    private SettingsAppService settingsAppService;

    @Override
    public boolean handle(FileHandlerArgs args) throws FileHandlerException {

        String input = Helpers.addEndSeparator(args.getSrcFilePath()) + args.getSrcFileName();
        String output = Helpers.addEndSeparator(args.getDstFilePath()) + args.getDstFileName();
        String outputTmp = Helpers.addEndSeparator(args.getDstFilePath()) + args.getDstFileName() + settingsAppService.getTemporaryFileExt();

        FileSystemObject src = new FileSystemObject(input, FileSystemObject.FileType.FILE, 0L, null);
        FileSystemObject dst = new FileSystemObject(output, FileSystemObject.FileType.FILE, 0L, null);
        FileSystemObject dstTmp = new FileSystemObject(outputTmp, FileSystemObject.FileType.FILE, 0L, null);

        try {
            
            if (StringUtils.isBlank(args.getDstFileName()) || StringUtils.isBlank(args.getDstFilePath())) {
                throw new Exception("Empty arg params");
            }

            fileSystemManagerService.copy(src, dstTmp);
            fileSystemManagerService.rename(dstTmp, dst);
        } catch (Exception ex) {
            log.error("Could not copy src to dst file - {}", ex);

            try {
                fileSystemManagerService.delete(dstTmp);
            } catch (Exception e) {
                log.error("Could not delete dst file on crush file handler - {}", ex);
            }

            throw new FileHandlerException("Could not finish file handler (copy src to dst file)", ex);
        }

        return true;
    }
}
