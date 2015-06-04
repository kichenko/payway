/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.commons.webapp.ui.components.table.paging;

/**
 * IPagingTable
 *
 * @author Sergey Kichenko
 * @created 27.05.15 00:00
 */
public interface IPagingTable extends IPagingContainer {

    public interface PageChangeListener {

        public void changed();
    }

    void addPageChangeListener(PageChangeListener listener);

    void removePageChangeListener(PageChangeListener listener);

}
