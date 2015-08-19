/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.webapp.reporting.transformer.impl;

import com.payway.messaging.model.reporting.ui.ComponentStateContainerDto;
import com.payway.webapp.reporting.exception.ReportException;
import com.payway.webapp.reporting.transformer.ReportUITransformer;
import com.payway.webapp.reporting.transformer.factory.ComponentTransformer;
import com.payway.webapp.reporting.utils.ReportComponentTransformerUtils;
import com.vaadin.ui.VerticalLayout;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

/**
 * DefaultReportUITransformer
 *
 * @author Sergey Kichenko
 * @created 04.08.15 00:00
 */
@Slf4j
@Component(value = "app.reporting.DefaultReportUITransformer")
public class DefaultReportUITransformer implements ReportUITransformer {
    
    @Autowired
    private ReportComponentTransformerUtils reportComponentTransformerUtils;
    
    @Override
    public com.vaadin.ui.Component transform(com.payway.messaging.model.reporting.ui.ComponentStateDto content) throws ReportException {
        
        VerticalLayout layout = new VerticalLayout();
        
        if (!(content instanceof ComponentStateContainerDto)) {
            throw new ReportException("Argument is not instance of ContainerComponent");
        }
        
        try {
            walk(content, layout);
        } catch (Exception ex) {
            throw new ReportException(ex.getMessage(), ex);
        }
        
        return (com.vaadin.ui.Component) layout;
    }
    
    private void walk(com.payway.messaging.model.reporting.ui.ComponentStateDto cmp, com.vaadin.ui.ComponentContainer parent) throws Exception {
        
        if (cmp instanceof ComponentStateContainerDto) {
            com.vaadin.ui.ComponentContainer child = (com.vaadin.ui.ComponentContainer) convert(cmp);
            if (!checkIfNull(parent, child)) {
                parent.addComponent(child);
                for (com.payway.messaging.model.reporting.ui.ComponentStateDto c : ((ComponentStateContainerDto) cmp).getChilds()) {
                    walk(c, child);
                }
            }
        } else {
            com.vaadin.ui.Component child = convert(cmp);
            if (!checkIfNull(parent, child)) {
                
                parent.addComponent(child);
/*
                //TODO: hack for Interval
                if (parent instanceof AbstractOrderedLayout) {
                    if (child instanceof Interval) {
                        ((AbstractOrderedLayout) parent).setExpandRatio(child, 0.0F);
                    } else {
                        ((AbstractOrderedLayout) parent).setExpandRatio(child, 1.0F);
                    }
                }

                //TODO: hack for Interval
                if (child instanceof Interval && parent instanceof HorizontalLayout) {
                    ((HorizontalLayout) parent).setComponentAlignment(child, Alignment.BOTTOM_RIGHT);
                }
*/
            } else {
                log.error("Converted component is null");
            }
        }
    }
    
    private com.vaadin.ui.Component convert(com.payway.messaging.model.reporting.ui.ComponentStateDto cmp) throws Exception {
        
        ComponentTransformer transformer = reportComponentTransformerUtils.getComponentTransformer(cmp.getClass().getSimpleName());
        if (transformer != null) {
            return transformer.transform(cmp);
        }
        
        return null;
    }
    
    private boolean checkIfNull(com.vaadin.ui.Component... components) {
        
        if (ObjectUtils.containsElement(components, null)) {
            log.error("On walking founded null component");
            return true;
        }
        
        return false;
    }
}
