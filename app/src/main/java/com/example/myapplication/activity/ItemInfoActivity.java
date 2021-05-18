package com.example.myapplication.activity;

import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.example.myapplication.databinding.ActivityItemInfoBinding;
import com.example.myapplication.entity.Item;
import com.example.myapplication.util.ImageUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;


import com.example.myapplication.R;

import java.util.Objects;

import butterknife.BindView;

/**
 * @author 黄熠
 * 藏品Activity
 */
public class ItemInfoActivity extends AppCompatActivity {
    private ActivityItemInfoBinding binding;
    private Item item;
    private FloatingActionButton explainItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_info);
        binding = ActivityItemInfoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        item = (Item) getIntent().getBundleExtra("item_data").get("item");

        setSupportActionBar(binding.toolbar);
        binding.itemInfoName.setText(item.getItemName());
        binding.itemIntroContent.setText(item.getItemIntro());
        Glide.with(this).load(ImageUtils.genItemImage(item.getImageAddress())).placeholder(R.mipmap.museum).centerCrop().into(binding.itemInfoImage);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);//添加默认的返回图标
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        getSupportActionBar().setTitle(item.getItemName());
        getSupportActionBar().setSubtitle(item.getShowName());

        explainItem = findViewById(R.id.explainItem);
        explainItem.setOnClickListener(v -> {
            //跳转藏品讲解
            Toast.makeText(this,"藏品讲解页面",Toast.LENGTH_SHORT).show();
        });
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