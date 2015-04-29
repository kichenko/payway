/*
 * (c) Sergey Kichenko, 2015. All right reserved.
 */
package com.payway.advertising.ui.navigation;

import com.payway.advertising.ui.view.core.AdminView;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.navigator.ViewProvider;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.UI;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 * AdminNavigator
 *
 * @author Sergey Kichenko
 * @created 22.04.15 00:00
 */
@Getter
@Setter
public final class AdminNavigator extends Navigator {

    private String errorViewName;
    private ViewProvider errorViewProvider;

    public AdminNavigator(final UI ui, final ComponentContainer container, final List<AdminView> views, String errorViewName) {
        super(ui, container);

        setErrorViewName(errorViewName);
        registerViewChangeListener();
        registerViewProviders(views);
    }

    /**
     * Register view change listener
     */
    private void registerViewChangeListener() {
        addViewChangeListener(new ViewChangeListener() {

            @Override
            public boolean beforeViewChange(final ViewChangeListener.ViewChangeEvent event) {
                //since there's no conditions in switching between the views we can always return true.
                return true;
            }

            @Override
            public void afterViewChange(final ViewChangeListener.ViewChangeEvent event) {
                //may be need in future
            }
        });
    }

    /**
     * Register list (with error view) of view providers
     *
     * @param views list of views
     * @param viewError error view
     */
    private void registerViewProviders(final List<AdminView> views) {

        for (final AdminView view : views) {
            ViewProvider viewProvider = new ClassBasedViewProvider(view.name(), view.getClass()) {

                //this field caches an already initialized view instance if the view should be cached (stateful views).
                private View cachedView;

                @Override
                public View getView(final String viewName) {
                    View result = null;
                    if (viewName.equals(view.name())) {
                        if (view.isStateful()) {
                            //stateful views lazily instantiated
                            if (cachedView == null) {
                                cachedView = super.getView(view.name());
                            }
                            result = cachedView;
                        } else {
                            //non-stateful views instantiated every time they're navigated to
                            result = super.getView(view.name());
                        }
                    }
                    return result;
                }
            };

            //initialize error view provider
            if (getErrorViewName().equals(view.name())) {
                setErrorViewProvider(viewProvider);
            }

            addProvider(viewProvider);
        }

        //set error provider for view
        setErrorProvider(new ViewProvider() {
            @Override
            public String getViewName(final String viewAndParameters) {
                return getErrorViewName();
            }

            @Override
            public View getView(final String viewName) {
                return errorViewProvider.getView(getErrorViewName());
            }
        });
    }
}
