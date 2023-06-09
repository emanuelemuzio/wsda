package com.muzio.common.MerchantNew;

import com.muzio.common.Base.BaseRequest;

public class MerchantNewRequest extends BaseRequest {
    public String name;
    public String surname;
    public String email;
    public String password;

    public MerchantNewRequest(String name, String surname, String email, String password, String token){
        super(token);
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.password = password;
    }

    public String name() {
        return name;
    }
    public String surname() {
        return surname;
    }
    public String email() {
        return email;
    }
    public String password() {
        return password;
    }
}
