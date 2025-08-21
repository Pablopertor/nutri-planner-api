package com.nutriplannerapp.nutri_planner_api.mapper;

import com.nutriplannerapp.nutri_planner_api.model.generated.Food;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class FoodMapper {

    public Food toApi(com.nutriplannerapp.nutri_planner_api.model.persistence.Food entity) {
        if (entity == null) {
            return null;
        }
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
}