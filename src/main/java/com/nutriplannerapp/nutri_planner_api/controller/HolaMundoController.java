package com.nutriplannerapp.nutri_planner_api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HolaMundoController {

    @GetMapping("/hola")
    public String holaMundo() {
        return "¡El servidor está funcionando!";
    }
}