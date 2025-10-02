package com.tencent.qcloud.tuikit.timcommon.util;

import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;
import com.blankj.utilcode.util.SizeUtils;

public class BusinessHelper {

    public static void setDisplayNameWithRole(TextView textView, String displayName, int role) {
        textView.setTextColor(Color.parseColor("#333333"));
        String roleName = "";
        if (role == 1) {
            roleName = " 官方客服";
        }
        if (TextUtils.isEmpty(roleName)) {
            textView.setText(displayName);
            return;
        }

        textView.setTextColor(Color.RED);
        SpannableStringBuilder sb = new SpannableStringBuilder(displayName + roleName);
        int start = displayName.length() + 1;
        int end = sb.length();
        RoundedCenteredSizeSpan span = new RoundedCenteredSizeSpan(
                0.75f,
                Color.parseColor("#3370FF"),
                SizeUtils.dp2px(4),
                SizeUtils.dp2px(4),
                SizeUtils.dp2px(2)
        );
        sb.setSpan(span, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        sb.setSpan(new ForegroundColorSpan(Color.WHITE), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(sb);
    }
}
