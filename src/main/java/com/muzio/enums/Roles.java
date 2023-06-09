package com.muzio.enums;

public enum Roles {
    ADMIN("ROLE_ADMIN"),
    CUSTOMER("ROLE_CUSTOMER"),
    MERCHANT("ROLE_MERCHANT");

    private String role;

    Roles(String role){
        this.role = role;
    }

    public String getRole(){
        return this.role;
    }
}
