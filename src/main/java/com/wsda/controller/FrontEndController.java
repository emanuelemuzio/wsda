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
    public static String cssPath;
    public static String jsPath;
    public static String assetsPath;
    @Autowired
    FrontEndController(
            @Value("${spring.base}") String path
    ){
        this.path = path;
        cssPath = path + "css/";
        jsPath = path + "js/";
        assetsPath = path + "assets/";
    }

    private void initModel(Model m){
        m.addAttribute("path", this.path);
        m.addAttribute("css", cssPath);
        m.addAttribute("js", jsPath);
        m.addAttribute("assets", assetsPath);
    }

    @GetMapping("/")
    String index(Model model){
        initModel(model);
        return "index";
    }

    @GetMapping("/login")
    String login(Model model){
        initModel(model);
        return "login";
    }

    @GetMapping("/dashboard")
    String dashboard(Model model){
        initModel(model);
        model.addAttribute("page", "dashboard");
        return "dashboard";
    }

    @GetMapping("/merchant/new")
    String newMerchant(Model model){
        initModel(model);
        model.addAttribute("page", "merchant-new");
        return "merchant/new";
    }

    @GetMapping("/merchant/list")
    String merchantsList(Model model){
        initModel(model);
        model.addAttribute("page", "merchant-list");
        return "merchant/list";
    }

//    @GetMapping("**")
//    String any(){
//
//        return "404";
//    }
}
