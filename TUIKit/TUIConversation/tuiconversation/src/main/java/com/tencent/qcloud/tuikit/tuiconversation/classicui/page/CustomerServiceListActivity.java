package com.tencent.qcloud.tuikit.tuiconversation.classicui.page;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.hjq.http.EasyHttp;
import com.hjq.http.listener.OnHttpListener;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.imsdk.v2.V2TIMUserFullInfo;
import com.tencent.imsdk.v2.V2TIMValueCallback;
import com.tencent.qcloud.tuikit.timcommon.component.TitleBarLayout;
import com.tencent.qcloud.tuikit.timcommon.component.activities.BaseLightActivity;
import com.tencent.qcloud.tuikit.timcommon.component.impl.GlideEngine;
import com.tencent.qcloud.tuikit.timcommon.model.HttpData;
import com.tencent.qcloud.tuikit.timcommon.util.BusinessHelper;
import com.tencent.qcloud.tuikit.tuiconversation.R;
import com.tencent.qcloud.tuikit.tuiconversation.api.CustomerServiceInfoApi;
import com.tencent.qcloud.tuikit.tuiconversation.bean.ConversationInfo;
import com.tencent.qcloud.tuikit.tuiconversation.classicui.util.TUIConversationUtils;

import java.util.ArrayList;
import java.util.List;

public class CustomerServiceListActivity extends BaseLightActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_service_list_layout);
        RecyclerView rvCustomerService = findViewById(R.id.rv_customer_service);
        ((TitleBarLayout)findViewById(R.id.title_bar)).getLeftIcon().setOnClickListener(view -> finish());
        rvCustomerService.addItemDecoration(new DividerItemDecoration(this, RecyclerView.VERTICAL));
        CustomerServiceAdapter adapter = new CustomerServiceAdapter(R.layout.item_customer_service_layout);
        rvCustomerService.setAdapter(adapter);
        EasyHttp.get(this)
                .api(new CustomerServiceInfoApi())
                .request(new OnHttpListener<HttpData<List<CustomerServiceInfoApi.Bean>>>() {
                    @Override
                    public void onHttpSuccess(@NonNull HttpData<List<CustomerServiceInfoApi.Bean>> result) {
                        adapter.setList(result.getData());
                        updateUserRole(adapter);
                    }

                    @Override
                    public void onHttpFail(@NonNull Throwable throwable) {
                        ToastUtils.showLong(throwable.getMessage());
                    }
                });
    }

    private void updateUserRole(BaseQuickAdapter<CustomerServiceInfoApi.Bean, ?> adapter) {
        List<CustomerServiceInfoApi.Bean> customerServiceList = adapter.getData();
        if (customerServiceList.isEmpty()) {
            return;
        }

        List<String> idList = new ArrayList<>();
        for (CustomerServiceInfoApi.Bean item: customerServiceList) {
            idList.add(item.getId());
        }
        V2TIMManager.getInstance().getUsersInfo(idList, new V2TIMValueCallback<List<V2TIMUserFullInfo>>() {
            @Override
            public void onSuccess(List<V2TIMUserFullInfo> v2TIMUserFullInfos) {
                if (v2TIMUserFullInfos == null || v2TIMUserFullInfos.isEmpty()) {
                    return;
                }
                try {
                    int privateChatCount = customerServiceList.size();
                    int userInfoListCount = v2TIMUserFullInfos.size();
                    for (int i = 0; i < privateChatCount; i++) {
                        CustomerServiceInfoApi.Bean customerServiceInfo = customerServiceList.get(i);
                        for (int j = 0; j < userInfoListCount; j++) {
                            if (!TextUtils.isEmpty(customerServiceInfo.getId()) && customerServiceInfo.getId().equals(v2TIMUserFullInfos.get(j).getUserID())) {
                                customerServiceInfo.setRole(v2TIMUserFullInfos.get(j).getRole());
                                break;
                            }
                        }
                    }
                    adapter.notifyDataSetChanged();
                } catch (Exception e) {
                    e.printStackTrace(System.err);
                }
            }

            @Override
            public void onError(int code, String desc) {

            }
        });
    }

    private static class CustomerServiceAdapter extends BaseQuickAdapter<CustomerServiceInfoApi.Bean, BaseViewHolder> {

        public CustomerServiceAdapter(int layoutResId) {
            super(layoutResId);
        }

        @Override
        public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
            setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                    CustomerServiceInfoApi.Bean itemInfo = (CustomerServiceInfoApi.Bean)adapter.getData().get(position);
                    if (TextUtils.isEmpty(itemInfo.getId())) {
                        return;
                    }
                    ConversationInfo conversationInfo = new ConversationInfo();
                    conversationInfo.setId(itemInfo.getId());
                    TUIConversationUtils.startChatActivity(conversationInfo);
                }
            });
        }

        @Override
        protected void convert(@NonNull BaseViewHolder baseViewHolder, CustomerServiceInfoApi.Bean bean) {
            GlideEngine.loadImage(baseViewHolder.getView(R.id.iv_icon), bean.getAvatar());
            BusinessHelper.setDisplayNameWithRole(baseViewHolder.getView(R.id.tv_nickname), bean.getNickName(), bean.getRole());
        }
    }
}
