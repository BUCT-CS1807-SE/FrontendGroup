package com.example.myapplication.adapter;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.activity.MuseumIntroActivity;
import com.example.myapplication.entity.Museum;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 黄熠
 * 布局适配器，创建并显示博物馆搜索结果
 */
public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.ViewHolder> {

    private ArrayList<Museum> localDataSet;

    public SearchResultAdapter(ArrayList<Museum> localDataSet) {
        this.localDataSet = localDataSet;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView museumName;
        private final ImageView museumBriefIcon;
        private final TextView museumIntro;
        private final TextView museumTime;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View
            museumBriefIcon = view.findViewById(R.id.museum_brief_icon);
            museumName = (TextView) view.findViewById(R.id.museum_name);
            museumIntro = view.findViewById(R.id.museun_intro);
            museumTime = view.findViewById(R.id.museum_time);
        }

        public TextView getMuseumName() {
            return museumName;
        }

        public ImageView getMuseumBriefIcon() {
            return museumBriefIcon;
        }

        public TextView getMuseumIntro() {
            return museumIntro;
        }

        public TextView getMuseumTime() {
            return museumTime;
        }
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_museum_brief, viewGroup, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        Museum data = localDataSet.get(position);
//        暂时没有图片
        viewHolder.getMuseumBriefIcon().setImageResource(R.drawable.bg_1);
        viewHolder.getMuseumName().setText(data.getName());
        viewHolder.getMuseumIntro().setText(data.getIntroduction());
        viewHolder.getMuseumTime().setText(data.getOpeningHours());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return localDataSet.size();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(holder.itemView.getContext(), MuseumIntroActivity.class);
            intent.putExtra("museum_name",holder.getMuseumName().getText().toString());
            holder.itemView.getContext().startActivity(intent);
        });
    }
}
