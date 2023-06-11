package com.muzio.controller;

import com.muzio.model.CreditCard;
import com.muzio.model.Role;
import com.muzio.model.Store;
import com.muzio.model.User;
import com.muzio.repository.CreditCardRepository;
import com.muzio.repository.RoleRepository;
import com.muzio.repository.StoreRepository;
import com.muzio.repository.UserRepository;
import com.muzio.service.AppService;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootApplication
@Controller
public class FrontEndController {
    public static void main(String[] args){
        SpringApplication.run(FrontEndController.class, args);
    }

    public String path;
    public static String apiPath;
    public static String cssPath;
    public static String jsPath;
    public static String assetsPath;
    @Autowired
    private AppService appService;

    @Autowired
    FrontEndController(
            @Value("${spring.base}") String path
    ){
        this.path = path;
        cssPath = path + "css/";
        jsPath = path + "js/";
        assetsPath = path + "assets/";
        apiPath = path + "api/";
    }

    private void initModel(Model m){
        m.addAttribute("path", this.path);
        m.addAttribute("css", cssPath);
        m.addAttribute("js", jsPath);
        m.addAttribute("assets", assetsPath);
        m.addAttribute("api", apiPath);
    }
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
        User newMerchant =  new User();

        List<Map> storeList = this.appService.getStores();
        model.addAttribute("page", "merchant-new");
        model.addAttribute("merchant", newMerchant);
        model.addAttribute("stores",storeList);
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

    @GetMapping("/credit-card/block")
    String creditCardBlock(Model model){
        initModel(model);
        model.addAttribute("page", "credit-card-block");
        return "credit-card/block";
    }

    @GetMapping("/credit-card/new")
    String creditCardNew(Model model){
        List<Map> stores = this.appService.getStores();

        CreditCard lastCreditCard = this.appService.getLastCreditCard();
        int zeroes = 4 - lastCreditCard.getId().toString().length();
        String newCardNumber = "5000-1234-5678-" + ("0".repeat(zeroes) + ((Integer)(lastCreditCard.getId() + 1)).toString());

        CreditCard creditCard = new CreditCard();

        creditCard.setNumber(newCardNumber);
        creditCard.setEnabled(0);

        initModel(model);
        model.addAttribute("page", "credit-card-new");
        model.addAttribute("creditCard", creditCard);
        model.addAttribute("stores", stores);
        return "credit-card/new";
    }
}
