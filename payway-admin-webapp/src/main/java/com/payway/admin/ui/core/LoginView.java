/*
 * (c) Sergey Kichenko, 2015. All right reserved.
 */
package com.payway.admin.ui.core;

import com.payway.admin.messaging.MessageServerSenderServiceImpl;
import com.payway.admin.messaging.ResponseCallBack;
import com.payway.messaging.core.response.ExceptionResponse;
import com.payway.messaging.core.response.SuccessResponse;
import com.payway.messaging.message.request.auth.AuthCommandRequest;
import com.payway.model.messaging.auth.UserImpl;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.TaskExecutor;
import org.vaadin.teemu.clara.Clara;
import org.vaadin.teemu.clara.binder.annotation.UiField;
import org.vaadin.teemu.clara.binder.annotation.UiHandler;

/**
 * LoginView of admin webapp
 *
 * @author Sergey Kichenko
 * @created 20.04.15 00:00
 */
@SpringView(name = "login")
public final class LoginView extends CustomComponentView implements View {

    @Autowired
    @Qualifier("serverTaskExecutor")
    private TaskExecutor serverTaskExecutor;

    @Autowired
    @Qualifier("serviceSender")
    MessageServerSenderServiceImpl service;

    @UiField
    private TextField textUserName;

    @UiField
    private PasswordField textPassword;

    @UiField
    private CheckBox checkBoxRememberMe;

    private final ProgressBarWindow progressBarWindow = new ProgressBarWindow();

    public LoginView() {
        setSizeFull();
        setCompositionRoot(Clara.create("LoginView.xml", this));
    }

    @UiHandler("buttonSignIn")
    public void clickButtonSignIn(Button.ClickEvent event) throws Exception {

        //final UI currentUI = UI.getCurrent();
        //UI.getCurrent().getNavigator().navigateTo("main");
        progressBarWindow.show();

        serverTaskExecutor.execute(new Runnable() {
            @Override
            public void run() {

                service.sendMessage(new AuthCommandRequest<>(new UserImpl(textUserName.getValue(), textPassword.getValue(), "", Boolean.TRUE, null), true),
                        new ResponseCallBack<SuccessResponse, ExceptionResponse>() {

                            /*
                             private UI ui;
                             public ResponseCallBack<SuccessResponse, ExceptionResponse> setUI(UI ui) {
                             this.ui = ui;
                             return this;
                             }
                             */
                            private UI getUI() {
                                return LoginView.this.getUI();
                            }

                            @Override
                            public void onServerResponse(SuccessResponse response) {
                                final UI ui = getUI();
                                if (ui != null) {
                                    ui.access(new Runnable() {
                                        @Override
                                        public void run() {
                                            progressBarWindow.close();
                                            Notification.show("Notification", "onServerResponse", Notification.Type.WARNING_MESSAGE);
                                            ui.getNavigator().navigateTo("main");
                                        }
                                    });
                                }
                            }

                            @Override
                            public void onServerException(ExceptionResponse exception) {
                                UI ui = getUI();
                                if (ui != null) {
                                    ui.access(new Runnable() {
                                        @Override
                                        public void run() {
                                            progressBarWindow.close();
                                            Notification.show("Notification", "onServerException", Notification.Type.WARNING_MESSAGE);
                                        }
                                    });
                                }
                            }

                            @Override
                            public void onTimeout() {
                                final UI ui = getUI();
                                if (ui != null) {
                                    ui.access(new Runnable() {
                                        @Override
                                        public void run() {
                                            progressBarWindow.close();
                                            Notification.show("Notification", "onTimeout", Notification.Type.WARNING_MESSAGE);
                                            ui.getNavigator().navigateTo("main");
                                        }
                                    });
                                }
                            }

                            @Override
                            public void onLocalException() {
                                final UI ui = getUI();
                                if (ui != null) {
                                    ui.access(new Runnable() {
                                        @Override
                                        public void run() {
                                            progressBarWindow.close();
                                            Notification.show("Notification", "onLocalException", Notification.Type.WARNING_MESSAGE);
                                            ui.getNavigator().navigateTo("main");
                                        }
                                    });
                                }
                            }

                        }/*.setUI(currentUI)*/);
            }
        });
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        //
    }
}
