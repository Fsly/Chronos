package com.example.chronos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.chronos.adapter.RecentLinkAdapter;
import com.example.chronos.database.RecentLink;
import com.netease.nim.uikit.common.fragment.TFragment;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class RecentContactsFragment extends TFragment {

    // view
    private RecyclerView recyclerView;

    // data
    private List<RecentLink> items;

    private RecentLinkAdapter adapter;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        findViews();
        initMessageList();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recent_contacts, container,
                false);
        return view;
    }

    /**
     * 查找页面控件
     */
    private void findViews() {
        recyclerView =  findView(R.id.recycler_view_rc);
    }

    /**
     * 初始化消息列表
     */
    private void initMessageList() {
        items = new ArrayList<>();
        items.add(new RecentLink(875996580,"靓仔","4567","想你~",881));
        // adapter
        adapter = new RecentLinkAdapter(recyclerView, items);
        //adapter.setCallback(callback);
        // recyclerView
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//        recyclerView.addOnItemTouchListener(touchListener);
//        // drop listener
//        DropManager.getInstance().setDropListener(new DropManager.IDropListener() {
//
//            @Override
//            public void onDropBegin() {
//                touchListener.setShouldDetectGesture(false);
//            }
//
//            @Override
//            public void onDropEnd() {
//                touchListener.setShouldDetectGesture(true);
//            }
//        });
    }
}
