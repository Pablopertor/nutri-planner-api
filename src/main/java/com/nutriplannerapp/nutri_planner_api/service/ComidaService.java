package com.nutriplannerapp.nutri_planner_api.service;

import com.nutriplannerapp.nutri_planner_api.model.Comida;
import com.nutriplannerapp.nutri_planner_api.repository.ComidaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class ComidaService implements CommandLineRunner {

    @Autowired
    private ComidaRepository comidaRepository;

    @Override
    public void run(String... args) throws Exception {
        // Creamos algunas comidas de ejemplo
        Comida pollo = new Comida();
        pollo.setNombre("Pechuga de pollo a la plancha (100g)");
        pollo.setCalorias(165);
        pollo.setProteinas(31);
        pollo.setGrasas(3.6);
        pollo.setCarbohidratos(0);

        Comida arroz = new Comida();
        arroz.setNombre("Arroz blanco cocido (100g)");
        arroz.setCalorias(130);
        arroz.setProteinas(2.7);
        arroz.setGrasas(0.3);
        arroz.setCarbohidratos(28);

        Comida brocoli = new Comida();
        brocoli.setNombre("Brócoli al vapor (100g)");
        brocoli.setCalorias(55);
        brocoli.setProteinas(3.7);
        brocoli.setGrasas(0.6);
        brocoli.setCarbohidratos(11.2);

        Comida salmon = new Comida();
        salmon.setNombre("Salmón a la plancha (100g)");
        salmon.setCalorias(208);
        salmon.setProteinas(20);
        salmon.setGrasas(13);
        salmon.setCarbohidratos(0);

        // Guardamos las comidas en la base de datos
        comidaRepository.saveAll(Arrays.asList(pollo, arroz, brocoli, salmon));
    }
}