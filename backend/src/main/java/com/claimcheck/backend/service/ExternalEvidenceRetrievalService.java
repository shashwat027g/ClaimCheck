package com.claimcheck.backend.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.claimcheck.backend.entity.Claim;
import com.claimcheck.backend.entity.Evidence;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
public class ExternalEvidenceRetrievalService {

    private final ObjectMapper objectMapper;
    private final HttpClient httpClient;

    public ExternalEvidenceRetrievalService() {
        this.objectMapper = new ObjectMapper();
        this.httpClient = HttpClient.newHttpClient();
    }

    public List<Evidence> searchWikipedia(
            String claimText,
            Claim claim) {

        List<Evidence> evidenceList = new ArrayList<>();

        try {

            String encodedQuery =
                    URLEncoder.encode(
                            claimText,
                            StandardCharsets.UTF_8
                    );

            String searchUrl =
                    "https://en.wikipedia.org/w/api.php"
                    + "?action=query"
                    + "&list=search"
                    + "&srsearch=" + encodedQuery
                    + "&srlimit=3"
                    + "&format=json";

            HttpRequest searchRequest =
                    HttpRequest.newBuilder()
                            .uri(URI.create(searchUrl))
                            .header(
                                    "User-Agent",
                                    "ClaimCheck/1.0"
                            )
                            .GET()
                            .build();

            HttpResponse<String> searchResponse =
                    httpClient.send(
                            searchRequest,
                            HttpResponse.BodyHandlers.ofString(
                                StandardCharsets.UTF_8
                            )
                    );

            if (searchResponse.statusCode() != 200) {
                return evidenceList;
            }

            JsonNode searchRoot =
                    objectMapper.readTree(
                            searchResponse.body()
                    );

            JsonNode searchResults =
                    searchRoot
                            .path("query")
                            .path("search");

            for (JsonNode result : searchResults) {

                String title =
                        result.path("title").asText();

                String encodedTitle =
                        URLEncoder.encode(
                                title,
                                StandardCharsets.UTF_8
                        );

                String extractUrl =
                        "https://en.wikipedia.org/w/api.php"
                        + "?action=query"
                        + "&prop=extracts"
                        + "&explaintext=true"
                        + "&exintro=true"
                        + "&titles=" + encodedTitle
                        + "&format=json";

                HttpRequest extractRequest =
                        HttpRequest.newBuilder()
                                .uri(URI.create(extractUrl))
                                .header(
                                        "User-Agent",
                                        "ClaimCheck/1.0"
                                )
                                .GET()
                                .build();

                HttpResponse<String> extractResponse =
                        httpClient.send(
                                extractRequest,
                                HttpResponse.BodyHandlers.ofString(
                                        StandardCharsets.UTF_8
                                )
                        );

                if (extractResponse.statusCode() != 200) {
                    continue;
                }

                JsonNode extractRoot =
                        objectMapper.readTree(
                                extractResponse.body()
                        );

                JsonNode pages =
                        extractRoot
                                .path("query")
                                .path("pages");

                String pageId =
                        pages.fieldNames().next();

                JsonNode page =
                        pages.path(pageId);

                String extract =
                        page.path("extract").asText();

                if (extract == null || extract.isBlank()) {
                        continue;
                }

                String pageUrl =
                        "https://en.wikipedia.org/wiki/"
                                + title.replace(" ", "_");

                Evidence evidence = new Evidence(
                        extract,
                        pageUrl,
                        title,
                        "Wikipedia",
                        claim
                );

                evidenceList.add(evidence);
            }

        } catch (Exception exception) {

            System.out.println(
                    "Wikipedia evidence retrieval failed: "
                            + exception.getMessage()
            );
        }

        return evidenceList;
    }      
}