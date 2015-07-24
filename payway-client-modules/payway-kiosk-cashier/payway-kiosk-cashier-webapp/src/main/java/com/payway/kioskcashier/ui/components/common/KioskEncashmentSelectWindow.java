/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.kioskcashier.ui.components.common;

import com.payway.kioskcashier.ui.components.wizard.terminal.encashment.datasource.KioskEncashmentDtoModelBeanContainer;
import com.payway.messaging.model.kioskcashier.KioskEncashmentDto;
import com.vaadin.data.util.converter.StringToDateConverter;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.ui.Button;
import com.vaadin.ui.Table;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.vaadin.teemu.clara.Clara;
import org.vaadin.teemu.clara.binder.annotation.UiField;
import org.vaadin.teemu.clara.binder.annotation.UiHandler;

/**
 * KioskEncashmentSelectWindow
 *
 * @author Sergey Kichenko
 * @created 20.07.15 00:00
 */
@Slf4j
public class KioskEncashmentSelectWindow extends Window {

    private static final long serialVersionUID = 7405482384413519855L;

    public interface SelectorListener {

        void select(KioskEncashmentDto item);
    }

    @UiField
    private Table gridKioskEncashments;

    @Setter(AccessLevel.PRIVATE)
    @Getter(AccessLevel.PRIVATE)
    private SelectorListener selector;

    public KioskEncashmentSelectWindow(String caption, List<KioskEncashmentDto> kioskEncashments, SelectorListener listener) {

        setModal(true);
        setResizable(false);
        setCaption(caption);
        setWidth(570, Unit.PIXELS);
        setHeight(305, Unit.PIXELS);

        setSelector(listener);
        init(kioskEncashments);
    }

    private void init(List<KioskEncashmentDto> kioskEncashments) {

        setContent(Clara.create("KioskEncashmentSelectWindow.xml", this));

        gridKioskEncashments.setSelectable(true);
        gridKioskEncashments.setContainerDataSource(new KioskEncashmentDtoModelBeanContainer());
        gridKioskEncashments.setColumnHeader("terminalName", "Terminal name");
        gridKioskEncashments.setColumnHeader("occuredDate", "Occured date");
        gridKioskEncashments.setColumnAlignment("terminalName", Table.Align.CENTER);
        gridKioskEncashments.setColumnAlignment("occuredDate", Table.Align.CENTER);
        gridKioskEncashments.setVisibleColumns("terminalName", "occuredDate");

        gridKioskEncashments.setConverter("occuredDate", new StringToDateConverter() {
            private static final long serialVersionUID = 3721451326801495897L;

            @Override
            protected DateFormat getFormat(Locale locale) {
                return new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
            }
        }
        );

        gridKioskEncashments.addItemClickListener(new ItemClickEvent.ItemClickListener() {
            private static final long serialVersionUID = -2318797984292753676L;

            @Override
            public void itemClick(ItemClickEvent event) {
                if (event.isDoubleClick()) {
                    select();
                }
            }
        });

        ((KioskEncashmentDtoModelBeanContainer) gridKioskEncashments.getContainerDataSource()).addAll(kioskEncashments);
    }

    private void select() {
        if (selector != null && gridKioskEncashments.getValue() != null) {
            selector.select(((KioskEncashmentDtoModelBeanContainer) gridKioskEncashments.getContainerDataSource()).getItem(gridKioskEncashments.getValue()).getBean());
            close();
        }
    }

    @UiHandler(value = "btnSelect")
    public void onClickCancel(Button.ClickEvent event) {
        select();
    }

    public void show() {
        if (!isAttached()) {
            UI.getCurrent().addWindow(this);
        }
    }
}
