/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.kioskcashier.ui.view.core;

import com.payway.commons.webapp.messaging.MessageServerSenderService;
import com.payway.commons.webapp.service.app.user.WebAppUserService;
import com.payway.commons.webapp.ui.view.core.AbstractWorkspaceView;
import com.payway.kioskcashier.service.app.settings.AbstractKioskCashierSettingsAppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * AbstractKioskCashierWorkspaceView
 *
 * @author Sergey Kichenko
 * @created 02.07.15 00:00
 */
public abstract class AbstractKioskCashierWorkspaceView extends AbstractWorkspaceView {

    public static final String WORKSPACE_VIEW_ID_PREFIX = "kiosk-cashier-workspace-view-";

    private static final long serialVersionUID = -2811992155156697362L;

    @Autowired
    @Qualifier("messageServerSenderService")
    protected MessageServerSenderService service;

    @Autowired
    protected AbstractKioskCashierSettingsAppService settingsAppService;

    @Autowired
    @Qualifier(value = "webApps.WebAppUserService")
    protected WebAppUserService userAppService;
}
