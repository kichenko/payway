/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.webapp.reporting.transformer.factory.impl;

import com.google.common.base.Function;
import com.payway.messaging.model.reporting.ui.SimpleComponentModelStateDto;
import com.payway.webapp.reporting.components.model.SimpleSelectionModel;
import com.payway.webapp.reporting.transformer.factory.ComponentTransformer;
import com.payway.webapp.reporting.transformer.factory.ComponentTransformer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

/**
 * AbstractComponentTransfrormer
 *
 * @author Sergey Kichenko
 * @created 06.08.2015
 */
public abstract class AbstractComponentTransfrormer implements ComponentTransformer {

    protected static class TransformerSimpleComponentModel implements Function<SimpleComponentModelStateDto, SimpleSelectionModel> {

        @Override
        public SimpleSelectionModel apply(SimpleComponentModelStateDto src) {
            return new SimpleSelectionModel(src.getId(), src.getValue(), false);
        }
    };

    protected List<String> split(String value, String separator) {

        if (!StringUtils.isBlank(value)) {
            return Arrays.asList(StringUtils.split(value, separator));
        }

        return new ArrayList<>(0);
    }
}
