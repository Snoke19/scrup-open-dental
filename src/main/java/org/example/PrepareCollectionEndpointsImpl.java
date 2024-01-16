package org.example;

import org.example.models.CollectionEndpoints;
import org.example.models.Endpoint;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.List;

public class PrepareCollectionEndpointsImpl implements PrepareCollectionEndpoints {

    private final PrepareApiMethod prepareApiMethod;

    public PrepareCollectionEndpointsImpl(PrepareApiMethod prepareApiMethod) {
        this.prepareApiMethod = prepareApiMethod;
    }

    @Override
    public CollectionEndpoints prepareCollectionEndpoints(Document apiPageDocument) {
        Element apiNameElement = apiPageDocument.select("div.TopBar2 > p").first();
        String htmlBlock = apiPageDocument.getElementsByClass("GeneralPageContent").toString();
        String[] endpointsStr = htmlBlock.split("<h2>");

        CollectionEndpoints collectionEndpoints = new CollectionEndpoints();
        List<Endpoint> endpoints = new ArrayList<>();

        for (int i = 1; i < endpointsStr.length; i++) {
            String descEndpoint = endpointsStr[i];
            Endpoint endpoint = prepareApiMethod.prepareApiMethod(descEndpoint);
            endpoints.add(endpoint);
        }

        collectionEndpoints.setName(apiNameElement == null ? "No name" : apiNameElement.text());
        collectionEndpoints.setEndpoints(endpoints);

        return collectionEndpoints;
    }
}
