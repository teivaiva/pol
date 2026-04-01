package com.example.polarion;

/**
 * Facade for the Polarion REST API. Create with base URL and Bearer token, then use
 * projects(), workItems(), and users() to access resource-specific endpoints.
 *
 * <p>API docs: <a href="https://polarion.rcs-hitachirail.com/polarion/rest/v1">Swagger UI</a>
 * Definition: <a href="https://polarion.rcs-hitachirail.com/polarion/rest/v1/definition">OpenAPI 3.0</a>
 */
public class PolarionApi {

    private final PolarionClient client;
    private final PolarionProjectsApi projectsApi;
    private final PolarionWorkItemsApi workItemsApi;
    private final PolarionUsersApi usersApi;

    public PolarionApi(String baseUrl, String bearerToken) {
        this.client = new PolarionClient(baseUrl, bearerToken);
        this.projectsApi = new PolarionProjectsApi(client);
        this.workItemsApi = new PolarionWorkItemsApi(client);
        this.usersApi = new PolarionUsersApi(client);
    }

    public PolarionApi(String bearerToken) {
        this(PolarionClient.DEFAULT_BASE_URL, bearerToken);
    }

    public PolarionClient getClient() {
        return client;
    }

    public PolarionProjectsApi projects() {
        return projectsApi;
    }

    public PolarionWorkItemsApi workItems() {
        return workItemsApi;
    }

    public PolarionUsersApi users() {
        return usersApi;
    }
}
