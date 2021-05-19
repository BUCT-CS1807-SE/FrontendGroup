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

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.myapplication.R;
import com.example.myapplication.activity.ItemInfoActivity;
import com.example.myapplication.activity.MuseumIntroActivity;
import com.example.myapplication.entity.Item;
import com.example.myapplication.entity.Museum;
import com.example.myapplication.util.ImageUtils;

import java.util.ArrayList;
import java.util.List;

public class MuseumItemAdapter extends RecyclerView.Adapter<MuseumItemAdapter.ItemViewHolder>  {

    private List<Item> localDataSet;

    public MuseumItemAdapter(List<Item> localDataSet) {
        this.localDataSet = localDataSet;
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        private final TextView itemName;
        private final ImageView itemIcon;

        public ItemViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View
            itemIcon = view.findViewById(R.id.itemImage);
            itemName = (TextView) view.findViewById(R.id.itemName);
        }

        public TextView getItemName() {
            return itemName;
        }

        public ImageView getItemIcon() {
            return itemIcon;
        }
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.museum_item, viewGroup, false);

        return new ItemViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ItemViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        Item item = localDataSet.get(position);
        viewHolder.getItemName().setText(item.getItemName());
        Glide.with(viewHolder.itemView).load(ImageUtils.genItemImage(item.getImageAddress())).placeholder(R.mipmap.museum).thumbnail(0.06f)
                .transform(new MultiTransformation(new CenterCrop(),new RoundedCorners(50)))
                .into(viewHolder.getItemIcon());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return localDataSet.size();
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position, @NonNull List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(holder.itemView.getContext(), ItemInfoActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("item",localDataSet.get(position));
            intent.putExtra("item_data",bundle);
            holder.itemView.getContext().startActivity(intent);
        });
    }

}
