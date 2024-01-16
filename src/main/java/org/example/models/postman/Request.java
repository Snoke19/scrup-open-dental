package org.example.models.postman;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class Request {

    public String method;
    public List<Header> header;
    public Url url;
    public String description;
    public Body body;
}
