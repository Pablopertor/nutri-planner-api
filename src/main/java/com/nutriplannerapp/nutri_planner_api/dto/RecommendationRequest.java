package com.nutriplannerapp.nutri_planner_api.dto;

import lombok.Data;

@Data
public class RecommendationRequest {

    private double userWeight; // Peso del usuario en kg
    private double proteinPerKg; // Gramos de proteína por kg de peso
    private int totalCalories; // Kcal totales deseadas
    private int fatPercentage; // % de grasas sobre las calorías restantes
    private int carbsPercentage; // % de carbohidratos sobre las calorías restantes

}