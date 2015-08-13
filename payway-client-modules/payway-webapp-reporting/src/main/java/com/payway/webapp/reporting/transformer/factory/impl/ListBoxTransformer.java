/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.webapp.reporting.transformer.factory.impl;

import com.google.common.collect.Collections2;
import com.google.gwt.thirdparty.guava.common.collect.Lists;
import com.payway.messaging.model.reporting.ui.ComponentStateDto;
import com.payway.messaging.model.reporting.ui.SimpleComponentModelStateDto;
import com.payway.webapp.reporting.components.ListBox;
import com.vaadin.ui.Component;
import java.util.Collection;
import java.util.List;

/**
 * ListBoxTransformer
 *
 * @author Sergey Kichenko
 * @created 04.08.15 00:00
 */
@org.springframework.stereotype.Component(value = "app.reporting.transformer.ListBoxTransformer")
public class ListBoxTransformer extends AbstractComponentTransfrormer {

    @Override
    public Component transform(ComponentStateDto cmp) throws Exception {

        if (!(cmp instanceof com.payway.messaging.model.reporting.ui.ListBoxStateDto)) {
            throw new IllegalArgumentException("Argument is not instance of ListBox");
        }

        com.payway.messaging.model.reporting.ui.ListBoxStateDto lst = (com.payway.messaging.model.reporting.ui.ListBoxStateDto) cmp;
        List<String> values = split((String) lst.getValue(), ",");

        ListBox listBox = new ListBox(cmp.getName(), cmp.getCaption());
        listBox.addAll(Lists.newArrayList(Collections2.transform((Collection<SimpleComponentModelStateDto>) lst.getDatasource(), new TransformerSimpleComponentModel())));
        listBox.select(values);

        return listBox;
    }
}
