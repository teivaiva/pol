package com.example.polarion.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Description object from Polarion API.
 * In the API response, description is an object with 'type' and 'value' fields.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Description {
    
    private String type;
    private String value;
    
    public Description() {
    }
    
    public Description(String type, String value) {
        this.type = type;
        this.value = value;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public String getValue() {
        return value;
    }
    
    public void setValue(String value) {
        this.value = value;
    }
    
    @Override
    public String toString() {
        return value != null ? value : "";
    }
}