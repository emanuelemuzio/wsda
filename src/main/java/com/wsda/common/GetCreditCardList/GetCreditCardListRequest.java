package com.wsda.common.GetCreditCardList;

public record GetCreditCardListRequest(String token) {
    public GetCreditCardListRequest(String token){
        this.token = token;
    }

    @Override
    public String token() {
        return token;
    }
}
