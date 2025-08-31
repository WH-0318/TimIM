package com.tencent.qcloud.tim.demo.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.tencent.cloud.tuikit.roomkit.view.basic.PrepareView;
import com.tencent.qcloud.tim.demo.R;
import com.trtc.tuikit.common.util.ScreenUtil;

public class ConferenceHomeFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup root = new FrameLayout(requireContext());
        PrepareView prepareView = new PrepareView(getContext(), true);
        prepareView.findViewById(com.tencent.cloud.tuikit.roomkit.R.id.img_back).setVisibility(View.GONE);
        prepareView.findViewById(com.tencent.cloud.tuikit.roomkit.R.id.img_head_prepare).setVisibility(View.GONE);
        prepareView.findViewById(com.tencent.cloud.tuikit.roomkit.R.id.tv_name_prepare).setVisibility(View.GONE);
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) prepareView.findViewById(com.tencent.cloud.tuikit.roomkit.R.id.ll_enter_room).getLayoutParams();
        params.topMargin = ScreenUtil.dip2px(24);
        root.addView(prepareView);
        return root;
    }
}
