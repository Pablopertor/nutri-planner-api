package com.nutriplannerapp.nutri_planner_api.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class OffResponseDTO {

    private List<OffProductDTO> products;
}