package com.wsda.common.Base;

import java.util.Objects;

public class BaseRequest {
    private String token;
    public BaseRequest(String token){
        this.token = token;
    }

    public String token() {
        return token;
    }
}
