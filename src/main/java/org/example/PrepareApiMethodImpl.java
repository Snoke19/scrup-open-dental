package org.example;

import org.example.models.ApiParameter;
import org.example.models.Endpoint;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.safety.Safelist;
import org.jsoup.select.Elements;

import java.util.*;

public class PrepareApiMethodImpl implements PrepareApiMethod {

    protected static final String HTTP_METHOD_GET = "GET";
    protected static final String HTTP_METHOD_PUT = "PUT";
    protected static final String HTTP_METHOD_POST = "POST";
    protected static final String HTTP_METHOD_DELETE = "DELETE";

    private static final Map<String, Integer> HTTP_METHOD_LENGTHS = Map.of(
        HTTP_METHOD_GET, 4,
        HTTP_METHOD_PUT, 4,
        HTTP_METHOD_POST, 5,
        HTTP_METHOD_DELETE, 7
    );

    @Override
    public Endpoint prepareApiMethod(String descEndpoint) {
        String name = getRequestUrl(descEndpoint);
        String description = getDescription(descEndpoint);
        String url = getRequestUrl(descEndpoint);
        String method = getMethodType(descEndpoint);
        String responseBody = getResponseBody(descEndpoint);
        String requestBody = getRequestBody(descEndpoint);
        List<ApiParameter> apiParameters = getApiParameters(descEndpoint);

        return new Endpoint(name, description, url, method, apiParameters, responseBody, requestBody);
    }

    private List<ApiParameter> getApiParameters(String block) {

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

    private Map<String, String> getApiParametersText(String block) {
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
                        Node sibling = boldElement.nextSibling();
                        if (sibling != null) {
                            String paramValue = sibling.toString().trim();
                            parameters.put(paramName, paramValue);
                        }
                    }
                }
            }
            return parameters;
        }
        return new HashMap<>();
    }

    private String getDescription(String block) {
        return Jsoup.clean(block, Safelist.basic());
    }

    private String getRequestBody(String block) {
        Document document = Jsoup.parse(block);

        Element codeBlockElement = document.select("span.codeblock").first();
        if (codeBlockElement == null) {
            return "";
        }

        return codeBlockElement.text();
    }

    private String getResponseBody(String block) {
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

    private String getMethodType(String block) {
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

    private String getRequestUrl(String block) {
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
}
