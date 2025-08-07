package com.nutriplannerapp.nutri_planner_api.service;

import com.nutriplannerapp.nutri_planner_api.dto.RecommendationRequest;
import com.nutriplannerapp.nutri_planner_api.model.Food;
import com.nutriplannerapp.nutri_planner_api.repository.FoodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class FoodService implements CommandLineRunner {

    @Autowired
    private FoodRepository foodRepository;

    @Override
    public void run(String... args) throws Exception {
        // Creamos algunas comidas de ejemplo
        Food pollo = new Food();
        pollo.setName("Pechuga de pollo a la plancha (100g)");
        pollo.setCalories(165);
        pollo.setProteins(31);
        pollo.setFats(3.6);
        pollo.setCarbs(0);

        Food arroz = new Food();
        arroz.setName("Arroz blanco cocido (100g)");
        arroz.setCalories(130);
        arroz.setProteins(2.7);
        arroz.setFats(0.3);
        arroz.setCarbs(28);

        Food brocoli = new Food();
        brocoli.setName("Brócoli al vapor (100g)");
        brocoli.setCalories(55);
        brocoli.setProteins(3.7);
        brocoli.setFats(0.6);
        brocoli.setCarbs(11.2);

        Food salmon = new Food();
        salmon.setName("Salmón a la plancha (100g)");
        salmon.setCalories(208);
        salmon.setProteins(20);
        salmon.setFats(13);
        salmon.setCarbs(0);

        // Guardamos las comidas en la base de datos
        foodRepository.saveAll(Arrays.asList(pollo, arroz, brocoli, salmon));



        }
    public List<Food> getAll() {
        return foodRepository.findAll();
    }

    public List<Food> generateRecommendation(RecommendationRequest request) {
        // Paso 1: Imprimir los datos recibidos para depurar y ver que llegan bien.
        System.out.println("Generando recomendación para la petición: " + request);

        // Paso 2: Implementar la lógica (Versión 1 - muy simple).
        // Por ahora, simplemente devolvemos todos los alimentos que tenemos en la BD.
        // En el futuro, aquí irá el algoritmo inteligente.
        List<Food> allFoods = foodRepository.findAll();
        return allFoods;
    }
}