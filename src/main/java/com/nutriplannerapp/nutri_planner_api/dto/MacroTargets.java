package com.nutriplannerapp.nutri_planner_api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MacroTargets {
    private double proteins;
    private double fats;
    private double carbs;
}