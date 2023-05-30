package com.wsda.common.CardBalance;

public record CardBalanceRequest(String cardNumber) {
    public CardBalanceRequest(String cardNumber){
        this.cardNumber = cardNumber;
    }

    @Override
    public String cardNumber() {
        return cardNumber;
    }
}
