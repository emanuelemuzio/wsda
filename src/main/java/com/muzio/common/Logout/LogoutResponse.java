package com.muzio.common.Logout;

import com.muzio.common.Base.BaseResponse;

public class LogoutResponse extends BaseResponse {
    public LogoutResponse(Integer APIStatusCode, Object APIMessage){
        super(APIStatusCode, APIMessage);
    }
}
