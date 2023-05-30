package com.wsda.common.Auth;

import com.wsda.common.Base.BaseResponse;

public class AuthResponse extends BaseResponse {
    public AuthResponse(Integer APIStatusCode, Object APIMessage){
        super(APIStatusCode, APIMessage);
    }
}
