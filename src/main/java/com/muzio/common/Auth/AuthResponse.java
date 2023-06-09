package com.muzio.common.Auth;

import com.muzio.common.Base.BaseResponse;

public class AuthResponse extends BaseResponse {
    public AuthResponse(Integer APIStatusCode, Object APIMessage){
        super(APIStatusCode, APIMessage);
    }
}
