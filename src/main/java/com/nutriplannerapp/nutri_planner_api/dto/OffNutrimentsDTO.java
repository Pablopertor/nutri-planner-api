package com.nutriplannerapp.nutri_planner_api.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class OffNutrimentsDTO {

    @JsonProperty("proteins_100g")
    private Double proteins;

    @JsonProperty("carbohydrates_100g")
    private Double carbs;

    @JsonProperty("fat_100g")
    private Double fats;

    @JsonProperty("energy-kcal_100g")
    private Double calories;
}