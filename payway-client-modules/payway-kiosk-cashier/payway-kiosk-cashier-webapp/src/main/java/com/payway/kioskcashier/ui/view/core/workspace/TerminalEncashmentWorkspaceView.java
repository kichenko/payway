/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.kioskcashier.ui.view.core.workspace;

import com.payway.commons.webapp.messaging.MessageServerSenderService;
import com.payway.commons.webapp.service.app.settings.SettingsAppService;
import com.payway.commons.webapp.service.app.user.WebAppUserService;
import com.payway.kioskcashier.service.app.settings.model.KioskCashierSessionSettings;
import com.payway.kioskcashier.ui.components.terminal.encashment.TerminalEncashmentWizard;
import com.payway.kioskcashier.ui.view.core.AbstractKioskCashierWorkspaceView;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.vaadin.teemu.clara.Clara;
import org.vaadin.teemu.clara.binder.annotation.UiField;

/**
 * TerminalEncashmentWorkspaceView
 *
 * @author Sergey Kichenko
 * @created 02.07.15 00:00
 */
@Slf4j
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Component(value = TerminalEncashmentWorkspaceView.TERMINAL_ENCASHMENT_WORKSPACE_VIEW_ID)
public class TerminalEncashmentWorkspaceView extends AbstractKioskCashierWorkspaceView {

    public static final String TERMINAL_ENCASHMENT_WORKSPACE_VIEW_ID = WORKSPACE_VIEW_ID_PREFIX + "terminal-encashment";

    private static final long serialVersionUID = -5429806955667759330L;

    @UiField
    private TerminalEncashmentWizard wizard;

    @Autowired
    @Qualifier("messageServerSenderService")
    private MessageServerSenderService service;

    @Autowired
    SettingsAppService<KioskCashierSessionSettings> settingsAppService;

    @Autowired
    @Qualifier(value = "webApps.WebAppUserService")
    private WebAppUserService userAppService;

    @PostConstruct
    public void postConstruct() {
        setSizeFull();
        addComponent(Clara.create("TerminalEncashmentWorkspaceView.xml", this));
    }

    @Override
    public void activate() {
        wizard.setService(service);
        wizard.setSessionId(userAppService.getUser().getSessionId());
        wizard.setCurrency(settingsAppService.getCurrency());
    }
}
