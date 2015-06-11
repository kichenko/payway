package com.payway.messaging.message;

import com.payway.messaging.core.response.SuccessResponse;
import com.payway.messaging.model.message.settings.SettingsDto;
import lombok.ToString;

/**
 * Created by mike on 20/05/15.
 */
@ToString(callSuper = true)
public class SettingsResponse implements SuccessResponse {

    private static final long serialVersionUID = -8060686512584612365L;

    private SettingsDto settings;

    public SettingsResponse(SettingsDto settings) {
        this.settings = settings;
    }

    public SettingsDto getSettings() {
        return settings;
    }
}
