package org.example.models.postman;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class Url {

    public String raw;
    public List<String> host;
    public List<String> path;
    public List<Query> query;
}
