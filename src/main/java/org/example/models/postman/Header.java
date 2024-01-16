package org.example.models.postman;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Header {

    public String key;
    public String value;
    public String type;
}
