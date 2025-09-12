package com.tencent.qcloud.tim.demo.login;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import com.hjq.http.EasyHttp;
import com.hjq.http.listener.OnHttpListener;
import com.tencent.qcloud.tim.demo.R;
import com.tencent.qcloud.tim.demo.http.api.RegisterApi;
import com.tencent.qcloud.tim.demo.http.api.SmsCodeApi;
import com.tencent.qcloud.tim.demo.http.model.HttpData;
import com.tencent.qcloud.tim.demo.utils.BusinessHelper;
import com.tencent.qcloud.tuikit.timcommon.component.activities.BaseLightActivity;
import com.trtc.tuikit.common.util.ToastUtil;

public class RegisterActivity extends BaseLightActivity {

    private EditText etAccount, etCode, etPwd, etConfirmPwd, etInviteCode;
    private TextView tvGetCode;
    private TextView tvRegister;
    private ImageView ivCheck;
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
        etAccount = findViewById(R.id.et_account);
        etPwd = findViewById(R.id.et_pwd);
        etConfirmPwd = findViewById(R.id.et_confirm_pwd);
        etCode = findViewById(R.id.et_code);
        etInviteCode = findViewById(R.id.et_invite_code);
        ivCheck = findViewById(R.id.iv_check);
        tvRegister = findViewById(R.id.tv_register);

        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
            getWindow().setNavigationBarColor(Color.TRANSPARENT);
        }
        TextView tvLogin = findViewById(R.id.tv_login);
        BusinessHelper.setPwdVisible(findViewById(R.id.iv_visible), etPwd);
        BusinessHelper.setPwdVisible(findViewById(R.id.iv_confirm_visible), etConfirmPwd);
        BusinessHelper.initProtocol(findViewById(R.id.tv_protocol));
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register(etAccount.getText().toString(), etPwd.getText().toString(), etConfirmPwd.getText().toString(), etCode.getText().toString(), etInviteCode.getText().toString());
            }
        });
        tvGetCode = findViewById(R.id.tv_get_code);
        tvGetCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCode(etAccount.getText().toString());
            }
        });
        ivCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ivCheck.setSelected(!ivCheck.isSelected());
            }
        });
        initLoginTextStyle(tvLogin);
        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ivCheck.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ivCheck.setSelected(!ivCheck.isSelected());
                    }
                }
        );
        BusinessHelper.updateTextStyle(tvRegister, etAccount, etCode, etPwd, etConfirmPwd, etInviteCode);
    }

    private void initLoginTextStyle(TextView loginText) {
        SpannableStringBuilder builder = new SpannableStringBuilder();
        builder.append("我有账号，");

        String loginSt = "立即登录";
        int start = builder.length();
        builder.append(loginSt);
        int end = builder.length();
        builder.setSpan(new ForegroundColorSpan(ContextCompat.getColor(this, R.color.colorPrimary)), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        loginText.setText(builder);
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
                ).request(new OnHttpListener<HttpData<Object>>() {
                    @Override
                    public void onHttpSuccess(@NonNull HttpData<Object> result) {
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

    private void register(String account, String password, String confirmPwd, String code, String inviteCode) {
        if (password.length() < 6 || password.length() > 16) {
            ToastUtil.toastShortMessage("请输入有效的密码");
            return;
        }
        if (!password.equals(confirmPwd)) {
            ToastUtil.toastShortMessage("两次输入的密码不一致，请重新输入");
            return;
        }
        if (!ivCheck.isSelected()) {
            com.trtc.tuikit.common.util.ToastUtil.toastShortMessage("请先阅读并同意隐私政策和用户协议");
            return;
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
                ).request(new OnHttpListener<HttpData<Object>>() {
                    @Override
                    public void onHttpSuccess(@NonNull HttpData<Object> result) {
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
