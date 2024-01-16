package org.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.postman.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Main {

    private static final Map<String, Integer> HTTP_METHOD_LENGTHS = Map.of(
        "GET", 4,
        "PUT", 4,
        "POST", 5,
        "DELETE", 7
    );

    private static final List<String> EXCLUDED_CHAPTERS = List.of(
        "API Events",
        "API Forum",
        "API Setup",
        "API Implementation",
        "API Permissions",
        "API Modes - Local, Service, and Cloud");
    public static final String BASE_URL = "https://www.opendental.com";

    public static void main(String[] args) throws JsonProcessingException {
        Document apiSpecificationDocument = fetchDocument("https://www.opendental.com/site/apispecification.html");

        List<CollectionEndpoints> collectionEndpointsList = new ArrayList<>();

        if (apiSpecificationDocument != null) {
            List<ApiEntry> apiEntries = extractApiEntries(apiSpecificationDocument);

            for (ApiEntry apiEntry : apiEntries) {
                Document apiPageDocument = fetchDocument(apiEntry.pageUrl());

                if (apiPageDocument != null) {
                    CollectionEndpoints collectionEndpoints = processApiPage(apiPageDocument);
                    collectionEndpointsList.add(collectionEndpoints);
                }
            }
        }

        Root root = getRoot(collectionEndpointsList);

        writeToJsonFile(root);
    }

    private static Root getRoot(List<CollectionEndpoints> collectionEndpointsList) {
        Root root = new Root();
        root.setInfo(Info.builder()
            ._postman_id("11a4a14e-b025-40a1-943f-14d0fb8782a1")
            .name("OpenDentalAPI")
            .schema("https://schema.getpostman.com/json/collection/v2.1.0/collection.json")
            ._exporter_id("14383245")
            .build());
        List<Item> l = new ArrayList<>();
        for (CollectionEndpoints collectionEndpoints : collectionEndpointsList) {
            Item itemGeneral = new Item();
            itemGeneral.setName(collectionEndpoints.getName());
            List<Endpoint> endpointList = collectionEndpoints.getEndpoints();
            List<Item> ites = new ArrayList<>();
            for (Endpoint endpoint : endpointList) {
                Item item = new Item();
                item.setName(endpoint.getName());
                item.setRequest(Request.builder()
                    .method(endpoint.getMethod())
                    .header(List.of(Header.builder().key("Authorization").type("default").value("{{token_auth}}").build()))
                    .body(Body.builder()
                        .mode("raw")
                        .raw(endpoint.getRequestBody())
                        .options(Options.builder().raw(Raw.builder().language("json").build()).build())
                        .build())
                    .url(Url.builder()
                        .raw("{{url_local}}{{version_api}}" + endpoint.getUrl())
                        .host(List.of("{{url_local}}{{version_api}}"))
                        .path(List.of(endpoint.getName()))
                        .build())
                    .description(endpoint.getDescription())
                    .build());
                ites.add(item);
            }
            itemGeneral.setItem(ites);
            l.add(itemGeneral);
        }
        root.setItem(l);
        return root;
    }

    private static CollectionEndpoints processApiPage(Document apiPageDocument) {
        Element apiNameElement = apiPageDocument.select("div.TopBar2 > p").first();
        String htmlBlock = apiPageDocument.getElementsByClass("GeneralPageContent").toString();
        String[] endpointsStr = htmlBlock.split("<h2>");

        CollectionEndpoints collectionEndpoints = new CollectionEndpoints();
        List<Endpoint> endpoints = new ArrayList<>();

        for (int i = 1; i < endpointsStr.length; i++) {
            String descEndpoint = endpointsStr[i];
            Endpoint endpoint = createEndpointFromHtmlBlock(descEndpoint);
            endpoints.add(endpoint);
        }

        collectionEndpoints.setName(apiNameElement == null ? "No name" : apiNameElement.text());
        collectionEndpoints.setEndpoints(endpoints);

        return collectionEndpoints;
    }

    private static Endpoint createEndpointFromHtmlBlock(String descEndpoint) {
        String name = getRequestUrl(descEndpoint);
        String description = getDescription(descEndpoint);
        String url = getRequestUrl(descEndpoint);
        String method = getMethodType(descEndpoint);
        String responseBody = getResponseBody(descEndpoint);
        String requestBody = getRequestBody(descEndpoint);
        List<ApiParameter> apiParameters = getApiParameters(descEndpoint);

        return new Endpoint(name, description, url, method, apiParameters, responseBody, requestBody);
    }

    private static List<ApiParameter> getApiParameters(String block) {

        Map<String, String> apiParametersInMap = getApiParametersText(block);
        List<ApiParameter> apiParameters = new ArrayList<>();

        for (Map.Entry<String, String> entry : apiParametersInMap.entrySet()) {
            ApiParameter apiParameter = new ApiParameter();
            apiParameter.setName(entry.getKey());
            apiParameter.setValue(entry.getValue());
            apiParameters.add(apiParameter);
        }

        return apiParameters;
    }

    private static Map<String, String> getApiParametersText(String block) {
        boolean isGetMethod = getMethodType(block).equals("GET");

        if (isGetMethod) {
            Document document = Jsoup.parse(block);

            Elements paragraphElements = document.select("body p");

            boolean isBetweenSections = false;

            Map<String, String> parameters = new LinkedHashMap<>();

            for (Element paragraphElement : paragraphElements) {
                String paragraphText = paragraphElement.text();

                if (paragraphText.contains("Parameters:")) {
                    isBetweenSections = true;
                }

                if (paragraphText.contains("Example Requests:")) {
                    break;
                }

                if (isBetweenSections && !paragraphText.contains("Parameters")) {
                    Elements boldElements = paragraphElement.select("b");
                    for (Element boldElement : boldElements) {
                        String paramName = boldElement.text().trim();
                        String paramValue = boldElement.nextSibling().toString().trim();
                        parameters.put(paramName, paramValue);
                    }
                }
            }
            return parameters;
        }
        return new HashMap<>();
    }

    private static String getDescription(String block) {
        return Jsoup.parse(block).text();
    }

    private static String getRequestBody(String block) {
        Document document = Jsoup.parse(block);

        Element codeBlockElement = document.select("span.codeblock").first();
        if (codeBlockElement == null) {
            return "";
        }

        return codeBlockElement.text();
    }

    private static String getResponseBody(String block) {
        Document document = Jsoup.parse(block);

        Element jsonElement = document.select("p:has(b:contains(Example Response))").first();
        if (jsonElement == null) {
            return "";
        }
        Element codeBlockElement = jsonElement.select("span.codeblock").first();
        if (codeBlockElement == null) {
            return "";

        }

        return codeBlockElement.text();
    }

    private static String getMethodType(String block) {
        Document document = Jsoup.parse(block);

        Element exampleRequestElement = document.select("p:has(b:contains(Example Request))").first();
        if (exampleRequestElement == null) {
            return "";
        }

        Element elementBr = exampleRequestElement.select("br").first();
        if (elementBr == null) {
            return "";
        }

        Node nodeBr = elementBr.nextSibling();
        if (nodeBr == null) {
            return "";
        }

        String exampleRequestText = nodeBr.toString().trim();

        for (Map.Entry<String, Integer> entry : HTTP_METHOD_LENGTHS.entrySet()) {
            if (exampleRequestText.contains(entry.getKey())) {
                return exampleRequestText.substring(0, entry.getValue()).trim();
            }
        }

        return "";
    }

    private static String getRequestUrl(String block) {
        Document document = Jsoup.parse(block);

        Element exampleRequestElement = document.select("p:has(b:contains(Example Request))").first();
        if (exampleRequestElement == null) {
            return "";
        }
        Element elementBr = exampleRequestElement.select("br").first();
        if (elementBr == null) {
            return "";
        }
        Node nodeBr = elementBr.nextSibling();
        if (nodeBr == null) {
            return "";
        }

        String exampleRequestText = nodeBr.toString().trim();

        for (Map.Entry<String, Integer> entry : HTTP_METHOD_LENGTHS.entrySet()) {
            if (exampleRequestText.contains(entry.getKey())) {
                return exampleRequestText.substring(entry.getValue());
            }
        }

        return "";
    }

    private static List<ApiEntry> extractApiEntries(Document document) {
        return document.getElementsByClass("GeneralPageContent").select("p > a")
            .stream()
            .map(Main::createApiEntry)
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
    }

    private static ApiEntry createApiEntry(Element element) {
        String nameApi = element.text();
        if (!EXCLUDED_CHAPTERS.contains(nameApi)) {
            String pageUrl = checkUrl(element.attr("href"));
            return new ApiEntry(nameApi, pageUrl);
        }
        return null;
    }

    private static Document fetchDocument(String url) {
        try {
            return Jsoup.connect(url).get();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    private static String checkUrl(String pageUrl) {
        if (pageUrl.contains("..")) {
            pageUrl = BASE_URL + pageUrl.substring(2);
        } else {
            pageUrl = BASE_URL + "/site/" + pageUrl;
        }

        return pageUrl;
    }

    private static void writeToJsonFile(Root root) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writeValue(new File("example.json"), root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private record ApiEntry(String name, String pageUrl) {
    }
}