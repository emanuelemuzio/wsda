package com.muzio.common.Base;

public class BaseRequest {
    private String token;
    public BaseRequest(String token){
        this.token = token;
    }

    public String token() {
        return token;
    }
}