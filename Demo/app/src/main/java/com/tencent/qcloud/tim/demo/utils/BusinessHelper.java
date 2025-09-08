package com.tencent.qcloud.tim.demo.utils;

import android.graphics.Color;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.LinkMovementMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.tencent.qcloud.tim.demo.R;
import com.trtc.tuikit.common.util.ToastUtil;

public class BusinessHelper {

    public static void initProtocol(TextView tvProtocol) {
        SpannableStringBuilder builder = new SpannableStringBuilder();
        builder.append("我已阅读并同意");

        String privacyProtocol = "隐私政策";
        int start2 = builder.length();
        builder.append(privacyProtocol);
        int end2 = builder.length();
        builder.setSpan(new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                ToastUtil.toastShortMessage("隐私政策");
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(ContextCompat.getColor(tvProtocol.getContext(), R.color.colorPrimary));
                ds.setUnderlineText(false);
            }
        }, start2, end2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.append(" 和 ");
        String transferProtocol = "用户协议";
        int start1 = builder.length();
        builder.append(transferProtocol);
        int end1 = builder.length();
        builder.setSpan(new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                ToastUtil.toastShortMessage("用户协议");
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(ContextCompat.getColor(tvProtocol.getContext(), R.color.colorPrimary));
                ds.setUnderlineText(false);
            }
        }, start1, end1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvProtocol.setText(builder);
        tvProtocol.setHighlightColor(Color.TRANSPARENT);
        tvProtocol.setMovementMethod(LinkMovementMethod.getInstance());
    }

    public static void setPwdVisible(ImageView ivVisible, EditText etPwd) {
        ivVisible.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ivVisible.setSelected(!ivVisible.isSelected());
                if (ivVisible.isSelected()) {
                    etPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    etPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                etPwd.setSelection(etPwd.length());
            }
        });
    }
}
