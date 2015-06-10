package com.payway.messaging.model.common;

import com.payway.messaging.model.AbstractDto;
import lombok.*;

import java.io.Serializable;

/**
 * Created by mike on 09/06/15.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true, exclude = "content")
@EqualsAndHashCode(callSuper = true)
public class ContentDto extends AbstractDto {

    String name;

    byte[] content;

    String contentType;

}
