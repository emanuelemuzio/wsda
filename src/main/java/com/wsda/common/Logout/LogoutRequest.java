package com.wsda.common.Logout;

public record LogoutRequest(String token) {
    public LogoutRequest(String token){
        this.token = token;
    }

    @Override
    public String token() {
        return token;
    }
}
