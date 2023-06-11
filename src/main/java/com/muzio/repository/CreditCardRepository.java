package com.muzio.repository;

import com.muzio.model.CreditCard;
import jakarta.annotation.Nullable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface CreditCardRepository extends JpaRepository<CreditCard, Integer>{
    @Nullable
    CreditCard findCreditCardByNumber(String number);
    CreditCard findTopByOrderByIdDesc();
}