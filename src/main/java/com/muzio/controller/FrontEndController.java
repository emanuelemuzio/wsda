package com.muzio.controller;

import com.muzio.repository.UserRepository;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

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

    //    @GetMapping("/error")
//    String error(HttpServletRequest request){
//        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
//        if(status != null){
//            Integer statusCode = Integer.valueOf(status.toString());
//            switch(statusCode){
//                case 403:
//                    return "redirect:/403";
//                default:
//                break;
//            }
//        }
//        return "error";
//    }

    @GetMapping("/")
    String index(Model model){
        initModel(model);
        return "index";
    }

    @GetMapping("/login")
    String login(Model model){
        initModel(model);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth != null  && !(auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ANONYMOUS")))){
            return "redirect:/dashboard";
        }
        return "login";
    }

    @GetMapping("/logout")
    String logout(){
        return "redirect:/login";
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

    @GetMapping("/credit-card/list")
    String creditCardList(Model model){
        initModel(model);
        model.addAttribute("page", "credit-card-list");
        return "credit-card/list";
    }

//    @GetMapping("**")
//    String any(){
//
//        return "404";
//    }
}
