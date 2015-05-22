package com.payway.messaging.message;

import com.payway.messaging.core.response.SuccessResponse;
import com.payway.messaging.model.message.settings.SettingsDto;

/**
 * Created by mike on 20/05/15.
 */
public class SettingsResponse implements SuccessResponse {

    private SettingsDto settings;

    public SettingsResponse(SettingsDto settings) {
        this.settings = settings;
    }

    public SettingsDto getSettings() {
        return settings;
    }
}
