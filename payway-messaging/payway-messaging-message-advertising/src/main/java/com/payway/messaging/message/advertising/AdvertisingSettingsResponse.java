package com.payway.messaging.message.advertising;

import com.payway.messaging.core.response.SuccessResponse;
import com.payway.messaging.model.advertising.SettingsDto;
import lombok.Getter;
import lombok.ToString;

/**
 * Created by mike on 20/05/15.
 */
@Getter
@ToString(callSuper = true)
public final class AdvertisingSettingsResponse implements SuccessResponse {

    private static final long serialVersionUID = -8060686512584612365L;

    private final SettingsDto settings;

    public AdvertisingSettingsResponse(SettingsDto settings) {
        this.settings = settings;
    }

}
