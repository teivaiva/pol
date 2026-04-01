package com.example.polarion;

import com.example.polarion.dto.*;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

/**
 * Example: use your API key via environment variable (never paste it in chat).
 * - GET projects, work items, users
 * - Deserialize JSON into Java DTOs
 * - Navigate with getters
 * - Serialize back to JSON
 *
 * Run: set POLARION_TOKEN=your-api-key && mvn exec:java
 * Or:  mvn exec:java -Dexec.args="your-api-key"
 */
public class Main {

    public static void main(String[] args) {
        String token = args.length > 0 ? args[0] : System.getenv("POLARION_TOKEN");
        if (token == null || token.isEmpty()) {
            System.err.println("Provide Bearer token: set POLARION_TOKEN=your-key or pass as first argument.");
            System.err.println("Do not paste your API key in chat or in source code.");
            return;
        }

        PolarionApi api = new PolarionApi(token);
        ObjectMapper mapper = api.getClient().getObjectMapper();

        try {
            // ---------- Projects (GET list -> DTO -> getters -> JSON) ----------
//            ApiListResponse<ProjectAttributes> projectsResponse = api.projects().getProjectsAsDto();
//            System.out.println("=== Projects (DTO) ===");
//            if (projectsResponse.getData() != null) {
//                for (Resource<ProjectAttributes> resource : projectsResponse.getData()) {
//                    String id = resource.getId();
//                    String type = resource.getType();
//                    ProjectAttributes attrs = resource.getAttributes();
//                    System.out.println("  id=" + id + " type=" + type);
//                    if (attrs != null) {
//                        System.out.println("    name=" + attrs.getName() + " description=" + attrs.getDescription());
//                        System.out.println("    lead=" + attrs.getLead() + " created=" + attrs.getCreated());
//                    }
//                }
//                // Serialize back to JSON
//                String jsonAgain = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(projectsResponse);
//                System.out.println("Re-serialized (first 500 chars): " + jsonAgain.substring(0, Math.min(500, jsonAgain.length())) + "...");
//            } else {
//                System.out.println("  (no data array - API may use different structure; check raw response)");
//            }

            // ---------- Users (GET list -> DTO -> getters) ----------
//            ApiListResponse<UserAttributes> usersResponse = api.users().getUsersAsDto();
//            System.out.println("\n=== Users (DTO) ===");
//            if (usersResponse.getData() != null) {
//                for (Resource<UserAttributes> resource : usersResponse.getData()) {
//                    UserAttributes u = resource.getAttributes();
//                    if (u != null) {
//                        System.out.println("  " + resource.getId() + " userName=" + u.getUserName() + " email=" + u.getEmail());
//                    }
//                }
//            }

            // ---------- Single project (if you have a project id) ----------
             ApiSingleResponse<ProjectAttributes> one = api.projects().getProjectAsDto("CABLE");
             Resource<ProjectAttributes> data = one.getData();
             if (data != null && data.getAttributes() != null) {
                 System.out.println("Project: " + data.getAttributes().getName());
             }

            // ---------- Work items for a project (replace PROJECT_ID) ----------
            // ApiListResponse<WorkItemAttributes> wis = api.workItems().getWorkItemsAsDto("PROJECT_ID", 10, 1, null, null, null);
            // if (wis.getData() != null) {
            //     for (Resource<WorkItemAttributes> r : wis.getData()) {
            //         WorkItemAttributes a = r.getAttributes();
            //         if (a != null) System.out.println(r.getId() + " " + a.getTitle() + " " + a.getStatus());
            //     }
            // }

        } catch (PolarionClient.PolarionApiException e) {
            System.err.println("API error " + e.getStatusCode() + ": " + e.getMessage());
            String body = e.getResponseBody();
            if (body != null && !body.isEmpty()) {
                try {
                    ApiErrorsResponse errBody = mapper.readValue(body, ApiErrorsResponse.class);
                    if (errBody.getErrors() != null) {
                        for (ApiError err : errBody.getErrors()) {
                            System.err.println("  " + err.getTitle() + " - " + err.getDetail());
                        }
                    }
                } catch (Exception ignored) { }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
