package com.muzio.enums;

public enum TransactionType {
    PURCHASE("PURCHASE"),
    RECHARGE("RECHARGE");

    private String type;

    TransactionType(String type){
        this.type = type;
    }

    public String getRole(){
        return this.type;
    }
}
