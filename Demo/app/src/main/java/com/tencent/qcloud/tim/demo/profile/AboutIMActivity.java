package com.tencent.qcloud.tim.demo.profile;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.blankj.utilcode.util.ActivityUtils;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.qcloud.tim.demo.R;
import com.tencent.qcloud.tim.demo.main.MainActivity;
import com.tencent.qcloud.tim.demo.utils.Constants;
import com.tencent.qcloud.tim.demo.utils.ProfileUtil;
import com.tencent.qcloud.tuicore.TUILogin;
import com.tencent.qcloud.tuicore.interfaces.TUICallback;
import com.tencent.qcloud.tuicore.util.ToastUtil;
import com.tencent.qcloud.tuikit.timcommon.component.LineControllerView;
import com.tencent.qcloud.tuikit.timcommon.component.TitleBarLayout;
import com.tencent.qcloud.tuikit.timcommon.component.activities.BaseLightActivity;
import com.tencent.qcloud.tuikit.timcommon.component.dialog.TUIKitDialog;
import com.tencent.qcloud.tuikit.timcommon.component.interfaces.ITitleBarLayout;

public class AboutIMActivity extends BaseLightActivity implements View.OnClickListener {
    private TitleBarLayout titleBarLayout;
    private LineControllerView sdkVersionLv;
    private LineControllerView aboutIMLv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_im);
        titleBarLayout = findViewById(R.id.about_im_title_bar);
        sdkVersionLv = findViewById(R.id.about_sdk_version_lv);

        aboutIMLv = findViewById(R.id.about_im_lv);
        setupViews();
    }

    private void setupViews() {
        titleBarLayout.getRightIcon().setVisibility(View.GONE);
        titleBarLayout.setTitle(getResources().getString(R.string.about_im), ITitleBarLayout.Position.MIDDLE);
        titleBarLayout.setOnLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        String sdkVersion = V2TIMManager.getInstance().getVersion();
        sdkVersionLv.setContent(sdkVersion);

        aboutIMLv.setOnClickListener(this);
        findViewById(R.id.about_im_privacy_lv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProtocolActivity.enter(AboutIMActivity.this, ProtocolActivity.PROTOCOL_TYPE_PRIVACY);
            }
        });
        findViewById(R.id.about_user_agreement_lv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProtocolActivity.enter(AboutIMActivity.this, ProtocolActivity.PROTOCOL_TYPE_USER);
            }
        });
        findViewById(R.id.about_statement_lv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProtocolActivity.enter(AboutIMActivity.this, ProtocolActivity.PROTOCOL_TYPE_STATEMENT);
            }
        });
        findViewById(R.id.self_infomation_collection_lv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProtocolActivity.enter(AboutIMActivity.this, ProtocolActivity.PROTOCOL_TYPE_PERSONAL_INFO);
            }
        });
        findViewById(R.id.cancel_account_lv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TUIKitDialog(AboutIMActivity.this)
                        .builder()
                        .setCancelable(true)
                        .setCancelOutside(true)
                        .setTitle(getString(R.string.cancel_account_tip))
                        .setDialogWidth(0.75f)
                        .setPositiveButton(getString(com.tencent.qcloud.tuicore.R.string.sure),
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        TUILogin.logout(new TUICallback() {
                                            @Override
                                            public void onSuccess() {
                                                ProfileUtil.onLogoutSuccess(AboutIMActivity.this);
                                                ActivityUtils.finishActivity(MainActivity.class);
                                            }

                                            @Override
                                            public void onError(int code, String desc) {
                                                ToastUtil.toastLongMessage("logout fail: " + code + "=" + desc);
                                            }
                                        });
                                    }
                                })
                        .setNegativeButton(getString(com.tencent.qcloud.tuicore.R.string.cancel),
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {}
                                })
                        .show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v == aboutIMLv) {
            startWebUrl(Constants.IM_ABOUT);
        }
    }

    private void startWebUrl(String url) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        Uri contentUrl = Uri.parse(url);
        intent.setData(contentUrl);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}