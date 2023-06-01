package com.wsda.common.Login;

import com.wsda.common.Base.BaseResponse;

public class LoginResponse extends BaseResponse {
    public LoginResponse(Integer APIStatusCode, Object APIMessage){
        super(APIStatusCode, APIMessage);
    }
}
