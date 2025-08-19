package com.nutriplannerapp.nutri_planner_api.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true) // Ignora campos del JSON que no tengamos en la clase
public class OffNutrimentsDTO {

    @JsonProperty("proteins_100g")
    private double proteins;

    @JsonProperty("carbohydrates_100g")
    private double carbs;

    @JsonProperty("fat_100g")
    private double fats;

    @JsonProperty("energy-kcal_100g")
    private double calories;
}