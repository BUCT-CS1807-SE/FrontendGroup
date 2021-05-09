package com.example.myapplication.xpopup;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.adapter.NearMuseumAdapter;
import com.example.myapplication.entity.NearMuseumEntity;
import com.lxj.xpopup.core.BottomPopupView;
import com.lxj.xpopup.util.XPopupUtils;


import java.util.ArrayList;
import java.util.List;

public class MapBottom extends BottomPopupView {
    RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    public MapBottom(@NonNull Context context) {
        super(context);
    }
    @Override
    protected int getImplLayoutId() {
        return R.layout.mapbottom;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        recyclerView = findViewById(R.id.recyclerView);
        linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        List<NearMuseumEntity> datas =new ArrayList<>();
        for(int i=0;i<8;i++)
        {
            NearMuseumEntity nearM=new NearMuseumEntity();
            nearM.setMuseumName("博物馆"+i);
            nearM.setLevel(4.8);
            nearM.setOpenTime("全天");
            nearM.setTicker("80元");
            datas.add(nearM);
        }
        NearMuseumAdapter nearMuseumAdapter = new NearMuseumAdapter(getContext(),datas);
        recyclerView.setAdapter(nearMuseumAdapter);
    }



    // 最大高度为Window的0.85
    @Override
    protected int getMaxHeight() {
        return (int) (XPopupUtils.getScreenHeight(getContext())*.85f);
    }
}
