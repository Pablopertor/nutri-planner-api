package com.nutriplannerapp.nutri_planner_api.repository;

import com.nutriplannerapp.nutri_planner_api.model.persistence.Food;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FoodRepository extends JpaRepository<Food, Long> {
}