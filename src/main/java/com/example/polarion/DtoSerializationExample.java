package com.example.polarion;

import com.example.polarion.dto.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

/**
 * Serialize and deserialize Polarion-style JSON to/from Java DTOs (no API key needed).
 * Use these same DTOs when calling the API: GET -> deserialize into ApiListResponse / ApiSingleResponse -> navigate with getters.
 */
public class DtoSerializationExample {

    public static void main(String[] args) throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        // Example JSON (simplified JSON:API-style list response)
        String json = """
            {
              "data": [
                {
                  "id": "project-1",
                  "type": "Project",
                  "attributes": {
                    "name": "My Project",
                    "description": "Example",
                    "lead": "admin"
                  }
                }
              ],
              "links": { "self": "https://polarion/rest/v1/projects" }
            }
            """;

        // Deserialize: JSON -> Java objects
        ApiListResponse<ProjectAttributes> response = mapper.readValue(json,
                new TypeReference<ApiListResponse<ProjectAttributes>>() {});

        List<Resource<ProjectAttributes>> data = response.getData();
        for (Resource<ProjectAttributes> resource : data) {
            System.out.println("id=" + resource.getId() + " type=" + resource.getType());
            ProjectAttributes attrs = resource.getAttributes();
            if (attrs != null) {
                System.out.println("  name=" + attrs.getName());
                System.out.println("  description=" + attrs.getDescription());
                System.out.println("  lead=" + attrs.getLead());
            }
        }
        if (response.getLinks() != null) {
            System.out.println("links.self=" + response.getLinks().getSelf());
        }

        // Serialize: Java objects -> JSON
        String backToJson = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(response);
        System.out.println("Re-serialized: " + backToJson.substring(0, Math.min(200, backToJson.length())) + "...");
    }
}
