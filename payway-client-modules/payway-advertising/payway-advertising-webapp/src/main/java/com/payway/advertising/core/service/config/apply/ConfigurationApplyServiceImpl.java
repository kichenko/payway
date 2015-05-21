/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.core.service.config.apply;

import com.google.gwt.thirdparty.guava.common.base.Function;
import com.payway.advertising.core.app.bus.AppBusEventImpl;
import com.payway.advertising.core.app.bus.AppEventBus;
import com.payway.advertising.core.service.ConfigurationService;
import com.payway.advertising.core.service.exception.ConfigurationApplyCancelException;
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
import com.payway.messaging.message.response.configuration.ApplySuccessConfigurationResponse;
import com.payway.messaging.model.message.configuration.AgentFileDto;
import com.payway.messaging.model.message.configuration.AgentFileOwnerDto;
import com.payway.messaging.model.message.configuration.ConfigurationDto;
import com.payway.messaging.model.message.configuration.DbFileTypeDto;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.LocalDateTime;
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
    @Value("1")
    private long time;

    @Getter(AccessLevel.PRIVATE)
    @Setter(AccessLevel.PRIVATE)
    private LocalDateTime startTime;

    @Getter(AccessLevel.PRIVATE)
    @Setter(AccessLevel.PRIVATE)
    private String login;

    @Autowired
    @Qualifier(value = "messageServerSenderService")
    MessageServerSenderService messageServerSenderService;

    @Getter
    @Setter(AccessLevel.PRIVATE)
    private volatile ApplyConfigurationStatus status = new ApplyConfigurationStatus("", new LocalDateTime(), ApplyConfigurationStatus.Step.None);

    @Autowired
    @Qualifier(value = "appEventBus")
    private AppEventBus appEventBus;

    private void notifyFail() {
        setStatus(new ApplyConfigurationStatus(getLogin(), getStartTime(), ApplyConfigurationStatus.Step.Fail));
        appEventBus.sendNotification(new AppBusEventImpl(new ApplyConfigurationStatus(getLogin(), getStartTime(), ApplyConfigurationStatus.Step.Fail)));

    }

    /**
     * Cancel applying configuration, only on step copying files.
     *
     * @return
     */
    @Override
    public boolean cancel() {
        if (ApplyConfigurationStatus.Step.Prepare.equals(getStatus().getStep()) || ApplyConfigurationStatus.Step.CopyFiles.equals(getStatus().getStep())) {
            setStatus(new ApplyConfigurationStatus(getLogin(), getStartTime(), ApplyConfigurationStatus.Step.Canceling));
            appEventBus.sendNotification(new AppBusEventImpl(new ApplyConfigurationStatus(getLogin(), getStartTime(), ApplyConfigurationStatus.Step.Canceling)));
            return true;
        }

        return false;
    }

    /**
     * Apply configuration, method execute async!
     *
     * @param configurationName
     * @param localPath
     * @param serverPath
     * @param result
     */
    @Override
    @Async(value = "serverTaskExecutor")
    public void apply(final String configurationName, final FileSystemObject localPath, final FileSystemObject serverPath, ApplyConfigRunCallback result) {

        //try lock
        if (clientApplyLockService.tryLock(getTime(), getUnit())) {

            if (result != null) {
                result.success();
            }

            //status - prepare
            setStatus(new ApplyConfigurationStatus(getLogin(), getStartTime(), ApplyConfigurationStatus.Step.Prepare));
            appEventBus.sendNotification(new AppBusEventImpl(new ApplyConfigurationStatus(getLogin(), getStartTime(), ApplyConfigurationStatus.Step.Prepare)));

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

                int i = 1;
                for (FileSystemObject src : files) {

                    if (ApplyConfigurationStatus.Step.Canceling.equals(getStatus().getStep())) {
                        throw new ConfigurationApplyCancelException();
                    }

                    //status - copy files
                    setStatus(new ApplyConfigurationStatus(getLogin(), getStartTime(), ApplyConfigurationStatus.Step.CopyFiles, files.size(), i, StringUtils.substringAfterLast(src.getPath(), "/")));
                    appEventBus.sendNotification(new AppBusEventImpl(new ApplyConfigurationStatus(getLogin(), getStartTime(), ApplyConfigurationStatus.Step.CopyFiles, files.size(), i, StringUtils.substringAfterLast(src.getPath(), "/"))));

                    fileManagerService.copy(src, new FileSystemObject(
                      serverRootPathName + "/" + clientTmpFolderName + "/" + StringUtils.substring(src.getPath(), localPath.getPath().length()),
                      serverPath.getFileSystemType(),
                      FileSystemObject.FileType.FILE,
                      0L,
                      null
                    ));

                    i++;
                }

                if (ApplyConfigurationStatus.Step.Canceling.equals(getStatus().getStep())) {
                    throw new ConfigurationApplyCancelException();
                }

                //status - update db
                setStatus(new ApplyConfigurationStatus(getLogin(), getStartTime(), ApplyConfigurationStatus.Step.UpdateDatabase));
                appEventBus.sendNotification(new AppBusEventImpl(new ApplyConfigurationStatus(getLogin(), getStartTime(), ApplyConfigurationStatus.Step.UpdateDatabase)));

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
                                        ApplySuccessConfigurationResponse rsp = (ApplySuccessConfigurationResponse) response;
                                        if (rsp != null) {
                                            try {
                                                //status - сonfirmation
                                                setStatus(new ApplyConfigurationStatus(getLogin(), getStartTime(), ApplyConfigurationStatus.Step.Confirmation));
                                                appEventBus.sendNotification(new AppBusEventImpl(new ApplyConfigurationStatus(getLogin(), getStartTime(), ApplyConfigurationStatus.Step.Confirmation)));

                                                //3.3.1 rename server folder to tmp
                                                fileManagerService.rename(serverPath, new FileSystemObject(serverRootPathName + "/" + serverTmpFolderName, serverPath.getFileSystemType(), FileSystemObject.FileType.FOLDER, 0L, null));

                                                //3.3.2 rename tmp local-server folder to server folder
                                                fileManagerService.rename(new FileSystemObject(serverRootPathName + "/" + clientTmpFolderName, serverPath.getFileSystemType(), FileSystemObject.FileType.FOLDER, 0L, null), serverPath);

                                                //3.3.3 remove tmp server folder, do it???
                                                fileManagerService.delete(new FileSystemObject(serverRootPathName + "/" + serverTmpFolderName, serverPath.getFileSystemType(), FileSystemObject.FileType.FOLDER, 0L, null));

                                                setStatus(new ApplyConfigurationStatus(getLogin(), getStartTime(), ApplyConfigurationStatus.Step.Success));
                                                appEventBus.sendNotification(new AppBusEventImpl(new ApplyConfigurationStatus(getLogin(), getStartTime(), ApplyConfigurationStatus.Step.Success)));
                                            } catch (Exception ex) {
                                                throw new Exception("CRITICAL ERROR RENAME ORIGINAL SERVER FOLDER");
                                            }
                                        } else {
                                            throw new Exception("Error unknown success applying command response from server");
                                        }
                                    } catch (Exception ex) {
                                        log.error("Error apply local->server configuration", ex);
                                        setStatus(new ApplyConfigurationStatus(getLogin(), getStartTime(), ApplyConfigurationStatus.Step.Fail));
                                        appEventBus.sendNotification(new AppBusEventImpl(new ApplyConfigurationStatus(getLogin(), getStartTime(), ApplyConfigurationStatus.Step.Fail)));
                                    } finally {
                                        serverApplyLockService.unlock();
                                        clientApplyLockService.unlock();
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
                                        notifyFail();
                                        serverApplyLockService.unlock();
                                        clientApplyLockService.unlock();
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
                                        notifyFail();
                                        serverApplyLockService.unlock();
                                        clientApplyLockService.unlock();
                                    }
                                }

                                @Override
                                public void onTimeout() {
                                    try {
                                        log.error("Error apply local->server configuration: Timeout server response");
                                        //3.3.1 remove tmp local-server folder
                                        fileManagerService.delete(new FileSystemObject(serverRootPathName + "/" + clientTmpFolderName, serverPath.getFileSystemType(), FileSystemObject.FileType.FOLDER, 0L, null));
                                    } catch (Exception ex) {
                                        log.error("Error apply local->server configuration", ex);
                                    } finally {
                                        notifyFail();
                                        serverApplyLockService.unlock();
                                        clientApplyLockService.unlock();
                                    }
                                }
                            });
                        } else {
                            throw new Exception(String.format("Error can not find local database configuration with name [%s]", configurationName));
                        }
                    } catch (Exception ex) {
                        //free server lock
                        serverApplyLockService.unlock();
                        throw ex;
                    }
                } else {
                    throw new Exception("Error apply local->server configuration, can not get server lock");
                }
            } catch (ConfigurationApplyCancelException ex) {
                log.info("Applying local->server configuration is canceled by user", ex);
                try {
                    //4. remove tmp local-remote folder
                    fileManagerService.delete(new FileSystemObject(serverRootPathName + "/" + clientTmpFolderName, serverPath.getFileSystemType(), FileSystemObject.FileType.FOLDER, 0L, null));
                } catch (Exception e) {
                    log.error("Error remove tmp local-remote folder", e);
                }

                setStatus(new ApplyConfigurationStatus(getLogin(), getStartTime(), ApplyConfigurationStatus.Step.Cancel));
                appEventBus.sendNotification(new AppBusEventImpl(new ApplyConfigurationStatus(getLogin(), getStartTime(), ApplyConfigurationStatus.Step.Cancel)));

                //free client lock
                clientApplyLockService.unlock();
            } catch (Exception ex) {
                log.error("Error apply local->server configuration", ex);
                try {
                    //4. remove tmp local-remote folder
                    fileManagerService.delete(new FileSystemObject(serverRootPathName + "/" + clientTmpFolderName, serverPath.getFileSystemType(), FileSystemObject.FileType.FOLDER, 0L, null));
                } catch (Exception e) {
                    log.error("Error remove tmp local-remote folder", e);
                }

                notifyFail();

                //free client lock
                clientApplyLockService.unlock();
            }
        } else {
            log.error("Error apply local->server configuration, can not get client lock");
            if (result != null) {
                result.fail();
            }
        }
    }
}
