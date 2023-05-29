package com.wsda.controller;

import com.wsda.model.WSDATokenRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.wsda.model.*;

import java.rmi.server.ExportException;
import java.util.List;

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

    @GetMapping("/")
    String index(){
        return "index";
    }

    @GetMapping("/login")
    String login(){
        return "login";
    }

    @GetMapping("/backend/home")
    String backendHome(){
        return "backend-home";
    }
}
