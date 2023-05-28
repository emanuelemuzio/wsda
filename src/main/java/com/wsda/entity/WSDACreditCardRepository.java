package com.wsda.entity;

import com.wsda.entity.WSDACreditCard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WSDACreditCardRepository extends JpaRepository<WSDACreditCard, Integer>{
    List<WSDACreditCard> findWSDACreditCardByNumber(String number);
}