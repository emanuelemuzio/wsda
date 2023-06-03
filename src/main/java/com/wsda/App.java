package com.wsda;

import com.wsda.model.WSDACreditCard;
import com.wsda.model.WSDARole;
import com.wsda.model.WSDAUser;
import com.wsda.repository.WSDACreditCardRepository;
import com.wsda.repository.WSDARoleRepository;
import com.wsda.repository.WSDAUserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    @Bean
    CommandLineRunner init(WSDARoleRepository wsdaRoleRepository, WSDAUserRepository wsdaUserRepository, WSDACreditCardRepository wsdaCreditCardRepository){
        return args -> {
            WSDARole adminRole = new WSDARole();
            adminRole.setRole("ROLE_ADMIN");

            wsdaRoleRepository.save(adminRole);

            WSDARole merchantRole = new WSDARole();
            merchantRole.setRole("ROLE_MERCHANT");

            wsdaRoleRepository.save(merchantRole);

            WSDAUser adminUser = new WSDAUser();
            adminUser.setWSDARole(adminRole);
            adminUser.setName("Emanuele Muzio");
            adminUser.setEmail("emanuelemuzio@hotmail.it");
            adminUser.setPassword("123");
            wsdaUserRepository.save(adminUser);

            WSDAUser merchantUser = new WSDAUser();
            merchantUser.setWSDARole(merchantRole);
            merchantUser.setName("Stefania Barraco");
            merchantUser.setEmail("barracostefania@outlook.it");
            merchantUser.setPassword("456");
            wsdaUserRepository.save(merchantUser);

            WSDACreditCard adminCreditCard = new WSDACreditCard();
            adminCreditCard.setBalance(500);
            adminCreditCard.setNumber("123");
            adminCreditCard.setWsda_user(adminUser);
            wsdaCreditCardRepository.save(adminCreditCard);

            WSDACreditCard merchantCreditCard = new WSDACreditCard();
            merchantCreditCard.setBalance(100);
            merchantCreditCard.setNumber("456");
            merchantCreditCard.setWsda_user(merchantUser);
            wsdaCreditCardRepository.save(merchantCreditCard);
        };
    }
}