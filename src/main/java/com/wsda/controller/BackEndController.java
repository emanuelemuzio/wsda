package com.wsda.controller;

import com.wsda.entity.WSDACreditCard;
import com.wsda.entity.WSDACreditCardRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@SpringBootApplication
@RestController
public class BackEndController {
    private final WSDACreditCardRepository credit_card_repo;

    BackEndController(WSDACreditCardRepository credit_card_repo){
        this.credit_card_repo = credit_card_repo;
    }

    public static void main(String[] args){
        SpringApplication.run(BackEndController.class, args);
    }

    @PostMapping("/get_card_balance")
    CardBalanceResponse home(CardBalanceRequest request){
        List<WSDACreditCard> credit_card_list = credit_card_repo.findWSDACreditCardByNumber(request.CardNumber);
        if(!(credit_card_list.isEmpty())){
            WSDACreditCard credit_card = credit_card_list.get(0);
            Integer balance = credit_card.getBalance();
            return new CardBalanceResponse(balance);
        }
        else{
            return new CardBalanceResponse(-1);
        }
    }

    record CardBalanceRequest(@RequestBody String CardNumber){
    }

    record CardBalanceResponse(@ResponseBody Integer balance){}
}
