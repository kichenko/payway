/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.kioskcashier.ui.core;

import com.vaadin.data.util.BeanContainer;
import com.vaadin.event.Action;
import com.vaadin.event.ShortcutAction;
import com.vaadin.ui.TextField;
import java.util.Map;
import lombok.NoArgsConstructor;

/**
 * KeyboardNavigatorHandler
 *
 * @author Sergey Kichenko
 * @created 23.07.15 00:00
 */
@NoArgsConstructor
public class KeyboardNavigatorHandler implements Action.Handler {

    private static final long serialVersionUID = -2496992285303828227L;

    //private final Action key_tab = new ShortcutAction("Tab", ShortcutAction.KeyCode.TAB, null);
    //private final Action key_tab_shift = new ShortcutAction("Shift+Tab", ShortcutAction.KeyCode.TAB, new int[]{ShortcutAction.ModifierKey.SHIFT});
    private final Action key_down = new ShortcutAction("Down", ShortcutAction.KeyCode.ARROW_DOWN, null);
    private final Action key_up = new ShortcutAction("Up", ShortcutAction.KeyCode.ARROW_UP, null);
    private final Action key_enter = new ShortcutAction("Enter", ShortcutAction.KeyCode.ENTER, null);
    private final Action key_shift_enter = new ShortcutAction("Shift+Enter", ShortcutAction.KeyCode.ENTER, new int[]{ShortcutAction.ModifierKey.SHIFT});

    private BeanContainer container;
    private Map<Long, TextField> mapEditors;

    public KeyboardNavigatorHandler(BeanContainer container, Map<Long, TextField> mapEditors) {
        this.container = container;
        this.mapEditors = mapEditors;
    }

    @Override
    public Action[] getActions(Object target, Object sender) {
        return new Action[]{/*key_tab, key_tab_shift,*/key_down, key_up, key_enter, key_shift_enter};
    }

    @Override
    public void handleAction(Action action, Object sender, Object target) {

        if (target instanceof TextField) {

            long itemId = (Long) ((TextField) target).getData();
            if (action == key_enter || /*action == key_tab ||*/ action == key_down) {

                int idx = container.getItemIds().indexOf(itemId);
                if (idx >= 0 && (idx + 1 < container.getItemIds().size())) {
                    itemId = (Long) container.getItemIds().get(idx + 1);
                    TextField txt = mapEditors.get(itemId);
                    if (txt != null) {
                        txt.selectAll();
                        txt.focus();
                    }
                }

            } else if (/*action == key_tab_shift ||*/action == key_up || action == key_shift_enter) {

                int idx = container.getItemIds().indexOf(itemId);
                if (idx >= 0 && (idx - 1) >= 0) {
                    itemId = (Long) container.getItemIds().get(idx - 1);
                    TextField txt = mapEditors.get(itemId);
                    if (txt != null) {
                        txt.selectAll();
                        txt.focus();
                    }
                }

            }
        }
    }
}
