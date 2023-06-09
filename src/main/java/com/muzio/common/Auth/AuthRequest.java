package com.muzio.common.Auth;

public record AuthRequest(String token) {
    public AuthRequest(String token){
        this.token = token;
    }

    @Override
    public String token() {
        return token;
    }
}
