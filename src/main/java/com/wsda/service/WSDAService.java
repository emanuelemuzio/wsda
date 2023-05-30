package com.wsda.service;

import com.wsda.model.*;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootApplication
public class WSDAService {
    @Autowired
    private EntityManager entityManager;

    public WSDAService(EntityManager entityManager){
        this.entityManager = entityManager;
    }

    public List<WSDACreditCard> getCreditCards(
        Integer id,
        String number,
        Integer balance
    ){
        String sql =
                "SELECT wsda_cc " +
                        "FROM WSDACreditCard wsda_cc " +
                        "WHERE wsda_cc.id IS NOT NULL ";

        HashMap<String, Object> params = new HashMap<String, Object>();

        if(id != null){
            sql += " AND wsda_cc.id = :id";
            params.put("id", id);
        }

        if(number != null){
            sql += "AND wsda_cc.number LIKE :number";
            params.put("number", number);
        }

        if(balance != null){
            sql += "AND wsda_cc.balance = :balance";
            params.put("balance", balance);
        }

        TypedQuery<WSDACreditCard> query = entityManager.createQuery(sql, WSDACreditCard.class);

        setParams(params, query);

        System.out.println(query);

        List<WSDACreditCard> result = query.getResultList();

        return result;
    }

    public void setParams(HashMap<String, Object>params, TypedQuery q){
        for(Map.Entry<String, Object> entry : params.entrySet()){
            q.setParameter(entry.getKey(), entry.getValue());
        }
    }
}
