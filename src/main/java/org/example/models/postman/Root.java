package org.example.models.postman;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class Root {

    public Info info;
    public List<Item> item;
}
