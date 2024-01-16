package org.example.postman;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Body {
    public String mode;
    public String raw;
    public Options options;
}
