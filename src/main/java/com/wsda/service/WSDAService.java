package com.wsda.service;

import com.wsda.model.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class WSDAService {
    private EntityManager entityManager;

    public WSDAService(EntityManager entityManager){
        this.entityManager = entityManager;
    }

    public WSDAUser getUserFromToken(String token){
        String sql = "SELECT t FROM WSDAToken t WHERE t.token = :token";
        TypedQuery<WSDAToken> query = entityManager.createQuery(sql, WSDAToken.class);
        query.setParameter("token", token);

        WSDAToken wsda_token = query.getSingleResult();
        return wsda_token.getWSDAUser();
    }

    public List<WSDACreditCard> getCreditCards(
        Integer id,
        String number,
        Integer balance,
        WSDAUser userOwner
    ){
        String sql =
                "SELECT wsda_cc " +
                        "FROM WSDACreditCard wsda_cc " +
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

        TypedQuery<WSDACreditCard> query = entityManager.createQuery(sql, WSDACreditCard.class);

        setParams(params, query);

        List<WSDACreditCard> result = query.getResultList();

        return result;
    }

    public void setParams(HashMap<String, Object>params, TypedQuery q){
        for(Map.Entry<String, Object> entry : params.entrySet()){
            q.setParameter(entry.getKey(), entry.getValue());
        }
    }
}
