package com.tencent.qcloud.tuikit.tuiconversation.api;

import androidx.annotation.NonNull;

import com.hjq.http.config.IRequestApi;

import java.io.Serializable;

public final class CustomerServiceInfoApi implements IRequestApi {

    @NonNull
    @Override
    public String getApi() {
        return "user/getAdminList";
    }

    public static final class Bean implements Serializable {
        private String id;
        private String avatar;
        private String nickName;
        private int role;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public int getRole() {
            return role;
        }

        public void setRole(int role) {
            this.role = role;
        }
    }

}