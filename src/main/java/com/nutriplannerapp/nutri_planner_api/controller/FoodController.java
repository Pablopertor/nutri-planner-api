package com.nutriplannerapp.nutri_planner_api.controller;

import com.nutriplannerapp.nutri_planner_api.dto.RecommendationRequest;
import com.nutriplannerapp.nutri_planner_api.dto.RecommendedFoodDTO;
import com.nutriplannerapp.nutri_planner_api.model.Food;
import com.nutriplannerapp.nutri_planner_api.service.FoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1") // Prefijo para todas las rutas de este controlador
public class FoodController {

    @Autowired
    private FoodService foodService;

    @GetMapping("/food")
    public List<Food> getAllFoods() {
        return foodService.getAll();
    }

    @PostMapping("/recommendations")
    public List<RecommendedFoodDTO> createRecommendation(@RequestBody RecommendationRequest request) {
        // El controlador delega toda la lógica al servicio
        return foodService.generateRecommendation(request);
    }
}