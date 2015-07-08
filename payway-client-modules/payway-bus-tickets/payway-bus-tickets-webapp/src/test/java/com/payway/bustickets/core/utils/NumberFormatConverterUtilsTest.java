/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.bustickets.core.utils;

import com.payway.commons.webapp.core.utils.NumberFormatConverterUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * NumberFormatConverterUtilsTest
 *
 * @author Sergey Kichenko
 * @created 19.06.15 00:00
 */
public class NumberFormatConverterUtilsTest {

    @Test
    public void testDefaultMax() {
        double number = 999999999999.99;
        Assert.assertEquals(NumberFormatConverterUtils.format(number), "999,999,999,999.99");
    }

    @Test
    public void testDefaultMin() {
        double number = 0.99;
        Assert.assertEquals(NumberFormatConverterUtils.format(number), "0.99");
    }

    @Test
    public void testCustom() {
        String pattern = "###,###,###,###.##";
        char decimalFormatSymbol = ',';
        double number = 75678.45;

        Assert.assertEquals(NumberFormatConverterUtils.format(number, pattern, decimalFormatSymbol), "75,678,45");
    }
}
