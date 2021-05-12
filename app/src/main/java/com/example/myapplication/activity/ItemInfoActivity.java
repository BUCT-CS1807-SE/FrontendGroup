package com.example.myapplication.activity;

import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.example.myapplication.databinding.ActivityItemInfoBinding;
import com.example.myapplication.entity.Item;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.MenuItem;
import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;


import com.example.myapplication.R;

import java.util.Objects;

public class ItemInfoActivity extends AppCompatActivity {
    private ActivityItemInfoBinding binding;
    private Item item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_info);
        binding = ActivityItemInfoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        item = (Item) getIntent().getBundleExtra("item_data").get("item");

        setSupportActionBar(binding.toolbar);
        binding.toolbar.setTitle(item.getItemName());
        binding.itemInfoName.setText(item.getItemName());
        binding.itemIntroContent.setText(item.getItemIntro());
        //@TODO 设置图片，暂时没有
        Glide.with(this).load(R.mipmap.default_bg).centerCrop().into(binding.itemInfoImage);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);//添加默认的返回图标
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //点击back键finish当前activity
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

}