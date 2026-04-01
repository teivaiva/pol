package com.example.polarion.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Polarion API error response body (4xx/5xx). Use for deserializing error JSON.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApiErrorsResponse {

    @JsonProperty("errors")
    private List<ApiError> errors;

    public List<ApiError> getErrors() {
        return errors;
    }

    public void setErrors(List<ApiError> errors) {
        this.errors = errors;
    }
}
