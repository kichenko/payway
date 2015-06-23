/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.ui.view.workspace.content.preview;

import com.vaadin.ui.VerticalLayout;
import lombok.extern.slf4j.Slf4j;

/**
 * AbstractFilePreview
 *
 * @author sergey kichenko
 * @created 23.06.15 00:00
 */
@Slf4j
public abstract class AbstractFilePreview extends VerticalLayout {

    private static final long serialVersionUID = 9074580570822532757L;

    protected abstract void init();
}
