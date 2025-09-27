package com.tencent.qcloud.tuikit.tuiconversation.api;

import androidx.annotation.NonNull;

import com.hjq.http.config.IRequestApi;

public final class CustomerServiceInfoApi implements IRequestApi {

    @NonNull
    @Override
    public String getApi() {
        return "user/getAdminList";
    }

    public static final class Bean {
        private String id;
        private String avatar;
        private String nickName;

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
    }

}