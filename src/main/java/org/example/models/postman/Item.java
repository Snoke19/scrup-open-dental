package org.example.models.postman;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class Item{

    public String name;
    public List<Item> item;
    public String description;
    public Request request;
    public ArrayList<Object> response;
    public ProtocolProfileBehavior protocolProfileBehavior;
}
