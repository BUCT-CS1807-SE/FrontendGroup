package com.example.myapplication.adapter;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.activity.ExhibitionInfoActivity;
import com.example.myapplication.activity.ItemInfoActivity;
import com.example.myapplication.entity.Exhibition;
import com.example.myapplication.entity.Item;

import java.util.ArrayList;
import java.util.List;

public class MuseumExhibitionAdapter extends RecyclerView.Adapter<MuseumExhibitionAdapter.ExhibitionViewHolder> {
    private ArrayList<Exhibition> localDataSet;

    public MuseumExhibitionAdapter(ArrayList<Exhibition> localDataSet) {
        this.localDataSet = localDataSet;

    }

    @NonNull
    @Override
    public ExhibitionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.museum_exhibition, parent, false);

        return new ExhibitionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExhibitionViewHolder holder, int position) {
        Exhibition exhibition = localDataSet.get(position);
        holder.getExhibitionName().setText(exhibition.getExhibitionName());
        holder.getExhibitionDescribe().setText(exhibition.getExhibitionDescribe());

    }

    @Override
    public void onBindViewHolder(@NonNull ExhibitionViewHolder holder , int position, @NonNull List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(holder.itemView.getContext(), ExhibitionInfoActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("exhibition",localDataSet.get(position));
            intent.putExtra("exhibition_data",bundle);
            holder.itemView.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return localDataSet.size();
    }

    public static class ExhibitionViewHolder extends RecyclerView.ViewHolder {
        private final TextView exhibitionName;
        private final TextView exhibitionDescribe;

        public ExhibitionViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View
            exhibitionDescribe = view.findViewById(R.id.exhibition_describe);
            exhibitionName = view.findViewById(R.id.exhibition_name);
        }

        public TextView getExhibitionName() {
            return exhibitionName;
        }

        public TextView getExhibitionDescribe() {
            return exhibitionDescribe;
        }
    }
}
