package com.muzio.controller;

import ch.qos.logback.core.model.Model;
import com.muzio.service.AppService;
import com.muzio.common.GetMerchantsList.*;
import com.muzio.common.MerchantNew.*;
import com.muzio.model.*;
import com.muzio.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.SecureRandom;
import java.util.*;

import com.muzio.common.Auth.*;
import com.muzio.common.GetCardBalance.*;

@RequestMapping("/api")
@Controller
public class BackEndController {
    private static final SecureRandom secureRandom = new SecureRandom();
    private static final Base64.Encoder base64Encoder = Base64.getUrlEncoder();
    private static final Base64.Decoder base64Decoder = Base64.getDecoder();
    @Autowired
    private CreditCardRepository CreditCardRepository;
    @Autowired
    private UserRepository UserRepository;
    private AppService AppService;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private StoreRepository storeRepository;
    @Autowired
    private CreditCardRepository creditCardRepository;
    @Autowired
    private AppService appService;

    public Authentication getActiveUser(){
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public static void main(String[] args){
        SpringApplication.run(BackEndController.class, args);
    }

    @PostMapping("/")
    public String test(){
        return "/";
    }

//    @PostMapping("/get_card_balance")
//    GetCardBalanceResponse getCardBalance(GetCardBalanceRequest request){
//        String cardNumber = request.cardNumber();
//        List<CreditCard> credit_card_list = AppService.get(null, cardNumber, null, null);
//        if(!(credit_card_list.isEmpty())){
//            CreditCard credit_card = credit_card_list.get(0);
//            Integer balance = credit_card.getBalance();
//            return new GetCardBalanceResponse(200, "The card's balance is : " + balance + "€");
//        }
//        else{
//            return new GetCardBalanceResponse(404, "Credit card not found!");
//        }
//    }

    @PostMapping("/credit-card/new")
    String createNewCreditCard(@ModelAttribute CreditCard creditCard, Model model){
        creditCard.setNumber(creditCard.getNumber().replace("-",""));
        creditCardRepository.save(creditCard);
        return "redirect:/dashboard?success";
    }

    @PostMapping("/credit-card/block")
    String blockCreditCard(@RequestParam String number, @RequestParam String enable){
        Integer enableInt = Integer.parseInt(enable);
        CreditCard creditCard = creditCardRepository.findCreditCardByNumber(number);
        if(creditCard == null){
            return "redirect:/credit-card/block?error=Credit card not found!";
        }
        if(creditCard.getEnabled().equals(enableInt)){
            return "redirect:/credit-card/block?error=Credit already in that state!";
        }
        creditCard.setEnabled(enableInt);
        creditCardRepository.save(creditCard);
        return "redirect:/dashboard?success";
    }

    @PostMapping("/merchant/list")
    GetMerchantsListResponse creditCardList(GetMerchantsListRequest request){
        String token = request.token();
        Role role = roleRepository.findByName("ROLE_MERCHANT");
        User merchantsList = UserRepository.findByEmail("ciao");

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

    @PostMapping("/merchant/new")
    MerchantNewResponse merchantNew(MerchantNewRequest request){
        String name = request.name();
        String surname = request.surname();
        String email = request.email();
        String password = request.password();
        String token = request.token();

        try{
//            User user = new User();
//            Role role = RoleRepository.findRoleByRole("merchant");

//            UserRepository.save(user);
        }
        catch(DataIntegrityViolationException e){
            return new MerchantNewResponse(500, "Chosen email is unavailable");
        }

        return new MerchantNewResponse(200, "Merchant created, returning to dashboard");
    }

    protected String generateNewToken(){
        byte[] randomBytes = new byte[24];
        secureRandom.nextBytes(randomBytes);
        return base64Encoder.encodeToString(randomBytes);
    }

}
