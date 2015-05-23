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
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;
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
    @Qualifier(value = "serverApplyLockService")
    private ConfigurationApplyLockService serverApplyLockService;

    @Getter
    @Setter
    @Value("SECONDS")
    private TimeUnit unit;

    @Getter
    @Setter
    @Value("1")
    private long applyLockTimeOut;

    @Getter
    @Setter
    @Value("1")
    private long clientTimeOut;

    private final Semaphore clientSemaphore = new Semaphore(1);

    /**
     * Time to wait response message from server, if timeout - log & free all
     * locks.
     */
    @Getter
    @Setter
    @Value("300")
    private long serverTimeOut;

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
    private volatile ApplyConfigurationStatus status = new ApplyConfigurationStatus("", new LocalDateTime(), ApplyStatus.None, new LocalDateTime());

    @Autowired
    @Qualifier(value = "appEventBus")
    private AppEventBus appEventBus;

    private void notifyFail() {
        ApplyConfigurationStatus acs = new ApplyConfigurationStatus(getLogin(), getStartTime(), ApplyStatus.Fail, new LocalDateTime());
        setStatus(acs);
        appEventBus.sendNotification(new AppBusEventImpl(acs));
    }

    private ApplyConfigurationRequest buildApplyConfigurationRequest(String configurationName) throws Exception {

        List<AgentFileDto> agentFilesDto;
        DbConfiguration cfg = configurationService.findConfigurationByNameWithFiles(configurationName);

        if (cfg == null) {
            throw new Exception(String.format("Can not find local database configuration with name [%s]", configurationName));
        }

        agentFilesDto = new ArrayList<>(cfg.getFiles().size());
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

        return new ApplyConfigurationRequest(new ConfigurationDto(agentFilesDto));
    }

    /**
     * Cancel applying configuration, only on step copying files.
     *
     * @return
     */
    @Override
    public boolean cancel() {
        if (ApplyStatus.Prepare.equals(getStatus().getStatus()) || ApplyStatus.CopyFiles.equals(getStatus().getStatus())) {
            ApplyConfigurationStatus acs = new ApplyConfigurationStatus(getLogin(), getStartTime(), ApplyStatus.Canceling, new LocalDateTime());
            setStatus(acs);
            appEventBus.sendNotification(new AppBusEventImpl(acs));
            return true;
        }

        return false;
    }

    /**
     * Apply configuration, method execute async!
     *
     * @param userName
     * @param configurationName
     * @param localPath
     * @param serverPath
     * @param result
     */
    @Override
    @Async(value = "serverTaskExecutor")
    public void apply(final String userName, final String configurationName, final FileSystemObject localPath, final FileSystemObject serverPath, ApplyConfigRunCallback result) {

        try {
            ApplyConfigurationStatus acs = new ApplyConfigurationStatus(getLogin(), getStartTime(), ApplyStatus.Prepare, new LocalDateTime());

            //1. get unique name
            final String serverRootPathName = StringUtils.substringBeforeLast(serverPath.getPath(), "/");
            final String clientTmpFolderName = configurationService.generateUniqueFolderName("local", configurationName);
            final String serverTmpFolderName = configurationService.generateUniqueFolderName("server", StringUtils.substringAfterLast(serverPath.getPath(), "/"));

            //try lock
            if (clientSemaphore.tryAcquire(getClientTimeOut(), getUnit())) {
                try {
                    try {
                        if (result != null) {
                            result.success();
                        }
                    } catch (Exception ex) {
                        //
                    }

                    //set info
                    setLogin(userName);
                    setStartTime(new LocalDateTime());

                    //status - prepare
                    setStatus(acs);
                    appEventBus.sendNotification(new AppBusEventImpl(acs));

                    //2. copy files
                    List<FileSystemObject> files = fileManagerService.list(localPath, false, true);
                    if (files == null || files.isEmpty()) {
                        throw new Exception("Apply configuration - files not found");
                    }

                    int counter = 1;
                    for (FileSystemObject src : files) {

                        if (ApplyStatus.Canceling.equals(getStatus().getStatus())) {
                            throw new ConfigurationApplyCancelException();
                        }

                        //status - copy files
                        acs = new ApplyConfigurationStatus(getLogin(), getStartTime(), ApplyStatus.CopyFiles, new LocalDateTime(), files.size(), counter, StringUtils.substringAfterLast(src.getPath(), "/"));
                        setStatus(acs);
                        appEventBus.sendNotification(new AppBusEventImpl(acs));

                        fileManagerService.copy(src, new FileSystemObject(
                          serverRootPathName + "/" + clientTmpFolderName + "/" + StringUtils.substring(src.getPath(), localPath.getPath().length()),
                          serverPath.getFileSystemType(),
                          FileSystemObject.FileType.FILE,
                          0L,
                          null
                        ));

                        counter++;
                    }

                    if (ApplyStatus.Canceling.equals(getStatus().getStatus())) {
                        throw new ConfigurationApplyCancelException();
                    }

                    //status - update db
                    acs = new ApplyConfigurationStatus(getLogin(), getStartTime(), ApplyStatus.UpdateDatabase, new LocalDateTime());
                    setStatus(acs);
                    appEventBus.sendNotification(new AppBusEventImpl(acs));

                    //3. send server msg
                    if (serverApplyLockService.tryLock(getApplyLockTimeOut(), getUnit())) {
                        try {
                            final CountDownLatch latch = new CountDownLatch(1);

                            //3.1 build request dto
                            ApplyConfigurationRequest req = buildApplyConfigurationRequest(configurationName);

                            if (req == null) {
                                throw new Exception("Empty request object constructed");
                            }

                            //3.2 send req dto to server, wait response
                            messageServerSenderService.sendMessage(req, new ResponseCallBack() {
                                @Override
                                public void onServerResponse(final SuccessResponse response) {

                                    ApplyConfigurationStatus acs;

                                    try {
                                        ApplySuccessConfigurationResponse rsp = (ApplySuccessConfigurationResponse) response;
                                        if (rsp != null) {
                                            try {
                                                //status - сonfirmation
                                                acs = new ApplyConfigurationStatus(getLogin(), getStartTime(), ApplyStatus.Confirmation, new LocalDateTime());
                                                setStatus(acs);
                                                appEventBus.sendNotification(new AppBusEventImpl(acs));

                                                //3.3.1 rename server folder to tmp
                                                fileManagerService.rename(serverPath, new FileSystemObject(serverRootPathName + "/" + serverTmpFolderName, serverPath.getFileSystemType(), FileSystemObject.FileType.FOLDER, 0L, null));

                                                //3.3.2 rename tmp local-server folder to server folder
                                                fileManagerService.rename(new FileSystemObject(serverRootPathName + "/" + clientTmpFolderName, serverPath.getFileSystemType(), FileSystemObject.FileType.FOLDER, 0L, null), serverPath);

                                                //3.3.3 remove tmp server folder
                                                fileManagerService.delete(new FileSystemObject(serverRootPathName + "/" + serverTmpFolderName, serverPath.getFileSystemType(), FileSystemObject.FileType.FOLDER, 0L, null));

                                                acs = new ApplyConfigurationStatus(getLogin(), getStartTime(), ApplyStatus.Success, new LocalDateTime());
                                                setStatus(acs);
                                                appEventBus.sendNotification(new AppBusEventImpl(acs));
                                            } catch (Exception ex) {
                                                throw new Exception("CAN NOT RENAME ORIGINAL SERVER FOLDER");
                                            }
                                        } else {
                                            throw new Exception("Unknown success applying command response from server");
                                        }
                                    } catch (Exception ex) {
                                        log.error("Apply local->server configuration", ex);
                                        acs = new ApplyConfigurationStatus(getLogin(), getStartTime(), ApplyStatus.Fail, new LocalDateTime());
                                        setStatus(acs);
                                        appEventBus.sendNotification(new AppBusEventImpl(acs));
                                    } finally {
                                        latch.countDown();
                                    }
                                }

                                @Override
                                public void onServerResponse(final SuccessResponse response, final Map data) {
                                    //never called
                                }

                                @Override
                                public void onServerException(final ExceptionResponse exception) {
                                    try {
                                        log.error("Apply local->server configuration", exception.getMessage());

                                        //3.3.1 remove tmp local-server folder
                                        fileManagerService.delete(new FileSystemObject(serverRootPathName + "/" + clientTmpFolderName, serverPath.getFileSystemType(), FileSystemObject.FileType.FOLDER, 0L, null));
                                    } catch (Exception ex) {
                                        log.error("Apply local->server configuration", ex);
                                    } finally {
                                        notifyFail();
                                        latch.countDown();
                                    }
                                }

                                @Override
                                public void onTimeout() {
                                    try {
                                        log.error("Apply local->server configuration: Timeout server response");
                                        //3.3.1 remove tmp local-server folder
                                        fileManagerService.delete(new FileSystemObject(serverRootPathName + "/" + clientTmpFolderName, serverPath.getFileSystemType(), FileSystemObject.FileType.FOLDER, 0L, null));
                                    } catch (Exception ex) {
                                        log.error("Apply local->server configuration", ex);
                                    } finally {
                                        notifyFail();
                                        latch.countDown();
                                    }
                                }

                                @Override
                                public void onLocalException(Exception ex) {
                                    try {
                                        log.error("Apply local->server configuration: Local server response exception");

                                        //3.3.1 remove tmp local-server folder
                                        fileManagerService.delete(new FileSystemObject(serverRootPathName + "/" + clientTmpFolderName, serverPath.getFileSystemType(), FileSystemObject.FileType.FOLDER, 0L, null));
                                    } catch (Exception e) {
                                        log.error("Apply local->server configuration", ex);
                                    } finally {
                                        notifyFail();
                                        latch.countDown();
                                    }
                                }
                            });

                            //warning: timeout used if server never answered (onTimeout event must be implemented)
                            if (!latch.await(serverTimeOut, getUnit())) {
                                log.error("Timeout applying task - no answer from the server");
                                throw new Exception("Timeout applying task - no answer from the server");
                            }

                            //must free server lock
                            serverApplyLockService.unlock();

                        } catch (Exception ex) {
                            //must free server lock
                            serverApplyLockService.unlock();
                            throw ex;
                        }
                    } else {
                        throw new Exception("Apply local->server configuration, can not get server lock");
                    }

                    //must free client lock
                    clientSemaphore.release();

                } catch (ConfigurationApplyCancelException ex) {
                    log.info("Applying local->server configuration is canceled by user", ex);
                    try {
                        //4. remove tmp local-remote folder
                        fileManagerService.delete(new FileSystemObject(serverRootPathName + "/" + clientTmpFolderName, serverPath.getFileSystemType(), FileSystemObject.FileType.FOLDER, 0L, null));
                    } catch (Exception e) {
                        log.error("Remove tmp local-remote folder", e);
                    }

                    acs = new ApplyConfigurationStatus(getLogin(), getStartTime(), ApplyStatus.Cancel, new LocalDateTime());
                    setStatus(acs);
                    appEventBus.sendNotification(new AppBusEventImpl(acs));

                    //must free client lock
                    clientSemaphore.release();

                } catch (Exception ex) {
                    log.error("Apply local->server configuration", ex);
                    try {
                        //4. remove tmp local-remote folder
                        fileManagerService.delete(new FileSystemObject(serverRootPathName + "/" + clientTmpFolderName, serverPath.getFileSystemType(), FileSystemObject.FileType.FOLDER, 0L, null));
                    } catch (Exception e) {
                        log.error("Remove tmp local-remote folder", e);
                    }

                    notifyFail();

                    //must free client lock
                    clientSemaphore.release();
                }
            } else {
                log.error("Apply local->server configuration, can not get client lock");
                try {
                    if (result != null) {
                        result.fail();
                    }
                } catch (Exception ex) {
                    //
                }
            }
        } catch (Exception ex) {
            log.error("Apply local->server configuration, can not get client lock", ex);
            try {
                if (result != null) {
                    result.fail();
                }
            } catch (Exception e) {
                //
            }
        }
    }
}
