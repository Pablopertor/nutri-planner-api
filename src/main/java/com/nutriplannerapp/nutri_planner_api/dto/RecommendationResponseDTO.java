package com.nutriplannerapp.nutri_planner_api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;

@Data
@AllArgsConstructor
public class RecommendationResponseDTO {
    private List<RecommendedFoodDTO> recommendedFoods;
    private MacroSummaryDTO summary;
}