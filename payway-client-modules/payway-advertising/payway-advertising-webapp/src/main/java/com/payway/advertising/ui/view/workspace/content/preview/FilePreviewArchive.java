/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.ui.view.workspace.content.preview;

import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.util.HierarchicalContainer;
import com.vaadin.server.Resource;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.TreeTable;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.vaadin.teemu.clara.Clara;
import org.vaadin.teemu.clara.binder.annotation.UiField;

/**
 * FilePreviewArchive
 *
 * @author Sergey Kichenko
 * @created 23.06.15 00:00
 */
@Slf4j
@NoArgsConstructor
public class FilePreviewArchive extends AbstractFilePreview {

    private static final long serialVersionUID = 6894765612390555165L;

    private static final String TREE_PROPERTY_CAPTION = "caption";
    private static final String TREE_PROPERTY_ICON = "icon";

    @UiField
    private TreeTable treeArchive;

    @Override
    protected void init() {

        setSizeFull();
        addComponent(Clara.create("FilePreviewArchive.xml", this));
        setupTreeContainer();
    }

    private void setupTreeContainer() {

        Container container = new HierarchicalContainer();
        container.addContainerProperty(TREE_PROPERTY_CAPTION, String.class, null);
        container.addContainerProperty(TREE_PROPERTY_ICON, Resource.class, null);

        treeArchive.setContainerDataSource(container);
        treeArchive.setItemCaptionPropertyId(TREE_PROPERTY_CAPTION);
        treeArchive.setItemIconPropertyId(TREE_PROPERTY_ICON);
        treeArchive.setColumnExpandRatio(TREE_PROPERTY_CAPTION, 1.0F);
        treeArchive.setVisibleColumns(TREE_PROPERTY_CAPTION);
    }

    @Override
    protected void loadContent(InputStream stream, String fileName) {

        try (InputStream is = stream) {
            showArchive(new ZipInputStream(is));
        } catch (Exception ex) {
            log.error("Cannot load archive content for preview - ", ex);
        }
    }

    @Override
    public AbstractFilePreview build(InputStream stream, String fileName) {
        init();
        loadContent(stream, fileName);
        return this;
    }

    private void showArchive(ZipInputStream zis) throws Exception {

        ZipEntry ze;
        while ((ze = zis.getNextEntry()) != null) {
            walk(ze.getName(), ze.isDirectory(), (HierarchicalContainer) treeArchive.getContainerDataSource());
        }

        treeArchive.setCollapsed(treeArchive.getContainerDataSource().getItemIds().iterator().next(), false);
    }

    private void walk(String path, boolean isDirectory, HierarchicalContainer container) {

        String currentPath = "";
        String[] tokens = StringUtils.split(path, "/");
        for (int i = 0; i < tokens.length; i++) {

            boolean isDir = (i != tokens.length - 1) ? true : isDirectory;
            String previousPath = currentPath;
            currentPath += tokens[i];

            if (isDir) {
                currentPath += "/";
            }

            Item currentItem = container.getItem(currentPath);
            if (currentItem != null) {
                continue;
            }

            currentItem = container.addItem(currentPath);
            if (!StringUtils.isBlank(previousPath)) {
                container.setParent(currentPath, previousPath);
            }

            container.setChildrenAllowed(currentPath, isDir);
            currentItem.getItemProperty(TREE_PROPERTY_CAPTION).setValue(tokens[i]);
            currentItem.getItemProperty(TREE_PROPERTY_ICON).setValue(isDir ? new ThemeResource("images/folder.png") : new ThemeResource("images/file.png"));
        }
    }
}
