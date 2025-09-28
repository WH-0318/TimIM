package com.tencent.qcloud.tuikit.tuicontact.api;

import androidx.annotation.NonNull;

import com.hjq.http.config.IRequestApi;

public final class FriendInfoApi implements IRequestApi {

    @NonNull
    @Override
    public String getApi() {
        return "user/getUserIdInfo";
    }

    private String id;
    public FriendInfoApi setId(String id) {
        this.id = id;
        return this;
    }

    public class Bean {
        private String id;
        private String mobile;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }
    }
}
