package com.payway.advertising.messaging;

import com.payway.messaging.core.response.ExceptionResponse;
import com.payway.messaging.core.response.SuccessResponse;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * Created by mike on 20/05/15.
 */
@Slf4j
public class ResponseCallbackSupport<R extends SuccessResponse, E extends ExceptionResponse> implements ResponseCallBack<R, E> {

    @Override
    public void onServerResponse(R response) {

    }

    @Override
    public void onServerResponse(R response, Map<String, Object> data) {

    }

    @Override
    public void onServerException(E exception) {
        log.error("Remote exception: code = {}, message = {}, description = {}", exception.getCode(), exception.getMessage(), exception.getDescription());
    }

    @Override
    public void onLocalException(Exception ex) {
        log.error("Local exception", ex);
    }

    @Override
    public void onTimeout() {
        log.error("Operation timed out");
    }

}
