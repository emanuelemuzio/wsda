package com.wsda.controller;

import com.sun.net.httpserver.HttpsServer;
import com.wsda.model.*;
import jakarta.servlet.http.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@SpringBootApplication
@RestController
public class BackEndController {
    private final WSDACreditCardRepository credit_card_repo;
    private final WSDAUserRepository user_repo;
    private final WSDATokenRepository token_repo;
    private static final SecureRandom secureRandom = new SecureRandom();
    private static final Base64.Encoder base64Encoder = Base64.getUrlEncoder();
    private static final Base64.Decoder base64Decoder = Base64.getDecoder();

    BackEndController(WSDACreditCardRepository credit_card_repo, WSDAUserRepository user_repo, WSDATokenRepository token_repo){
        this.credit_card_repo = credit_card_repo;
        this.user_repo = user_repo;
        this.token_repo = token_repo;
    }

    public static void main(String[] args){
        SpringApplication.run(BackEndController.class, args);
    }

    @PostMapping("/get_card_balance")
    CardBalanceResponse getCardBalance(CardBalanceRequest request){
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

    @PostMapping("/login")
    LoginResponse login(LoginRequest request, HttpSession session){
        String base64 = request.credentials;
        String credentials = new String(base64Decoder.decode(base64), StandardCharsets.UTF_8);
        String[] login_credentials = credentials.split(":");
        if(login_credentials.length == 0){
            return new LoginResponse(false, null);
        }
        List<WSDAUser> result = this.user_repo.findOneByEmailAndPassword(login_credentials[0], login_credentials[1]);
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
            this.token_repo.save(token_entity);
            return new LoginResponse(true, token_entity.getToken());
        }
    }

    @PostMapping ("/auth")
    AuthResponse auth(AuthRequest request){
        String token = request.token;
        List<WSDAToken> wsda_token = this.token_repo.findWSDATokenByTokenAndLoggedOutIsNull(token);
        return new AuthResponse(!wsda_token.isEmpty());
    }

    record AuthRequest(@RequestBody String token){}

    record AuthResponse(@ResponseBody Boolean login){}

    record CardBalanceRequest(@RequestBody String CardNumber){}

    record CardBalanceResponse(@ResponseBody Integer balance){}

    record LoginRequest(@RequestBody String credentials){}
    record LoginResponse(@ResponseBody Boolean login, String token){}

    protected String generateNewToken(){
        byte[] randomBytes = new byte[24];
        secureRandom.nextBytes(randomBytes);
        return base64Encoder.encodeToString(randomBytes);
    }
}
