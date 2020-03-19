package com.example.chronos.holder;

import android.util.Log;
import android.widget.TextView;

import com.example.chronos.R;
import com.example.chronos.database.RecentLink;
import com.netease.nim.uikit.common.ui.drop.DropFake;
import com.netease.nim.uikit.common.ui.recyclerview.adapter.BaseQuickAdapter;
import com.netease.nim.uikit.common.ui.recyclerview.holder.BaseViewHolder;
import com.netease.nim.uikit.common.ui.recyclerview.holder.RecyclerViewHolder;
import com.netease.nim.uikit.common.util.sys.TimeUtil;

public class RecentLinkHolder extends RecyclerViewHolder<BaseQuickAdapter, BaseViewHolder, RecentLink> {

    protected TextView DateTime;
    protected TextView NickName;
    protected TextView Message;
    protected DropFake UnreadTip;

    public RecentLinkHolder(BaseQuickAdapter adapter) {
        super(adapter);
    }

    @Override
    public void convert(BaseViewHolder holder, RecentLink data, int position, boolean isScrolling) {
        inflate(holder, data);
    }

    public void inflate(BaseViewHolder holder, final RecentLink recent) {
        Log.d("MainActivity", "使充气");
        this.DateTime = holder.getView(R.id.tv_date_time);
        this.NickName = holder.getView(R.id.tv_nickname);
        this.Message = holder.getView(R.id.tv_message);
        this.UnreadTip = holder.getView(R.id.unread_number_tip);
        DateTime.setText(TimeUtil.getTimeShowString(recent.getDateTime(), true));
        NickName.setText(recent.getNickName());
        Message.setText(recent.getMessage());
        UnreadTip.setText(String.valueOf(Math.min(recent.getUnreadTip(), 99)));
    }

    @Override
    public BaseQuickAdapter getAdapter() {
        return super.getAdapter();
    }
}
