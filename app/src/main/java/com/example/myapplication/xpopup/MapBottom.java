package com.example.myapplication.xpopup;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.adapter.NearMuseumAdapter;
import com.example.myapplication.entity.NearMuseumEntity;
import com.example.myapplication.entity.RowsDTO;
import com.lxj.xpopup.core.BottomPopupView;
import com.lxj.xpopup.util.XPopupUtils;


import java.util.ArrayList;
import java.util.List;

public class MapBottom extends BottomPopupView {
    RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private List<RowsDTO> nearMuseum;
    public MapBottom(@NonNull Context context, List<RowsDTO> neardatas) {
        super(context);
        nearMuseum=neardatas;
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
        String museumName;
        String museumLev;
        String museumOpen;
        Integer museumTicket;
        for(int i=0;i<nearMuseum.size();i++)
        {
            museumName=nearMuseum.get(i).getName();
            museumLev=nearMuseum.get(i).getMuseumlevel();
            museumOpen=nearMuseum.get(i).getOpeninghours();
            museumTicket=nearMuseum.get(i).getTicketprice();

            NearMuseumEntity nearM=new NearMuseumEntity();
            nearM.setMuseumName(museumName);
            if(museumLev==""||museumLev==null)
                nearM.setLevel("暂无");
            else
            nearM.setLevel(museumLev);
            if(museumOpen==""||museumOpen==null)
                nearM.setOpenTime("暂无");
            else
            nearM.setOpenTime(museumOpen);
            if(museumTicket==null)
                nearM.setTicker("暂无");
            else
            nearM.setTicker(museumTicket+"元");

            nearM.setImageUrl("8.140.136.108/coverpic/"+museumName+".jpg");
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
