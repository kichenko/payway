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
import com.vaadin.data.Property;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.converter.StringToDateConverter;
import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Label;
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
import org.vaadin.teemu.clara.binder.annotation.UiHandler;

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

    private final static class TransformerNoteCountingDepositModel implements Function<NoteCountingDepositDto, NoteCountingDepositModel> {

        private final boolean selected;

        public TransformerNoteCountingDepositModel(boolean selected) {
            this.selected = selected;
        }

        @Override
        public NoteCountingDepositModel apply(NoteCountingDepositDto src) {

            if (src != null) {
                return new NoteCountingDepositModel(src.getId(), src.getCreated(), src.getTerminalName(), src.getSeqNum(), selected);
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
    private TextField editAccount;

    @UiField
    private TextField editTeller;

    @UiField
    private Table gridDepositCountings;

    @UiField
    private Label lblSelectSummary;

    private boolean selectedAll;
    private int selectedCount;

    private final NoteCountingDepositModelBeanItemContainer containerDepositCountings = new NoteCountingDepositModelBeanItemContainer();

    public CreateBankCashDepositWizardStep() {
        selectedAll = true;
        selectedCount = 0;
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

        editAccount.setRequired(true);

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

                checkBox.addValueChangeListener(new Property.ValueChangeListener() {
                    private static final long serialVersionUID = -382717228031608542L;

                    @Override
                    public void valueChange(Property.ValueChangeEvent event) {

                        boolean flag = (Boolean) event.getProperty().getValue();
                        if (flag) {
                            selectedCount += 1;
                        } else {
                            selectedCount -= 1;
                            if (selectedCount < 0) {
                                selectedCount = 0;
                            }
                        }
                        refreshSelectSummary();
                    }
                });

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

    private void refreshSelectSummary() {
        lblSelectSummary.setValue(String.format("<b>%d/%d</b>", selectedCount, containerDepositCountings.getItemIds().size()));
    }

    private void changeMenuSelectedState(boolean selected) {

        for (NoteCountingDepositModel bean : containerDepositCountings.getItemIds()) {
            bean.setSelected(selected);
        }

        gridDepositCountings.refreshRowCache();
        refreshSelectSummary();
    }

    @UiHandler(value = "btnSelectAll")
    public void onClickSelectAll(Button.ClickEvent event) {
        selectedAll = true;
        selectedCount = containerDepositCountings.getItemIds().size();
        changeMenuSelectedState(true);
    }

    @UiHandler(value = "btnClearAll")
    public void onClickClearAll(Button.ClickEvent event) {
        selectedAll = false;
        selectedCount = 0;
        changeMenuSelectedState(false);
    }

    @Override
    public void setBeanItem(BeanItem<BankCashDepositModel> value) {

        super.setBeanItem(value);

        editTeller.setPropertyDataSource(getBeanItem().getItemProperty("teller"));
        cbDepositedBy.setPropertyDataSource(getBeanItem().getItemProperty("depositedBy"));
        cbDateCreated.setPropertyDataSource(getBeanItem().getItemProperty("created"));
    }

    @Override
    public void refreshStep(AbstractWizardStepParams state) {

        CreateBankCashDepositWizardStepParams st = (CreateBankCashDepositWizardStepParams) state;
        if (st != null) {
            editAccount.setValue(st.getAcccount().getAccount());
        }
    }

    @Override
    public void setupStep(AbstractWizardStepParams state) {

        CreateBankCashDepositWizardStepParams st = (CreateBankCashDepositWizardStepParams) state;

        editTeller.setValue("");
        cbDepositedBy.setValue(null);
        cbDateCreated.setValue(new Date());
        gridDepositCountings.removeAllItems();
        editAccount.setValue(st.getAcccount().getAccount());

        ((InteractionUI) UI.getCurrent()).showProgressBar();
        getService().sendMessage(new CreateBankCashDepositParamsRequest(),
                new UIResponseCallBackSupport(UI.getCurrent(), new UIResponseCallBackSupport.ResponseCallBackHandler() {

                    @Override
                    public void doServerResponse(SuccessResponse response) {

                        if (response instanceof CreateBankCashDepositParamsResponse) {

                            CreateBankCashDepositParamsResponse rsp = (CreateBankCashDepositParamsResponse) response;
                            BeanNameModelBeanItemContainer depositedByContainer = (BeanNameModelBeanItemContainer) cbDepositedBy.getContainerDataSource();

                            containerDepositCountings.removeAllItems();
                            containerDepositCountings.addAll(Lists.transform(rsp.getKioskEncashments(), new TransformerNoteCountingDepositModel(selectedAll)));

                            for (int i = 0; i < 50; i++) {
                                containerDepositCountings.addBean(new NoteCountingDepositModel());
                            }

                            if (selectedAll) {
                                selectedCount = containerDepositCountings.size();
                            } else {
                                selectedCount = 0;
                            }

                            refreshSelectSummary();

                            depositedByContainer.removeAllItems();
                            depositedByContainer.addAll(Lists.transform(rsp.getStaffs(), transformerStuffModel));
                            gridDepositCountings.sort();
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
