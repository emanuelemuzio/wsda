package com.muzio.controller;

import com.muzio.service.AppService;
import com.muzio.model.*;
import com.muzio.service.CustomUserDetails;
import jakarta.persistence.EntityManager;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RequestMapping("/api")
@Controller
public class BackEndController {
    @Autowired
    private AppService appService;

    public static void main(String[] args){
        SpringApplication.run(BackEndController.class, args);
    }

    @PostMapping("/credit-card/new")
    String createNewCreditCard(@ModelAttribute CreditCard creditCard, HttpSession session){
        creditCard.setNumber(creditCard.getNumber().replace("-",""));
        creditCard.setEnabled(1);
        this.appService.save(creditCard);
        return redirectWithMsg(session, "Card created successfully","/dashboard", false);
    }

    @PostMapping("/credit-card/block")
    String blockCreditCard(@RequestParam String number, @RequestParam String enable, HttpSession session){
        Integer enableInt = Integer.parseInt(enable);
        CreditCard creditCard = this.appService.getCreditCardRepository().findCreditCardByNumber(number);
        if(creditCard == null){
            return redirectWithMsg(session, "Credit card not found","/credit-card/block", true);
        }
        if(creditCard.getEnabled() == enableInt){
            return redirectWithMsg(session, "Credit card already in that state","/credit-card/block", true);
        }
        creditCard.setEnabled(enableInt);
        this.appService.save(creditCard);
        return redirectWithMsg(session, "Credit card blocked","/dashboard", false);
    }

    @PostMapping("/store/delete")
    String deleteStore(@RequestParam String id, HttpSession session){
        Integer intId = Integer.parseInt(id);
        Optional<Store> store = this.appService.getStoreRepository().findById(intId);

        if(store.isPresent()){
            this.appService.getStoreRepository().delete(store.get());
            return redirectWithMsg(session, "Store delete","/dashboard", false);
        }

        return redirectWithMsg(session, "Store not found","/dashboard", true);
    }

    @PostMapping("/merchant/new")
    String createMerchantNew(
            @ModelAttribute User newMerchant,
            HttpSession session
            ){
        User existingUser = this.appService.getUserRepository().findByEmail(newMerchant.getEmail());
        if(existingUser != null){
            return redirectWithMsg(session, "Email already in use","/merchant/new", true);
        }

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        Role merchantRole = this.appService.getRole("ROLE_MERCHANT");

        newMerchant.setRole(merchantRole);
        newMerchant.setEnabled(true);
        newMerchant.setPassword(encoder.encode(newMerchant.getPassword()));
        this.appService.save(newMerchant);

        return redirectWithMsg(session, "Merchant created","/dashboard", false);
    }

    @PostMapping("/customer/bind-credit-card")
    String bindCreditCard(
            @RequestParam String userId,
            @RequestParam String creditCardId,
            HttpSession session
    ){
        Optional <CreditCard> creditCard = this.appService.getCreditCardRepository().findById(Integer.parseInt(creditCardId));
        Optional <User> customer = this.appService.getUserRepository().findById(Integer.parseInt(userId));

        if(creditCard.isEmpty()){
            return redirectWithMsg(session, "Credit card not found","/customer/list", true);
        }

        if(customer.isEmpty()){
            return redirectWithMsg(session, "User not found","/customer/list", true);
        }

        creditCard.get().setOwner(customer.get());
        creditCard.get().setEnabled(1);
        this.appService.save(creditCard.get());

        return redirectWithMsg(session, "Credit card assigned to " + customer.get().getFirstName() + " " + customer.get().getLastName(),"/customer/list", false);
    }

    @PostMapping("/recharge-card")
    String rechargeCreditCard(
            @RequestParam String rechargeCreditCardId,
            @RequestParam String rechargeAmount,
            HttpSession session
    ){
        Optional <CreditCard> creditCard = this.appService.getCreditCardRepository().findById(Integer.parseInt(rechargeCreditCardId));

        if(creditCard.isEmpty()){
            return redirectWithMsg(session, "Credit card not found","/credit-card/list", true);
        }

        creditCard.get().setBalance(creditCard.get().getBalance() + Integer.parseInt(rechargeAmount));
        this.appService.save(creditCard.get());

        Transaction t = new Transaction();
        t.setCreditCard(creditCard.get());
        t.setType("RECHARGE");
        t.setTime(new Date());
        t.setAmount(Integer.parseInt(rechargeAmount));
        this.appService.save(t);

        return redirectWithMsg(session, "Card recharged","/credit-card/list", false);
    }

    @PostMapping("/charge-card")
    String chargeCreditCard(
            @RequestParam String purchaseCreditCardId,
            @RequestParam String purchaseAmount,
            HttpSession session
    ){
        Optional <CreditCard> creditCard = this.appService.getCreditCardRepository().findById(Integer.parseInt(purchaseCreditCardId));

        if(creditCard.isEmpty()){
            return redirectWithMsg(session, "Credit card not found","/credit-card/list", true);
        }

        creditCard.get().setBalance(creditCard.get().getBalance() - Integer.parseInt(purchaseAmount));
        this.appService.save(creditCard.get());

        Transaction t = new Transaction();
        t.setCreditCard(creditCard.get());
        t.setType("PURCHASE");
        t.setTime(new Date());
        t.setAmount(Integer.parseInt(purchaseAmount));
        this.appService.save(t);

        return redirectWithMsg(session, "Purchase completed","/credit-card/list", false);
    }

    @PostMapping("/store/new")
    String newStore(@ModelAttribute Store store, HttpSession session){
        Optional<Store> existingStore = this.appService.getStoreRepository().findByName(store.getName());

        if(existingStore.isPresent()){
            return redirectWithMsg(session, "Store with this name already exists","/store/new", true);
        }

        this.appService.save(store);
        return redirectWithMsg(session, "Store created","/dashboard", false);
    }

    @PostMapping("/customer/new")
    String createCustomerNew(
            @ModelAttribute User newCustomer,
            HttpSession session
    ){
        User existingUser = this.appService.getUserRepository().findByEmail(newCustomer.getEmail());
        if(existingUser != null){
            return redirectWithMsg(session, "Email already in use","/customer/new", true);
        }

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        Role customerRole = this.appService.getRole("ROLE_CUSTOMER");

        newCustomer.setRole(customerRole);
        newCustomer.setEnabled(true);
        newCustomer.setPassword(encoder.encode(newCustomer.getPassword()));
        this.appService.save(newCustomer);

        Store store = this.getCurrentMerchantStore();

        store.addCustomer(newCustomer);
        this.appService.save(store);

        return redirectWithMsg(session, "New customer created","/dashboard", false);
    }

    @GetMapping("/merchant/delete")
    String deleteMerchant(@RequestParam String id, HttpSession session){
        Optional<User> user = this.appService.getUserRepository().findById((Integer.parseInt(id)));

        if(user.isEmpty()){
            return redirectWithMsg(session, "User not found","/merchant/list", true);
        }
        this.appService.getUserRepository().delete(user.get());

        return redirectWithMsg(session, "Merchant deleted","/merchant/list", false);
    }

    @GetMapping("/merchant/disable")
    String disableMerchant(@RequestParam String id, HttpSession session){
        Optional<User> user = this.appService.getUserRepository().findById((Integer.parseInt(id)));

        if(user.isEmpty()){
            return redirectWithMsg(session, "User not found","/merchant/list", true);
        }

        user.get().setEnabled(false);
        this.appService.save(user.get());

        return redirectWithMsg(session, "Merchant disabled","/merchant/list", false);
    }

    @GetMapping("/merchant/enable")
    String enableMerchant(@RequestParam String id, HttpSession session){
        Optional<User> user = this.appService.getUserRepository().findById((Integer.parseInt(id)));

        if(user.isEmpty()){
            return redirectWithMsg(session, "User not found","/merchant/list", true);
        }

        user.get().setEnabled(true);
        this.appService.save(user.get());

        return redirectWithMsg(session, "Merchant enabled","/merchant/list", false);
    }

    @GetMapping("/customer/delete")
    String customerMerchant(@RequestParam String id, HttpSession session){
        Optional<User> user = this.appService.getUserRepository().findById((Integer.parseInt(id)));

        if(user.isEmpty()){
            return redirectWithMsg(session, "User not found","/customer/list", true);
        }

        this.appService.getUserRepository().delete(user.get());

        return redirectWithMsg(session, "Customer deleted","/customer/list", false);
    }

    @GetMapping("/customer/disable")
    String disableCustomer(@RequestParam String id, HttpSession session, Model model){
        Optional<User> user = this.appService.getUserRepository().findById((Integer.parseInt(id)));

        if(user.isEmpty()){
            return redirectWithMsg(session, "User not found","/customer/list", true);
        }

        user.get().setEnabled(false);
        this.appService.save(user.get());

        return redirectWithMsg(session, "Customer disabled","/customer/list", false);
    }

    @GetMapping("/customer/enable")
    String enableCustomer(@RequestParam String id, HttpSession session){
        Optional<User> user = this.appService.getUserRepository().findById((Integer.parseInt(id)));

        if(user.isEmpty()){
            return redirectWithMsg(session, "User not found!","/customer/list", true);
        }
        user.get().setEnabled(true);
        this.appService.save(user.get());

        return redirectWithMsg(session, "Customer enabled","/customer/list", false);
    }

    public String redirectWithMsg(HttpSession session, String msg, String url, Boolean isError){
        String result = isError ? "error" : "success";
        session.setAttribute("msg", msg);
        return "redirect:" + url + "?" + result;
    }

    public Store getCurrentMerchantStore(){
        CustomUserDetails customUserDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return customUserDetails.getStore();
    }
}
