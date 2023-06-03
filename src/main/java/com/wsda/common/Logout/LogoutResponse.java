package com.wsda.common.Logout;

import com.wsda.common.Base.BaseResponse;

public class LogoutResponse extends BaseResponse {
    public LogoutResponse(Integer APIStatusCode, Object APIMessage){
        super(APIStatusCode, APIMessage);
    }
}
