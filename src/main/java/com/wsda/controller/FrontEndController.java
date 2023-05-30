package com.wsda.controller;

import com.wsda.repository.WSDATokenRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@SpringBootApplication
@Controller
public class FrontEndController {
    private final WSDATokenRepository token_repo;
    public static void main(String[] args){
        SpringApplication.run(FrontEndController.class, args);
    }

    FrontEndController(WSDATokenRepository token_repo){
        this.token_repo = token_repo;
    }

    @Value("${server.host}")
    private String host;

    @Value("${server.port}")
    private Integer port;

    private String path = this.host + "/" + this.port;

    @GetMapping("/")
    String index(Model model){
        model.addAttribute("path", path);
        return "index";
    }

    @GetMapping("/login")
    String login(Model model){
        model.addAttribute("path", path);
        return "login";
    }

    @GetMapping("/dashboard")
    String dashboard(Model model){
        model.addAttribute("path", path);
        return "dashboard";
    }
}
