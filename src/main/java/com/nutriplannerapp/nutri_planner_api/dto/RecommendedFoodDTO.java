package com.nutriplannerapp.nutri_planner_api.dto;

import com.nutriplannerapp.nutri_planner_api.model.Food;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RecommendedFoodDTO {

    private Food foodDetails;
    private double quantityInGrams;

}