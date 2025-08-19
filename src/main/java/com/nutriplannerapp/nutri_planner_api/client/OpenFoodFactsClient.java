package com.nutriplannerapp.nutri_planner_api.client;

import com.nutriplannerapp.nutri_planner_api.dto.OffResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class OpenFoodFactsClient {

    private static final String SEARCH_URL = "https://world.openfoodfacts.org/cgi/search.pl?action=process&json=1&search_terms={query}&page_size=5";

    @Autowired
    private RestTemplate restTemplate;

    public OffResponseDTO searchFoodByQuery(String query) {
        // Ahora le pedimos a RestTemplate que convierta la respuesta directamente a nuestro DTO
        OffResponseDTO response = restTemplate.getForObject(SEARCH_URL, OffResponseDTO.class, query);

        System.out.println("Respuesta de la API mapeada a objetos Java:");
        System.out.println(response);

        return response;
    }
}