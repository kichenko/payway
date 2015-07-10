package com.payway.messaging.model.common;

import com.payway.messaging.model.AbstractDto;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * Created by mike on 09/06/15.
 */
@Getter
@ToString(callSuper = true, exclude = "content")
@EqualsAndHashCode(callSuper = true)
public class ContentDto extends AbstractDto {

    private static final long serialVersionUID = 3350347387882308342L;

    final private String name;

    final private byte[] content;

    final private String contentType;

    public ContentDto(String name, byte[] content, String contentType) {
        this.name = name;
        this.content = content;
        this.contentType = contentType;
    }

}
