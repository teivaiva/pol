package com.example.polarion;

import com.example.polarion.dto.ApiListResponse;
import com.example.polarion.dto.ApiSingleResponse;
import com.example.polarion.dto.ProjectAttributes;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.HashMap;
import java.util.Map;

/**
 * Polarion REST API – Projects.
 * Paths: GET/DELETE/PATCH /projects, GET/PATCH/DELETE /projects/{projectId}, etc.
 */
public class PolarionProjectsApi {

    private final PolarionClient client;

    public PolarionProjectsApi(PolarionClient client) {
        this.client = client;
    }

    /**
     * Returns a list of Projects.
     * GET /projects
     */
    public JsonNode getProjects() throws Exception {
        return client.getAsJson("/projects");
    }

    /**
     * Returns a list of Projects with paging and optional query.
     *
     * @param pageSize  limit (e.g. 20)
     * @param pageNumber 1-based page number
     * @param query      optional query string
     * @param sort       optional sort string
     */
    public JsonNode getProjects(Integer pageSize, Integer pageNumber, String query, String sort) throws Exception {
        Map<String, String> params = new HashMap<>();
        if (pageSize != null) params.put("page[size]", String.valueOf(pageSize));
        if (pageNumber != null) params.put("page[number]", String.valueOf(pageNumber));
        if (query != null) params.put("query", query);
        if (sort != null) params.put("sort", sort);
        return client.getAsJson("/projects", params.isEmpty() ? null : params);
    }

    /**
     * Returns the specified Project.
     * GET /projects/{projectId}
     */
    public JsonNode getProject(String projectId) throws Exception {
        return client.getAsJson("/projects/" + encodePath(projectId));
    }

    /**
     * Updates the specified Project. PATCH /projects/{projectId}
     */
    public String updateProject(String projectId, Object body) throws Exception {
        return client.patch("/projects/" + encodePath(projectId), body);
    }

    /**
     * Deletes the specified Project. DELETE /projects/{projectId}
     */
    public void deleteProject(String projectId) throws Exception {
        client.delete("/projects/" + encodePath(projectId));
    }

    /**
     * Returns a list of Project Templates. GET /projecttemplates
     */
    public JsonNode getProjectTemplates() throws Exception {
        return client.getAsJson("/projecttemplates");
    }

    /** Returns a list of Projects as typed DTOs (for Jackson serialize/deserialize and getter navigation). */
    public ApiListResponse<ProjectAttributes> getProjectsAsDto() throws Exception {
        return client.get("/projects", new TypeReference<ApiListResponse<ProjectAttributes>>() {});
    }

    /** Returns a list of Projects with paging as typed DTOs. */
    public ApiListResponse<ProjectAttributes> getProjectsAsDto(Integer pageSize, Integer pageNumber, String query, String sort) throws Exception {
        Map<String, String> params = new HashMap<>();
        if (pageSize != null) params.put("page[size]", String.valueOf(pageSize));
        if (pageNumber != null) params.put("page[number]", String.valueOf(pageNumber));
        if (query != null) params.put("query", query);
        if (sort != null) params.put("sort", sort);
        return client.get("/projects", params.isEmpty() ? null : params, new TypeReference<ApiListResponse<ProjectAttributes>>() {});
    }

    /** Returns the specified Project as a typed DTO. */
    public ApiSingleResponse<ProjectAttributes> getProjectAsDto(String projectId) throws Exception {
        return client.get("/projects/" + encodePath(projectId), new TypeReference<ApiSingleResponse<ProjectAttributes>>() {});
    }

    private static String encodePath(String segment) {
        return java.net.URLEncoder.encode(segment, java.nio.charset.StandardCharsets.UTF_8);
    }
}
