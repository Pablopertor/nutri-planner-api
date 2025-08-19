package com.nutriplannerapp.nutri_planner_api.service;

import com.nutriplannerapp.nutri_planner_api.client.OpenFoodFactsClient;
import com.nutriplannerapp.nutri_planner_api.dto.*;
import com.nutriplannerapp.nutri_planner_api.model.Food;
import com.nutriplannerapp.nutri_planner_api.repository.FoodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class FoodService {

    // --- CONSTANTES ---
    private static final int CALORIES_PER_PROTEIN_GRAM = 4;
    private static final int CALORIES_PER_FAT_GRAM = 9;
    private static final int CALORIES_PER_CARB_GRAM = 4;

    @Autowired
    private FoodRepository foodRepository;

    @Autowired
    private OpenFoodFactsClient openFoodFactsClient;

    // =================================================================
    // --- MÉTODOS PÚBLICOS (API) ---
    // =================================================================

    /**
     * Orquesta el proceso de generación de una recomendación de 3 comidas.
     */
//    public RecommendationResponseDTO generateRecommendation(RecommendationRequest request) {
//        // 1. Calcular los objetivos DIARIOS totales
//        MacroTargets dailyTargets = calculateMacroTargets(request);
//
//        // 2. Calcular los objetivos POR COMIDA (total / 3)
//        MacroTargets mealTargets = new MacroTargets(
//                dailyTargets.getProteins() / 3,
//                dailyTargets.getFats() / 3,
//                dailyTargets.getCarbs() / 3
//        );
//        System.out.println(String.format("OBJETIVO POR COMIDA -> Proteína: %.2fg, Grasas: %.2fg, Carbs: %.2fg",
//                mealTargets.getProteins(), mealTargets.getFats(), mealTargets.getCarbs()));
//
//        // 3. Buscar los 3 MEJORES candidatos para cada macronutriente
//        List<Food> allFoods = foodRepository.findAll();
//        List<Food> top3ProteinSources = findTopNFoodsForMacro(new ArrayList<>(allFoods), "protein", 3);
//        List<Food> top3CarbSources = findTopNFoodsForMacro(new ArrayList<>(allFoods), "carbs", 3);
//        List<Food> top3FatSources = findTopNFoodsForMacro(new ArrayList<>(allFoods), "fat", 3);
//
//        // 4. Añadimos aleatoriedad desordenando las listas de candidatos
//        Collections.shuffle(top3ProteinSources);
//        Collections.shuffle(top3CarbSources);
//        Collections.shuffle(top3FatSources);
//
//        // 5. Construir las 3 comidas
//        List<RecommendedFoodDTO> finalRecommendation = new ArrayList<>();
//        if (top3ProteinSources.size() >= 3 && top3CarbSources.size() >= 3 && top3FatSources.size() >= 3) {
//            finalRecommendation.addAll(buildMeal(mealTargets, top3ProteinSources.get(0), top3CarbSources.get(0), top3FatSources.get(0)));
//            finalRecommendation.addAll(buildMeal(mealTargets, top3ProteinSources.get(1), top3CarbSources.get(1), top3FatSources.get(1)));
//            finalRecommendation.addAll(buildMeal(mealTargets, top3ProteinSources.get(2), top3CarbSources.get(2), top3FatSources.get(2)));
//        }
//
//        // 6. Calcular el resumen final y devolver la respuesta
//        MacroSummaryDTO summary = calculateSummary(finalRecommendation);
//        return new RecommendationResponseDTO(finalRecommendation, summary);
//    }
    public RecommendationResponseDTO generateRecommendation(RecommendationRequest request) {

        System.out.println("--- INICIANDO PRUEBA DE CONEXIÓN API EXTERNA ---");

        // 1. Hacemos una llamada con una query
        OffResponseDTO apiResponse = openFoodFactsClient.searchFoodByQuery("pollo");

        // 2. Imprimimos la respuesta en la consola para poder verla
        System.out.println(apiResponse);

        System.out.println("--- FIN DE LA PRUEBA ---");

        // 3. Como esto es solo una prueba, devolvemos una respuesta vacía al cliente.
        // El objetivo es ver el resultado en la consola de IntelliJ, no en Postman.
        return new RecommendationResponseDTO(new ArrayList<>(), new MacroSummaryDTO(0, 0, 0, 0));
    }

    /**
     * Devuelve todos los alimentos de la base de datos.
     */
    public List<Food> getAll() {
        return foodRepository.findAll();
    }

    // =================================================================
    // --- MÉTODOS PRIVADOS (LÓGICA INTERNA) ---
    // =================================================================

    /**
     * Calcula los gramos objetivo para cada macronutriente a partir de la petición del usuario.
     */
    private MacroTargets calculateMacroTargets(RecommendationRequest request) {
        double totalProteinGrams = request.getUserWeight() * request.getProteinPerKg();
        double proteinCalories = totalProteinGrams * CALORIES_PER_PROTEIN_GRAM;
        double remainingCalories = request.getTotalCalories() - proteinCalories;
        double totalFatGrams = (remainingCalories * (request.getFatPercentage() / 100.0)) / CALORIES_PER_FAT_GRAM;
        double totalCarbsGrams = (remainingCalories * (request.getCarbsPercentage() / 100.0)) / CALORIES_PER_CARB_GRAM;

        System.out.println(String.format("OBJETIVO DIARIO CALCULADO -> Proteína: %.2fg, Grasas: %.2fg, Carbs: %.2fg", totalProteinGrams, totalFatGrams, totalCarbsGrams));

        return new MacroTargets(totalProteinGrams, totalFatGrams, totalCarbsGrams);
    }

    /**
     * Construye UNA comida a partir de los objetivos y las fuentes de macros dadas.
     */
    private List<RecommendedFoodDTO> buildMeal(MacroTargets mealTargets, Food proteinSource, Food carbSource, Food fatSource) {
        List<RecommendedFoodDTO> meal = new ArrayList<>();

        double remainingProteins = mealTargets.getProteins();
        double remainingFats = mealTargets.getFats();
        double remainingCarbs = mealTargets.getCarbs();

        // 1. Añadir Proteína (80% del objetivo de la comida)
        if (proteinSource != null && proteinSource.getProteins() > 0) {
            double quantity = (remainingProteins * 0.8) * 100 / proteinSource.getProteins();
            meal.add(new RecommendedFoodDTO(proteinSource, Math.round(quantity)));
            remainingProteins -= (proteinSource.getProteins() / 100) * quantity;
            remainingFats -= (proteinSource.getFats() / 100) * quantity;
            remainingCarbs -= (proteinSource.getCarbs() / 100) * quantity;
        }

        // 2. Añadir Carbohidratos
        if (carbSource != null && carbSource.getCarbs() > 0 && remainingCarbs > 0) {
            double quantity = remainingCarbs * 100 / carbSource.getCarbs();
            meal.add(new RecommendedFoodDTO(carbSource, Math.round(quantity)));
            remainingFats -= (carbSource.getFats() / 100) * quantity;
        }

        // 3. Añadir Grasas
        if (fatSource != null && fatSource.getFats() > 0 && remainingFats > 0) {
            double quantity = remainingFats * 100 / fatSource.getFats();
            meal.add(new RecommendedFoodDTO(fatSource, Math.round(quantity)));
        }

        return meal;
    }

    /**
     * Busca en una lista los N alimentos con el valor más alto del macronutriente especificado.
     */
    /**
     * Busca en una lista los N alimentos con la mayor "pureza" para un macronutriente específico.
     * La "pureza" se mide como el % de calorías que provienen de ese macronutriente.
     */
    private List<Food> findTopNFoodsForMacro(List<Food> foods, String macro, int n) {
        foods.sort((food1, food2) -> {
            double score1 = 0;
            double score2 = 0;

            // Evitamos la división por cero si un alimento no tiene calorías
            if (food1.getCalories() > 0) {
                switch (macro) {
                    case "protein":
                        score1 = (food1.getProteins() * CALORIES_PER_PROTEIN_GRAM) / food1.getCalories();
                        break;
                    case "carbs":
                        score1 = (food1.getCarbs() * CALORIES_PER_CARB_GRAM) / food1.getCalories();
                        break;
                    case "fat":
                        score1 = (food1.getFats() * CALORIES_PER_FAT_GRAM) / food1.getCalories();
                        break;
                }
            }

            if (food2.getCalories() > 0) {
                switch (macro) {
                    case "protein":
                        score2 = (food2.getProteins() * CALORIES_PER_PROTEIN_GRAM) / food2.getCalories();
                        break;
                    case "carbs":
                        score2 = (food2.getCarbs() * CALORIES_PER_CARB_GRAM) / food2.getCalories();
                        break;
                    case "fat":
                        score2 = (food2.getFats() * CALORIES_PER_FAT_GRAM) / food2.getCalories();
                        break;
                }
            }

            // Orden descendente (de mayor a menor puntuación)
            return Double.compare(score2, score1);
        });

        return new ArrayList<>(foods.subList(0, Math.min(n, foods.size())));
    }

    /**
     * Calcula el resumen total de macros y calorías para la lista final de alimentos recomendados.
     */
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