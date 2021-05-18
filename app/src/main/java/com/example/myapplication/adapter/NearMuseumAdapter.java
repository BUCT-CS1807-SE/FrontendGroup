package com.example.myapplication.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.activity.MusicPlayer;
import com.example.myapplication.activity.RouteActivity;
import com.example.myapplication.api.Api;
import com.example.myapplication.api.ApiConfig;
import com.example.myapplication.api.TtitCallback;
import com.example.myapplication.entity.NearMuseumEntity;
import com.example.myapplication.entity.RowsDTOXX;
import com.example.myapplication.entity.exhibitionItem;
import com.example.myapplication.entity.exhibitionResponse;
import com.example.myapplication.entity.exhtestEntity;
import com.example.myapplication.util.ImageUtils;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NearMuseumAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public Context mContext;
    List<NearMuseumEntity> Data;
    List<exhtestEntity> itemlist;
    private LinearLayoutManager linearLayoutManager;
    public  NearMuseumAdapter(Context context, List<NearMuseumEntity> data)
    {
        itemlist=new ArrayList<>();

        this.mContext=context;
        this.Data=data;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.info_near_museum,parent,false);
        ViewHolder viewHolder =new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolder vh=(ViewHolder) holder;
        NearMuseumEntity nearMuseumEntity=Data.get(position);
        linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        vh.showItem.setLayoutManager(linearLayoutManager);
        vh.LibName.setText(nearMuseumEntity.getMuseumName());
        vh.Lev.setText(nearMuseumEntity.getLevel());
        vh.open.setText(nearMuseumEntity.getOpenTime());
        vh.price.setText(nearMuseumEntity.getTicker());
        vh.exh.setText(nearMuseumEntity.getExhibitionName());
        Glide.with(vh.itemView)
                .load(ImageUtils.genURL(nearMuseumEntity.getMuseumName()))
                .centerCrop()
                .placeholder(R.drawable.ic_museum_explain)
                .into(vh.nearLibImage);

        itemlist=nearMuseumEntity.getExhItem();
        innerNearMuseumAdapter innernearMuseumAdapter = new innerNearMuseumAdapter(mContext,itemlist);
        vh.showItem.setAdapter(innernearMuseumAdapter);

//        vh.mediaPlayer.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent in = new Intent(mContext, MusicPlayer.class);
//                mContext.startActivity(in);
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return Data.size();
    }

    static  class ViewHolder extends RecyclerView.ViewHolder{
        private TextView LibName;
        private TextView open;
        private TextView Lev;
        private TextView price;
        private TextView exh;
        private LinearLayout mediaPlayer;
        private ImageView nearLibImage;
        private RecyclerView showItem;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            LibName=itemView.findViewById(R.id.textView10);
            open=itemView.findViewById(R.id.textView19);
            price=itemView.findViewById(R.id.textView17);
            Lev=itemView.findViewById(R.id.textView13);
            exh=itemView.findViewById(R.id.textView8);
            mediaPlayer=itemView.findViewById(R.id.mediaplayer);
            nearLibImage=itemView.findViewById(R.id.imageView4);
            showItem=itemView.findViewById(R.id.show);
        }
    }
}
