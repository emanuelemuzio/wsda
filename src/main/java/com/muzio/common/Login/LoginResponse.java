package com.muzio.common.Login;

import com.muzio.common.Base.BaseResponse;

public class LoginResponse extends BaseResponse {
    public LoginResponse(Integer APIStatusCode, Object APIMessage){
        super(APIStatusCode, APIMessage);
    }
}
