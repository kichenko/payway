/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.core.handlers;

import com.payway.advertising.core.service.AgentFileService;
import com.payway.advertising.core.service.app.settings.SettingsAppService;
import com.payway.advertising.core.service.file.FileSystemManagerService;
import com.payway.advertising.core.service.file.FileSystemObject;
import com.payway.advertising.core.utils.Helpers;
import com.payway.advertising.model.DbAgentFile;
import com.payway.advertising.model.DbFileType;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * FileAgentMappingHandler
 *
 * @author Sergey Kichenko
 * @created 18.08.2015
 */
@Slf4j
@Getter
@Setter
public class FileAgentMappingHandler implements FileHandler {

    private AgentFileService agentFileService;
    private SettingsAppService settingsAppService;
    private FileSystemManagerService fileSystemManagerService;

    @Override
    public boolean handle(FileHandlerArgs args) throws FileHandlerException {

        String pattern = "/";
        try {

            final String digest;
            final String fileName;
            final DbAgentFile agentFile;

            fileName = StringUtils.substringAfter(
                    args.getDstFilePath(),
                    StringUtils.appendIfMissingIgnoreCase(
                            settingsAppService.getLocalConfigPath().replace("\\", pattern),
                            pattern
                    )
            ) + args.getDstFileName();

            digest = fileSystemManagerService.digestMD5Hex(new FileSystemObject(
                    Helpers.addEndSeparator(args.getSrcFilePath()) + args.getSrcFileName(),
                    FileSystemObject.FileType.FILE,
                    0L,
                    null)
            );

            agentFile = agentFileService.save(new DbAgentFile(fileName, DbFileType.Unknown, null, null, digest, Boolean.FALSE, agentFileService.getNextSeqNo()));
            args.setAgentFile(agentFile);
            return true;
        } catch (Exception ex) {
            log.error("Could not mapping file to database - ", ex);
            throw new FileHandlerException("Could not mapping file to database", ex);
        }
    }
}
