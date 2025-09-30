package com.tencent.qcloud.tim.demo.utils;

import android.graphics.Color;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextWatcher;
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
import com.tencent.qcloud.tim.demo.profile.ProtocolActivity;
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
                ProtocolActivity.enter(widget.getContext(), ProtocolActivity.PROTOCOL_TYPE_PRIVACY);
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
                ProtocolActivity.enter(widget.getContext(), ProtocolActivity.PROTOCOL_TYPE_USER);
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

    public static void updateTextStyle(TextView textView, EditText... editTexts) {
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                boolean allFilled = true;
                for(EditText editText: editTexts) {
                    if (TextUtils.isEmpty(editText.getText())) {
                        allFilled = false;
                        break;
                    }
                }
                if (allFilled) {
                    textView.setEnabled(true);
                    textView.setAlpha(1f);
                } else {
                    textView.setEnabled(false);
                    textView.setAlpha(0.3f);
                }
            }
        };
        for(EditText editText: editTexts) {
            editText.addTextChangedListener(textWatcher);
        }
        textView.setEnabled(false);
        textView.setAlpha(0.3f);
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
