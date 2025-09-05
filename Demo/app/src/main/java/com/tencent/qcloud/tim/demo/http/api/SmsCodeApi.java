package com.tencent.qcloud.tim.demo.http.api;

import androidx.annotation.NonNull;

import com.hjq.http.config.IRequestApi;

public final class SmsCodeApi implements IRequestApi {

    @NonNull
    @Override
    public String getApi() {
        return "user/noTokenSendMobileCode";
    }

    private String phone;
    private String countryCode = "+86";

    public SmsCodeApi setPhone(String phone) {
        this.phone = phone;
        return this;
    }
}