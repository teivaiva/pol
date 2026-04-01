# Polarion REST API – Java Client

Generated from the [Polarion REST API](https://polarion.rcs-hitachirail.com/polarion/rest/v1) **Swagger/OpenAPI 3.0** definition. This client lets you call the API from Java with Bearer token authentication, **deserialize GET responses into Java DTOs** (Jackson), and navigate with getters. Never paste your API key in chat—use the `POLARION_TOKEN` environment variable or pass it at runtime.

## API reference

- **Swagger UI:** [https://polarion.rcs-hitachirail.com/polarion/rest/v1](https://polarion.rcs-hitachirail.com/polarion/rest/v1)
- **OpenAPI definition:** [https://polarion.rcs-hitachirail.com/polarion/rest/v1/definition](https://polarion.rcs-hitachirail.com/polarion/rest/v1/definition)

The API uses **Bearer token** authentication and covers Projects, Work Items, Users, Documents, Test Runs, Plans, Collections, and more.

## Requirements

- Java 17+
- Maven

## Build & run

```bash
cd PolarionApiClient
mvn compile
```

Run the example using your API key **from the environment** (do not paste the key in chat or in code):

```bash
set POLARION_TOKEN=your-api-key
mvn exec:java
# or
mvn exec:java -Dexec.args="your-api-key"
```

## Usage

### 1. Create client and API facade

```java
String token = "your-bearer-token";
PolarionApi api = new PolarionApi(token);

// Or with custom base URL
PolarionApi api = new PolarionApi("https://polarion.rcs-hitachirail.com/polarion/rest/v1", token);
```

### 2. Projects

```java
// List all projects
JsonNode projects = api.projects().getProjects();

// Get one project
JsonNode project = api.projects().getProject("my-project-id");

// List with paging and query
JsonNode page = api.projects().getProjects(20, 1, "query", "sort");
```

### 3. Work items

```java
// List work items in a project
JsonNode items = api.workItems().getWorkItems("my-project-id");

// With paging and Polarion query
JsonNode filtered = api.workItems().getWorkItems(
    "my-project-id",
    10,      // page size
    1,       // page number
    "type:task",  // query
    "created",    // sort
    null          // include
);

// Get single work item
JsonNode wi = api.workItems().getWorkItem("my-project-id", "task-123");

// Comments and linked items
JsonNode comments = api.workItems().getWorkItemComments("my-project-id", "task-123");
JsonNode linked = api.workItems().getLinkedWorkItems("my-project-id", "task-123");
```

### 4. Users

```java
JsonNode users = api.users().getUsers();
JsonNode user = api.users().getUser("john.doe");
```

### 5. GET as typed DTOs (serialize/deserialize, navigate with getters)

Use the `*AsDto()` methods to get Jackson-friendly Java objects. You can then navigate with getters and serialize back to JSON:

```java
// List response
ApiListResponse<ProjectAttributes> resp = api.projects().getProjectsAsDto();
for (Resource<ProjectAttributes> resource : resp.getData()) {
    String id = resource.getId();
    ProjectAttributes attrs = resource.getAttributes();
    if (attrs != null) {
        String name = attrs.getName();
        String lead = attrs.getLead();
    }
}
ApiLinks links = resp.getLinks();

// Single resource
ApiSingleResponse<WorkItemAttributes> one = api.workItems().getWorkItemAsDto("projectId", "workItemId");
Resource<WorkItemAttributes> data = one.getData();
if (data != null && data.getAttributes() != null) {
    String title = data.getAttributes().getTitle();
    String status = data.getAttributes().getStatus();
}

// Serialize back to JSON
ObjectMapper mapper = api.getClient().getObjectMapper();
String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(resp);
```

Available DTOs: `ApiListResponse<T>`, `ApiSingleResponse<T>`, `Resource<T>`, `ApiLinks`, `ApiError`, `ApiErrorsResponse`; attribute types: `ProjectAttributes`, `WorkItemAttributes`, `UserAttributes`, `CollectionAttributes`, `DocumentAttributes`, `TestRunAttributes`. All have getters/setters and `@JsonIgnoreProperties(ignoreUnknown = true)` so extra API fields do not break deserialization.

### 6. Extract data from JSON (raw)

Responses can also be used as `JsonNode` for ad-hoc access:

```java
JsonNode projects = api.projects().getProjects();
JsonNode data = projects.get("data");
if (data != null && data.isArray()) {
    for (JsonNode item : data) {
        String id = item.get("id").asText();
        JsonNode attrs = item.has("attributes") ? item.get("attributes") : item;
        // ...
    }
}
```

### 7. Low-level client

For endpoints not wrapped by the facade (e.g. Documents, Test Runs, Plans), use the raw client:

```java
PolarionClient client = api.getClient();
JsonNode doc = client.getAsJson("/projects/myproject/spaces/_default/documents/mydoc");
String raw = client.get("/projects/myproject/testruns");
```

### 8. Error handling

Non-2xx responses throw `PolarionClient.PolarionApiException`:

```java
try {
    api.projects().getProject("missing");
} catch (PolarionClient.PolarionApiException e) {
    System.err.println("HTTP " + e.getStatusCode() + ": " + e.getMessage());
}
```

## Extending the client

The OpenAPI spec defines many more paths (Documents, Document Attachments, Test Runs, Test Records, Plans, Collections, Enumerations, Icons, etc.). You can:

1. Add new methods in `PolarionClient` (get/patch/post/delete) and call them with the path from the [definition](https://polarion.rcs-hitachirail.com/polarion/rest/v1/definition).
2. Add new API classes (e.g. `PolarionTestRunsApi`, `PolarionDocumentsApi`) following the same pattern as `PolarionProjectsApi` and `PolarionWorkItemsApi`.

## License

The Polarion REST API is documented under [Apache 2.0](http://www.apache.org/licenses/LICENSE-2.0.html). This client code is for integration use with your Polarion instance.
