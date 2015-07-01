/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.commons.webapp.utils;

import com.vaadin.server.VaadinRequest;
import com.vaadin.server.WebBrowser;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * WebAppUtils
 *
 * @author Sergey Kichenko
 * @created 01.07.15 00:00
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class WebAppUtils {

    /**
     * X-Forwarded-For: client, proxy1, proxy2, ...
     */
    private static final String HTTP_HEADER_X_FORWARDED_FOR = "X-Forwarded-For";

    public static String getRemoteIPAddress(final VaadinRequest request, final WebBrowser webBrowser) {

        if (log.isDebugEnabled()) {
            log.debug("Receive remote ip address");
        }

        final String value;

        if (request == null || webBrowser == null) {

            if (log.isDebugEnabled()) {
                log.debug("Empty params, could not get remote ip address");
            }

            return null;
        }

        value = request.getHeader(HTTP_HEADER_X_FORWARDED_FOR);

        if (log.isDebugEnabled()) {
            log.debug("'X-Forwarded-For' http header value - [{}]", value);
            log.debug("Web browser remote ip address - [{}]", webBrowser.getAddress());
            log.debug("Servlet request remote ip address - [{}]", request.getRemoteAddr());
        }

        if (StringUtils.isBlank(value)) {
            return StringUtils.isBlank(request.getRemoteAddr()) ? webBrowser.getAddress() : request.getRemoteAddr();
        }

        if (StringUtils.contains(value, ",")) {
            return StringUtils.trim(StringUtils.substringBefore(value, ","));
        }

        return StringUtils.trim(value);
    }
}
