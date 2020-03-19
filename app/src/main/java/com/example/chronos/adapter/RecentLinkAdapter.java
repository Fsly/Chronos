package com.example.chronos.adapter;

import com.example.chronos.R;
import com.example.chronos.database.RecentLink;
import com.example.chronos.holder.RecentLinkHolder;
import com.netease.nim.uikit.common.ui.recyclerview.adapter.BaseMultiItemQuickAdapter;
import com.netease.nim.uikit.common.ui.recyclerview.holder.BaseViewHolder;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class RecentLinkAdapter extends BaseMultiItemQuickAdapter<RecentLink, BaseViewHolder> {
    public RecentLinkAdapter(RecyclerView recyclerView, List<RecentLink> data) {
        super(recyclerView, data);
        addItemType(0, R.layout.recent_contact_list_item, RecentLinkHolder.class);
    }

    @Override
    protected int getViewType(RecentLink item) {
        return 0;
    }

    @Override
    protected String getItemKey(RecentLink item) {
        return null;
    }
}
