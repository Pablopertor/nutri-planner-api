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

    // --- CONSTANTES ---
    private static final int CALORIES_PER_PROTEIN_GRAM = 4;
    private static final int CALORIES_PER_FAT_GRAM = 9;
    private static final int CALORIES_PER_CARB_GRAM = 4;

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
        // Paso 2: Vamos con el calculo de las proteinas en funcion del peso y de los g/kgs de peso del usuario
        double totalProteinGrams = request.getProteinPerKg()* request.getUserWeight();
        double proteinCalories= totalProteinGrams * CALORIES_PER_PROTEIN_GRAM;
        // Paso 3: Una vez obtenidas las calorias de la proteina, vamos a calcular las kcal restantes y su distribucion en grasas y carbohidratos
        double totalKcal = request.getTotalCalories();
        double kCalLeft = totalKcal-proteinCalories;
        double fatCalories = kCalLeft * request.getFatPercentage() / 100.0;
        double carbsCalories = kCalLeft - fatCalories;
        // Paso 4: Pasamos de kCal a gramos
        double fatGrams = fatCalories / CALORIES_PER_FAT_GRAM;
        double carbsGrams = carbsCalories / CALORIES_PER_CARB_GRAM;
        // Paso 5: Vamos a imprimir en la consola los resultados
        System.out.println("--- CÁLCULO DE MACROS OBJETIVO ---");
        System.out.println("Calorías Totales: " + request.getTotalCalories() + " kcal");
        System.out.println("Proteínas: " + String.format("%.2f", totalProteinGrams) + " g");
        System.out.println("Grasas: " + String.format("%.2f", fatGrams) + " g");
        System.out.println("Carbohidratos: " + String.format("%.2f", carbsGrams) + " g");
        System.out.println("------------------------------------");
        // Paso 6: Seguimos de momento devolviendo toda nuestra lista de comida
        return foodRepository.findAll();
    }
}