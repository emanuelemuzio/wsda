package com.wsda.controller;

import com.wsda.model.*;
import com.wsda.repository.*;
import jakarta.servlet.http.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.web.bind.annotation.*;
import jakarta.persistence.EntityManager;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import com.wsda.common.Auth.*;
import com.wsda.common.CardBalance.*;
import com.wsda.service.*;

@RequestMapping("/api")
@RestController
public class BackEndController {
    private static final SecureRandom secureRandom = new SecureRandom();
    private static final Base64.Encoder base64Encoder = Base64.getUrlEncoder();
    private static final Base64.Decoder base64Decoder = Base64.getDecoder();
    private final WSDACreditCardRepository WSDACreditCardRepository;
    private final WSDAUserRepository WSDAUserRepository;
    private final WSDATokenRepository WSDATokenRepository;
    private final WSDAService WSDAService;
    private EntityManager entityManager;

    @Autowired
    BackEndController(
            EntityManager entityManager,
            WSDACreditCardRepository WSDACreditCardRepository,
            WSDAUserRepository WSDAUserRepository,
            WSDATokenRepository WSDATokenRepository,
            WSDAService WSDAService
    ){
        this.entityManager = entityManager;
        this.WSDACreditCardRepository = WSDACreditCardRepository;
        this.WSDAUserRepository = WSDAUserRepository;
        this.WSDATokenRepository = WSDATokenRepository;
        this.WSDAService = WSDAService;
    }

    public static void main(String[] args){
        SpringApplication.run(BackEndController.class, args);
    }

    @PostMapping("/get_card_balance")
    CardBalanceResponse getCardBalance(CardBalanceRequest request){
        String cardNumber = request.cardNumber();
        List<WSDACreditCard> credit_card_list = WSDAService.getCreditCards(null, cardNumber, null);
        if(!(credit_card_list.isEmpty())){
            WSDACreditCard credit_card = credit_card_list.get(0);
            Integer balance = credit_card.getBalance();
            return new CardBalanceResponse(200, "The card's balance is : " + balance + "€");
        }
        else{
            return new CardBalanceResponse(404, "Credit card not found!");
        }
    }

    @PostMapping("/login")
    LoginResponse login(LoginRequest request, HttpSession session){
        String base64 = request.credentials;
        String credentials = new String(base64Decoder.decode(base64), StandardCharsets.UTF_8);
        String[] login_credentials = credentials.split(":");
        if(login_credentials.length == 0){
            return new LoginResponse(false, null);
        }
        List<WSDAUser> result = WSDAUserRepository.findOneByEmailAndPassword(login_credentials[0], login_credentials[1]);
        if(result.isEmpty()){
            return new LoginResponse(false, null);
        }
        else{
            WSDAUser wsda_user = (WSDAUser) result.get(0);
            String token = generateNewToken();
            session.setAttribute("login", true);
            session.setAttribute("token", token);
            Date now = new Date();
            Date tomorrow = new Date();
            Calendar c = Calendar.getInstance();
            c.setTime(tomorrow);
            c.add(Calendar.DATE, 1);
            tomorrow = c.getTime();
            WSDAToken token_entity = new WSDAToken();
            token_entity.setWSDAUser(wsda_user);
            token_entity.setLoggedIn(now);
            token_entity.setExpiration(tomorrow);
            token_entity.setToken(token);
            WSDATokenRepository.save(token_entity);
            return new LoginResponse(true, token_entity.getToken());
        }
    }

    @PostMapping ("/auth")
    AuthResponse auth(AuthRequest request){
        String token = request.token();
        List<WSDAToken> wsda_token = WSDATokenRepository.findWSDATokenByTokenAndLoggedOutIsNull(token);
        if(wsda_token.isEmpty()){
            return new AuthResponse(403, "Unauthorized user");
        }
        else{
            return new AuthResponse(200, "Authorized user");
        }
    }

    record LoginRequest(@RequestBody String credentials){}
    record LoginResponse(@ResponseBody Boolean login, String token){}

    protected String generateNewToken(){
        byte[] randomBytes = new byte[24];
        secureRandom.nextBytes(randomBytes);
        return base64Encoder.encodeToString(randomBytes);
    }
}
