/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.webapp.reporting.ui.dialog;

import com.payway.messaging.model.reporting.ReportExportFormatTypeDto;
import com.payway.messaging.model.reporting.ReportParameterDto;
import com.payway.messaging.model.reporting.ReportUIDto;
import com.payway.webapp.reporting.collector.ParametersCollector;
import com.payway.webapp.reporting.transformer.ReportUITransformer;
import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import java.util.List;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.vaadin.teemu.clara.Clara;
import org.vaadin.teemu.clara.binder.annotation.UiField;
import org.vaadin.teemu.clara.binder.annotation.UiHandler;

/**
 * StandartReportParameterDialog
 *
 * @author Sergey Kichenko
 * @created 06.08.2015
 */
@Slf4j
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@org.springframework.stereotype.Component(value = StandartReportParameterDialog.BEAN_NAME)
public class StandartReportParameterDialog extends Window {

    public static final String BEAN_NAME = "app.reporting.ui.dialog.StandartReportParameterDialog";

    private static final long serialVersionUID = 7961562345080310340L;

    public interface ExecuteCallback {

        void execute(long reportId, boolean ignorePagination, ReportExportFormatTypeDto format, List<ReportParameterDto> params);

        void error(Exception ex);
    }

    @UiField
    private OptionGroup ogExportFormat;

    @UiField
    private CheckBox chIgnorePagination;

    @UiField
    private VerticalLayout layoutParametersPanelContent;

    @UiField
    private VerticalLayout layoutViewAsPanelContent;

    @UiField
    private VerticalLayout layoutOptionsPanelContent;

    @UiField
    private HorizontalLayout layoutParametersPanel;

    @UiField
    private Button btnParameters;

    @UiField
    private Button btnViewAs;

    @UiField
    private Button btnOptions;

    @Autowired
    private ReportUITransformer transformer;

    @Autowired
    private ParametersCollector collector;

    private ExecuteCallback callback;

    private final ReportUIDto metadata;

    public StandartReportParameterDialog(ReportUIDto metadata) throws Exception {
        this.metadata = metadata;
    }

    @PostConstruct
    protected void postConstruct() throws Exception {

        setModal(true);
        setSizeUndefined();
        setResizable(false);

        setCaption(metadata.getReportDescription());
        setContent(Clara.create("StandartReportParameterDialog.xml", this));

        if (metadata.getReportForm() != null) {
            layoutParametersPanelContent.addComponent(transformer.transform(metadata.getReportForm()));
        } else {
            log.debug("Detected no args report, creating standard report dialog with empty parameters layout");
            layoutParametersPanel.setVisible(false);
        }

        btnParameters.setIcon(FontAwesome.MINUS);
        btnViewAs.setIcon(FontAwesome.MINUS);
        btnOptions.setIcon(FontAwesome.MINUS);

        IndexedContainer container = new IndexedContainer();
        container.addContainerProperty("id", ReportExportFormatTypeDto.class, ReportExportFormatTypeDto.PDF);
        container.addContainerProperty("caption", String.class, "");

        for (ReportExportFormatTypeDto fmt : metadata.getFormats()) {
            Item item = container.getItem(container.addItem());
            item.getItemProperty("id").setValue(fmt);
            item.getItemProperty("caption").setValue(fmt.name());
        }

        ogExportFormat.setItemCaptionPropertyId("caption");
        ogExportFormat.setItemCaptionMode(AbstractSelect.ItemCaptionMode.PROPERTY);
        ogExportFormat.setContainerDataSource(container);
        ogExportFormat.select(container.getIdByIndex(0));
    }

    @UiHandler("btnParameters")
    public void clickSave(Button.ClickEvent event) {
        layoutParametersPanelContent.setVisible(!layoutParametersPanelContent.isVisible());
        btnParameters.setIcon(layoutParametersPanelContent.isVisible() ? FontAwesome.MINUS : FontAwesome.PLUS);
    }

    @UiHandler("btnViewAs")
    public void clickViewAs(Button.ClickEvent event) {
        layoutViewAsPanelContent.setVisible(!layoutViewAsPanelContent.isVisible());
        btnViewAs.setIcon(layoutViewAsPanelContent.isVisible() ? FontAwesome.MINUS : FontAwesome.PLUS);
    }

    @UiHandler("btnOptions")
    public void clickOptions(Button.ClickEvent event) {
        layoutOptionsPanelContent.setVisible(!layoutOptionsPanelContent.isVisible());
        btnOptions.setIcon(layoutOptionsPanelContent.isVisible() ? FontAwesome.MINUS : FontAwesome.PLUS);
    }

    @UiHandler(value = "btnCancel")
    public void onClickCancel(Button.ClickEvent event) {
        close();
    }

    @UiHandler(value = "btnExecute")
    public void onClickExecute(Button.ClickEvent event) {

        try {
            if (callback != null) {
                callback.execute(metadata.getReportId(), chIgnorePagination.getValue(), (ReportExportFormatTypeDto) ogExportFormat.getItem(ogExportFormat.getValue()).getItemProperty("id").getValue(), collector.collect(layoutParametersPanelContent));
            }
        } catch (Exception ex) {
            log.error("Cannot execute report - [{}]", ex);
            if (callback != null) {
                try {
                    callback.error(ex);
                } catch (Exception e) {
                    log.error("Callback execute report - [{}]", e);
                }
            }
        }

        close();
    }

    public void show(ExecuteCallback callback) {
        this.callback = callback;
        if (!this.isAttached()) {
            UI.getCurrent().addWindow(this);
        }
    }
}
