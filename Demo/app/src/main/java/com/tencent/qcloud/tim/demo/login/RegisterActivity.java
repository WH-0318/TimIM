package com.tencent.qcloud.tim.demo.login;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.hjq.http.EasyHttp;
import com.hjq.http.listener.OnHttpListener;
import com.tencent.qcloud.tim.demo.R;
import com.tencent.qcloud.tim.demo.http.api.RegisterApi;
import com.tencent.qcloud.tim.demo.http.api.SmsCodeApi;
import com.tencent.qcloud.tuikit.timcommon.component.activities.BaseLightActivity;
import com.trtc.tuikit.common.util.ToastUtil;

public class RegisterActivity extends BaseLightActivity {

    private TextView tvGetCode;
    private int second = 60;
    private final Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            --second;
            if (second > 0) {
                tvGetCode.setText(second + "s");
                sendEmptyMessageDelayed(1, 1000);
            } else {
                second = 60;
                tvGetCode.setText("获取验证码");
                tvGetCode.setEnabled(true);
            }
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_layout);
        EditText etAccount = findViewById(R.id.et_account);
        EditText etPwd = findViewById(R.id.et_pwd);
        EditText etCode = findViewById(R.id.et_code);
        EditText etInviteCode = findViewById(R.id.et_invite_code);
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        findViewById(R.id.tv_register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register(etAccount.getText().toString(), etPwd.getText().toString(), etCode.getText().toString(), etInviteCode.getText().toString());
            }
        });
        tvGetCode = findViewById(R.id.tv_get_code);
        tvGetCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCode(etAccount.getText().toString());
            }
        });
    }

    private void getCode(String account) {
        if (TextUtils.isEmpty(account)) {
            ToastUtil.toastShortMessage("请先输入账号");
            return;
        }

        tvGetCode.setEnabled(false);
        EasyHttp.post(this)
                .api(new SmsCodeApi()
                        .setPhone(account)
                ).request(new OnHttpListener<Object>() {
                    @Override
                    public void onHttpSuccess(@NonNull Object result) {
                        ToastUtil.toastShortMessage("验证码已发送到您的手机，请注意查收~");
                        handler.sendEmptyMessage(1);
                    }

                    @Override
                    public void onHttpFail(@NonNull Throwable throwable) {
                        tvGetCode.setEnabled(true);
                        ToastUtil.toastShortMessage(throwable.getMessage());
                    }
                });
    }

    private void register(String account, String password, String code, String inviteCode) {
        if (TextUtils.isEmpty(account)) {
            ToastUtil.toastShortMessage("请先输入账号");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            ToastUtil.toastShortMessage("请先输入密码");
            return;
        }
        if (TextUtils.isEmpty(code)) {
            ToastUtil.toastShortMessage("请先输入验证码");
            return;
        }
        if (TextUtils.isEmpty(inviteCode)) {
            inviteCode = "1";
        }
        realRegister(account, password, code, inviteCode);
    }

    private void realRegister(String account, String password, String code, String inviteCode) {
        EasyHttp.post(this)
                .api(new RegisterApi()
                        .setUsername(account)
                        .setPassword(password)
                        .setCode(code)
                        .setInviteCode(inviteCode)
                ).request(new OnHttpListener<Object>() {
                    @Override
                    public void onHttpSuccess(@NonNull Object result) {
                        ToastUtil.toastShortMessage("注册成功");
                        finish();
                    }

                    @Override
                    public void onHttpFail(@NonNull Throwable throwable) {
                        ToastUtil.toastShortMessage(throwable.getMessage());
                    }
                });
    }
}
