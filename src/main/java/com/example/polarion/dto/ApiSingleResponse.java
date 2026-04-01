package com.example.polarion.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

/**
 * JSON:API single resource response: data object, links, meta.
 * Deserialize GET single-resource responses (e.g. one project, one work item) into this.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApiSingleResponse<T> {

    @JsonProperty("data")
    private Resource<T> data;

    @JsonProperty("links")
    private ApiLinks links;

    @JsonProperty("meta")
    private Map<String, Object> meta;

    @JsonProperty("included")
    private Object included;

    public Resource<T> getData() {
        return data;
    }

    public void setData(Resource<T> data) {
        this.data = data;
    }

    public ApiLinks getLinks() {
        return links;
    }

    public void setLinks(ApiLinks links) {
        this.links = links;
    }

    public Map<String, Object> getMeta() {
        return meta;
    }

    public void setMeta(Map<String, Object> meta) {
        this.meta = meta;
    }

    public Object getIncluded() {
        return included;
    }

    public void setIncluded(Object included) {
        this.included = included;
    }
}
