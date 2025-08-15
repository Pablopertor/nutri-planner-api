package com.nutriplannerapp.nutri_planner_api.service;

import com.nutriplannerapp.nutri_planner_api.dto.*;
import com.nutriplannerapp.nutri_planner_api.model.Food;
import com.nutriplannerapp.nutri_planner_api.repository.FoodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FoodService {

    // --- CONSTANTES ---
    private static final int CALORIES_PER_PROTEIN_GRAM = 4;
    private static final int CALORIES_PER_FAT_GRAM = 9;
    private static final int CALORIES_PER_CARB_GRAM = 4;

    @Autowired
    private FoodRepository foodRepository;

    // =================================================================
    // --- MÉTODOS PÚBLICOS (API) ---
    // =================================================================

    /**
     * Orquesta el proceso de generación de una recomendación de 3 comidas.
     */
    public RecommendationResponseDTO generateRecommendation(RecommendationRequest request) {
        // 1. Calcular los objetivos de macronutrientes
        MacroTargets targets = calculateMacroTargets(request);

        // 2. Encontrar alimentos y calcular sus cantidades
        List<RecommendedFoodDTO> recommendationList = findAndAllocateFoods(targets);
        // 3. Hacer resumen de los resultados
        MacroSummaryDTO summary = calculateSummary(recommendationList);

        // Devolvemos el objeto de respuesta final que contiene la lista y el resumen
        return new RecommendationResponseDTO(recommendationList, summary);
    }

    /**
     * Devuelve todos los alimentos de la base de datos.
     */
    public List<Food> getAll() {
        return foodRepository.findAll();
    }

    // --- MÉTODOS PRIVADOS (LÓGICA INTERNA) ---
    /**
     * Calcula los gramos objetivo para cada macronutriente a partir de la petición del usuario.
     */
    private MacroTargets calculateMacroTargets(RecommendationRequest request) {
        double totalProteinGrams = request.getUserWeight() * request.getProteinPerKg();
        double proteinCalories = totalProteinGrams * CALORIES_PER_PROTEIN_GRAM;
        double remainingCalories = request.getTotalCalories() - proteinCalories;
        double totalFatGrams = (remainingCalories * (request.getFatPercentage() / 100.0)) / CALORIES_PER_FAT_GRAM;
        double totalCarbsGrams = (remainingCalories * (request.getCarbsPercentage() / 100.0)) / CALORIES_PER_CARB_GRAM;

        System.out.println(String.format("OBJETIVO CALCULADO -> Proteína: %.2fg, Grasas: %.2fg, Carbs: %.2fg", totalProteinGrams, totalFatGrams, totalCarbsGrams));

        return new MacroTargets(totalProteinGrams, totalFatGrams, totalCarbsGrams);
    }

    /**
     * Implementa el algoritmo secuencial para seleccionar 3 alimentos y calcular sus cantidades.
     */
    private List<RecommendedFoodDTO> findAndAllocateFoods(MacroTargets targets) {
        List<Food> allFoods = foodRepository.findAll();
        List<RecommendedFoodDTO> recommendation = new java.util.ArrayList<>();

        double remainingProteins = targets.getProteins();
        double remainingFats = targets.getFats();
        double remainingCarbs = targets.getCarbs();

        // 1. Seleccionar fuente de Proteína
        Food proteinSource = findBestFoodForMacro(allFoods, "protein");
        if (proteinSource != null) {
            double quantity = (remainingProteins * 0.8) * 100 / proteinSource.getProteins();
            recommendation.add(new RecommendedFoodDTO(proteinSource, Math.round(quantity)));

            remainingProteins -= (proteinSource.getProteins() / 100) * quantity;
            remainingFats -= (proteinSource.getFats() / 100) * quantity;
            remainingCarbs -= (proteinSource.getCarbs() / 100) * quantity;
            allFoods.remove(proteinSource);
        }

        // 2. Seleccionar fuente de Carbohidratos
        Food carbSource = findBestFoodForMacro(allFoods, "carbs");
        if (carbSource != null && remainingCarbs > 0) {
            double quantity = (remainingCarbs * 0.8) * 100 / carbSource.getCarbs();
            recommendation.add(new RecommendedFoodDTO(carbSource, Math.round(quantity)));

            remainingProteins -= (carbSource.getProteins() / 100) * quantity;
            remainingFats -= (carbSource.getFats() / 100) * quantity;
            remainingCarbs -= (carbSource.getCarbs() / 100) * quantity;
            allFoods.remove(carbSource);
        }

        // 3. Seleccionar "Relleno" de Grasas
        Food fatSource = findBestFoodForMacro(allFoods, "fat");
        if (fatSource != null && remainingFats > 0) {
            double quantity = remainingFats * 100 / fatSource.getFats();
            recommendation.add(new RecommendedFoodDTO(fatSource, Math.round(quantity)));
        }

        return recommendation;
    }

    /**
     * Busca en una lista el alimento con el valor más alto del macronutriente especificado.
     */
    private Food findBestFoodForMacro(List<Food> foods, String macro) {
        Food bestFood = null;
        double maxValue = -1;

        for (Food food : foods) {
            switch (macro) {
                case "protein":
                    if (food.getProteins() > maxValue) {
                        maxValue = food.getProteins();
                        bestFood = food;
                    }
                    break;
                case "carbs":
                    if (food.getCarbs() > maxValue) {
                        maxValue = food.getCarbs();
                        bestFood = food;
                    }
                    break;
                case "fat":
                    if (food.getFats() > maxValue) {
                        maxValue = food.getFats();
                        bestFood = food;
                    }
                    break;
            }
        }
        return bestFood;
    }


private MacroSummaryDTO calculateSummary(List<RecommendedFoodDTO> recommendationList) {
    double finalCalories = 0;
    double finalProteins = 0;
    double finalCarbs = 0;
    double finalFats = 0;

    for (RecommendedFoodDTO item : recommendationList) {
        double quantity = item.getQuantityInGrams();
        Food food = item.getFoodDetails();

        finalCalories += (food.getCalories() / 100) * quantity;
        finalProteins += (food.getProteins() / 100) * quantity;
        finalCarbs += (food.getCarbs() / 100) * quantity;
        finalFats += (food.getFats() / 100) * quantity;
    }

    return new MacroSummaryDTO(
            Math.round(finalCalories),
            Math.round(finalProteins),
            Math.round(finalCarbs),
            Math.round(finalFats)
    );
    }
}

