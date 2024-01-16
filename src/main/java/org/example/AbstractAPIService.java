package org.example;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.example.models.ApiEntry;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
public abstract class AbstractAPIService {

    protected static final List<String> EXCLUDED_CHAPTERS = List.of(
        "API Events",
        "API Forum",
        "API Setup",
        "API Implementation",
        "API Permissions",
        "API Modes - Local, Service, and Cloud");

    protected static final String BASE_URL = "https://www.opendental.com";

    protected String checkUrl(String pageUrl) {
        return pageUrl.contains("..") ? BASE_URL + pageUrl.substring(2) : BASE_URL + "/site/" + pageUrl;
    }

    protected List<ApiEntry> extractApiEntries(Document document) {
        return document.getElementsByClass("GeneralPageContent")
            .select("p > a")
            .stream()
            .map(this::createApiEntry)
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
    }

    protected ApiEntry createApiEntry(Element element) {
        String nameApi = element.text();
        if (!EXCLUDED_CHAPTERS.contains(nameApi)) {
            String pageUrl = checkUrl(element.attr("href"));
            return new ApiEntry(nameApi, pageUrl);
        }
        return null;
    }

    protected Document fetchDocument(String url) {
        try {
            Document document = Jsoup.connect(url).get();
            log.info("Fetching document from {}", url);
            return document;
        } catch (IOException e) {
            log.error("Error fetching document from {}. Stack trace: {}", url, ExceptionUtils.getStackTrace(e));
        }
        return null;
    }
}
