package com.wsda.common.Login;

public record LoginRequest(String credentials) {
    public LoginRequest(String credentials){
        this.credentials = credentials;
    }

    @Override
    public String credentials() {
        return credentials;
    }
}
