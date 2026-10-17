package com.ferreteria.proyecto_ferreteria.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/home")
public class HomeController {

    @GetMapping("/dashboard")
    public String dashboard() {
        // Retorna la vista templates/home/dashboard.html
        return "home/dashboard"; 
    }
}