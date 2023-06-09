package com.muzio.controller;

import com.muzio.service.AppService;
import com.muzio.common.GetCreditCardList.*;
import com.muzio.common.GetMerchantsList.*;
import com.muzio.common.MerchantNew.*;
import com.muzio.model.*;
import com.muzio.repository.*;
import com.muzio.service.UserService;
import jakarta.servlet.http.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.*;

import com.muzio.common.Auth.*;
import com.muzio.common.GetCardBalance.*;
import com.muzio.common.Login.*;

@RequestMapping("/api")
@RestController
public class BackEndController {
    private static final SecureRandom secureRandom = new SecureRandom();
    private static final Base64.Encoder base64Encoder = Base64.getUrlEncoder();
    private static final Base64.Decoder base64Decoder = Base64.getDecoder();
    private final CreditCardRepository CreditCardRepository;
    private final UserRepository UserRepository;
    private final TokenRepository TokenRepository;
    private final AppService AppService;
    private final RoleRepository RoleRepository;
    private UserService userService;
    @Autowired
    BackEndController(
            CreditCardRepository CreditCardRepository,
            UserRepository UserRepository,
            TokenRepository TokenRepository,
            RoleRepository RoleRepository,
            AppService AppService
    ){
        this.CreditCardRepository = CreditCardRepository;
        this.UserRepository = UserRepository;
        this.TokenRepository = TokenRepository;
        this.RoleRepository = RoleRepository;
        this.AppService = AppService;
    }

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

    @PostMapping("/get_card_balance")
    GetCardBalanceResponse getCardBalance(GetCardBalanceRequest request){
        String cardNumber = request.cardNumber();
        List<CreditCard> credit_card_list = AppService.getCreditCards(null, cardNumber, null, null);
        if(!(credit_card_list.isEmpty())){
            CreditCard credit_card = credit_card_list.get(0);
            Integer balance = credit_card.getBalance();
            return new GetCardBalanceResponse(200, "The card's balance is : " + balance + "€");
        }
        else{
            return new GetCardBalanceResponse(404, "Credit card not found!");
        }
    }

    @PostMapping("/merchant/list")
    GetMerchantsListResponse creditCardList(GetMerchantsListRequest request){
        String token = request.token();
        if(!AppService.validateToken(token)){
            return new GetMerchantsListResponse(401, "Unauthorized");
        }
        Role role = RoleRepository.findByName("ROLE_MERCHANT");
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

//    @PostMapping("/login")
//    LoginResponse login(LoginRequest request, HttpSession session){
//        String base64 = request.credentials();
//        String credentials = new String(base64Decoder.decode(base64), StandardCharsets.UTF_8);
//        String[] login_credentials = credentials.split(":");
//        if(login_credentials.length == 0){
//            return new LoginResponse(400, "Invalid request");
//        }
//        Optional<User> result = UserRepository.findByUsername(login_credentials[0]);
//        if(result.isEmpty()){
//            return new LoginResponse(401, "Wrong credentials");
//        }
//        else{
//            User user = new User();
//            String token = generateNewToken();
//            session.setAttribute("login", true);
//            session.setAttribute("token", token);
//            Date now = new Date();
//            Date tomorrow = new Date();
//            Calendar c = Calendar.getInstance();
//            c.setTime(tomorrow);
//            c.add(Calendar.DATE, 1);
//            tomorrow = c.getTime();
//            Token token_entity = new Token();
//            token_entity.setUser(user);
//            token_entity.setLoggedIn(now);
//            token_entity.setExpiration(tomorrow);
//            token_entity.setToken(token);
//            TokenRepository.save(token_entity);
//            return new LoginResponse(200, token);
//        }
//    }

//    @PostMapping("/logout")
//    LogoutResponse logout(LogoutRequest request){
//        String token = request.token();
//        List<Token> activeTokenRes = TokenRepository.findTokenByTokenAndLoggedOutIsNull(token);
//        if((activeTokenRes.isEmpty())){
//           return new LogoutResponse(404, "Token not found");
//        }
//        Token activeToken = activeTokenRes.get(0);
//        Date now = new Date();
//        activeToken.setLoggedOut(now);
//        TokenRepository.save(activeToken);
//
//        return new LogoutResponse(200, "SUCCESS");
//    }

    @PostMapping ("/auth")
    AuthResponse auth(AuthRequest request){
        String token = request.token();
        List<Token> token_res = TokenRepository.findTokenByTokenAndLoggedOutIsNull(token);
        if(token_res.isEmpty()){
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

        if(!AppService.validateToken(token)){
            return new MerchantNewResponse(401, "Unauthorized");
        }

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
