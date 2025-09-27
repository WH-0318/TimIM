package com.tencent.qcloud.tim.demo.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.hjq.http.EasyHttp;
import com.hjq.http.listener.OnHttpListener;
import com.tencent.cloud.tuikit.roomkit.view.basic.PrepareView;
import com.tencent.qcloud.tim.demo.http.api.UserInfoApi;
import com.tencent.qcloud.tuikit.timcommon.model.HttpData;
import com.trtc.tuikit.common.util.ScreenUtil;

public class ConferenceHomeFragment extends Fragment {

    private View crateRoomView;
    private View joinRoomView;
    private View scheduleView;
    private View ivJoinRoom;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup root = new FrameLayout(requireContext());
        PrepareView prepareView = new PrepareView(getContext(), true);
        prepareView.findViewById(com.tencent.cloud.tuikit.roomkit.R.id.img_back).setVisibility(View.GONE);
        prepareView.findViewById(com.tencent.cloud.tuikit.roomkit.R.id.img_head_prepare).setVisibility(View.GONE);
        prepareView.findViewById(com.tencent.cloud.tuikit.roomkit.R.id.tv_name_prepare).setVisibility(View.GONE);
        crateRoomView = prepareView.findViewById(com.tencent.cloud.tuikit.roomkit.R.id.ll_create_room);
        joinRoomView = prepareView.findViewById(com.tencent.cloud.tuikit.roomkit.R.id.ll_enter_room);
        scheduleView = prepareView.findViewById(com.tencent.cloud.tuikit.roomkit.R.id.ll_schedule_conference);
        ivJoinRoom = prepareView.findViewById(com.tencent.cloud.tuikit.roomkit.R.id.iv_join_room);
        crateRoomView.setVisibility(View.GONE);
        joinRoomView.setVisibility(View.GONE);
        scheduleView.setVisibility(View.GONE);
//        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) prepareView.findViewById(com.tencent.cloud.tuikit.roomkit.R.id.ll_enter_room).getLayoutParams();
//        params.topMargin = ScreenUtil.dip2px(24);
        root.addView(prepareView);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        EasyHttp.post(this)
                .api(new UserInfoApi())
                .request(new OnHttpListener<HttpData<UserInfoApi.Bean>>() {
                    @Override
                    public void onHttpSuccess(@NonNull HttpData<UserInfoApi.Bean> result) {
                        updateLayout(!result.getData().isAdmin());
                    }

                    @Override
                    public void onHttpFail(@NonNull Throwable throwable) {
                        updateLayout(false);
                    }
                });
    }

    private void updateLayout(boolean isAdmin) {
        if (isAdmin) {
            crateRoomView.setVisibility(View.VISIBLE);
            joinRoomView.setVisibility(View.VISIBLE);
            scheduleView.setVisibility(View.VISIBLE);
            ivJoinRoom.setVisibility(View.VISIBLE);
        } else {
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) joinRoomView.getLayoutParams();
            params.width = ScreenUtils.getScreenWidth() - SizeUtils.dp2px(40);
            params.height = SizeUtils.dp2px(56);
            params.topMargin = 0;
            joinRoomView.requestLayout();
            ivJoinRoom.setVisibility(View.GONE);
            joinRoomView.setVisibility(View.VISIBLE);
        }
    }
}
