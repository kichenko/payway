/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.ui.component.table.paging;

/**
 * IPagingContainer
 *
 * @author Sergey Kichenko
 * @created 27.05.15 00:00
 */
public interface IPagingContainer {

    public interface IErrorPagingLoad {
        void error(Exception ex);
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
