/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.webapp.reporting.components;

import com.payway.webapp.reporting.components.utils.IntervalCalculatorFactory;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Component;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.VerticalLayout;
import java.util.Iterator;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.ObjectUtils;
import org.vaadin.teemu.clara.Clara;
import org.vaadin.teemu.clara.binder.annotation.UiField;

/**
 * Interval
 *
 * @author Sergey Kichenko
 * @created 05.08.2015
 */
public class Interval extends VerticalLayout {

    private static final long serialVersionUID = 7713232413487659639L;

    @UiField
    private MenuBar menuBar;

    @Setter
    @Getter
    private String from = "From";

    @Setter
    @Getter
    private String to = "To";

    private PopupDateField popupDateFieldFrom;
    private PopupDateField popupDateFieldTo;

    public Interval() {
        init();
    }

    public Interval(String id, String from, String to) {

        setId(id);
        setTo(to);
        setFrom(from);

        init();
    }

    private void init() {

        setSizeFull();
        addComponent(Clara.create(getClass().getResourceAsStream("/com/payway/webapp/reporting/components/Interval.xml"), this));

        addAttachListener(new AttachListener() {
            private static final long serialVersionUID = 928024836680874045L;

            @Override
            public void attach(AttachEvent event) {
                configureDateFields();
                configureMenuBar();
            }
        });
    }

    private void configureMenuBar() {

        MenuBar.MenuItem dropdown;
      
        dropdown = menuBar.addItem("", FontAwesome.CLOCK_O, null);

        dropdown.addItem("Today", new MenuBar.Command() {

            @Override
            public void menuSelected(MenuBar.MenuItem selectedItem) {
                configureDateTime(IntervalCalculatorFactory.IntervalCalculatorType.Today);
            }
        });

        dropdown.addItem("Today (Whole)", new MenuBar.Command() {

            @Override
            public void menuSelected(MenuBar.MenuItem selectedItem) {
                configureDateTime(IntervalCalculatorFactory.IntervalCalculatorType.TodayWhole);
            }
        });

        dropdown.addItem("Yesterday", new MenuBar.Command() {

            @Override
            public void menuSelected(MenuBar.MenuItem selectedItem) {
                configureDateTime(IntervalCalculatorFactory.IntervalCalculatorType.Yesterday);
            }
        });

        dropdown.addItem("This week", new MenuBar.Command() {

            @Override
            public void menuSelected(MenuBar.MenuItem selectedItem) {
                configureDateTime(IntervalCalculatorFactory.IntervalCalculatorType.ThisWeek);
            }
        });

        dropdown.addItem("Last week", new MenuBar.Command() {

            @Override
            public void menuSelected(MenuBar.MenuItem selectedItem) {
                configureDateTime(IntervalCalculatorFactory.IntervalCalculatorType.LastWeek);
            }
        });

        dropdown.addItem("This month", new MenuBar.Command() {

            @Override
            public void menuSelected(MenuBar.MenuItem selectedItem) {
                configureDateTime(IntervalCalculatorFactory.IntervalCalculatorType.ThisMonth);
            }
        });
        dropdown.addItem("Last month", new MenuBar.Command() {

            @Override
            public void menuSelected(MenuBar.MenuItem selectedItem) {
                configureDateTime(IntervalCalculatorFactory.IntervalCalculatorType.LastMonth);
            }
        });

        dropdown.addItem("This Quarter", new MenuBar.Command() {

            @Override
            public void menuSelected(MenuBar.MenuItem selectedItem) {
                configureDateTime(IntervalCalculatorFactory.IntervalCalculatorType.ThisQuarter);
            }
        });

        dropdown.addItem("Last Quarter", new MenuBar.Command() {

            @Override
            public void menuSelected(MenuBar.MenuItem selectedItem) {
                configureDateTime(IntervalCalculatorFactory.IntervalCalculatorType.LastQuarter);
            }
        });

        dropdown.addItem("This Year", new MenuBar.Command() {

            @Override
            public void menuSelected(MenuBar.MenuItem selectedItem) {
                configureDateTime(IntervalCalculatorFactory.IntervalCalculatorType.ThisYear);
            }
        });

        dropdown.addItem("Last Year", new MenuBar.Command() {

            @Override
            public void menuSelected(MenuBar.MenuItem selectedItem) {
                configureDateTime(IntervalCalculatorFactory.IntervalCalculatorType.LastYear);
            }
        });
    }

    private void configureDateFields() {

        int counter = 0;
        Iterator<Component> iterator = getParent().iterator();
        while (iterator.hasNext()) {
            Component cmp = iterator.next();
            if (cmp.getClass().equals(PopupDateField.class)) {
                if (ObjectUtils.equals(from, cmp.getId())) {
                    popupDateFieldFrom = (PopupDateField) cmp;
                    counter += 1;
                } else if (ObjectUtils.equals(to, cmp.getId())) {
                    popupDateFieldTo = (PopupDateField) cmp;
                    counter += 1;
                }
            }

            if (counter == 2) {
                break;
            }
        }
    }

    private void configureDateTime(IntervalCalculatorFactory.IntervalCalculatorType kind) {

        IntervalCalculatorFactory.Interval interval = IntervalCalculatorFactory.getIntervalCalculator(kind).getInterval();
        if (interval != null) {
            popupDateFieldFrom.setValue(interval.getBeginDate());
            popupDateFieldTo.setValue(interval.getEndDate());
        }
    }
}
