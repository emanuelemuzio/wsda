package com.muzio.controller;

import ch.qos.logback.core.model.Model;
import com.muzio.service.AppService;
import com.muzio.common.GetMerchantsList.*;
import com.muzio.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RequestMapping("/api")
@Controller
public class BackEndController {
    @Autowired
    private AppService appService;

    public Authentication getActiveUser(){
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public static void main(String[] args){
        SpringApplication.run(BackEndController.class, args);
    }

    @PostMapping("/credit-card/new")
    String createNewCreditCard(@ModelAttribute CreditCard creditCard, Model model){
        creditCard.setNumber(creditCard.getNumber().replace("-",""));
        creditCard.setEnabled(1);
        this.appService.save(creditCard);
        return "redirect:/dashboard?success";
    }

    @PostMapping("/credit-card/block")
    String blockCreditCard(@RequestParam String number, @RequestParam String enable){
        Integer enableInt = Integer.parseInt(enable);
        CreditCard creditCard = this.appService.getCreditCardRepository().findCreditCardByNumber(number);
        if(creditCard == null){
            return "redirect:/credit-card/block?error=Credit card not found!";
        }
        if(creditCard.getEnabled() == enableInt){
            return "redirect:/credit-card/block?error=Credit already in that state!";
        }
        creditCard.setEnabled(enableInt);
        this.appService.save(creditCard);
        return "redirect:/dashboard?success";
    }

    @PostMapping("/merchant/new")
    String createMerchantNew(
            @ModelAttribute User newMerchant
            ){
        User existingUser = this.appService.getUserRepository().findByEmail(newMerchant.getEmail());
        if(existingUser != null){
            return "redirect:/merchant/new?error=This email is already in use!";
        }

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        Role merchantRole = this.appService.getRole("ROLE_MERCHANT");

        newMerchant.setRole(merchantRole);
        newMerchant.setEnabled(true);
        newMerchant.setPassword(encoder.encode(newMerchant.getPassword()));
        this.appService.save(newMerchant);

        return "redirect:/dashboard?success";
    }

    @PostMapping("/customer/bind-credit-card")
    String bindCreditCard(
            @RequestParam String userId,
            @RequestParam String creditCardId
    ){
        Optional <CreditCard> creditCard = this.appService.getCreditCardRepository().findById(Integer.parseInt(creditCardId));
        Optional <User> customer = this.appService.getUserRepository().findById(Integer.parseInt(userId));

        if(creditCard.isEmpty()){
            return "redirect:/customer/list?error=Credit card not found";
        }

        if(customer.isEmpty()){
            return "redirect:/customer/list?error=User not found";
        }

        creditCard.get().setOwner(customer.get());
        creditCard.get().setEnabled(1);
        this.appService.save(creditCard.get());

        return "redirect:/dashboard?success";
    }

    @PostMapping("/recharge-card")
    String rechargeCreditCard(
            @RequestParam String rechargeCreditCardId,
            @RequestParam String rechargeAmount
    ){
        Optional <CreditCard> creditCard = this.appService.getCreditCardRepository().findById(Integer.parseInt(rechargeCreditCardId));

        if(creditCard.isEmpty()){
            return "redirect:/credit-card/list?error=Credit card not found";
        }

        creditCard.get().setBalance(creditCard.get().getBalance() + Integer.parseInt(rechargeAmount));
        this.appService.save(creditCard.get());

        return "redirect:/dashboard?success";
    }

    @PostMapping("/charge-card")
    String chargeCreditCard(
            @RequestParam String purchaseCreditCardId,
            @RequestParam String purchaseAmount
    ){
        Optional <CreditCard> creditCard = this.appService.getCreditCardRepository().findById(Integer.parseInt(purchaseCreditCardId));

        if(creditCard.isEmpty()){
            return "redirect:/credit-card/list?error=Credit card not found";
        }

        creditCard.get().setBalance(creditCard.get().getBalance() - Integer.parseInt(purchaseAmount));
        this.appService.save(creditCard.get());

        return "redirect:/dashboard?success";
    }

    @PostMapping("/customer/new")
    String createCustomerNew(
            @ModelAttribute User newCustomer
    ){
        User existingUser = this.appService.getUserRepository().findByEmail(newCustomer.getEmail());
        if(existingUser != null){
            return "redirect:/customer/new?error=This email is already in use!";
        }

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        Role customerRole = this.appService.getRole("ROLE_CUSTOMER");

        newCustomer.setRole(customerRole);
        newCustomer.setEnabled(true);
        newCustomer.setPassword(encoder.encode(newCustomer.getPassword()));
        this.appService.save(newCustomer);

        return "redirect:/dashboard?success";
    }

    @GetMapping("/merchant/delete")
    String deleteMerchant(@RequestParam String id){
        Optional<User> user = this.appService.getUserRepository().findById((Integer.parseInt(id)));
        List<String> err = new ArrayList<>();

        user.ifPresentOrElse(
                merchant -> this.appService.getUserRepository().delete(merchant),
                () -> err.add("redirect:/merchant/list?error=User not found")
            );

        if(!err.isEmpty()){
            return err.get(0);
        }

        return "redirect:/dashboard?success";
    }

    @GetMapping("/merchant/disable")
    String disableMerchant(@RequestParam String id){
        Optional<User> user = this.appService.getUserRepository().findById((Integer.parseInt(id)));
        List<String> err = new ArrayList<>();

        user.ifPresentOrElse(
                merchant -> {
                    merchant.setEnabled(false);
                    this.appService.getUserRepository().save(merchant);
                },
                () -> err.add("redirect:/merchant/list?error=User not found")
        );

        if(!err.isEmpty()){
            return err.get(0);
        }

        return "redirect:/dashboard?success";
    }

    @GetMapping("/merchant/enable")
    String enableMerchant(@RequestParam String id){
        Optional<User> user = this.appService.getUserRepository().findById((Integer.parseInt(id)));
        List<String> err = new ArrayList<>();

        user.ifPresentOrElse(
                merchant -> {
                    merchant.setEnabled(true);
                    this.appService.getUserRepository().save(merchant);
                },
                () -> err.add("redirect:/merchant/list?error=User not found")
        );

        if(!err.isEmpty()){
            return err.get(0);
        }

        return "redirect:/dashboard?success";
    }

    @GetMapping("/customer/delete")
    String customerMerchant(@RequestParam String id){
        Optional<User> user = this.appService.getUserRepository().findById((Integer.parseInt(id)));
        List<String> err = new ArrayList<>();

        if(user.isPresent()){
            this.appService.getUserRepository().delete(user.get());
            return "redirect:/dashboard?success";
        }
        return "redirect:/customer/list?error=User not found";
    }

    @GetMapping("/customer/disable")
    String disableCustomer(@RequestParam String id){
        Optional<User> user = this.appService.getUserRepository().findById((Integer.parseInt(id)));
        List<String> err = new ArrayList<>();

        if(user.isPresent()){
            user.get().setEnabled(false);
            this.appService.save(user.get());
            return "redirect:/dashboard?success";
        }
        return "redirect:/customer/list?error=User not found";
    }

    @GetMapping("/customer/enable")
    String enableCustomer(@RequestParam String id){
        Optional<User> user = this.appService.getUserRepository().findById((Integer.parseInt(id)));
        List<String> err = new ArrayList<>();

        if(user.isPresent()){
            user.get().setEnabled(true);
            this.appService.save(user.get());
            return "redirect:/dashboard?success";
        }
        return "redirect:/customer/list?error=User not found";
    }

    @PostMapping("/merchant/list")
    String creditCardList(Model model){
        List<User> merchantsList = this.appService.getMerchants();

        return "/merchant/list";
    }

//    @PostMapping("/get_credit_card_list")
//    GetCreditCardListResponse creditCardList(GetCreditCardListRequest request){
//        String token = request.token();
//        if(!AppService.validateToken(token)){
//            return new GetCreditCardListResponse(401, "Unauthorized");
//        }
//        User user = AppService.getUserFromToken(token);
//        User userOwner = null;
//        if(user.getAuthorities().equals("MERCHANT")){
//            userOwner = user;
//        }
//        List<CreditCard> response = AppService.getCreditCards(null, null, null, userOwner);
//
//        ArrayList formattedResponse = new ArrayList();
//
//        for(CreditCard c : response){
//            HashMap<String, Object> item = new HashMap<>();
//            item.put("ownerName",c.getOwner().getUsername());
//            item.put("cardBalance",c.getBalance());
//            item.put("cardNumber",c.getNumber());
//
//            formattedResponse.add(item);
//        }
//
//        return new GetCreditCardListResponse(200, formattedResponse);
//    }
}
