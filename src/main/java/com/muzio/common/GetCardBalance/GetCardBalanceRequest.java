package com.muzio.common.GetCardBalance;

public record GetCardBalanceRequest(String cardNumber) {
    public GetCardBalanceRequest(String cardNumber){
        this.cardNumber = cardNumber;
    }

    @Override
    public String cardNumber() {
        return cardNumber;
    }
}
