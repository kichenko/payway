/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.core.service;

import com.google.gwt.thirdparty.guava.common.base.Function;
import com.payway.advertising.core.service.file.FileSystemManagerService;
import com.payway.advertising.core.service.file.FileSystemObject;
import com.payway.advertising.messaging.MessageServerSenderService;
import com.payway.advertising.messaging.ResponseCallBack;
import com.payway.advertising.model.DbAgentFile;
import com.payway.advertising.model.DbAgentFileOwner;
import com.payway.advertising.model.DbConfiguration;
import com.payway.advertising.model.DbFileType;
import com.payway.messaging.core.response.ExceptionResponse;
import com.payway.messaging.core.response.SuccessResponse;
import com.payway.messaging.message.request.configuration.ApplyConfigurationRequest;
import com.payway.messaging.message.response.configuration.ApplyConfigurationResponse;
import com.payway.messaging.model.message.configuration.AgentFileDto;
import com.payway.messaging.model.message.configuration.AgentFileOwnerDto;
import com.payway.messaging.model.message.configuration.ConfigurationDto;
import com.payway.messaging.model.message.configuration.DbFileTypeDto;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * ConfigurationApplyService - apply local->server configuration
 *
 * @author Sergey Kichenko
 * @created 19.05.15 00:00
 */
@Slf4j
@Service(value = "configurationApplyService")
public class ConfigurationApplyServiceImpl implements ConfigurationApplyService {

    @Autowired
    @Qualifier(value = "fileManagerService")
    FileSystemManagerService fileManagerService;

    @Autowired
    @Qualifier(value = "configurationService")
    private ConfigurationService configurationService;

    @Autowired
    @Qualifier(value = "clientApplyLockService")
    private ConfigurationApplyLockService clientApplyLockService;

    @Autowired
    @Qualifier(value = "serverApplyLockService")
    private ConfigurationApplyLockService serverApplyLockService;

    @Getter
    @Setter
    @Value("SECONDS")
    private TimeUnit unit;

    @Getter
    @Setter
    @Value("10")
    private long time;

    @Autowired
    @Qualifier(value = "serviceSender")
    MessageServerSenderService messageServerSenderService;

    /**
     * Apply configuration, method execute async!
     *
     * @param configurationName
     * @param localPath
     * @param serverPath
     * @param listener
     */
    @Override
    @Async(value = "serverTaskExecutor")
    public void apply(final String configurationName, final FileSystemObject localPath, final FileSystemObject serverPath, final ConfigurationApplyCallback listener) {

        Thread th = Thread.currentThread();


        //try lock
        if (clientApplyLockService.tryLock(getTime(), getUnit())) {

            //1. get unique name
            final String serverRootPathName = StringUtils.substringBeforeLast(serverPath.getPath(), "/");
            final String clientTmpFolderName = configurationService.generateUniqueFolderName("local", configurationName);
            final String serverTmpFolderName = configurationService.generateUniqueFolderName("server", StringUtils.substringAfterLast(serverPath.getPath(), "/"));

            try {
                //2. copy files
                List<FileSystemObject> files = fileManagerService.list(localPath, false, true);

                if (files == null || files.isEmpty()) {
                    throw new Exception("Error apply configuration - files not found");
                }

                for (FileSystemObject src : files) {
                    fileManagerService.copy(src,
                      new FileSystemObject(
                        serverRootPathName + "/" + clientTmpFolderName + "/" + StringUtils.substring(src.getPath(), localPath.getPath().length()),
                        serverPath.getFileSystemType(),
                        FileSystemObject.FileType.FILE,
                        0L,
                        null
                      ));
                }

                //3. send server msg
                if (serverApplyLockService.tryLock(getTime(), getUnit())) {
                    try {
                        //3.1 build request dto
                        DbConfiguration cfg = configurationService.findConfigurationByNameWithFiles(configurationName);
                        if (cfg != null) {
                            List<AgentFileDto> agentFilesDto = new ArrayList<>(cfg.getFiles().size());
                            for (DbAgentFile f : cfg.getFiles()) {
                                agentFilesDto.add(new Function<DbAgentFile, AgentFileDto>() {
                                    @Override
                                    public AgentFileDto apply(DbAgentFile file) {
                                        AgentFileDto dto = new AgentFileDto();
                                        dto.setName(file.getName());
                                        dto.setDigest(file.getDigest());
                                        dto.setExpression(file.getExpression());
                                        dto.setIsCountHits(file.getIsCountHits());

                                        dto.setOwner(new Function<DbAgentFileOwner, AgentFileOwnerDto>() {
                                            @Override
                                            public AgentFileOwnerDto apply(DbAgentFileOwner owner) {
                                                return owner == null ? null : new AgentFileOwnerDto(owner.getName(), owner.getDescription());
                                            }
                                        }.apply(file.getOwner()));

                                        dto.setKind(new Function<DbFileType, DbFileTypeDto>() {
                                            @Override
                                            public DbFileTypeDto apply(DbFileType kind) {
                                                if (kind != null) {
                                                    switch (kind) {
                                                        case Unknown:
                                                            return DbFileTypeDto.Unknown;
                                                        case Popup:
                                                            return DbFileTypeDto.Popup;
                                                        case Logo:
                                                            return DbFileTypeDto.Logo;
                                                        case Clip:
                                                            return DbFileTypeDto.Clip;
                                                        case Banner:
                                                            return DbFileTypeDto.Banner;
                                                        case Archive:
                                                            return DbFileTypeDto.Archive;
                                                        default:
                                                            return null;
                                                    }
                                                }
                                                return null;
                                            }
                                        }.apply(file.getKind()));

                                        return dto;
                                    }
                                }.apply(f));
                            }

                            ApplyConfigurationRequest req = new ApplyConfigurationRequest(new ConfigurationDto(agentFilesDto));

                            //3.2 send req dto to server, wait response
                            messageServerSenderService.sendMessage(req, new ResponseCallBack() {
                                @Override
                                public void onServerResponse(final SuccessResponse response) {
                                    try {
                                        ApplyConfigurationResponse rsp = (ApplyConfigurationResponse) response;
                                        if (rsp != null && rsp.isSuccess()) {
                                            try {
                                                //3.3.1 rename server folder to tmp
                                                fileManagerService.rename(serverPath, new FileSystemObject(serverRootPathName + "/" + serverTmpFolderName, serverPath.getFileSystemType(), FileSystemObject.FileType.FOLDER, 0L, null));

                                                //3.3.2 rename tmp local-server folder to server folder
                                                fileManagerService.rename(new FileSystemObject(serverRootPathName + "/" + clientTmpFolderName, serverPath.getFileSystemType(), FileSystemObject.FileType.FOLDER, 0L, null), serverPath);

                                                //3.3.3 remove tmp server folder, do it???
                                                fileManagerService.delete(new FileSystemObject(serverRootPathName + "/" + serverTmpFolderName, serverPath.getFileSystemType(), FileSystemObject.FileType.FOLDER, 0L, null));
                                            } catch (Exception ex) {
                                                throw new Exception("CRITICAL ERROR RENAME ORIGINAL SERVER FOLDER");
                                            }
                                        } else {
                                            throw new Exception("Error unsuccess response from server");
                                        }
                                    } catch (Exception ex) {
                                        log.error("Error apply local->server configuration", ex);
                                    } finally {
                                        clientApplyLockService.unlock();
                                        serverApplyLockService.unlock();
                                    }
                                }

                                @Override
                                public void onServerResponse(final SuccessResponse response, final Map data) {
                                    //never be called
                                }

                                @Override
                                public void onServerException(final ExceptionResponse exception) {
                                    try {
                                        log.error("Error apply local->server configuration", exception.getMessage());
                                        //3.3.1 remove tmp local-server folder
                                        fileManagerService.delete(new FileSystemObject(serverRootPathName + "/" + clientTmpFolderName, serverPath.getFileSystemType(), FileSystemObject.FileType.FOLDER, 0L, null));
                                    } catch (Exception ex) {
                                        log.error("Error apply local->server configuration", ex);
                                    } finally {
                                        clientApplyLockService.unlock();
                                        serverApplyLockService.unlock();
                                    }
                                }

                                @Override
                                public void onLocalException(Exception ex) {
                                    try {
                                        log.error("Error apply local->server configuration: Local server response exception");
                                        //3.3.1 remove tmp local-server folder
                                        fileManagerService.delete(new FileSystemObject(serverRootPathName + "/" + clientTmpFolderName, serverPath.getFileSystemType(), FileSystemObject.FileType.FOLDER, 0L, null));
                                    } catch (Exception e) {
                                        log.error("Error apply local->server configuration", ex);
                                    } finally {
                                        clientApplyLockService.unlock();
                                        serverApplyLockService.unlock();
                                    }
                                }

                                @Override
                                public void onTimeout() {
                                    try {
                                        log.error("Error apply local->server configuration: Timeput server response");
                                        //3.3.1 remove tmp local-server folder
                                        fileManagerService.delete(new FileSystemObject(serverRootPathName + "/" + clientTmpFolderName, serverPath.getFileSystemType(), FileSystemObject.FileType.FOLDER, 0L, null));
                                    } catch (Exception ex) {
                                        log.error("Error apply local->server configuration", ex);
                                    } finally {
                                        clientApplyLockService.unlock();
                                        serverApplyLockService.unlock();
                                    }
                                }
                            });
                        } else {
                            throw new Exception(String.format("Error can not find local configuration with name [%s] in DB", configurationName));
                        }
                    } catch (Exception ex) {
                        throw ex;
                    } finally {
                        serverApplyLockService.unlock();
                    }
                } else {
                    throw new Exception("Error apply local->server configuration, can not get server lock");
                }
            } catch (Exception ex) {
                log.error("Error apply local->server configuration", ex);
                try {
                    //4. remove tmp local-remote folder
                    fileManagerService.delete(new FileSystemObject(serverRootPathName + "/" + clientTmpFolderName, serverPath.getFileSystemType(), FileSystemObject.FileType.FOLDER, 0L, null));
                } catch (Exception e) {
                    log.error("Error remove tmp local remote folder", e);
                }
            } finally {
                clientApplyLockService.unlock();
            }
        } else {
            log.error("Error apply local->server configuration, can not get client lock");
        }
    }
}