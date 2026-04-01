package com.example.polarion;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * HTTP client for the Polarion REST API (OpenAPI 3.0 at /polarion/rest/v1).
 * Uses Bearer token authentication as required by the API.
 *
 * @see <a href="https://polarion.rcs-hitachirail.com/polarion/rest/v1">Polarion REST API (Swagger)</a>
 * @see <a href="https://polarion.rcs-hitachirail.com/polarion/rest/v1/definition">OpenAPI definition</a>
 */
public class PolarionClient {

    public static final String DEFAULT_BASE_URL = "https://polarion.rcs-hitachirail.com/polarion/rest/v1";

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    private final String baseUrl;
    private final String bearerToken;

    public PolarionClient(String bearerToken) {
        this(DEFAULT_BASE_URL, bearerToken);
    }

    public PolarionClient(String baseUrl, String bearerToken) {
        this.baseUrl = baseUrl.endsWith("/") ? baseUrl.substring(0, baseUrl.length() - 1) : baseUrl;
        this.bearerToken = bearerToken != null ? bearerToken : "";
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(30))
                .build();
        this.objectMapper = new ObjectMapper();
    }

    /**
     * GET request; returns response body as string.
     */
    public String get(String path) throws Exception {
        return get(path, (Map<String, String>) null);
    }

    /**
     * GET with optional query parameters. Param names can include brackets, e.g. "page[size]", "page[number]".
     */
    public String get(String path, Map<String, String> queryParams) throws Exception {
        String url = buildUrl(path, queryParams);
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .header("Accept", "application/json")
                .timeout(Duration.ofSeconds(60));
        addAuth(builder);
        HttpResponse<String> response = httpClient.send(builder.build(), HttpResponse.BodyHandlers.ofString());
        handleError(response);
        return response.body();
    }

    /**
     * GET and parse response as JsonNode for flexible data extraction.
     */
    public JsonNode getAsJson(String path) throws Exception {
        return getAsJson(path, null);
    }

    public JsonNode getAsJson(String path, Map<String, String> queryParams) throws Exception {
        String json = get(path, queryParams);
        return objectMapper.readTree(json);
    }

    /**
     * GET and deserialize response into the given type (e.g. ApiListResponse&lt;ProjectAttributes&gt;).
     */
    public <T> T get(String path, Map<String, String> queryParams, TypeReference<T> typeRef) throws Exception {
        String json = get(path, queryParams);
        return objectMapper.readValue(json, typeRef);
    }

    public <T> T get(String path, TypeReference<T> typeRef) throws Exception {
        return get(path, null, typeRef);
    }

    /**
     * PATCH request with JSON body (e.g. for updating work items, projects).
     */
    public String patch(String path, Object body) throws Exception {
        String url = buildUrl(path, null);
        String bodyJson = body == null ? "{}" : objectMapper.writeValueAsString(body);
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .timeout(Duration.ofSeconds(60))
                .method("PATCH", HttpRequest.BodyPublishers.ofString(bodyJson));
        addAuth(builder);
        HttpResponse<String> response = httpClient.send(builder.build(), HttpResponse.BodyHandlers.ofString());
        handleError(response);
        return response.body();
    }

    /**
     * POST request with JSON body.
     */
    public String post(String path, Object body) throws Exception {
        String url = buildUrl(path, null);
        String bodyJson = body == null ? "{}" : objectMapper.writeValueAsString(body);
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .timeout(Duration.ofSeconds(60))
                .POST(HttpRequest.BodyPublishers.ofString(bodyJson));
        addAuth(builder);
        HttpResponse<String> response = httpClient.send(builder.build(), HttpResponse.BodyHandlers.ofString());
        handleError(response);
        return response.body();
    }

    /**
     * DELETE request.
     */
    public void delete(String path) throws Exception {
        String url = buildUrl(path, null);
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .DELETE()
                .header("Accept", "application/json")
                .timeout(Duration.ofSeconds(60));
        addAuth(builder);
        HttpResponse<String> response = httpClient.send(builder.build(), HttpResponse.BodyHandlers.ofString());
        handleError(response);
    }

    private void addAuth(HttpRequest.Builder builder) {
        if (bearerToken != null && !bearerToken.isEmpty()) {
            builder.header("Authorization", "Bearer " + bearerToken);
        }
    }

    private String buildUrl(String path, Map<String, String> queryParams) {
        String p = path.startsWith("/") ? path : "/" + path;
        String url = baseUrl + p;
        if (queryParams != null && !queryParams.isEmpty()) {
            String query = queryParams.entrySet().stream()
                    .filter(e -> e.getValue() != null)
                    .map(e -> URLEncoder.encode(e.getKey(), StandardCharsets.UTF_8) + "=" +
                            URLEncoder.encode(e.getValue(), StandardCharsets.UTF_8))
                    .collect(Collectors.joining("&"));
            if (!query.isEmpty()) {
                url += (url.contains("?") ? "&" : "?") + query;
            }
        }
        return url;
    }

    private void handleError(HttpResponse<String> response) {
        if (response.statusCode() >= 400) {
            throw new PolarionApiException(response.statusCode(), response.body());
        }
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    public static class PolarionApiException extends RuntimeException {
        private final int statusCode;
        private final String responseBody;

        public PolarionApiException(int statusCode, String responseBody) {
            super("HTTP " + statusCode + (responseBody != null && !responseBody.isEmpty() ? ": " + responseBody : ""));
            this.statusCode = statusCode;
            this.responseBody = responseBody;
        }

        public int getStatusCode() {
            return statusCode;
        }

        /** Raw response body (e.g. JSON error payload for deserializing into ApiErrorsResponse). */
        public String getResponseBody() {
            return responseBody;
        }
    }
}
