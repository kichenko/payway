/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.webapp.reporting.web.handler;

import com.vaadin.server.RequestHandler;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinResponse;
import com.vaadin.server.VaadinSession;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import javax.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * ReportingContentRequestHandler
 *
 * @author Sergey Kichenko
 * @created 11.08.2015
 */
@Slf4j
@Getter
@Setter
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Component(value = "app.reporting.web.handler.ReportingContentRequestHandler")
public class ReportingContentRequestHandler implements RequestHandler {

    private static final long serialVersionUID = 7455925096017816938L;

    @Value("${app.config.reporting.root.path:file:///${user.home}/var/apps/reporting}")
    private String rootPath;

    @Value("${app.config.reporting.rest.path:/reporting}")
    private String restPath;

    @PostConstruct
    public void postConstruct() {

        final String pattern = "/";

        if (!StringUtils.isBlank(getRootPath())) {
            setRootPath(getRootPath().replace("\\", pattern));
        }

        if (!StringUtils.isBlank(getRestPath())) {
            setRestPath(StringUtils.prependIfMissingIgnoreCase(StringUtils.appendIfMissingIgnoreCase(restPath, pattern), pattern));
        }
    }

    @Override
    public boolean handleRequest(VaadinSession session, VaadinRequest request, VaadinResponse response) throws IOException {

        if (request.getPathInfo().contains(getRestPath())) {
            log.debug("Receive report content web handler request with path = [{}]", request.getPathInfo());
            String result = new StringBuilder(getRootPath()).append("/").append(session.getSession().getId()).append("/").append(request.getPathInfo().replace(getRestPath(), "")).toString();

            try (final FileInputStream fis = new FileInputStream(new File(new URI(result)))) {
                IOUtils.copy(fis, response.getOutputStream());
            } catch (Exception ex) {
                log.error("Cannot copy input stream to response", ex);
                throw new IOException(ex.getMessage(), ex);
            }
            return true;
        }

        return false;
    }
}
