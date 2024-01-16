package org.example;

import org.example.models.CollectionEndpoints;
import org.example.models.Endpoint;
import org.example.models.postman.*;

import java.util.List;
import java.util.stream.Collectors;

public class PostmanConverter {

    private static final String AUTHORIZATION = "Authorization";
    private static final String TYPE_HEADER = "default";
    private static final String HEADER_VALUE = "{{token_auth}}";

    public static Root convertEndpointsToPostmanModel(List<CollectionEndpoints> collectionEndpointsList) {

        List<Item> generalItems = collectionEndpointsList.stream()
            .map(collectionEndpoints -> Item.builder()
                .name(collectionEndpoints.getName())
                .item(prepareItems(collectionEndpoints))
                .build()
            ).collect(Collectors.toList());

        return Root.builder()
            .info(prepareInfo())
            .item(generalItems).build();
    }

    private static List<Item> prepareItems(CollectionEndpoints collectionEndpoints) {
        return collectionEndpoints.getEndpoints().stream()
            .map(PostmanConverter::prepareItem)
            .collect(Collectors.toList());
    }

    private static Item prepareItem(Endpoint endpoint) {
        return Item.builder()
            .name(endpoint.getName())
            .request(prepareRequest(endpoint))
            .build();
    }

    private static Info prepareInfo() {
        return Info.builder()
            ._postman_id("11a4a14e-b025-40a1-943f-14d0fb8782a1")
            .name("OpenDentalAPI")
            .schema("https://schema.getpostman.com/json/collection/v2.1.0/collection.json")
            ._exporter_id("14383245")
            .build();
    }

    private static Request prepareRequest(Endpoint endpoint) {
        return Request.builder()
            .method(endpoint.getMethod())
            .header(List.of(prepareHeader()))
            .body(prepareBody(endpoint))
            .url(prepareUrl(endpoint))
            .description(endpoint.getDescription())
            .build();
    }

    private static Url prepareUrl(Endpoint endpoint) {
        return Url.builder()
            .raw("{{url_local}}{{version_api}}" + endpoint.getUrl())
            .host(List.of("{{url_local}}{{version_api}}"))
            .path(List.of(endpoint.getName()))
            .build();
    }

    private static Body prepareBody(Endpoint endpoint) {
        return Body.builder()
            .mode("raw")
            .raw(endpoint.getRequestBody())
            .options(Options.builder().raw(Raw.builder().language("json").build()).build())
            .build();
    }

    private static Header prepareHeader() {
        return Header.builder()
            .key(AUTHORIZATION)
            .type(TYPE_HEADER)
            .value(HEADER_VALUE)
            .build();
    }
}
