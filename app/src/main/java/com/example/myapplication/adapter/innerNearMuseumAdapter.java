package com.example.myapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.entity.exhtestEntity;

import java.util.List;

public class innerNearMuseumAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public Context mContext;
    List<exhtestEntity> Data;
    public  innerNearMuseumAdapter(Context context, List<exhtestEntity> data)
    {
        this.mContext=context;
        this.Data=data;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.info_near_libitem,parent,false);
        ViewHolder viewHolder =new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolder vh=(ViewHolder) holder;
        exhtestEntity exhibitionitem=Data.get(position);

        if(exhibitionitem.getName()!="")
        vh.exhItemName.setText(exhibitionitem.getName());

        if(exhibitionitem.getImageUrl()!=null)
        Glide.with(vh.itemView)
                .load("http://8.140.136.108/exhibitcollection/"+exhibitionitem.getImageUrl())
                .centerCrop()
                .placeholder(R.drawable.ic_museum_explain)
                .into(vh.nearLibImage);

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
//        return Math.min(2,Data.size());
        return  Data.size();
    }

    static  class ViewHolder extends RecyclerView.ViewHolder{
        private TextView exhItemName;
        private ImageView nearLibImage;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            exhItemName=itemView.findViewById(R.id.textView15);
            nearLibImage=itemView.findViewById(R.id.museumRankImage);
        }
    }
}
