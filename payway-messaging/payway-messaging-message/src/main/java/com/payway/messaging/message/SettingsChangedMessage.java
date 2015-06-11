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

    private static final long serialVersionUID = 8047599227972230869L;

    private String appName;

}
