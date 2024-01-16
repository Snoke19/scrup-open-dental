package org.example.postman;

import lombok.Data;

import java.util.List;

@Data
public class Root {

    public Info info;
    public List<Item> item;
}
