package com.muzio.common.Base;

import java.util.Objects;

public class BaseResponse {
    private Integer apiStatusCode;
    private Object apiMessage;
    public BaseResponse(Integer apiStatusCode, Object apiMessage){
        this.apiStatusCode = apiStatusCode;
        this.apiMessage = apiMessage;
    }

    public Integer getApiStatusCode() {
        return apiStatusCode;
    }

    public void setApiStatusCode(Integer apiStatusCode) {
        this.apiStatusCode = apiStatusCode;
    }

    public Object getApiMessage() {
        return apiMessage;
    }

    public void setApiMessage(Object apiMessage) {
        this.apiMessage = apiMessage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaseResponse that = (BaseResponse) o;
        return Objects.equals(apiStatusCode, that.apiStatusCode) && Objects.equals(apiMessage, that.apiMessage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(apiStatusCode, apiMessage);
    }

    @Override
    public String toString() {
        return "BaseResponse{" +
                "apiStatusCode=" + apiStatusCode +
                ", apiMessage=" + apiMessage +
                '}';
    }
}
