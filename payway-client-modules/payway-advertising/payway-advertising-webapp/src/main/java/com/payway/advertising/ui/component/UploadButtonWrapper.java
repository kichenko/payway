/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.ui.component;

import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Upload;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

/**
 * UploadButtonWrapper
 *
 * @author Sergey Kichenko
 * @created 28.05.15 00:00
 */
public class UploadButtonWrapper extends HorizontalLayout implements Upload.StartedListener {

    public interface UploadStartedEventProcessor {

        boolean process(Upload upload, Upload.StartedEvent event);
    }

    private static final long serialVersionUID = 7826554048988102828L;

    @Setter
    private UploadStartedEventProcessor startedEventProcessorListener;

    @Getter(AccessLevel.PUBLIC)
    @Setter(AccessLevel.PRIVATE)
    private Upload upload;

    public UploadButtonWrapper() {
        createUploadButton();
    }

    private void createUploadButton() {

        if (getUpload() != null) {
            removeComponent(getUpload());
        }

        setUpload(new Upload());

        getUpload().setImmediate(true);
        getUpload().setButtonCaption("");
        getUpload().addStartedListener(this);
        addComponent(getUpload());
    }

    @Override
    public void uploadStarted(Upload.StartedEvent event) {

        //then process event, clear & create new upload
        if (startedEventProcessorListener != null) {
            if (!startedEventProcessorListener.process(getUpload(), event)) {
                getUpload().interruptUpload();
            }
        }

        createUploadButton();
    }
}
