package com.nutriplannerapp.nutri_planner_api.controller;

import com.nutriplannerapp.nutri_planner_api.model.generated.Food;
import com.nutriplannerapp.nutri_planner_api.model.generated.RecommendationRequest;
import com.nutriplannerapp.nutri_planner_api.model.generated.RecommendationResponse;
import com.nutriplannerapp.nutri_planner_api.service.FoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "Food API", description = "Operaciones relacionadas con los alimentos y recomendaciones")
public class FoodController {

    @Autowired
    private FoodService foodService;

    @Operation(summary = "Obtener todos los alimentos", description = "Devuelve una lista de todos los alimentos disponibles en la base de datos.")
    @GetMapping("/food")
    public List<Food> getAllFoods() {
        return foodService.getAll();
    }

    @Operation(summary = "Crear una recomendación", description = "Genera una recomendación de 3 comidas basada en los requisitos del usuario.")
    @ApiResponse(responseCode = "200", description = "Recomendación generada con éxito")
    @PostMapping("/recommendations")
    public RecommendationResponse createRecommendation(@RequestBody RecommendationRequest request) {
        return foodService.generateRecommendation(request);
    }
}