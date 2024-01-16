package org.example.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Endpoint {

    private String name;
    private String description;
    private String url;
    private String method;
    private List<ApiParameter> parameters;
    private String responseBody;
    private String requestBody;
}
