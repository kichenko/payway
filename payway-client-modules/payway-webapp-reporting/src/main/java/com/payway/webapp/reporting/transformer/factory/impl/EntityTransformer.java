/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.webapp.reporting.transformer.factory.impl;

import com.google.common.collect.Collections2;
import com.google.gwt.thirdparty.guava.common.collect.Lists;
import com.payway.messaging.model.reporting.ui.ComponentStateDto;
import com.payway.messaging.model.reporting.ui.EntityComponentStateDto;
import com.payway.messaging.model.reporting.ui.SimpleComponentModelStateDto;
import com.payway.webapp.reporting.components.EntityBox;
import com.vaadin.ui.Component;
import java.util.Collection;
import java.util.List;

/**
 * EntityTransformer
 *
 * @author Sergey Kichenko
 * @created 04.08.15 00:00
 */
@org.springframework.stereotype.Component(value = "app.reporting.transformer.EntityTransformer")
public class EntityTransformer extends AbstractComponentTransfrormer {

    @Override
    public Component transform(ComponentStateDto cmp) throws Exception {

        if (!(cmp instanceof EntityComponentStateDto)) {
            throw new IllegalArgumentException("Argument is not instance of EntityComponent");
        }

        EntityComponentStateDto entity = (EntityComponentStateDto) cmp;
        List<String> values = split((String) entity.getValue(), ",");

        EntityBox entityBox = new EntityBox(entity.getName(), entity.getCaption(), EntityComponentStateDto.ViewStyle.Listbox.equals(entity.getStyle()) ? EntityBox.ViewStyle.ListBox : EntityBox.ViewStyle.Combobox);
        entityBox.addAll(Lists.newArrayList(Collections2.transform((Collection<SimpleComponentModelStateDto>) entity.getDatasource(), new TransformerSimpleComponentModel())));
        entityBox.select(values);

        return entityBox;
    }
}
