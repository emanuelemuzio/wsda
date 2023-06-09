package com.muzio.service;

import com.muzio.model.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.security.core.userdetails.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@org.springframework.stereotype.Service
public class AppService {
    private EntityManager entityManager;

    public AppService(EntityManager entityManager){
        this.entityManager = entityManager;
    }

    public void getUserFromToken(String token){
        String sql = "SELECT t FROM Token t WHERE t.token = :token";
        TypedQuery<Token> query = entityManager.createQuery(sql, Token.class);
        query.setParameter("token", token);

        Token tokenEntity = query.getSingleResult();
//        return tokenEntity.getUser();
    }

    public List<CreditCard> getCreditCards(
        Integer id,
        String number,
        Integer balance,
        User userOwner
    ){
        String sql =
                "SELECT wsda_cc " +
                        "FROM CreditCard wsda_cc " +
                        "WHERE wsda_cc.id IS NOT NULL ";

        HashMap<String, Object> params = new HashMap<String, Object>();

        if(id != null){
            sql += " AND wsda_cc.id = :id ";
            params.put("id", id);
        }

        if(number != null){
            sql += "AND wsda_cc.number LIKE :number ";
            params.put("number", number);
        }

        if(balance != null){
            sql += "AND wsda_cc.balance = :balance ";
            params.put("balance", balance);
        }

        if(userOwner != null){
            sql += "AND wsda_cc.wsda_user = :userOwner";
            params.put("userOwner", userOwner);
        }

        TypedQuery<CreditCard> query = entityManager.createQuery(sql, CreditCard.class);

        setParams(params, query);

        List<CreditCard> result = query.getResultList();

        return result;
    }

    public Boolean validateToken(String token){
        String sql =
                "SELECT t " +
                        "FROM Token t " +
                        "WHERE t.loggedOut IS NULL ";

        HashMap<String, Object> params = new HashMap<String, Object>();

        TypedQuery<Token> query = entityManager.createQuery(sql, Token.class);

        setParams(params, query);

        List<Token> result = query.getResultList();

        return !result.isEmpty();
    }

    public void setParams(HashMap<String, Object>params, TypedQuery q){
        for(Map.Entry<String, Object> entry : params.entrySet()){
            q.setParameter(entry.getKey(), entry.getValue());
        }
    }
}
