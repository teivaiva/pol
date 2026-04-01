package com.example.polarion.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

/**
 * JSON:API list response: data array, links, meta.
 * Deserialize GET list responses (e.g. projects, work items, users) into this.
 *
 * <pre>
 * ApiListResponse&lt;ProjectAttributes&gt; resp = objectMapper.readValue(json,
 *     objectMapper.getTypeFactory().constructParametricType(ApiListResponse.class, ProjectAttributes.class));
 * List&lt;Resource&lt;ProjectAttributes&gt;&gt; items = resp.getData();
 * </pre>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApiListResponse<T> {

    @JsonProperty("data")
    private List<Resource<T>> data;

    @JsonProperty("links")
    private ApiLinks links;

    @JsonProperty("meta")
    private Map<String, Object> meta;

    @JsonProperty("included")
    private List<Object> included;

    public List<Resource<T>> getData() {
        return data;
    }

    public void setData(List<Resource<T>> data) {
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

    public List<Object> getIncluded() {
        return included;
    }

    public void setIncluded(List<Object> included) {
        this.included = included;
    }
}
