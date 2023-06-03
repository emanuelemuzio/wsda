package com.wsda.common.GetMerchantsList;

public record GetMerchantsListRequest(String token) {
    public GetMerchantsListRequest(String token){
        this.token = token;
    }

    @Override
    public String token() {
        return token;
    }
}
