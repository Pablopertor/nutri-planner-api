package com.nutriplannerapp.nutri_planner_api.repository;

import com.nutriplannerapp.nutri_planner_api.model.Comida;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ComidaRepository extends JpaRepository<Comida, Long> {
}