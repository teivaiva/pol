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
            // ---------- Test simple API root first ----------
//            System.out.println("Testing API root endpoint...");
//            try {
//                String rootResponse = api.getClient().get("/");
//                System.out.println("API root response (first 200 chars): " + rootResponse.substring(0, Math.min(200, rootResponse.length())) + "...");
//            } catch (Exception e) {
//                System.err.println("Failed to get API root: " + e.getMessage());
//            }
            
            // ---------- Projects list (commented out - user doesn't have permission) ----------
            // ApiListResponse<ProjectAttributes> projectsResponse = api.projects().getProjectsAsDto();
            // System.out.println("\n=== Projects (DTO) ===");
            // if (projectsResponse.getData() != null) {
            //     for (Resource<ProjectAttributes> resource : projectsResponse.getData()) {
            //         String id = resource.getId();
            //         String type = resource.getType();
            //         ProjectAttributes attrs = resource.getAttributes();
            //         System.out.println("  id=" + id + " type=" + type);
            //         if (attrs != null) {
            //             System.out.println("    name=" + attrs.getName() + " description=" + attrs.getDescription());
            //             System.out.println("    lead=" + attrs.getLead() + " created=" + attrs.getCreated());
            //         }
            //     }
            //     // Serialize back to JSON
            //     String jsonAgain = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(projectsResponse);
            //     System.out.println("Re-serialized (first 500 chars): " + jsonAgain.substring(0, Math.min(500, jsonAgain.length())) + "...");
            // } else {
            //     System.out.println("  (no data array - API may use different structure; check raw response)");
            // }
            
            // ---------- Single project CABLE (raw JSON first) ----------
            System.out.println("\n=== Testing access to project CABLE ===");
            try {
                // First get raw JSON response
                System.out.println("Getting raw JSON response for /projects/CABLE...");
                String rawResponse = api.getClient().get("/projects/CABLE");
                System.out.println("Raw response (first 1000 chars):\n" + rawResponse.substring(0, Math.min(1000, rawResponse.length())) + (rawResponse.length() > 1000 ? "..." : ""));
                
                // Then try DTO deserialization
                System.out.println("\nTrying DTO deserialization...");
                ApiSingleResponse<ProjectAttributes> one = api.projects().getProjectAsDto("CABLE");
                Resource<ProjectAttributes> data = one.getData();
                if (data != null && data.getAttributes() != null) {
                    System.out.println("Project CABLE found!");
                    System.out.println("  Name: " + data.getAttributes().getName());
                    System.out.println("  Description: " + (data.getAttributes().getDescription() != null ? data.getAttributes().getDescription().getValue() : "null"));
                    System.out.println("  Lead: " + data.getAttributes().getLead());
                    System.out.println("  Created: " + data.getAttributes().getCreated());
                } else {
                    System.out.println("Project CABLE not found or no attributes returned");
                }
            } catch (PolarionClient.PolarionApiException e) {
                System.err.println("API error accessing project CABLE: " + e.getStatusCode() + ": " + e.getMessage());
                String body = e.getResponseBody();
                if (body != null && !body.isEmpty()) {
                    System.err.println("Response body: " + body);
                }
            } catch (Exception e) {
                System.err.println("General error accessing project CABLE: " + e.getMessage());
                e.printStackTrace();
            }

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


            // ---------- Work items for a project ----------
            System.out.println("\n=== Testing work items retrieval for project CABLE ===");
            try {
                // First get raw JSON response to understand structure
                System.out.println("\nGetting raw JSON response for work items...");
                String rawWorkItems = api.getClient().get("/projects/CABLE/workitems");
                System.out.println("Raw work items response (first 1000 chars):\n" + rawWorkItems.substring(0, Math.min(1000, rawWorkItems.length())) + (rawWorkItems.length() > 1000 ? "..." : ""));
                
                // Fetch a single work item to see full attribute structure
                System.out.println("\nFetching single work item CBL-4297 for full attributes...");
                String singleWorkItem = api.getClient().get("/projects/CABLE/workitems/CBL-4297");
                System.out.println("Single work item response (first 1500 chars):\n" + singleWorkItem.substring(0, Math.min(1500, singleWorkItem.length())) + (singleWorkItem.length() > 1500 ? "..." : ""));
                ApiListResponse<WorkItemAttributes> wis = api.workItems().getWorkItemsAsDto("CABLE", 10, 1, null, null, null);
                
                if (wis.getData() != null) {
                    System.out.println("Found " + wis.getData().size() + " work items:");
                    int count = 0;
                    for (Resource<WorkItemAttributes> r : wis.getData()) {
                        count++;
                        System.out.println("\n  Work item #" + count + ":");
                        System.out.println("    ID: " + r.getId());
                        System.out.println("    Type: " + r.getType());
                        WorkItemAttributes a = r.getAttributes();
                        if (a != null) {
                            System.out.println("    Title: " + a.getTitle());
                            System.out.println("    Status: " + a.getStatus());
                            System.out.println("    Work item type: " + a.getType());
                            System.out.println("    Description: " + (a.getDescription() != null ? a.getDescription().getValue() : "null"));
                        } else {
                            System.out.println("    Attributes: null (deserialization may have failed)");
                        }
                    }
                } else {
                    System.out.println("No work items data returned");
                }
            } catch (PolarionClient.PolarionApiException e) {
                System.err.println("API error getting work items: " + e.getStatusCode() + ": " + e.getMessage());
                String body = e.getResponseBody();
                if (body != null && !body.isEmpty()) {
                    System.err.println("Response body: " + body);
                }
            } catch (Exception e) {
                System.err.println("General error getting work items: " + e.getMessage());
                e.printStackTrace();
            }

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
