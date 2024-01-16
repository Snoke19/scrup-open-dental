package org.example;

import org.example.models.ApiEntry;
import org.example.models.CollectionEndpoints;
import org.jsoup.nodes.Document;

import java.util.ArrayList;
import java.util.List;

public class OpenDentalScrapperAPISpec extends AbstractAPIService implements ScrapperAPISpec {

    private final PrepareCollectionEndpoints prepareCollectionEndpoints;

    public OpenDentalScrapperAPISpec(PrepareCollectionEndpoints prepareCollectionEndpoints) {
        this.prepareCollectionEndpoints = prepareCollectionEndpoints;
    }

    @Override
    public List<CollectionEndpoints> getCollectionEndpoints() {

        Document apiSpecificationDocument = fetchDocument("https://www.opendental.com/site/apispecification.html");

        List<CollectionEndpoints> collectionEndpointsList = new ArrayList<>();

        if (apiSpecificationDocument != null) {
            List<ApiEntry> apiEntries = extractApiEntries(apiSpecificationDocument);

            for (ApiEntry apiEntry : apiEntries) {
                Document apiPageDocument = fetchDocument(apiEntry.pageUrl());

                if (apiPageDocument != null) {
                    CollectionEndpoints collectionEndpoints = prepareCollectionEndpoints.prepareCollectionEndpoints(apiPageDocument);
                    collectionEndpointsList.add(collectionEndpoints);
                }
            }
        }

        return collectionEndpointsList;
    }
}
