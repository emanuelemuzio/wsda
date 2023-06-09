package com.muzio;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

//    @Bean
//    CommandLineRunner init(RoleRepository roleRepository, UserRepository userRepository, CreditCardRepository creditCardRepository){
//        return args -> {
//            Role adminRole = new Role();
//            adminRole.setRole("ROLE_ADMIN");
//
//            roleRepository.save(adminRole);
//
//            Role merchantRole = new Role();
//            merchantRole.setRole("ROLE_MERCHANT");
//
//            roleRepository.save(merchantRole);
//
//            User adminUser = new User();
//            adminUser.setRole(adminRole);
//            adminUser.setName("Emanuele Muzio");
//            adminUser.setEmail("emanuelemuzio@hotmail.it");
//            adminUser.setPassword("123");
//            userRepository.save(adminUser);
//
//            User merchantUser = new User();
//            merchantUser.setRole(merchantRole);
//            merchantUser.setName("Stefania Barraco");
//            merchantUser.setEmail("barracostefania@outlook.it");
//            merchantUser.setPassword("456");
//            userRepository.save(merchantUser);
//
//            CreditCard adminCreditCard = new CreditCard();
//            adminCreditCard.setBalance(500);
//            adminCreditCard.setNumber("123");
//            adminCreditCard.setUser(adminUser);
//            creditCardRepository.save(adminCreditCard);
//
//            CreditCard merchantCreditCard = new CreditCard();
//            merchantCreditCard.setBalance(100);
//            merchantCreditCard.setNumber("456");
//            merchantCreditCard.setUser(merchantUser);
//            creditCardRepository.save(merchantCreditCard);
//        };
//    }
}