package com.muzio.service;

import com.muzio.model.*;
import com.muzio.repository.CreditCardRepository;
import com.muzio.repository.RoleRepository;
import com.muzio.repository.StoreRepository;
import com.muzio.repository.UserRepository;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Getter
public class AppService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private StoreRepository storeRepository;
    @Autowired
    private CreditCardRepository creditCardRepository;

    public List getStores(){
        List<Store> storeEntityList = storeRepository.findByOrderByNameAsc();
        List<Map> storesList = new ArrayList();
        for(Store s : storeEntityList){
            HashMap<String, String> storeObj = this.createStoreObject(s);
            storesList.add(storeObj);
        }
        return storesList;
    }

    public CreditCard getLastCreditCard(){
        CreditCard lastCreditCard = this.creditCardRepository.findTopByOrderByIdDesc();
        return lastCreditCard;
    }

    public Role getRole(String role){
        Role roleEntity = this.roleRepository.findByName(role);
        return roleEntity;
    }

    public HashMap<String, String> createStoreObject(Store s){
        HashMap<String, String> storeObj = new HashMap<>();

        storeObj.put("value",s.getId().toString());
        storeObj.put("label", s.getName());

        return storeObj;
    }

    public List<CreditCard> getFreeCreditCards(){
        return this.creditCardRepository.findCreditCardsByOwnerNull();
    }

    public List<CreditCard> getFreeStoreCreditCards(Store store){
        return this.creditCardRepository.findCreditCardsByOwnerNullAndStoreAndEnabled(store, 0);
    }

    public List getCustomers(){
        Role customerRole = roleRepository.findByName("ROLE_CUSTOMER");
        List<User> customersEntityList = userRepository.findByRole(customerRole);
        return customersEntityList;
    }

    public List<User> getStoreCustomers(Store store){
        Role customerRole = roleRepository.findByName("ROLE_CUSTOMER");
        return this.userRepository.findUserByStoreAndRole(store, customerRole);
    }

    public CreditCard getCreditCardByNumber(String number){
        return this.creditCardRepository.findCreditCardByNumber(number);
    }

    public List<User> getMerchants(){
        Role merchantRole = roleRepository.findByName("ROLE_MERCHANT");
        return userRepository.findByRole(merchantRole);
    }

    public HashMap<String, String> createUserObject(User u){
        HashMap<String, String> userObj = new HashMap<>();

        userObj.put("value",u.getId().toString());
        userObj.put("label", u.getFirstName() + " " + u.getFirstName());

        return userObj;
    }

    public void save(User u){
        this.userRepository.save(u);
    }

    public void save(CreditCard c){
        this.creditCardRepository.save(c);
    }

    public void save(Store s){
        this.storeRepository.save(s);
    }
}
