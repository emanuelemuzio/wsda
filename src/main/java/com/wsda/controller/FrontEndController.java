package com.wsda.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@SpringBootApplication
@Controller
public class FrontEndController {
    public static void main(String[] args){
        SpringApplication.run(FrontEndController.class, args);
    }

    public String path;
    @Autowired
    FrontEndController(
            @Value("${spring.base}") String path
    ){
        this.path = path;
    }

    @GetMapping("/")
    String index(Model model){
        model.addAttribute("path", this.path);
        return "index";
    }

    @GetMapping("/login")
    String login(Model model){
        model.addAttribute("path", this.path);
        return "login";
    }

    @GetMapping("/dashboard")
    String dashboard(Model model){
        model.addAttribute("path", this.path);
        return "dashboard";
    }

//    @GetMapping("**")
//    String any(){
//
//        return "404";
//    }
}
