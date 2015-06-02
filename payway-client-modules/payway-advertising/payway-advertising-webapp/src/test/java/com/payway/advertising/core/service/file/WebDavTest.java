/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.core.service.file;

import java.util.UUID;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemManager;
import org.apache.commons.vfs2.Selectors;
import org.apache.commons.vfs2.VFS;
import org.apache.commons.vfs2.impl.StandardFileSystemManager;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * WebDavTest
 *
 * @author Sergey Kichenko
 * @created 02.03.15 00:00
 */
public class WebDavTest {

    private FileSystemManager fileSystemManager;
    private String webDavPath;

    @BeforeClass
    public void setUp() throws Exception {
        fileSystemManager = new StandardFileSystemManager();
        ((StandardFileSystemManager) fileSystemManager).init();

        webDavPath = "webdav://mike:ekimka@test.pwypp.com:9988/WebApps/shared/";

        //to prevent bug https://issues.apache.org/jira/browse/VFS-467, https://issues.apache.org/jira/browse/VFS-465
        //use last 2.1-SNAPSHOT
        VFS.setUriStyle(true);
    }

    @Test(enabled = false)
    public void testCreateAndRemove() throws Exception {

        VFS.setUriStyle(true);

        String folderName = webDavPath + UUID.randomUUID().toString() + "/";

        FileObject foCreate;
        FileObject foDelete;

        foCreate = fileSystemManager.resolveFile(folderName);
        foCreate.createFolder();
        foCreate = fileSystemManager.resolveFile(folderName + "/" + UUID.randomUUID().toString());
        foCreate.createFolder();

        foDelete = fileSystemManager.resolveFile(folderName);

        foDelete.delete(Selectors.SELECT_ALL);
    }
}
