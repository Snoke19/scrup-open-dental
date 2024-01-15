package org.example;

import java.util.List;
import java.util.Objects;

public class Endpoint {

    private String name;
    private String description;
    private String url;
    private String method;
    private List<String> parameters;
    private String responseBody;
    private String requestBody;

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public void setParameters(List<String> parameters) {
        this.parameters = parameters;
    }

    public void setResponseBody(String responseBody) {
        this.responseBody = responseBody;
    }

    public void setRequestBody(String requestBody) {
        this.requestBody = requestBody;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Endpoint endpoint = (Endpoint) o;
        return Objects.equals(name, endpoint.name) && Objects.equals(description, endpoint.description) && Objects.equals(url, endpoint.url) && Objects.equals(method, endpoint.method) && Objects.equals(parameters, endpoint.parameters) && Objects.equals(responseBody, endpoint.responseBody) && Objects.equals(requestBody, endpoint.requestBody);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, url, method, parameters, responseBody, requestBody);
    }

    @Override
    public String toString() {
        return "Endpoint{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", url='" + url + '\'' +
                ", method='" + method + '\'' +
                ", parameters=" + parameters +
                ", responseBody='" + responseBody + '\'' +
                ", requestBody='" + requestBody + '\'' +
                '}';
    }
}
