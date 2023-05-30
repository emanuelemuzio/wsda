package com.wsda.repository;

import com.wsda.model.WSDACreditCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface WSDACreditCardRepository extends JpaRepository<WSDACreditCard, Integer>{
    List<WSDACreditCard> findWSDACreditCardByNumber(String number);
}