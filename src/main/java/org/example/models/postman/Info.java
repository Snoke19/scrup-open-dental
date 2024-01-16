package org.example.models.postman;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Info{

    public String _postman_id;
    public String name;
    public String schema;
    public String _exporter_id;
}
