package com.example.myapplication.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.activity.MusicPlayer;
import com.example.myapplication.activity.RouteActivity;
import com.example.myapplication.entity.NearMuseumEntity;

import java.util.List;

public class NearMuseumAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public Context mContext;
    List<NearMuseumEntity> Data;
    public  NearMuseumAdapter(Context context, List<NearMuseumEntity> data)
    {
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
        vh.LibName.setText(nearMuseumEntity.getMuseumName());
        vh.Lev.setText(nearMuseumEntity.getLevel());
        vh.open.setText(nearMuseumEntity.getOpenTime());
        vh.price.setText(nearMuseumEntity.getTicker());
        vh.mediaPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(mContext, MusicPlayer.class);
                mContext.startActivity(in);
            }
        });
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
        private LinearLayout mediaPlayer;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            LibName=itemView.findViewById(R.id.textView10);
            open=itemView.findViewById(R.id.textView19);
            price=itemView.findViewById(R.id.textView17);
            Lev=itemView.findViewById(R.id.textView13);
            mediaPlayer=itemView.findViewById(R.id.mediaplayer);
        }
    }
}
