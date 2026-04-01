package com.example.polarion;

import com.example.polarion.dto.ApiListResponse;
import com.example.polarion.dto.ApiSingleResponse;
import com.example.polarion.dto.WorkItemAttributes;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.HashMap;
import java.util.Map;

/**
 * Polarion REST API – Work Items.
 * Paths: GET/POST/PATCH/DELETE /projects/{projectId}/workitems, etc.
 */
public class PolarionWorkItemsApi {

    private final PolarionClient client;

    public PolarionWorkItemsApi(PolarionClient client) {
        this.client = client;
    }

    /**
     * Returns a list of Work Items for the project.
     * GET /projects/{projectId}/workitems
     */
    public JsonNode getWorkItems(String projectId) throws Exception {
        return getWorkItems(projectId, null, null, null, null, null);
    }

    /**
     * Returns a list of Work Items with paging and filters.
     *
     * @param projectId   project ID
     * @param pageSize    page[size]
     * @param pageNumber  page[number] (1-based)
     * @param query       query string (Polarion query)
     * @param sort        sort string
     * @param include     include related entities
     */
    public JsonNode getWorkItems(String projectId, Integer pageSize, Integer pageNumber,
                                  String query, String sort, String include) throws Exception {
        Map<String, String> params = new HashMap<>();
        if (pageSize != null) params.put("page[size]", String.valueOf(pageSize));
        if (pageNumber != null) params.put("page[number]", String.valueOf(pageNumber));
        if (query != null) params.put("query", query);
        if (sort != null) params.put("sort", sort);
        if (include != null) params.put("include", include);
        String path = "/projects/" + encodePath(projectId) + "/workitems";
        return client.getAsJson(path, params.isEmpty() ? null : params);
    }

    /**
     * Returns the specified Work Item.
     * GET /projects/{projectId}/workitems/{workItemId}
     */
    public JsonNode getWorkItem(String projectId, String workItemId) throws Exception {
        String path = "/projects/" + encodePath(projectId) + "/workitems/" + encodePath(workItemId);
        return client.getAsJson(path);
    }

    /**
     * Updates the specified Work Item. PATCH /projects/{projectId}/workitems/{workItemId}
     */
    public String updateWorkItem(String projectId, String workItemId, Object body) throws Exception {
        String path = "/projects/" + encodePath(projectId) + "/workitems/" + encodePath(workItemId);
        return client.patch(path, body);
    }

    /**
     * Creates a list of Work Items. POST /projects/{projectId}/workitems
     */
    public String createWorkItems(String projectId, Object body) throws Exception {
        return client.post("/projects/" + encodePath(projectId) + "/workitems", body);
    }

    /**
     * Returns a list of Work Item Comments.
     * GET /projects/{projectId}/workitems/{workItemId}/comments
     */
    public JsonNode getWorkItemComments(String projectId, String workItemId) throws Exception {
        String path = "/projects/" + encodePath(projectId) + "/workitems/" + encodePath(workItemId) + "/comments";
        return client.getAsJson(path);
    }

    /**
     * Returns a list of Linked Work Items.
     * GET /projects/{projectId}/workitems/{workItemId}/linkedworkitems
     */
    public JsonNode getLinkedWorkItems(String projectId, String workItemId) throws Exception {
        String path = "/projects/" + encodePath(projectId) + "/workitems/" + encodePath(workItemId) + "/linkedworkitems";
        return client.getAsJson(path);
    }

    /**
     * Returns a list of Test Steps for the work item.
     * GET /projects/{projectId}/workitems/{workItemId}/teststeps
     */
    public JsonNode getTestSteps(String projectId, String workItemId) throws Exception {
        String path = "/projects/" + encodePath(projectId) + "/workitems/" + encodePath(workItemId) + "/teststeps";
        return client.getAsJson(path);
    }

    /** Returns a list of Work Items as typed DTOs. */
    public ApiListResponse<WorkItemAttributes> getWorkItemsAsDto(String projectId) throws Exception {
        return getWorkItemsAsDto(projectId, null, null, null, null, null);
    }

    /** Returns a list of Work Items with paging/filters as typed DTOs. */
    public ApiListResponse<WorkItemAttributes> getWorkItemsAsDto(String projectId, Integer pageSize, Integer pageNumber,
                                                                  String query, String sort, String include) throws Exception {
        Map<String, String> params = new HashMap<>();
        if (pageSize != null) params.put("page[size]", String.valueOf(pageSize));
        if (pageNumber != null) params.put("page[number]", String.valueOf(pageNumber));
        if (query != null) params.put("query", query);
        if (sort != null) params.put("sort", sort);
        if (include != null) params.put("include", include);
        String path = "/projects/" + encodePath(projectId) + "/workitems";
        return client.get(path, params.isEmpty() ? null : params, new TypeReference<ApiListResponse<WorkItemAttributes>>() {});
    }

    /** Returns the specified Work Item as a typed DTO. */
    public ApiSingleResponse<WorkItemAttributes> getWorkItemAsDto(String projectId, String workItemId) throws Exception {
        String path = "/projects/" + encodePath(projectId) + "/workitems/" + encodePath(workItemId);
        return client.get(path, new TypeReference<ApiSingleResponse<WorkItemAttributes>>() {});
    }

    private static String encodePath(String segment) {
        return java.net.URLEncoder.encode(segment, java.nio.charset.StandardCharsets.UTF_8);
    }
}
