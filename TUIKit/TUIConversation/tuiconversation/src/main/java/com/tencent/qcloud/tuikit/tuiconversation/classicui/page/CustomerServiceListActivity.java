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
import com.tencent.qcloud.tuikit.timcommon.component.activities.BaseLightActivity;
import com.tencent.qcloud.tuikit.timcommon.component.impl.GlideEngine;
import com.tencent.qcloud.tuikit.timcommon.model.HttpData;
import com.tencent.qcloud.tuikit.tuiconversation.R;
import com.tencent.qcloud.tuikit.tuiconversation.api.CustomerServiceInfoApi;
import com.tencent.qcloud.tuikit.tuiconversation.bean.ConversationInfo;
import com.tencent.qcloud.tuikit.tuiconversation.classicui.util.TUIConversationUtils;

import java.util.List;

public class CustomerServiceListActivity extends BaseLightActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_service_list_layout);
        RecyclerView rvCustomerService = findViewById(R.id.rv_customer_service);
        rvCustomerService.addItemDecoration(new DividerItemDecoration(this, RecyclerView.VERTICAL));
        CustomerServiceAdapter adapter = new CustomerServiceAdapter(R.layout.item_customer_service_layout);
        rvCustomerService.setAdapter(adapter);
        EasyHttp.get(this)
                .api(new CustomerServiceInfoApi())
                .request(new OnHttpListener<HttpData<List<CustomerServiceInfoApi.Bean>>>() {
                    @Override
                    public void onHttpSuccess(@NonNull HttpData<List<CustomerServiceInfoApi.Bean>> result) {
                        adapter.setList(result.getData());
                    }

                    @Override
                    public void onHttpFail(@NonNull Throwable throwable) {
                        ToastUtils.showLong(throwable.getMessage());
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
            baseViewHolder.setText(R.id.tv_nickname, bean.getNickName());
        }
    }
}
