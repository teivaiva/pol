package com.example.polarion;

import com.example.polarion.dto.WorkItemAttributes;
import com.example.polarion.dto.ApiSingleResponse;
import com.example.polarion.dto.Resource;

/**
 * Service layer for Polarion operations providing simplified access to work items.
 * This service wraps the lower-level API client and provides business-focused methods.
 */
public class PolarionService {
    
    private final PolarionApi polarionApi;
    private final String defaultProjectId;
    
    /**
     * Creates a PolarionService with the specified API client and default project.
     * 
     * @param polarionApi the Polarion API client
     * @param defaultProjectId the default project ID to use for operations (e.g., "CABLE")
     */
    public PolarionService(PolarionApi polarionApi, String defaultProjectId) {
        this.polarionApi = polarionApi;
        this.defaultProjectId = defaultProjectId;
    }
    
    /**
     * Creates a PolarionService with the specified API client using "CABLE" as default project.
     * 
     * @param polarionApi the Polarion API client
     */
    public PolarionService(PolarionApi polarionApi) {
        this(polarionApi, "CABLE");
    }
    
    /**
     * Retrieves a work item by its ID using the default project.
     * 
     * @param workItemId the work item ID (e.g., "CBL-4297")
     * @return the work item attributes, or null if not found
     * @throws Exception if an API error occurs
     */
    public WorkItemAttributes getWorkItem(String workItemId) throws Exception {
        return getWorkItem(defaultProjectId, workItemId);
    }
    
    /**
     * Retrieves a work item by its ID and project ID.
     * 
     * @param projectId the project ID (e.g., "CABLE")
     * @param workItemId the work item ID (e.g., "CBL-4297")
     * @return the work item attributes, or null if not found
     * @throws Exception if an API error occurs
     */
    public WorkItemAttributes getWorkItem(String projectId, String workItemId) throws Exception {
        ApiSingleResponse<WorkItemAttributes> response = polarionApi.workItems().getWorkItemAsDto(projectId, workItemId);
        Resource<WorkItemAttributes> resource = response.getData();
        return resource != null ? resource.getAttributes() : null;
    }
    
    /**
     * Gets the default project ID configured for this service.
     * 
     * @return the default project ID
     */
    public String getDefaultProjectId() {
        return defaultProjectId;
    }
    
    /**
     * Gets the Polarion API client used by this service.
     * 
     * @return the Polarion API client
     */
    public PolarionApi getPolarionApi() {
        return polarionApi;
    }
}
