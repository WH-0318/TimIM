package com.tencent.qcloud.tim.demo.login;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.hjq.http.EasyConfig;
import com.hjq.http.EasyHttp;
import com.hjq.http.listener.OnHttpListener;
import com.tencent.qcloud.tim.demo.R;
import com.tencent.qcloud.tim.demo.TIMAppService;
import com.tencent.qcloud.tim.demo.bean.UserInfo;
import com.tencent.qcloud.tim.demo.config.AppConfig;
import com.tencent.qcloud.tim.demo.http.api.LoginApi;
import com.tencent.qcloud.tuikit.timcommon.model.HttpData;
import com.tencent.qcloud.tim.demo.main.MainActivity;
import com.tencent.qcloud.tim.demo.main.MainMinimalistActivity;
import com.tencent.qcloud.tim.demo.utils.BusinessHelper;
import com.tencent.qcloud.tim.demo.utils.DemoLog;
import com.tencent.qcloud.tim.demo.utils.TUIUtils;
import com.tencent.qcloud.tuicore.interfaces.TUICallback;
import com.tencent.qcloud.tuicore.util.SPUtils;
import com.tencent.qcloud.tuicore.util.ToastUtil;
import com.tencent.qcloud.tuikit.timcommon.component.activities.BaseLightActivity;
import com.tencent.qcloud.tuikit.tuicallkit.common.data.Logger;

/**
 *
 * Login Activity
 * The username can be any non-blank character, but the premise is to modify the SDKAPPID and PRIVATEKEY in the code according to the following documents:
 * https://github.com/tencentyun/TIMSDK/tree/master/Android
 */

public class LoginForDevActivity extends BaseLightActivity {
    private static final String TAG = LoginForDevActivity.class.getSimpleName();
    private TextView mLoginView;
    private EditText mUserAccount;
    private EditText etPwd;
    private ImageView ivCheck;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initActivity();
    }

    private void initActivity() {
        setContentView(R.layout.login_for_dev_activity);
        View view = findViewById(android.R.id.content);
        view.setLayoutDirection(getResources().getConfiguration().getLayoutDirection());
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
            getWindow().setNavigationBarColor(Color.TRANSPARENT);
        }
        findViewById(R.id.tv_register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginForDevActivity.this, RegisterActivity.class));
            }
        });
        mLoginView = findViewById(R.id.tv_login);

        // https://github.com/tencentyun/TIMSDK/tree/master/Android
        mUserAccount = findViewById(R.id.et_account);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        etPwd = findViewById(R.id.et_pwd);
        BusinessHelper.setPwdVisible(findViewById(R.id.iv_visible), etPwd);
        BusinessHelper.initProtocol(findViewById(R.id.tv_protocol));
        ivCheck = findViewById(R.id.iv_check);
        ivCheck.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ivCheck.setSelected(!ivCheck.isSelected());
                    }
                }
        );
        mLoginView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login(mUserAccount.getText().toString(), etPwd.getText().toString());
            }
        });

        //mUserAccount.setText(UserInfo.getInstance().getUserId());
        BusinessHelper.updateTextStyle(mLoginView, mUserAccount, etPwd);

        findViewById(R.id.tv_forget_pwd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.toastShortMessage("忘记密码");
            }
        });
    }

    private void updateLoginTextStyle() {
        if (TextUtils.isEmpty(mUserAccount.getText()) || TextUtils.isEmpty(etPwd.getText().toString())) {
            mLoginView.setEnabled(false);
            mLoginView.setAlpha(0.3f);
        } else {
            mLoginView.setEnabled(true);
            mLoginView.setAlpha(1f);
        }
    }

    private void login(String account, String password) {
        if (!ivCheck.isSelected()) {
            com.trtc.tuikit.common.util.ToastUtil.toastShortMessage("请先阅读并同意隐私政策和用户协议");
            return;
        }
        realLogin(account, password);
    }

    private void realLogin(String account, String password) {
        EasyHttp.post(this)
                .api(new LoginApi()
                        .setUsername(account)
                        .setPassword(password)
                )
                .request(new OnHttpListener<HttpData<LoginApi.Bean>>() {
                    @Override
                    public void onHttpSuccess(@NonNull HttpData<LoginApi.Bean> result) {
                        LoginApi.Bean userInfo = result.getData();
                        if (userInfo != null) {
                            String token = userInfo.getTokenHead()  + userInfo.getToken();
                            EasyConfig.getInstance().addHeader("Authorization", token);
                            SPUtils.getInstance().put("token", token);
                            loginIM(userInfo);
                        } else {
                            com.trtc.tuikit.common.util.ToastUtil.toastShortMessage(result.getMessage());
                        }
                    }

                    @Override
                    public void onHttpFail(@NonNull Throwable throwable) {
                        com.trtc.tuikit.common.util.ToastUtil.toastShortMessage(throwable.getMessage());
                    }
                });
    }

    private void loginIM(LoginApi.Bean userInfo) {
        TIMAppService.getInstance().initBeforeLogin(0);
        mLoginView.setEnabled(false);
        final String userID = userInfo.getImAccountUsername();
        final String userSig = userInfo.getImUserSig();
        Logger.INSTANCE.e("out", "userSig===" + userSig);
        LoginWrapper.getInstance().loginIMSDK(
                LoginForDevActivity.this, AppConfig.DEMO_SDK_APPID, userID, userSig, TUIUtils.getLoginConfig(), new TUICallback() {
                    @Override
                    public void onError(final int code, final String desc) {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                ToastUtil.toastLongMessage(getString(R.string.failed_login_tip) + ", errCode = " + code + ", errInfo = " + desc);
                                mLoginView.setEnabled(true);
                            }
                        });
                        DemoLog.i(TAG, "imLogin errorCode = " + code + ", errorInfo = " + desc);
                    }

                    @Override
                    public void onSuccess() {
                        UserInfo.getInstance().setUserId(userID);
                        UserInfo.getInstance().setUserSig(userSig);
                        UserInfo.getInstance().setAutoLogin(true);
                        UserInfo.getInstance().setDebugLogin(true);
                        Intent intent;
                        if (AppConfig.DEMO_UI_STYLE == AppConfig.DEMO_UI_STYLE_CLASSIC) {
                            intent = new Intent(LoginForDevActivity.this, MainActivity.class);
                        } else {
                            intent = new Intent(LoginForDevActivity.this, MainMinimalistActivity.class);
                        }
                        startActivity(intent);

                        TIMAppService.getInstance().registerPushManually();

                        finish();
                    }
                });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
