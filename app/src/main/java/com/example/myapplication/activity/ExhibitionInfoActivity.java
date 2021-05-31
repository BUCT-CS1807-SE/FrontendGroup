package com.example.myapplication.activity;


import android.content.Intent;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.example.myapplication.adapter.MuseumItemAdapter;
import com.example.myapplication.databinding.ActivityExihibitionInfoBinding;
import com.example.myapplication.databinding.ActivityItemInfoBinding;
import com.example.myapplication.entity.Exhibition;
import com.example.myapplication.entity.Item;
import com.example.myapplication.util.NetworkUtils;
import com.example.myapplication.view.InfoContainerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.myapplication.R;

import java.util.List;
import java.util.Objects;

import butterknife.BindView;

import static com.example.myapplication.util.NetworkUtils.HttpRequestGet;

/**
 * @author zhy
 * 展览Activity
 */
public class ExhibitionInfoActivity extends AppCompatActivity {
    private ActivityExihibitionInfoBinding binding;
    private Exhibition exhibition;
    private FloatingActionButton explainExhibition;

    private InfoContainerView item;

    private List<Item> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exihibition_info);
        binding = ActivityExihibitionInfoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        exhibition = (Exhibition) getIntent().getBundleExtra("exhibition_data").get("exhibition");

        setSupportActionBar(binding.toolbar);
        binding.exhibitionInfoName.setText(exhibition.getExhibitionName());
        binding.exhibitionIntroContent.setText(exhibition.getExhibitionDescribe());

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);//添加默认的返回图标
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        getSupportActionBar().setTitle(exhibition.getExhibitionName());

        explainExhibition = findViewById(R.id.explainItem);
        explainExhibition.setOnClickListener(v -> {
            //跳转展览讲解
            Toast.makeText(this,"展览讲解页面",Toast.LENGTH_SHORT).show();
            //
            Intent intent=new Intent();
            intent.putExtra("id","1642");
            intent.putExtra("ShowName",exhibition.getExhibitionName());
            intent.putExtra("kind","EXHIBITION");
            intent.setClass(this,UserexplainActivity.class);
            startActivity(intent);
        });


        item = findViewById(R.id.exhibition_items);
        RecyclerView itemContainer = new RecyclerView(item.getContainer().getContext());
        LinearLayoutManager manager = new LinearLayoutManager(itemContainer.getContext());
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        itemContainer.setLayoutManager(manager);

        Handler getItems = new Handler(Looper.myLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                if (msg.what == 1) {
                    items = (List<Item>) msg.obj;
                    runOnUiThread(() -> {
                        itemContainer.setAdapter(new MuseumItemAdapter(items));
                        item.addElement(itemContainer);
                    });
                } else {
                    showToast("获取藏品失败");
                }
            }
        };
        HttpRequestGet(NetworkUtils.ResultType.ITEMS,getItems,exhibition.getMuseumId(),exhibition.getExhibitionName());
    }

    public void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
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
