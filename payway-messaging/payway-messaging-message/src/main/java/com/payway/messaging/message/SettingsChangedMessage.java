package com.payway.messaging.message;

import com.payway.messaging.core.Message;
import lombok.*;

/**
 * Created by mike on 20/05/15.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class SettingsChangedMessage implements Message {

    private String appName;

}
