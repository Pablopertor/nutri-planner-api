package com.nutriplannerapp.nutri_planner_api.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class OffProductDTO {

    @JsonProperty("product_name")
    private String name;

    @JsonProperty("categories_tags")
    private List<String> categoryTags;

    @JsonProperty("nutriments")
    private OffNutrimentsDTO nutriments;
}