package com.nutriplannerapp.nutri_planner_api.service;

import com.nutriplannerapp.nutri_planner_api.mapper.FoodMapper;
import com.nutriplannerapp.nutri_planner_api.model.generated.Food;
import com.nutriplannerapp.nutri_planner_api.client.OpenFoodFactsClient;
import com.nutriplannerapp.nutri_planner_api.model.generated.*; // Import all generated models
import com.nutriplannerapp.nutri_planner_api.repository.FoodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.nutriplannerapp.nutri_planner_api.dto.MacroTargets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class FoodService {

    // --- CONSTANTS ---
    private static final int CALORIES_PER_PROTEIN_GRAM = 4;
    private static final int CALORIES_PER_FAT_GRAM = 9;
    private static final int CALORIES_PER_CARB_GRAM = 4;

    @Autowired
    private FoodRepository foodRepository;

    @Autowired
    private FoodMapper foodMapper;

    @Autowired
    private OpenFoodFactsClient openFoodFactsClient;

    // =================================================================
    // --- PUBLIC METHODS (API) ---
    // =================================================================

    /**
     * Orchestrates the process of generating a 3-meal recommendation.
     */
    public RecommendationResponse generateRecommendation(RecommendationRequest request) {
        // 1. Calculate total daily targets
        MacroTargets dailyTargets = calculateMacroTargets(request);

        // 2. Calculate targets PER MEAL (total / 3)
        MacroTargets mealTargets = new MacroTargets();
        mealTargets.setProteins(dailyTargets.getProteins() / 3);
        mealTargets.setFats(dailyTargets.getFats() / 3);
        mealTargets.setCarbs(dailyTargets.getCarbs() / 3);

        System.out.println(String.format("OBJECTIVE PER MEAL -> Protein: %.2fg, Fats: %.2fg, Carbs: %.2fg",
                mealTargets.getProteins(), mealTargets.getFats(), mealTargets.getCarbs()));

        // 3. Find the top 3 candidates for each macronutrient
        List<com.nutriplannerapp.nutri_planner_api.model.persistence.Food> allFoodEntities = foodRepository.findAll();
        List<Food> allFoodsApi = foodMapper.toApiList(allFoodEntities);
        List<Food> top3ProteinSources = findTopNFoodsForMacro(new ArrayList<>(allFoodsApi), "protein", 3);
        List<Food> top3CarbSources = findTopNFoodsForMacro(new ArrayList<>(allFoodsApi), "carbs", 3);
        List<Food> top3FatSources = findTopNFoodsForMacro(new ArrayList<>(allFoodsApi), "fat", 3);
        // 4. Add randomness by shuffling the candidate lists
        Collections.shuffle(top3ProteinSources);
        Collections.shuffle(top3CarbSources);
        Collections.shuffle(top3FatSources);

        // 5. Build the 3 meals
        List<RecommendedFood> finalRecommendation = new ArrayList<>();
        if (top3ProteinSources.size() >= 3 && top3CarbSources.size() >= 3 && top3FatSources.size() >= 3) {
            finalRecommendation.addAll(buildMeal(mealTargets, top3ProteinSources.get(0), top3CarbSources.get(0), top3FatSources.get(0)));
            finalRecommendation.addAll(buildMeal(mealTargets, top3ProteinSources.get(1), top3CarbSources.get(1), top3FatSources.get(1)));
            finalRecommendation.addAll(buildMeal(mealTargets, top3ProteinSources.get(2), top3CarbSources.get(2), top3FatSources.get(2)));
        }

        // 6. Calculate the final summary and return the response
        MacroSummary summary = calculateSummary(finalRecommendation);

        RecommendationResponse response = new RecommendationResponse();
        response.setRecommendedFoods(finalRecommendation);
        response.setSummary(summary);
        return response;
    }

    /**
     * Returns all foods from the database.
     */
    public List<Food> getAll()  {
        // 1. Obtenemos las entidades de la base de datos
        List<com.nutriplannerapp.nutri_planner_api.model.persistence.Food> foodEntities = foodRepository.findAll();
        // 2. Las convertimos a DTOs de la API antes de devolverlas
        return foodMapper.toApiList(foodEntities);
    }
    // =================================================================
    // --- PRIVATE HELPER METHODS ---
    // =================================================================

    private MacroTargets calculateMacroTargets(RecommendationRequest request) {
        double totalProteinGrams = request.getUserWeight() * request.getProteinPerKg();
        double proteinCalories = totalProteinGrams * CALORIES_PER_PROTEIN_GRAM;
        double remainingCalories = request.getTotalCalories() - proteinCalories;
        double totalFatGrams = (remainingCalories * (request.getFatPercentage() / 100.0)) / CALORIES_PER_FAT_GRAM;
        double totalCarbsGrams = (remainingCalories * (request.getCarbsPercentage() / 100.0)) / CALORIES_PER_CARB_GRAM;

        System.out.println(String.format("DAILY TARGET CALCULATED -> Protein: %.2fg, Fats: %.2fg, Carbs: %.2fg", totalProteinGrams, totalFatGrams, totalCarbsGrams));

        MacroTargets targets = new MacroTargets();
        targets.setProteins(totalProteinGrams);
        targets.setFats(totalFatGrams);
        targets.setCarbs(totalCarbsGrams);
        return targets;
    }

    private List<RecommendedFood> buildMeal(MacroTargets mealTargets, Food proteinSource, Food carbSource, Food fatSource) {
        List<RecommendedFood> meal = new ArrayList<>();

        double remainingProteins = mealTargets.getProteins();
        double remainingFats = mealTargets.getFats();
        double remainingCarbs = mealTargets.getCarbs();

        if (proteinSource != null && proteinSource.getProteins() > 0) {
            double quantity = (remainingProteins * 0.8) * 100 / proteinSource.getProteins();
            meal.add(new RecommendedFood().foodDetails(proteinSource).quantityInGrams((double) Math.round(quantity)));
            remainingProteins -= (proteinSource.getProteins() / 100) * quantity;
            remainingFats -= (proteinSource.getFats() / 100) * quantity;
            remainingCarbs -= (proteinSource.getCarbs() / 100) * quantity;
        }

        if (carbSource != null && carbSource.getCarbs() > 0 && remainingCarbs > 0) {
            double quantity = remainingCarbs * 100 / carbSource.getCarbs();
            meal.add(new RecommendedFood().foodDetails(carbSource).quantityInGrams((double) Math.round(quantity)));
            remainingFats -= (carbSource.getFats() / 100) * quantity;
        }

        if (fatSource != null && fatSource.getFats() > 0 && remainingFats > 0) {
            double quantity = remainingFats * 100 / fatSource.getFats();
            meal.add(new RecommendedFood().foodDetails(fatSource).quantityInGrams((double) Math.round(quantity)));
        }

        return meal;
    }

    private List<Food> findTopNFoodsForMacro(List<Food> foods, String macro, int n) {
        foods.sort((food1, food2) -> {
            double score1 = 0;
            double score2 = 0;

            if (food1.getCalories() > 0) {
                switch (macro) {
                    case "protein": score1 = (food1.getProteins() * CALORIES_PER_PROTEIN_GRAM) / food1.getCalories(); break;
                    case "carbs": score1 = (food1.getCarbs() * CALORIES_PER_CARB_GRAM) / food1.getCalories(); break;
                    case "fat": score1 = (food1.getFats() * CALORIES_PER_FAT_GRAM) / food1.getCalories(); break;
                }
            }
            if (food2.getCalories() > 0) {
                switch (macro) {
                    case "protein": score2 = (food2.getProteins() * CALORIES_PER_PROTEIN_GRAM) / food2.getCalories(); break;
                    case "carbs": score2 = (food2.getCarbs() * CALORIES_PER_CARB_GRAM) / food2.getCalories(); break;
                    case "fat": score2 = (food2.getFats() * CALORIES_PER_FAT_GRAM) / food2.getCalories(); break;
                }
            }
            return Double.compare(score2, score1);
        });
        return new ArrayList<>(foods.subList(0, Math.min(n, foods.size())));
    }

    private MacroSummary calculateSummary(List<RecommendedFood> recommendationList) {
        double finalCalories = 0, finalProteins = 0, finalCarbs = 0, finalFats = 0;

        for (RecommendedFood item : recommendationList) {
            double quantity = item.getQuantityInGrams();
            Food food = item.getFoodDetails();
            finalCalories += (food.getCalories() / 100) * quantity;
            finalProteins += (food.getProteins() / 100) * quantity;
            finalCarbs += (food.getCarbs() / 100) * quantity;
            finalFats += (food.getFats() / 100) * quantity;
        }

        MacroSummary summary = new MacroSummary();
        summary.setTotalCalories((double) Math.round(finalCalories));
        summary.setTotalProteins((double) Math.round(finalProteins));
        summary.setTotalCarbs((double) Math.round(finalCarbs));
        summary.setTotalFats((double) Math.round(finalFats));
        return summary;
    }
}