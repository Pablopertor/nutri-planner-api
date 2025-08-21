package com.nutriplannerapp.nutri_planner_api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MacroTargets {
    private double proteins;
    private double fats;
    private double carbs;
}