package com.tencent.qcloud.tuikit.tuicontact.classicui.widget;

import android.graphics.Matrix;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.CollectionUtils;
import com.tencent.imsdk.v2.V2TIMGroupInfo;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.imsdk.v2.V2TIMUserFullInfo;
import com.tencent.imsdk.v2.V2TIMValueCallback;
import com.tencent.qcloud.tuicore.TUIConstants;
import com.tencent.qcloud.tuikit.timcommon.bean.GroupProfileBean;
import com.tencent.qcloud.tuikit.timcommon.component.impl.GlideEngine;
import com.tencent.qcloud.tuikit.timcommon.util.BusinessHelper;
import com.tencent.qcloud.tuikit.timcommon.util.ScreenUtil;
import com.tencent.qcloud.tuikit.tuicontact.R;
import com.tencent.qcloud.tuikit.tuicontact.bean.GroupMemberInfo;
import com.tencent.qcloud.tuikit.tuicontact.interfaces.GroupMemberGridListener;
import java.util.ArrayList;
import java.util.List;

public class GroupMemberGridAdapter extends RecyclerView.Adapter<GroupMemberGridAdapter.MyViewHolder> {
    private static final int OWNER_PRIVATE_MAX_LIMIT = 8;
    private static final int OWNER_PUBLIC_MAX_LIMIT = 9;
    private static final int OWNER_CHATROOM_MAX_LIMIT = 9;
    private static final int OWNER_COMMUNITY_MAX_LIMIT = 8;
    private static final int NORMAL_PRIVATE_MAX_LIMIT = 9;
    private static final int NORMAL_PUBLIC_MAX_LIMIT = 10;
    private static final int NORMAL_CHATROOM_MAX_LIMIT = 10;
    private static final int NORMAL_COMMUNITY_MAX_LIMIT = 9;

    private final List<GroupMemberInfo> groupMembers = new ArrayList<>();
    private GroupMemberGridListener groupMemberGridListener;
    private GroupProfileBean groupProfileBean;

    public GroupMemberInfo addItem = new GroupMemberInfo();
    public GroupMemberInfo removeItem = new GroupMemberInfo();

    public void setGroupMemberGridListener(GroupMemberGridListener groupMemberGridListener) {
        this.groupMemberGridListener = groupMemberGridListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.group_member_item_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final GroupMemberInfo groupMemberInfo = getItem(position);
        GlideEngine.loadImage(holder.memberIcon, groupMemberInfo.getFaceUrl());
        BusinessHelper.setDisplayNameWithRole(holder.memberName, groupMemberInfo.getDisplayName(), groupMemberInfo.getRole());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (groupMemberGridListener != null) {
                    groupMemberGridListener.onGroupMemberClicked(groupMemberInfo);
                }
            }
        });
        holder.memberIcon.setBackground(null);
        holder.memberIcon.setPaddingRelative(0, 0, 0, 0);
        int controlPadding = ScreenUtil.dip2px(15);
        if (groupMemberInfo == addItem) {
            holder.memberIcon.setImageResource(R.drawable.add_group_member);
            holder.memberIcon.setPaddingRelative(controlPadding, controlPadding, controlPadding, controlPadding);
            holder.memberIcon.setBackgroundResource(R.drawable.bottom_action_border);
        } else if (groupMemberInfo == removeItem) {
            holder.memberIcon.setImageResource(R.drawable.del_group_member);
            holder.memberIcon.setPaddingRelative(controlPadding, controlPadding, controlPadding, controlPadding);
            holder.memberIcon.setBackgroundResource(R.drawable.bottom_action_border);
        }
    }

    public GroupMemberInfo getItem(int i) {
        return groupMembers.get(i);
    }

    @Override
    public int getItemCount() {
        return groupMembers.size();
    }

    public void setGroupProfileBean(GroupProfileBean groupProfileBean) {
        this.groupProfileBean = groupProfileBean;
    }

    public void setGroupMembers(List<GroupMemberInfo> members) {
        groupMembers.clear();
        int shootMemberCount = 0;
        if (TextUtils.equals(groupProfileBean.getGroupType(), TUIConstants.GroupType.TYPE_PRIVATE)
            || TextUtils.equals(groupProfileBean.getGroupType(), TUIConstants.GroupType.TYPE_WORK)) {
            if (groupProfileBean.isOwner()) {
                shootMemberCount = members.size() > OWNER_PRIVATE_MAX_LIMIT ? OWNER_PRIVATE_MAX_LIMIT : members.size();
            } else {
                shootMemberCount = members.size() > NORMAL_PRIVATE_MAX_LIMIT ? NORMAL_PRIVATE_MAX_LIMIT : members.size();
            }
        } else if (TextUtils.equals(groupProfileBean.getGroupType(), TUIConstants.GroupType.TYPE_PUBLIC)) {
            if (groupProfileBean.isOwner()) {
                shootMemberCount = members.size() > OWNER_PUBLIC_MAX_LIMIT ? OWNER_PUBLIC_MAX_LIMIT : members.size();
            } else {
                shootMemberCount = members.size() > NORMAL_PUBLIC_MAX_LIMIT ? NORMAL_PUBLIC_MAX_LIMIT : members.size();
            }
        } else if (TextUtils.equals(groupProfileBean.getGroupType(), TUIConstants.GroupType.TYPE_CHAT_ROOM)
            || TextUtils.equals(groupProfileBean.getGroupType(), TUIConstants.GroupType.TYPE_MEETING)) {
            if (groupProfileBean.isOwner()) {
                shootMemberCount = members.size() > OWNER_CHATROOM_MAX_LIMIT ? OWNER_CHATROOM_MAX_LIMIT : members.size();
            } else {
                shootMemberCount = members.size() > NORMAL_CHATROOM_MAX_LIMIT ? NORMAL_CHATROOM_MAX_LIMIT : members.size();
            }
        } else if (TextUtils.equals(groupProfileBean.getGroupType(), TUIConstants.GroupType.TYPE_COMMUNITY)) {
            if (groupProfileBean.isOwner()) {
                shootMemberCount = members.size() > OWNER_COMMUNITY_MAX_LIMIT ? OWNER_COMMUNITY_MAX_LIMIT : members.size();
            } else {
                shootMemberCount = members.size() > NORMAL_COMMUNITY_MAX_LIMIT ? NORMAL_COMMUNITY_MAX_LIMIT : members.size();
            }
        }

        for (int i = 0; i < shootMemberCount; i++) {
            groupMembers.add(members.get(i));
        }
        if (groupProfileBean.getApproveOpt() != V2TIMGroupInfo.V2TIM_GROUP_ADD_FORBID) {
            groupMembers.add(addItem);
        }

        if (groupProfileBean.canManage()) {
            groupMembers.add(removeItem);
        }
        notifyDataSetChanged();
        updateGroupMemberInfo(groupMembers);
    }

    public void updateGroupMemberInfo(List<GroupMemberInfo> groupMemberList) {
        if (CollectionUtils.isEmpty(groupMemberList)) {
            return;
        }
        List<String> idList = new ArrayList<>();
        for (GroupMemberInfo item: groupMemberList) {
            idList.add(item.getUserId());
        }
        V2TIMManager.getInstance().getUsersInfo(idList, new V2TIMValueCallback<List<V2TIMUserFullInfo>>() {
            @Override
            public void onSuccess(List<V2TIMUserFullInfo> v2TIMUserFullInfos) {
                if (v2TIMUserFullInfos == null || v2TIMUserFullInfos.isEmpty()) {
                    return;
                }
                try {
                    int contactCount = groupMemberList.size();
                    int userInfoListCount = v2TIMUserFullInfos.size();
                    for (int i = 0; i < contactCount; i++) {
                        GroupMemberInfo contactInfo = groupMemberList.get(i);
                        for (int j = 0; j < userInfoListCount; j++) {
                            if (!TextUtils.isEmpty(contactInfo.getUserId()) && contactInfo.getUserId().equals(v2TIMUserFullInfos.get(j).getUserID())) {
                                contactInfo.setRole(v2TIMUserFullInfos.get(j).getRole());
                                break;
                            }
                        }
                    }
                    notifyDataSetChanged();
                } catch (Exception e) {
                    e.printStackTrace(System.err);
                }
            }

            @Override
            public void onError(int code, String desc) {

            }
        });
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private final ImageView memberIcon;
        private final TextView memberName;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            memberIcon = itemView.findViewById(R.id.group_member_icon);
            memberName = itemView.findViewById(R.id.group_member_name);
        }
    }
}
