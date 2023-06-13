package com.muzio.controller;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import com.muzio.model.CreditCard;
import com.muzio.model.Store;
import com.muzio.model.Transaction;
import com.muzio.model.User;
import com.muzio.service.AppService;
import com.muzio.service.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.FileOutputStream;
import java.security.Principal;
import java.text.SimpleDateFormat;
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

    @GetMapping("/testpdf")
    String generateReport(){

        return "index";
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

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth != null  && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))){
            List<Transaction> transactionEntityList = this.appService.getAllTransactions();
            List<Map> transactionList = new ArrayList<>();
            for(Transaction t : transactionEntityList){
                HashMap<String, String> transactionInfo = new HashMap<>();


                transactionInfo.put("id", t.getId().toString());
                transactionInfo.put("type", t.getType());
                transactionInfo.put("customer", t.getCreditCard().getOwner().getEmail());
                transactionInfo.put("store", t.getCreditCard().getStore().getName());
                transactionInfo.put("time", t.getTime().toString());
                transactionInfo.put("amount", t.getAmount().toString());

                transactionList.add(transactionInfo);
            }

            model.addAttribute("transactions", transactionList);
        }
        else if(auth != null  && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_MERCHANT"))){
            CustomUserDetails customUserDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Store store = customUserDetails.getStore();
            List<Transaction> transactionEntityList = this.appService.getStoreTransactions(store);

            List<Map> transactionList = new ArrayList<>();
            for(Transaction t : transactionEntityList){
                HashMap<String, String> transactionInfo = new HashMap<>();

                transactionInfo.put("id", t.getId().toString());
                transactionInfo.put("type", t.getType());
                transactionInfo.put("customer", t.getCreditCard().getOwner().getEmail());
                transactionInfo.put("time", t.getTime().toString());
                transactionInfo.put("amount", t.getAmount().toString());

                transactionList.add(transactionInfo);
            }

            model.addAttribute("transactions", transactionList);
        }
        else if(auth != null  && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_CUSTOMER"))){
            CustomUserDetails customUserDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            List<Transaction> transactionEntityList = this.appService.getUserTransactions(customUserDetails.getUser());

            List<Map> transactionList = new ArrayList<>();
            for(Transaction t : transactionEntityList){
                HashMap<String, String> transactionInfo = new HashMap<>();

                transactionInfo.put("id", t.getId().toString());
                transactionInfo.put("type", t.getType());
                transactionInfo.put("store", t.getCreditCard().getStore().getName());
                transactionInfo.put("time", t.getTime().toString());
                transactionInfo.put("amount", t.getAmount().toString());

                transactionList.add(transactionInfo);
            }

            model.addAttribute("transactions", transactionList);
        }


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

    @GetMapping("/store/new")
    String newStore(Model model){
        initModel(model);
        Store store =  new Store();

        model.addAttribute("page", "store-new");
        model.addAttribute("store", store);
        return "store/new";
    }

    @GetMapping("/store/delete")
    String deleteStore(Model model){
        initModel(model);

        List<Map> storesList = this.appService.getStores();

        model.addAttribute("page", "store-delete");
        model.addAttribute("stores", storesList);
        return "store/delete";
    }

    @GetMapping("/customer/new")
    String newCustomer(Model model){
        initModel(model);
        User newCustomer =  new User();

        CustomUserDetails customUserDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Store store = customUserDetails.getStore();

        model.addAttribute("page", "customer-new");
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
        CustomUserDetails customUserDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Store store = customUserDetails.getStore();

        List<User> customerEntityList = this.appService.getStoreCustomers(store);
        List<Map> creditCards = new ArrayList<>();

        for(User u : customerEntityList){
            List<CreditCard> customerCreditCards = this.appService.getCustomerCreditCards(u);
            for(CreditCard c : customerCreditCards){
                HashMap<String, String> creditCardInfo = new HashMap<>();

                creditCardInfo.put("id", c.getId().toString());
                creditCardInfo.put("number", c.getNumber());
                creditCardInfo.put("balance", c.getBalance().toString());
                creditCardInfo.put("owner", c.getOwner().getEmail());
                creditCardInfo.put("enabled", c.getEnabled() == 1 ? "Yes" : "No");

                creditCards.add(creditCardInfo);
            }
        }

        initModel(model);
        model.addAttribute("page", "credit-card-list");
        model.addAttribute("creditCards", creditCards);

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
        String numbers = String.format("%04d", lastCreditCard.getId() + 1);
        String newCardNumber = "5000-1234-5678-" + numbers;

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
