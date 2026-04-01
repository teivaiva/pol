package com.example.polarion;

import com.example.polarion.dto.ApiListResponse;
import com.example.polarion.dto.ApiSingleResponse;
import com.example.polarion.dto.UserAttributes;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.HashMap;
import java.util.Map;

/**
 * Polarion REST API – Users.
 * Paths: GET /users, GET/PATCH /users/{userId}, POST /users, etc.
 */
public class PolarionUsersApi {

    private final PolarionClient client;

    public PolarionUsersApi(PolarionClient client) {
        this.client = client;
    }

    /**
     * Returns a list of Users. GET /users
     */
    public JsonNode getUsers() throws Exception {
        return getUsers(null, null);
    }

    /**
     * Returns a list of Users with paging.
     */
    public JsonNode getUsers(Integer pageSize, Integer pageNumber) throws Exception {
        Map<String, String> params = new HashMap<>();
        if (pageSize != null) params.put("page[size]", String.valueOf(pageSize));
        if (pageNumber != null) params.put("page[number]", String.valueOf(pageNumber));
        return client.getAsJson("/users", params.isEmpty() ? null : params);
    }

    /**
     * Returns the specified User. GET /users/{userId}
     */
    public JsonNode getUser(String userId) throws Exception {
        return client.getAsJson("/users/" + encodePath(userId));
    }

    /**
     * Updates the specified User. PATCH /users/{userId}
     */
    public String updateUser(String userId, Object body) throws Exception {
        return client.patch("/users/" + encodePath(userId), body);
    }

    /**
     * Creates a list of Users. POST /users
     */
    public String createUsers(Object body) throws Exception {
        return client.post("/users", body);
    }

    /** Returns a list of Users as typed DTOs. */
    public ApiListResponse<UserAttributes> getUsersAsDto() throws Exception {
        return client.get("/users", new TypeReference<ApiListResponse<UserAttributes>>() {});
    }

    /** Returns a list of Users with paging as typed DTOs. */
    public ApiListResponse<UserAttributes> getUsersAsDto(Integer pageSize, Integer pageNumber) throws Exception {
        Map<String, String> params = new HashMap<>();
        if (pageSize != null) params.put("page[size]", String.valueOf(pageSize));
        if (pageNumber != null) params.put("page[number]", String.valueOf(pageNumber));
        return client.get("/users", params.isEmpty() ? null : params, new TypeReference<ApiListResponse<UserAttributes>>() {});
    }

    /** Returns the specified User as a typed DTO. */
    public ApiSingleResponse<UserAttributes> getUserAsDto(String userId) throws Exception {
        return client.get("/users/" + encodePath(userId), new TypeReference<ApiSingleResponse<UserAttributes>>() {});
    }

    private static String encodePath(String segment) {
        return java.net.URLEncoder.encode(segment, java.nio.charset.StandardCharsets.UTF_8);
    }
}
