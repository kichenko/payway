/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.ui.view.workspace.content.preview;

import com.vaadin.server.DefaultErrorHandler;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.Video;
import java.io.InputStream;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.vaadin.teemu.clara.Clara;
import org.vaadin.teemu.clara.binder.annotation.UiField;

/**
 * FilePreviewVideo
 *
 * @author Sergey Kichenko
 * @created 23.06.15 00:00
 */
@Slf4j
@NoArgsConstructor
public class FilePreviewVideo extends AbstractFilePreview {

    private static final long serialVersionUID = 8484774321723519679L;

    @UiField
    private Video video;

    @Override
    protected void init() {
        setSizeFull();
        addComponent(Clara.create("FilePreviewVideo.xml", this));

        video.setAutoplay(true);
        video.setShowControls(true);
        video.setErrorHandler(new DefaultErrorHandler() {
            private static final long serialVersionUID = -8922788429015008561L;

            @Override
            public void error(com.vaadin.server.ErrorEvent event) {
                log.error("Cannot play video file - ", event.getThrowable().getMessage());
            }
        });
    }

    @Override
    protected void loadContent(InputStream stream, String fileName) {
        video.setSource(createContentResource(stream, fileName));
    }

    @Override
    public AbstractFilePreview build(InputStream stream, String fileName) {
        init();
        loadContent(stream, fileName);
        return this;
    }

    private StreamResource createContentResource(final InputStream stream, String fileName) {

        StreamResource streamResource = new StreamResource(new StreamResource.StreamSource() {
            private static final long serialVersionUID = -2480723276190894707L;

            @Override
            public InputStream getStream() {
                return stream;
            }
        }, fileName);

        streamResource.setCacheTime(0);

        return streamResource;
    }
}
