package com.muzio.repository;

import com.muzio.model.CreditCard;
import com.muzio.model.Store;
import com.muzio.model.User;
import jakarta.annotation.Nullable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface CreditCardRepository extends JpaRepository<CreditCard, Integer>{
    @Nullable
    CreditCard findCreditCardByNumber(String number);
    CreditCard findTopByOrderByIdDesc();
    List<CreditCard> findCreditCardsByOwnerNull();
    List<CreditCard> findCreditCardsByOwnerNullAndStoreAndEnabled(Store store, Integer enabled);
    List<CreditCard> findCreditCardsByOwner(User owner);
}