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
        creditCard.setEnabled(0);
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

    @PostMapping("/merchant/list")
    GetMerchantsListResponse creditCardList(GetMerchantsListRequest request){
        String token = request.token();
        Role role = this.appService.getRoleRepository().findByName("ROLE_MERCHANT");
        User merchantsList = this.appService.getUserRepository().findByEmail("ciao");

        ArrayList formattedResponse = new ArrayList();

//        for(User u : merchantsList){
//            HashMap<String, Object> record = new HashMap<>();
//            record.put("name", u.getUsername());
//            record.put("email", u.getUsername());
//            formattedResponse.add(record);
//        }

        return new GetMerchantsListResponse(200, formattedResponse);
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
