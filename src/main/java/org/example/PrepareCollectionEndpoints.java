package org.example;

import org.example.models.CollectionEndpoints;
import org.jsoup.nodes.Document;

public interface  PrepareCollectionEndpoints {

    CollectionEndpoints prepareCollectionEndpoints(Document apiPageDocument);
}
