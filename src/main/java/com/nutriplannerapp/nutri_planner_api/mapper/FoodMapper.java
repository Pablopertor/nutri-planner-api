package com.nutriplannerapp.nutri_planner_api.mapper;

import com.nutriplannerapp.nutri_planner_api.dto.OffProductDTO;
import com.nutriplannerapp.nutri_planner_api.model.generated.Food;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class FoodMapper {

    // --- MÉTODOS EXISTENTES (De Entidad a DTO de API) ---

    public Food toApi(com.nutriplannerapp.nutri_planner_api.model.persistence.Food entity) {
        if (entity == null) return null;
        Food dto = new Food();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setCalories(entity.getCalories());
        dto.setProteins(entity.getProteins());
        dto.setCarbs(entity.getCarbs());
        dto.setFats(entity.getFats());
        return dto;
    }

    public List<Food> toApiList(List<com.nutriplannerapp.nutri_planner_api.model.persistence.Food> entities) {
        return entities.stream().map(this::toApi).collect(Collectors.toList());
    }

    // --- NUEVOS MÉTODOS (De DTO de OpenFoodFacts a DTO de API) ---

    public Food fromOffDtoToApiDto(OffProductDTO offDto) {
        if (offDto == null || offDto.getNutriments() == null) return null;

        Food apiDto = new Food();
        apiDto.setName(offDto.getName());
        apiDto.setCalories(offDto.getNutriments().getCalories() != null ? offDto.getNutriments().getCalories() : 0.0);
        apiDto.setProteins(offDto.getNutriments().getProteins() != null ? offDto.getNutriments().getProteins() : 0.0);
        apiDto.setCarbs(offDto.getNutriments().getCarbs() != null ? offDto.getNutriments().getCarbs() : 0.0);
        apiDto.setFats(offDto.getNutriments().getFats() != null ? offDto.getNutriments().getFats() : 0.0);

        return apiDto;
    }

    public List<Food> fromOffDtoListToApiDtoList(List<OffProductDTO> offDtoList) {
        if (offDtoList == null) return new ArrayList<>();
        return offDtoList.stream()
                .map(this::fromOffDtoToApiDto)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}