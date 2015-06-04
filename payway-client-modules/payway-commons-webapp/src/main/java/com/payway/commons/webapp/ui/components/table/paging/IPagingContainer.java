/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.commons.webapp.ui.components.table.paging;

/**
 * IPagingContainer
 *
 * @author Sergey Kichenko
 * @created 27.05.15 00:00
 */
public interface IPagingContainer {

    public interface IPagingLoadCallback {

        void start();

        void finish();

        void exception(Exception ex);
    }

    boolean nextPage();

    boolean previousPage();

    int getCurrentPage();

    boolean setCurrentPage(int page);

    int getTotalPages();

    int getPageSize();

    boolean setPageSize(int pageSize);

    boolean refresh();

    boolean addCriteria(String property, Object value);

    boolean removeCriteria(String property);
}
