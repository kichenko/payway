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
import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
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

        void execute(long reportId, ReportExportFormatTypeDto format, List<ReportParameterDto> params);

        void error(Exception ex);
    }

    @UiField
    private VerticalLayout layoutReportParameterContent;

    @UiField
    private ComboBox cbExportFormat;

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
        layoutReportParameterContent.addComponent(transformer.transform(metadata.getReportForm()));

        IndexedContainer container = new IndexedContainer();
        container.addContainerProperty("id", ReportExportFormatTypeDto.class, ReportExportFormatTypeDto.PDF);
        container.addContainerProperty("caption", String.class, "");

        for (ReportExportFormatTypeDto fmt : metadata.getFormats()) {
            Item item = container.getItem(container.addItem());
            item.getItemProperty("id").setValue(fmt);
            item.getItemProperty("caption").setValue(fmt.name());
        }

        cbExportFormat.setItemCaptionPropertyId("caption");
        cbExportFormat.setItemCaptionMode(AbstractSelect.ItemCaptionMode.PROPERTY);
        cbExportFormat.setContainerDataSource(container);
        cbExportFormat.select(container.getIdByIndex(0));
    }

    @UiHandler(value = "btnCancel")
    public void onClickCancel(Button.ClickEvent event) {
        close();
    }

    @UiHandler(value = "btnExecute")
    public void onClickExecute(Button.ClickEvent event) {

        try {
            if (callback != null) {
                callback.execute(metadata.getReportId(), (ReportExportFormatTypeDto) cbExportFormat.getItem(cbExportFormat.getValue()).getItemProperty("id").getValue(), collector.collect(layoutReportParameterContent));
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
