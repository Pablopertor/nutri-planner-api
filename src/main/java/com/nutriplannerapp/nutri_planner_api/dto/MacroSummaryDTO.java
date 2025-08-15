package com.nutriplannerapp.nutri_planner_api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MacroSummaryDTO {
    private double totalCalories;
    private double totalProteins;
    private double totalCarbs;
    private double totalFats;
}