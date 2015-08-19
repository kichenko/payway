/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.commons.webapp.ui.components.table.paging;

import com.vaadin.data.Property;
import com.vaadin.data.validator.IntegerRangeValidator;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import org.vaadin.teemu.clara.Clara;
import org.vaadin.teemu.clara.binder.annotation.UiField;

/**
 * PagingTableControls
 *
 * @author Sergey Kichenko
 * @created 27.05.15 00:00
 */
public class PagingTableControls extends VerticalLayout {

    @UiField
    private Button btnRefresh;

    @UiField
    private ComboBox cbPageSize;

    @UiField
    private Button btnFirst;

    @UiField
    private Button btnPrevious;

    @UiField
    private TextField editPage;

    @UiField
    private Label lbPageTotal;

    @UiField
    private Button btnNext;
    @UiField
    private Button btnLast;

    private IPagingTable table;

    private IntegerRangeValidator pageNumberValidator;

    private boolean skipEditPageEvent;
    private boolean skipCbPageSize;
    private boolean skipInitControls;

    public PagingTableControls() {
        init();
    }

    public void setPagingTable(IPagingTable table) {
        this.table = table;
        initControls();
    }

    private void init() {
        setSizeFull();
        addComponent(Clara.create("PagingTableControls.xml", this));
    }

    private void initControls() {
        
        skipInitControls = true;
        
        if (table == null) {
            return;
        }

        pageNumberValidator = new IntegerRangeValidator("Invalid page number", 1, table.getTotalPages() <= 0 ? 1 : table.getTotalPages());

        cbPageSize.setNullSelectionAllowed(false);
        cbPageSize.setImmediate(true);

        cbPageSize.addItem("5");
        cbPageSize.addItem("10");
        cbPageSize.addItem("25");
        cbPageSize.addItem("50");

        cbPageSize.addValueChangeListener(new Property.ValueChangeListener() {
            private static final long serialVersionUID = -2255853716069800092L;

            @Override
            public void valueChange(com.vaadin.data.Property.ValueChangeEvent event) {
                if (!skipCbPageSize && !skipInitControls) {
                    table.setPageSize(Integer.valueOf(String.valueOf(event.getProperty().getValue())));
                }
                skipCbPageSize = false;
            }
        });

        cbPageSize.select("25");

        editPage.setConverter(Integer.class);
        editPage.setValue(String.valueOf(table.getCurrentPage() + 1));
        editPage.addValidator(pageNumberValidator);

        editPage.setImmediate(true);
        editPage.addValueChangeListener(new Property.ValueChangeListener() {
            private static final long serialVersionUID = -2255853716069800092L;

            @Override
            public void valueChange(com.vaadin.data.Property.ValueChangeEvent event) {
                if (!skipEditPageEvent && !skipInitControls && editPage.isValid() && editPage.getValue() != null) {
                    table.setCurrentPage(Integer.valueOf(String.valueOf(editPage.getValue())) - 1);
                }
                skipEditPageEvent = false;
            }
        });

        lbPageTotal.setContentMode(ContentMode.HTML);
        lbPageTotal.setValue(String.valueOf(table.getTotalPages()));

        btnRefresh.setIcon(FontAwesome.REFRESH);
        btnRefresh.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                table.refresh();
            }
        });

        btnFirst.setIcon(FontAwesome.FAST_BACKWARD);
        btnFirst.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                table.setCurrentPage(0);
            }
        });

        btnPrevious.setIcon(FontAwesome.BACKWARD);
        btnPrevious.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                table.previousPage();
            }
        });

        btnLast.setIcon(FontAwesome.FAST_FORWARD);
        btnLast.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                table.setCurrentPage(table.getTotalPages() - 1);
            }
        });

        btnNext.setIcon(FontAwesome.FORWARD);
        btnNext.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                table.nextPage();
            }
        });

        table.addPageChangeListener(new IPagingTable.PageChangeListener() {
            @Override
            public void changed() {

                btnRefresh.setEnabled(true);

                btnFirst.setEnabled(table.getCurrentPage() > 0);
                btnPrevious.setEnabled(table.getCurrentPage() > 0);

                btnNext.setEnabled(table.getTotalPages() > 0 && table.getCurrentPage() < (table.getTotalPages() - 1));
                btnLast.setEnabled(table.getTotalPages() > 0 && table.getCurrentPage() != (table.getTotalPages() - 1));

                if (table.getCurrentPage() + 1 != Integer.parseInt((String) editPage.getValue())) {
                    skipEditPageEvent = true;
                    editPage.setValue(String.valueOf(table.getCurrentPage() + 1));
                }

                lbPageTotal.setValue(String.valueOf(table.getTotalPages()));

                if (table.getPageSize() != Integer.parseInt((String) cbPageSize.getValue())) {
                    skipCbPageSize = true;
                    cbPageSize.setValue(String.valueOf(table.getPageSize()));
                }

                pageNumberValidator.setMaxValue(table.getTotalPages());
            }
        });
        
        skipInitControls = false;
    }
}
