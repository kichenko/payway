package com.payway.messaging.model.common;

import com.payway.messaging.model.AbstractDto;
import lombok.*;

/**
 * Created by mike on 09/06/15.
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true, exclude = "content")
@EqualsAndHashCode(callSuper = true)
public class ContentDto extends AbstractDto {

    private static final long serialVersionUID = 3350347387882308342L;

    private String name;

    private byte[] content;

    private String contentType;

}
