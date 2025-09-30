package com.tencent.qcloud.tim.demo.profile;

import android.content.Context;
import android.content.Intent;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.tencent.qcloud.tim.demo.R;
import com.tencent.qcloud.tuikit.timcommon.component.TitleBarLayout;
import com.tencent.qcloud.tuikit.timcommon.component.activities.BaseLightActivity;

public class ProtocolActivity extends BaseLightActivity {

    public static final int PROTOCOL_TYPE_PRIVACY = 1;
    public static final int PROTOCOL_TYPE_USER = 2;
    public static final int PROTOCOL_TYPE_STATEMENT = 3;
    public static final int PROTOCOL_TYPE_PERSONAL_INFO = 4;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_protocol_layout);
        TitleBarLayout titleBarLayout = findViewById(R.id.title_bar);
        WebView webView = findViewById(R.id.web_view);
        titleBarLayout.getLeftIcon().setOnClickListener(view -> finish());
        int titleResId = 0;
        String fileName = "";
        int protocolType = getIntent().getIntExtra("protocolType", PROTOCOL_TYPE_PRIVACY);
        switch (protocolType) {
            case PROTOCOL_TYPE_PRIVACY:
                titleResId = R.string.im_privacy;
                fileName = "privacy_protocol.html";
                break;
            case PROTOCOL_TYPE_USER:
                titleResId = R.string.im_user_agreement;
                fileName = "user_agreement.html";
                break;
            case PROTOCOL_TYPE_STATEMENT:
                titleResId = R.string.im_statement;
                fileName = "statement_protocol.html";
                break;
            case PROTOCOL_TYPE_PERSONAL_INFO:
                titleResId = R.string.self_infomation_collection_list;
                fileName = "personal_info_protocol.html";
                break;
        }
        titleBarLayout.getMiddleTitle().setText(getString(titleResId));
        initWebView(webView);
        webView.loadUrl("file:///android_asset/" + fileName);
    }

    private void initWebView(WebView webView) {
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);
        settings.setDisplayZoomControls(false);
        settings.setDomStorageEnabled(true);
        settings.setAllowFileAccess(true);
        settings.setLoadsImagesAutomatically(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setDefaultTextEncodingName("UTF-8");

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.cancel();
                if (isFinishing() || isDestroyed()) {
                    return;
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(ProtocolActivity.this);
                builder.setMessage("证书验证失败");
                builder.setCancelable(false);
                builder.setNegativeButton(getString(android.R.string.ok), (dialog, which) -> finish());
                AlertDialog dialog = builder.create();
                dialog.show();
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
            }
        });

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String webTitle) {
                super.onReceivedTitle(view, webTitle);
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
            }
        });
    }

    public static void enter(Context context, int protocolType) {
        Intent intent = new Intent(context, ProtocolActivity.class);
        intent.putExtra("protocolType", protocolType);
        context.startActivity(intent);
    }
}
