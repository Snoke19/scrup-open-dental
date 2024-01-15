package org.example;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {

    private static final List<String> EXCEPT_CHAPTERS = List.of(
            "API Events",
            "API Forum",
            "API Setup",
            "API Implementation",
            "API Permissions",
            "API Modes - Local, Service, and Cloud");
    public static final String BASE_URL = "https://www.opendental.com";

    public static void main(String[] args) {
        Document document = fetchDocument("https://www.opendental.com/site/apispecification.html");

        if (document != null) {
            List<ApiEntry> apiEntries = extractApiEntries(document);

            for (ApiEntry data : apiEntries) {
                Document apiPage = fetchDocument(data.pageUrl());
                if (apiPage != null) {
                    String htmlBlock = apiPage.getElementsByClass("GeneralPageContent").toString();

                    String[] endpointsStr = htmlBlock.split("<h2>");


                    CollectionEndpoints collectionEndpoints = new CollectionEndpoints();
                    List<Endpoint> endpoints = new ArrayList<>();
                    for (int i = 1; i < endpointsStr.length; i++) {

                        String descEndpoint = endpointsStr[i];
                        Endpoint endpoint = new Endpoint();
                        endpoint.setName(getRequestUrl(descEndpoint));
                        endpoint.setDescription(geDescription(descEndpoint));
                        endpoint.setUrl(getRequestUrl(descEndpoint));
                        endpoint.setMethod(getMethodType(descEndpoint));
                        endpoint.setParameters(new ArrayList<>());
                        endpoint.setResponseBody(getResponseBody(descEndpoint));
                        endpoint.setRequestBody("");

                        endpoints.add(endpoint);
                        System.out.println(endpoint);
                        break;
                    }

                    collectionEndpoints.setName(endpointsStr[0]);
                    collectionEndpoints.setEndpoints(endpoints);
                }

                break;
            }
        }
    }

    private static String geDescription(String block) {
        Document document = Jsoup.parse(block);

        return document.text();
    }

    private static String getResponseBody(String block) {
        Document document = Jsoup.parse(block);

        Element jsonElement = document.select("p:has(b:contains(Example Response))").first();

        if (jsonElement != null) {
            Element codeBlockElement = document.select("span.codeblock").first();

            if (codeBlockElement != null) {
                return codeBlockElement.text();

            }
        }
        return "";
    }

    private static String getMethodType(String block) {
        Document document = Jsoup.parse(block);

        Element exampleRequestElement = document.select("p:has(b:contains(Example Request))").first();

        if (exampleRequestElement != null) {
            Element elementBr = exampleRequestElement.select("br").first();
            if (elementBr != null) {
                Node nodeBr = elementBr.nextSibling();
                if (nodeBr != null) {
                    String exampleRequestText = nodeBr.toString().trim();

                    if (exampleRequestText.contains("GET") || exampleRequestText.contains("PUT")) {
                        return exampleRequestText.substring(0, 4).trim();
                    } else if (exampleRequestText.contains("POST")) {
                        return exampleRequestText.substring(0, 5).trim();
                    } else if (exampleRequestText.contains("DELETE")) {
                        return exampleRequestText.substring(0, 7).trim();
                    }
                }
            }
        }

        return "";
    }

    private static String getRequestUrl(String block) {
        Document document = Jsoup.parse(block);

        Element exampleRequestElement = document.select("p:has(b:contains(Example Request))").first();

        if (exampleRequestElement != null) {
            Element elementBr = exampleRequestElement.select("br").first();
            if (elementBr != null) {
                Node nodeBr = elementBr.nextSibling();
                if (nodeBr != null) {
                    String exampleRequestText = nodeBr.toString().trim();

                    if (exampleRequestText.contains("GET")) {
                        return exampleRequestText.substring(4);
                    } else if (exampleRequestText.contains("POST")) {
                        return exampleRequestText.substring(5);
                    }
                }
            }
        }

        return "";
    }

    private static List<ApiEntry> extractApiEntries(Document document) {
        Elements elements = document.getElementsByClass("GeneralPageContent").select("p > a");
        List<ApiEntry> apiEntries = new ArrayList<>();

        for (Element element : elements) {
            String nameApi = element.text();
            if (!EXCEPT_CHAPTERS.contains(nameApi)) {
                String pageUrl = checkUrl(element.attr("href"));
                apiEntries.add(new ApiEntry(nameApi, pageUrl));
            }
        }

        return apiEntries;
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

    private record ApiEntry(String name, String pageUrl) {
    }
}