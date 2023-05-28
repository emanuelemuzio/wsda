package com.wsda.controller;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@SpringBootApplication
@Controller
public class FrontEndController {
    public static void main(String[] args){
        SpringApplication.run(FrontEndController.class, args);
    }
    @GetMapping("/")
    String home(Model model){
        model.addAttribute("name","Emanuele");
        return "home";
    }
}
