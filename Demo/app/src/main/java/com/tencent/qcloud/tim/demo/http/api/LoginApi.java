package com.tencent.qcloud.tim.demo.http.api;

import androidx.annotation.NonNull;

import com.hjq.http.config.IRequestApi;

public final class LoginApi implements IRequestApi {

    @NonNull
    @Override
    public String getApi() {
        return "user/login";
    }

    private String username;
    private String password;

    public LoginApi setUsername(String username) {
        this.username = username;
        return this;
    }

    public LoginApi setPassword(String password) {
        this.password = password;
        return this;
    }

    public static final class Bean {

        private int isFaceAuth;
        private String imAccountUsername;
        private String imUserSig;
        private String token;
        private String tokenHead;

        public int getIsFaceAuth() {
            return isFaceAuth;
        }

        public String getImAccountUsername() {
            return imAccountUsername;
        }

        public String getImUserSig() {
            return imUserSig;
        }

        public String getToken() {
            return token;
        }

        public String getTokenHead() {
            return tokenHead;
        }
    }

}