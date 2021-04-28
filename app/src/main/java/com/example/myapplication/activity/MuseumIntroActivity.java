package com.example.myapplication.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.myapplication.R;

/**
 * 博物馆详情页
 * @author 黄熠
 */
public class MuseumIntroActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int initLayout() {
        return R.layout.activity_museum_intro;
    }

    @Override
    protected void initView() {
        String museum_name = getIntent().getStringExtra("museum_name");
        TextView name = findViewById(R.id.museumTitle);
        name.setText(museum_name);
    }

    @Override
    protected void initData() {

    }
}
