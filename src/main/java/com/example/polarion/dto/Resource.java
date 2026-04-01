package com.example.polarion.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * JSON:API resource object: id, type, attributes, relationships, links.
 * Use generic getData() returning List&lt;Resource&gt; or Resource, then getAttributes()
 * and cast or use typed subclasses (ProjectResource, WorkItemResource, etc.).
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Resource<T> {

    private String id;
    private String type;
    private T attributes;
    private Object relationships;
    private ApiLinks links;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public T getAttributes() {
        return attributes;
    }

    public void setAttributes(T attributes) {
        this.attributes = attributes;
    }

    public Object getRelationships() {
        return relationships;
    }

    public void setRelationships(Object relationships) {
        this.relationships = relationships;
    }

    public ApiLinks getLinks() {
        return links;
    }

    public void setLinks(ApiLinks links) {
        this.links = links;
    }
}
