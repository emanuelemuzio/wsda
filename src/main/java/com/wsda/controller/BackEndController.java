package com.wsda.controller;

import com.wsda.common.GetCreditCardList.*;
import com.wsda.common.Logout.*;
import com.wsda.common.MerchantNew.*;
import com.wsda.model.*;
import com.wsda.repository.*;
import jakarta.servlet.http.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.web.bind.annotation.*;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.*;

import com.wsda.common.Auth.*;
import com.wsda.common.GetCardBalance.*;
import com.wsda.common.Login.*;
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
    private final WSDARoleRepository WSDARoleRepository;
    private String adminRole;
    private String merchantRole;
    @Autowired
    BackEndController(
            WSDACreditCardRepository WSDACreditCardRepository,
            WSDAUserRepository WSDAUserRepository,
            WSDATokenRepository WSDATokenRepository,
            WSDARoleRepository WSDARoleRepository,
            WSDAService WSDAService
    ){
        this.WSDACreditCardRepository = WSDACreditCardRepository;
        this.WSDAUserRepository = WSDAUserRepository;
        this.WSDATokenRepository = WSDATokenRepository;
        this.WSDARoleRepository = WSDARoleRepository;
        this.WSDAService = WSDAService;
        this.adminRole = "ROLE_ADMIN";
        this.merchantRole = "ROLE_MERCHANT";
    }

    public static void main(String[] args){
        SpringApplication.run(BackEndController.class, args);
    }

    @PostMapping("/get_card_balance")
    GetCardBalanceResponse getCardBalance(GetCardBalanceRequest request){
        String cardNumber = request.cardNumber();
        List<WSDACreditCard> credit_card_list = WSDAService.getCreditCards(null, cardNumber, null, null);
        if(!(credit_card_list.isEmpty())){
            WSDACreditCard credit_card = credit_card_list.get(0);
            Integer balance = credit_card.getBalance();
            return new GetCardBalanceResponse(200, "The card's balance is : " + balance + "€");
        }
        else{
            return new GetCardBalanceResponse(404, "Credit card not found!");
        }
    }

    @PostMapping("/get_credit_card_list")
    GetCreditCardListResponse creditCardList(GetCreditCardListRequest request){
        String token = request.token();
        if(!WSDAService.validateToken(token)){
            return new GetCreditCardListResponse(401, "Unauthorized");
        }
        WSDAUser user = WSDAService.getUserFromToken(token);
        WSDAUser userOwner = null;
        if(user.getWSDARole().getRole().equals(this.merchantRole)){
            userOwner = user;
        }
        List<WSDACreditCard> response = WSDAService.getCreditCards(null, null, null, userOwner);

        ArrayList formattedResponse = new ArrayList();

        for(WSDACreditCard c : response){
            HashMap<String, Object> item = new HashMap<>();
            item.put("ownerName",c.getWsda_user().getName());
            item.put("cardBalance",c.getBalance());
            item.put("cardNumber",c.getNumber());

            formattedResponse.add(item);
        }

        return new GetCreditCardListResponse(200, formattedResponse);
    }

    @PostMapping("/login")
    LoginResponse login(LoginRequest request, HttpSession session){
        String base64 = request.credentials();
        String credentials = new String(base64Decoder.decode(base64), StandardCharsets.UTF_8);
        String[] login_credentials = credentials.split(":");
        if(login_credentials.length == 0){
            return new LoginResponse(400, "Invalid request");
        }
        List<WSDAUser> result = WSDAUserRepository.findOneByEmailAndPassword(login_credentials[0], login_credentials[1]);
        if(result.isEmpty()){
            return new LoginResponse(401, "Wrong credentials");
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
            return new LoginResponse(200, token);
        }
    }

    @PostMapping("/logout")
    LogoutResponse logout(LogoutRequest request){
        String token = request.token();
        List<WSDAToken> activeTokenRes = WSDATokenRepository.findWSDATokenByTokenAndLoggedOutIsNull(token);
        if((activeTokenRes.isEmpty())){
           return new LogoutResponse(404, "Token not found");
        }
        WSDAToken activeToken = activeTokenRes.get(0);
        Date now = new Date();
        activeToken.setLoggedOut(now);
        WSDATokenRepository.save(activeToken);

        return new LogoutResponse(200, "SUCCESS");
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

    @PostMapping("/merchant/new")
    MerchantNewResponse merchantNew(MerchantNewRequest request){
        String name = request.name();
        String surname = request.surname();
        String email = request.email();
        String password = request.password();
        String token = request.token();

        if(!WSDAService.validateToken(token)){
            return new MerchantNewResponse(401, "Unauthorized");
        }

        WSDAUser user = new WSDAUser();
        WSDARole role = WSDARoleRepository.findWSDARoleByRole("ROLE_MERCHANT");

        user.setName(name + " " + surname);
        user.setEmail(email);
        user.setPassword(password);
        user.setWSDARole(role);
        WSDAUserRepository.save(user);

        return new MerchantNewResponse(200, "Success");
    }


    protected String generateNewToken(){
        byte[] randomBytes = new byte[24];
        secureRandom.nextBytes(randomBytes);
        return base64Encoder.encodeToString(randomBytes);
    }

}
