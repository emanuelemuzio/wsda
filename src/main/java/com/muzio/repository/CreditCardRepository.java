package com.muzio.repository;

import com.muzio.model.CreditCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface CreditCardRepository extends JpaRepository<CreditCard, Integer>{
    List<CreditCard> findCreditCardByNumber(String number);
}