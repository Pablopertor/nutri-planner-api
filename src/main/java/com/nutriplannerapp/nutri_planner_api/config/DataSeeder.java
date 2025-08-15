package com.nutriplannerapp.nutri_planner_api.config;

import com.nutriplannerapp.nutri_planner_api.model.Food;
import com.nutriplannerapp.nutri_planner_api.repository.FoodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataSeeder implements CommandLineRunner {

    @Autowired
    private FoodRepository foodRepository;

    @Override
    public void run(String... args) throws Exception {
        foodRepository.deleteAll();

        Food f1 = new Food("Pechuga de pollo (100g)", 165, 31, 0, 3.6);
        Food f2 = new Food("Pechuga de pavo (100g)", 135, 30, 0, 1.5);
        Food f3 = new Food("Lomo de ternera magro (100g)", 200, 26, 0, 10);
        Food f4 = new Food("Claras de huevo (100g)", 52, 11, 1, 0.2);
        Food f5 = new Food("Salmón (100g)", 208, 20, 0, 13);
        Food f6 = new Food("Atún en agua (100g)", 100, 24, 0, 0.5);
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
        Food f19 = new Food("Manzana (100g)", 95, 0.5, 25, 0.3);
        Food f20 = new Food("Plátano (100g)", 105, 1.3, 27, 0.4);
        Food f21 = new Food("Cuscús cocido (100g)", 112, 3.8, 23, 0.2);

        // --- FUENTES DE GRASAS SALUDABLES Y VERDURAS ---
        Food f22 = new Food("Aguacate (100g)", 160, 2, 8.5, 15);
        Food f23 = new Food("Aceite de oliva virgen extra (100g)", 900, 0, 0, 100);
        Food f24 = new Food("Almendras (100g)", 164*3.33, 6*3.33, 6*3.33, 14*3.33);
        Food f25 = new Food("Nueces (100g)", 185*3.33, 4.3*3.33, 3.9*3.33, 18.5*3.33);
        Food f26 = new Food("Semillas de chía (100g)", 73*6.66, 2.5*6.66, 6.3*6.66, 4.6*6.66);
        Food f27 = new Food("Huevo entero cocido (100g)", 78*2, 6.3*2, 0.6*2, 5.3*2);
        Food f28 = new Food("Brócoli (100g)", 55, 3.7, 11.2, 0.6);
        Food f29 = new Food("Espinacas (100g)", 23, 2.9, 3.6, 0.4);
        Food f30 = new Food("Pimiento rojo (100g)", 31, 1, 6, 0.3);

        foodRepository.saveAll(List.of(
                f1, f2, f3, f4, f5, f6, f7, f8, f9, f10, f11, f12,
                f13, f14, f15, f16, f17, f18, f19, f20, f21, f22, f23,
                f24, f25, f26, f27, f28, f29, f30
        ));

        System.out.println("--- BASE DE DATOS POBLADA CON 30 ALIMENTOS ---");
    }
}