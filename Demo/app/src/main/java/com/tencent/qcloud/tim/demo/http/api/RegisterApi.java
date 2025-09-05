package com.tencent.qcloud.tim.demo.http.api;

import androidx.annotation.NonNull;

import com.hjq.http.config.IRequestApi;

public final class RegisterApi implements IRequestApi {

    @NonNull
    @Override
    public String getApi() {
        return "user/register";
    }

    private String username;
    private String password;
    private String code;
    private String inviteCode;

    public RegisterApi setUsername(String username) {
        this.username = username;
        return this;
    }

    public RegisterApi setPassword(String password) {
        this.password = password;
        return this;
    }

    public RegisterApi setCode(String code) {
        this.code = code;
        return this;
    }

    public RegisterApi setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
        return this;
    }
}