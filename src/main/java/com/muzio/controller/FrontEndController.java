package com.muzio.controller;

import com.muzio.model.CreditCard;
import com.muzio.model.Role;
import com.muzio.model.Store;
import com.muzio.model.User;
import com.muzio.service.AppService;
import com.muzio.service.CustomUserDetails;
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
import java.util.ArrayList;
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

    @PostMapping("/balance")
    String getCardBalance(@RequestParam String number, Model model){
        CreditCard cc = this.appService.getCreditCardByNumber(number);
        if(cc == null){
            return "redirect:/?err";
        }
        model.addAttribute("balance", cc.getBalance());
        return index(model);
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

    @GetMapping("/customer/new")
    String newCustomer(Model model){
        initModel(model);
        User newCustomer =  new User();

        CustomUserDetails customUserDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Store store = customUserDetails.getStore();

        model.addAttribute("page", "merchant-new");
        model.addAttribute("customer", newCustomer);
        model.addAttribute("storeId", store.getId());
        model.addAttribute("storeName", store.getName());
        return "customer/new";
    }

    @GetMapping("/merchant/list")
    String merchantsList(Model model){
        List<User> merchantEntityList = this.appService.getMerchants();
        List<Map> merchantList = new ArrayList<>();

        for(User u : merchantEntityList){
            HashMap<String, String> userInfo = new HashMap<>();

            userInfo.put("id", u.getId().toString());
            userInfo.put("firstName", u.getFirstName());
            userInfo.put("lastName", u.getLastName());
            userInfo.put("email", u.getEmail());
            userInfo.put("store", u.getStore().getName());
            userInfo.put("enabled", u.getEnabled() ? "Yes" : "No");

            merchantList.add(userInfo);
        }

        initModel(model);
        model.addAttribute("page", "merchant-list");
        model.addAttribute("merchants", merchantList);
        return "merchant/list";
    }

    @GetMapping("/customer/list")
    String customersList(Model model){
        CustomUserDetails customUserDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Store store = customUserDetails.getStore();

        List<User> customerEntityList = this.appService.getStoreCustomers(store);
        List<Map> customerList = new ArrayList<>();

        for(User u : customerEntityList){
            HashMap<String, String> userInfo = new HashMap<>();

            userInfo.put("id", u.getId().toString());
            userInfo.put("firstName", u.getFirstName());
            userInfo.put("lastName", u.getLastName());
            userInfo.put("email", u.getEmail());
            userInfo.put("enabled", u.getEnabled() ? "Yes" : "No");

            customerList.add(userInfo);
        }

        List<CreditCard> freeCreditCardsEntityList = this.appService.getFreeStoreCreditCards(store);

        List<Map> freeCreditCardsList = new ArrayList<>();

        for(CreditCard c : freeCreditCardsEntityList){
            HashMap<String, String> ccInfo = new HashMap<>();
            ccInfo.put("value", c.getId().toString());
            ccInfo.put("label", c.getNumber());

            freeCreditCardsList.add(ccInfo);
        }

        initModel(model);
        model.addAttribute("page", "customer-list");
        model.addAttribute("customers", customerList);
        model.addAttribute("creditCards", freeCreditCardsList);
        return "customer/list";
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
