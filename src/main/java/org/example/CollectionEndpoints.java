package org.example;

import java.util.List;
import java.util.Objects;

public class CollectionEndpoints {

    private String name;
    private List<Endpoint> endpoints;

    public void setName(String name) {
        this.name = name;
    }

    public void setEndpoints(List<Endpoint> endpoints) {
        this.endpoints = endpoints;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CollectionEndpoints that = (CollectionEndpoints) o;
        return Objects.equals(name, that.name) && Objects.equals(endpoints, that.endpoints);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, endpoints);
    }

    @Override
    public String toString() {
        return "CollectionEndpoints{" +
                "name='" + name + '\'' +
                ", endpoints=" + endpoints +
                '}';
    }
}
