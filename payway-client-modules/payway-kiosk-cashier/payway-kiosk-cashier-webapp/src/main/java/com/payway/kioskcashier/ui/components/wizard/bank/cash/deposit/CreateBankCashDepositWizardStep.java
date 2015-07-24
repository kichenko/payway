/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.kioskcashier.ui.components.wizard.bank.cash.deposit;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.gwt.thirdparty.guava.common.base.Predicate;
import com.google.gwt.thirdparty.guava.common.collect.FluentIterable;
import com.payway.commons.webapp.messaging.UIResponseCallBackSupport;
import com.payway.commons.webapp.ui.InteractionUI;
import com.payway.commons.webapp.ui.components.wizard.WizardStepValidationException;
import com.payway.kioskcashier.ui.components.wizard.bank.cash.deposit.datasource.AccountModel;
import com.payway.kioskcashier.ui.components.wizard.bank.cash.deposit.datasource.BankCashDepositModel;
import com.payway.kioskcashier.ui.components.wizard.bank.cash.deposit.datasource.BeanNameModelBeanItemContainer;
import com.payway.kioskcashier.ui.components.wizard.bank.cash.deposit.datasource.NoteCountingDepositModel;
import com.payway.kioskcashier.ui.components.wizard.bank.cash.deposit.datasource.NoteCountingDepositModelBeanItemContainer;
import com.payway.kioskcashier.ui.components.wizard.bank.cash.deposit.datasource.StuffModel;
import com.payway.messaging.core.response.ExceptionResponse;
import com.payway.messaging.core.response.SuccessResponse;
import com.payway.messaging.message.kioskcashier.CashDepositNominalsRequest;
import com.payway.messaging.message.kioskcashier.CashDepositNominalsResponse;
import com.payway.messaging.message.kioskcashier.CreateBankCashDepositParamsRequest;
import com.payway.messaging.message.kioskcashier.CreateBankCashDepositParamsResponse;
import com.payway.messaging.model.common.BankAccountDto;
import com.payway.messaging.model.common.StaffDto;
import com.payway.messaging.model.kioskcashier.NoteCountingDepositDto;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.converter.StringToDateConverter;
import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.vaadin.teemu.clara.Clara;
import org.vaadin.teemu.clara.binder.annotation.UiField;

/**
 * CreateBankCashDepositWizardStep
 *
 * @author Sergey Kichenko
 * @created 21.07.15 00:00
 */
@Slf4j
public final class CreateBankCashDepositWizardStep extends AbstractBankCashDepositBeanItemWizardStep {

    private static final long serialVersionUID = 8838845065975817724L;

    @Getter
    @AllArgsConstructor
    public final static class CreateBankCashDepositWizardStepParams extends AbstractWizardStepParams {

        private final BankAccountDto acccount;
    }

    private static final Function<NoteCountingDepositDto, NoteCountingDepositModel> transformerNoteCountingDeposit = new Function<NoteCountingDepositDto, NoteCountingDepositModel>() {

        @Override
        public NoteCountingDepositModel apply(NoteCountingDepositDto src) {

            if (src != null) {
                return new NoteCountingDepositModel(src.getId(), src.getCreated(), src.getTerminalName(), src.getSeqNum(), true);
            }
            return null;
        }
    };

    private static final Function<StaffDto, StuffModel> transformerStuffModel = new Function<StaffDto, StuffModel>() {

        @Override
        public StuffModel apply(StaffDto src) {
            if (src != null) {
                return new StuffModel(src.getId(), src.getName());
            }
            return null;
        }
    };

    @UiField
    private ComboBox cbDepositedBy;

    @UiField
    private PopupDateField cbDateCreated;

    @UiField
    private ComboBox cbAccount;

    @UiField
    private TextField editTeller;

    @UiField
    private Table gridDepositCountings;

    private final NoteCountingDepositModelBeanItemContainer containerDepositCountings = new NoteCountingDepositModelBeanItemContainer();

    public CreateBankCashDepositWizardStep() {
        init();
    }

    @Override
    protected void init() {

        setSizeFull();
        addComponent(Clara.create("CreateBankCashDepositWizardStep.xml", this));

        cbDateCreated.setRequired(true);
        editTeller.setRequired(true);

        //comboboxes
        cbDepositedBy.setRequired(true);
        cbDepositedBy.setItemCaptionPropertyId("name");
        cbDepositedBy.setItemCaptionMode(AbstractSelect.ItemCaptionMode.PROPERTY);
        cbDepositedBy.setContainerDataSource(new BeanNameModelBeanItemContainer());

        cbAccount.setRequired(true);
        cbAccount.setItemCaptionPropertyId("name");
        cbAccount.setItemCaptionMode(AbstractSelect.ItemCaptionMode.PROPERTY);
        cbAccount.setContainerDataSource(new BeanNameModelBeanItemContainer());

        //grid container
        gridDepositCountings.setContainerDataSource(containerDepositCountings);

        //columns
        gridDepositCountings.setColumnHeader("created", "Created");
        gridDepositCountings.setConverter("created", new StringToDateConverter() {
            private static final long serialVersionUID = 3721451326801495897L;

            @Override
            protected DateFormat getFormat(Locale locale) {
                return new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
            }
        });

        gridDepositCountings.setColumnHeader("terminalName", "Terminal name");
        gridDepositCountings.setColumnHeader("seqNum", "Report No");
        gridDepositCountings.addGeneratedColumn("checked", new Table.ColumnGenerator() {
            private static final long serialVersionUID = 2855441121974230973L;

            @Override
            public Object generateCell(final Table source, final Object itemId, Object columnId) {
                CheckBox checkBox = new CheckBox(null, source.getContainerDataSource().getItem(itemId).getItemProperty("selected"));
                VerticalLayout layout = new VerticalLayout(checkBox);
                layout.setComponentAlignment(checkBox, Alignment.MIDDLE_CENTER);
                return layout;
            }
        });
        gridDepositCountings.setColumnHeader("checked", "");

        //column align
        gridDepositCountings.setColumnAlignment("created", Table.Align.CENTER);
        gridDepositCountings.setColumnAlignment("terminalName", Table.Align.CENTER);
        gridDepositCountings.setColumnAlignment("seqNum", Table.Align.CENTER);

        gridDepositCountings.setVisibleColumns("created", "terminalName", "seqNum", "checked");
        gridDepositCountings.sort(new Object[]{"created"}, new boolean[]{true});
    }

    @Override
    public void setBeanItem(BeanItem<BankCashDepositModel> value) {

        super.setBeanItem(value);

        editTeller.setPropertyDataSource(getBeanItem().getItemProperty("teller"));
        cbDepositedBy.setPropertyDataSource(getBeanItem().getItemProperty("depositedBy"));
        cbDateCreated.setPropertyDataSource(getBeanItem().getItemProperty("created"));
        cbAccount.setPropertyDataSource(getBeanItem().getItemProperty("account"));
    }

    @Override
    public void setupStep(AbstractWizardStepParams state) {

        CreateBankCashDepositWizardStepParams st = (CreateBankCashDepositWizardStepParams) state;
        AccountModel account = new AccountModel(st.getAcccount().getId(), st.getAcccount().getAccount());

        editTeller.setValue("");
        cbDepositedBy.setValue(null);
        cbDateCreated.setValue(new Date());
        gridDepositCountings.removeAllItems();

        ((BeanNameModelBeanItemContainer) cbAccount.getContainerDataSource()).removeAllItems();
        ((BeanNameModelBeanItemContainer) cbAccount.getContainerDataSource()).addBean(account);
        cbAccount.select(account);

        ((InteractionUI) UI.getCurrent()).showProgressBar();
        getService().sendMessage(new CreateBankCashDepositParamsRequest(),
                new UIResponseCallBackSupport(UI.getCurrent(), new UIResponseCallBackSupport.ResponseCallBackHandler() {

                    @Override
                    public void doServerResponse(SuccessResponse response) {

                        if (response instanceof CreateBankCashDepositParamsResponse) {

                            CreateBankCashDepositParamsResponse rsp = (CreateBankCashDepositParamsResponse) response;
                            BeanNameModelBeanItemContainer depositedByContainer = (BeanNameModelBeanItemContainer) cbDepositedBy.getContainerDataSource();

                            containerDepositCountings.removeAllItems();
                            containerDepositCountings.addAll(Lists.transform(rsp.getKioskEncashments(), transformerNoteCountingDeposit));

                            depositedByContainer.removeAllItems();
                            depositedByContainer.addAll(Lists.transform(rsp.getStaffs(), transformerStuffModel));
                        } else {
                            log.error("Bad server response (unknown type) - {}", response);
                            ((InteractionUI) UI.getCurrent()).showNotification("Server error", "Internal server error", Notification.Type.ERROR_MESSAGE);
                        }

                        ((InteractionUI) UI.getCurrent()).closeProgressBar();
                    }

                    @Override
                    public void doServerException(ExceptionResponse exception) {
                        log.error("Bad exception response (server exception) - {}", exception);
                        ((InteractionUI) UI.getCurrent()).closeProgressBar();
                        ((InteractionUI) UI.getCurrent()).showNotification("Server error", "Internal server error", Notification.Type.ERROR_MESSAGE);
                    }

                    @Override
                    public void doLocalException(Exception exception) {
                        log.error("Bad exception response (local exception) - {}", exception);
                        ((InteractionUI) UI.getCurrent()).closeProgressBar();
                        ((InteractionUI) UI.getCurrent()).showNotification("Server error", "Internal server error", Notification.Type.ERROR_MESSAGE);
                    }

                    @Override
                    public void doTimeout() {
                        log.error("Bad exception response (time out)");
                        ((InteractionUI) UI.getCurrent()).closeProgressBar();
                        ((InteractionUI) UI.getCurrent()).showNotification("Server error", "Internal server error", Notification.Type.ERROR_MESSAGE);
                    }
                })
        );
    }

    @Override
    public void next(final ActionWizardStepHandler listener, Object... args) {

        ((InteractionUI) UI.getCurrent()).showProgressBar();
        getService().sendMessage(new CashDepositNominalsRequest(),
                new UIResponseCallBackSupport(UI.getCurrent(), new UIResponseCallBackSupport.ResponseCallBackHandler() {

                    @Override
                    public void doServerResponse(SuccessResponse response) {
                        if (response instanceof CashDepositNominalsResponse) {

                            CashDepositNominalsResponse rsp = (CashDepositNominalsResponse) response;
                            getBeanItem().getBean().getDepositCountings().clear();
                            getBeanItem().getBean().getDepositCountings().addAll(FluentIterable.from(containerDepositCountings.getItemIds()).filter(new Predicate<NoteCountingDepositModel>() {
                                @Override
                                public boolean apply(NoteCountingDepositModel counting) {
                                    return counting.isSelected();
                                }
                            }).toList());

                            listener.success(rsp.getNominals());
                        } else {
                            log.error("Bad server response (unknown type) - {}", response);
                            ((InteractionUI) UI.getCurrent()).showNotification("Server error", "Internal server error", Notification.Type.ERROR_MESSAGE);
                            listener.exception(new Exception("Internal server error"));
                        }

                        ((InteractionUI) UI.getCurrent()).closeProgressBar();
                    }

                    @Override

                    public void doServerException(ExceptionResponse exception) {
                        log.error("Bad exception response (server exception) - {}", exception);
                        ((InteractionUI) UI.getCurrent()).closeProgressBar();
                        ((InteractionUI) UI.getCurrent()).showNotification("Server error", "Internal server error", Notification.Type.ERROR_MESSAGE);
                        listener.exception(new Exception("Internal server error"));
                    }

                    @Override
                    public void doLocalException(Exception exception) {
                        log.error("Bad exception response (local exception) - {}", exception);
                        ((InteractionUI) UI.getCurrent()).closeProgressBar();
                        ((InteractionUI) UI.getCurrent()).showNotification("Server error", "Internal server error", Notification.Type.ERROR_MESSAGE);
                        listener.exception(exception);
                    }

                    @Override
                    public void doTimeout() {
                        log.error("Bad exception response (time out)");
                        ((InteractionUI) UI.getCurrent()).closeProgressBar();
                        ((InteractionUI) UI.getCurrent()).showNotification("Server error", "Internal server error", Notification.Type.ERROR_MESSAGE);
                        listener.exception(new Exception("Internal server error"));
                    }
                }));
    }

    @Override
    public void previous(ActionWizardStepHandler listener, Object... args) {
        //
    }

    @Override
    public void validate() throws WizardStepValidationException {

        try {

            boolean selected = false;

            cbDepositedBy.validate();
            cbDateCreated.validate();
            cbAccount.validate();
            editTeller.validate();

            for (NoteCountingDepositModel model : containerDepositCountings.getItemIds()) {
                if (model.isSelected()) {
                    selected = true;
                    break;
                }
            }

            if (!selected) {
                throw new Exception("No selected grid items");
            }

        } catch (Exception ex) {
            throw new WizardStepValidationException();
        }
    }
}
