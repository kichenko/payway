/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.messaging.message.reporting;

import com.payway.messaging.message.SessionCommandRequest;
import com.payway.messaging.model.common.data.OrderDto;
import java.util.List;
import lombok.Getter;
import lombok.ToString;

/**
 * GetReportListRequest
 *
 * @author Sergey Kichenko
 * @created 30.07.15 00:00
 */
@ToString(callSuper = true)
public final class GetReportListQueryRequest extends SessionCommandRequest {

    private static final long serialVersionUID = -2990884617758127707L;

    private final int firstResult;

    private final int maxResults;

    private final List<OrderDto> orders;

    private final String search;

    public GetReportListQueryRequest(String sessionId, List<OrderDto> orders, String search, int firstResult, int maxResults) {
        super(sessionId);
        this.orders = orders;
        this.search = search;
        this.firstResult = firstResult;
        this.maxResults = maxResults;
    }

    public int getFirstResult() {
        return firstResult;
    }

    public int getMaxResults() {
        return maxResults;
    }

    public List<OrderDto> getOrders() {
        return orders;
    }

    public String getSearch() {
        return search;
    }

}
