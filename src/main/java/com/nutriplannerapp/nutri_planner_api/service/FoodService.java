package com.nutriplannerapp.nutri_planner_api.service;

import com.nutriplannerapp.nutri_planner_api.dto.RecommendationRequest;
import com.nutriplannerapp.nutri_planner_api.dto.RecommendedFoodDTO;
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
        // Limpiamos la base de datos en cada inicio para evitar duplicados
        foodRepository.deleteAll();

        // --- FUENTES DE PROTEÍNA ---
        Food f1 = new Food("Pechuga de pollo (100g)", 165, 31, 0, 3.6);
        Food f2 = new Food("Pechuga de pavo (100g)", 135, 30, 0, 1.5);
        Food f3 = new Food("Lomo de ternera magro (100g)", 200, 26, 0, 10);
        Food f4 = new Food("Claras de huevo (100g)", 52, 11, 1, 0.2);
        Food f5 = new Food("Salmón (100g)", 208, 20, 0, 13);
        Food f6 = new Food("Atún en agua (lata 80g escurrido)", 100, 24, 0, 0.5);
        Food f7 = new Food("Lentejas cocidas (100g)", 116, 9, 20, 0.4);
        Food f8 = new Food("Garbanzos cocidos (100g)", 139, 8.4, 26, 2.4);
        Food f9 = new Food("Tofu firme (100g)", 145, 16, 3, 9);
        Food f10 = new Food("Merluza (100g)", 89, 16, 0, 2.8);
        Food f11 = new Food("Queso cottage (100g)", 98, 11, 3.4, 4.3);

        // --- FUENTES DE CARBOHIDRATOS ---
        Food f12 = new Food("Arroz integral cocido (100g)", 111, 2.6, 23, 0.9);
        Food f13 = new Food("Avena en copos (100g)", 389, 16.9, 66, 6.9);
        Food f14 = new Food("Quinoa cocida (100g)", 120, 4.4, 21, 1.9);
        Food f15 = new Food("Patata cocida (100g)", 87, 2, 20, 0.1);
        Food f16 = new Food("Boniato asado (100g)", 90, 2, 21, 0.1);
        Food f17 = new Food("Pan integral (100g)", 247, 13, 49, 3.2);
        Food f18 = new Food("Pasta integral cocida (100g)", 124, 5, 26, 0.8);
        Food f19 = new Food("Manzana (unidad mediana)", 95, 0.5, 25, 0.3);
        Food f20 = new Food("Plátano (unidad mediana)", 105, 1.3, 27, 0.4);
        Food f21 = new Food("Cuscús cocido (100g)", 112, 3.8, 23, 0.2);

        // --- FUENTES DE GRASAS SALUDABLES Y VERDURAS ---
        Food f22 = new Food("Aguacate (100g)", 160, 2, 8.5, 15);
        Food f23 = new Food("Aceite de oliva virgen extra (cucharada 15ml)", 120, 0, 0, 14);
        Food f24 = new Food("Almendras (30g)", 164, 6, 6, 14);
        Food f25 = new Food("Nueces (30g)", 185, 4.3, 3.9, 18.5);
        Food f26 = new Food("Semillas de chía (cucharada 15g)", 73, 2.5, 6.3, 4.6);
        Food f27 = new Food("Huevo entero cocido (unidad grande)", 78, 6.3, 0.6, 5.3);
        Food f28 = new Food("Brócoli (100g)", 55, 3.7, 11.2, 0.6);
        Food f29 = new Food("Espinacas (100g)", 23, 2.9, 3.6, 0.4);
        Food f30 = new Food("Pimiento rojo (100g)", 31, 1, 6, 0.3);


        // Guardamos todos los alimentos en la base de datos de una sola vez
        foodRepository.saveAll(List.of(
                f1, f2, f3, f4, f5, f6, f7, f8, f9, f10, f11, f12,
                f13, f14, f15, f16, f17, f18, f19, f20, f21, f22, f23,
                f24, f25, f26, f27, f28, f29, f30
        ));

        }
    public List<Food> getAll() {
        return foodRepository.findAll();
    }

    public List<RecommendedFoodDTO> generateRecommendation(RecommendationRequest request) {
        // Paso 1: Imprimir los datos recibidos para depurar y ver que llegan bien.
        System.out.println("Generando recomendación para la petición: " + request);
        // Paso 2: Vamos con el calculo de las proteinas en funcion del peso y de los g/kgs de peso del usuario
        double proteinGrams = request.getProteinPerKg()* request.getUserWeight();
        double proteinCalories= proteinGrams * CALORIES_PER_PROTEIN_GRAM;
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
        System.out.println("Proteínas: " + String.format("%.2f", proteinGrams) + " g");
        System.out.println("Grasas: " + String.format("%.2f", fatGrams) + " g");
        System.out.println("Carbohidratos: " + String.format("%.2f", carbsGrams) + " g");
        System.out.println("------------------------------------");
        // Paso 6: Version de algoritmo que busca los alimentos mas densos de cada macro
        List<Food> allFoods = foodRepository.findAll();

        Food proteinSource = null;
        Food carbSource = null;
        Food fatSource = null;

        double maxProtein = -1;
        double maxCarbs = -1;
        double maxFats = -1;

        for (Food food : allFoods) {
            // Encontrar la mejor fuente de proteína
            if (food.getProteins() > maxProtein) {
                maxProtein = food.getProteins();
                proteinSource = food;
            }
            // Encontrar la mejor fuente de carbohidratos
            if (food.getCarbs() > maxCarbs) {
                maxCarbs = food.getCarbs();
                carbSource = food;
            }
            // Encontrar la mejor fuente de grasas
            if (food.getFats() > maxFats) {
                maxFats = food.getFats();
                fatSource = food;
            }
        }
        double proteinSourceGrams = Math.round(proteinGrams * 100 / proteinSource.getProteins());
        double carbSourceGrams = Math.round(carbsGrams * 100 / carbSource.getCarbs());
        double fatSourceGrams = Math.round(fatGrams * 100 / fatSource.getFats());

        // --- PASO FINAL: CREAR Y DEVOLVER LA LISTA DE DTOs ---
        RecommendedFoodDTO recommendedProtein = new RecommendedFoodDTO(proteinSource, proteinSourceGrams);
        RecommendedFoodDTO recommendedCarb = new RecommendedFoodDTO(carbSource, carbSourceGrams);
        RecommendedFoodDTO recommendedFat = new RecommendedFoodDTO(fatSource, fatSourceGrams);

        // Devolvemos la nueva lista de DTOs
        return List.of(recommendedProtein, recommendedCarb, recommendedFat);
    }
}