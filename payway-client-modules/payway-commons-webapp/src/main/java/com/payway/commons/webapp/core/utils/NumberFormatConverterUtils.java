/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.commons.webapp.core.utils;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * NumberFormatConverterUtils
 *
 * @author sergey kichenko
 * @created 19.06.15 00:00
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class NumberFormatConverterUtils {

    public static final String DEFAULT_PATTERN_WITH_DECIMALS = "###,###,###,###.##";
    public static final String DEFAULT_PATTERN_WITHOUT_DECIMALS = "###,###,###,###";

    private static final String DEFAULT_PATTERN = "###,###,###,###.##";
    private static final char DEFAULT_DECIMAL_FORMAT_SYMBOL = '.';
    private static final Locale DEFAULT_LOCALE = Locale.US;

    public static String format(final double number, final String pattern) {
        return format(number, pattern, DEFAULT_DECIMAL_FORMAT_SYMBOL, DEFAULT_LOCALE);
    }

    public static String format(final double number) {
        return format(number, DEFAULT_PATTERN, DEFAULT_DECIMAL_FORMAT_SYMBOL, DEFAULT_LOCALE);
    }

    public static String format(final double number, final String pattern, final char decimalFormatSymbol) {
        return format(number, pattern, decimalFormatSymbol, DEFAULT_LOCALE);
    }

    public static String format(final double number, final String pattern, final char decimalFormatSymbol, final Locale locale) {

        DecimalFormat decimalFormat = (DecimalFormat) NumberFormat.getNumberInstance(locale);
        DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols(locale);

        decimalFormatSymbols.setDecimalSeparator(decimalFormatSymbol);
        decimalFormat.setDecimalFormatSymbols(decimalFormatSymbols);
        decimalFormat.applyPattern(pattern);

        return decimalFormat.format(number);
    }

}
