package com.tencent.qcloud.tim.demo.http.server;

import androidx.annotation.NonNull;

import com.hjq.http.config.IRequestBodyStrategy;
import com.hjq.http.config.IRequestServer;
import com.hjq.http.config.impl.RequestJsonBodyStrategy;
import com.hjq.http.model.RequestBodyType;

/**
 *    author : Android 轮子哥
 *    github : https://github.com/getActivity/EasyHttp
 *    time   : 2019/05/19
 *    desc   : 正式环境
 */
public class ReleaseServer implements IRequestServer {

    @NonNull
    @Override
    public String getHost() {
        return "http://206.238.115.238:32222/";
    }

    @NonNull
    @Override
    public IRequestBodyStrategy getBodyType() {
        return RequestBodyType.JSON;
    }
}