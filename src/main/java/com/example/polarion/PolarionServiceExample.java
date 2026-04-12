package com.example.polarion;

import com.example.polarion.dto.WorkItemAttributes;

/**
 * Example demonstrating usage of PolarionService.
 * This class shows how to use the simplified service interface
 * to retrieve work items from Polarion.
 */
public class PolarionServiceExample {
    
    public static void main(String[] args) {
        // Get API token from environment or command line
        String token = args.length > 0 ? args[0] : System.getenv("POLARION_TOKEN");
        if (token == null || token.isEmpty()) {
            System.err.println("Provide Bearer token: set POLARION_TOKEN=your-key or pass as first argument.");
            System.err.println("Do not paste your API key in chat or in source code.");
            return;
        }
        
        // Create Polarion API client
        PolarionApi api = new PolarionApi(token);
        
        // Create PolarionService with default project "CABLE"
        PolarionService service = new PolarionService(api);
        
        System.out.println("PolarionService Example");
        System.out.println("Default project: " + service.getDefaultProjectId());
        System.out.println();
        
        // Example work item IDs to fetch
        String[] exampleWorkItemIds = {"CBL-4297", "CBL-1", "CBL-1000"};
        
        for (String workItemId : exampleWorkItemIds) {
            System.out.println("Fetching work item: " + workItemId);
            try {
                WorkItemAttributes workItem = service.getWorkItem(workItemId);
                
                if (workItem != null) {
                    System.out.println("  Title: " + workItem.getTitle());
                    System.out.println("  Status: " + workItem.getStatus());
                    System.out.println("  Type: " + workItem.getType());
                    System.out.println("  Created: " + workItem.getCreated());
                    System.out.println("  Updated: " + workItem.getUpdated());
                    
                    if (workItem.getDescription() != null) {
                        String desc = workItem.getDescription().getValue();
                        System.out.println("  Description (first 100 chars): " + 
                            (desc != null && desc.length() > 100 ? desc.substring(0, 100) + "..." : desc));
                    }
                } else {
                    System.out.println("  Work item not found");
                }
            } catch (Exception e) {
                System.err.println("  Error fetching work item: " + e.getMessage());
                if (e instanceof PolarionClient.PolarionApiException) {
                    PolarionClient.PolarionApiException apiEx = (PolarionClient.PolarionApiException) e;
                    System.err.println("    Status code: " + apiEx.getStatusCode());
                }
            }
            System.out.println();
        }
        
        System.out.println("Example completed.");
    }
}
