package com.example.myapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.adapter.SearchResultAdapter;
import com.example.myapplication.databinding.ActivityCollectInfoBinding;
import com.example.myapplication.databinding.ActivityExihibitionInfoBinding;
import com.example.myapplication.entity.Museum;
import com.example.myapplication.fragment.SearchFragment;
import com.example.myapplication.util.ImageUtils;
import com.example.myapplication.util.MuseumCollectUtil;
import com.example.myapplication.util.NetworkUtils;
import com.scwang.smart.refresh.header.MaterialHeader;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static com.example.myapplication.util.NetworkUtils.HttpRequestGet;

public class CollectedMuseumActivity extends AppCompatActivity {
    private ActivityCollectInfoBinding binding;
    private LinearLayout container;
    private RefreshLayout refreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect_info);


        binding = ActivityCollectInfoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);//???????????????????????????
        getSupportActionBar().setHomeButtonEnabled(true); //?????????????????????
        getSupportActionBar().setTitle("??????");


        container = findViewById(R.id.collect_container);
        Handler handler = new Handler(Looper.myLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                if (msg.what != 1) {
                    return;
                }
                Set<Integer> mumIds = MuseumCollectUtil.getMuseumIds();

                Handler getMuseumHandler = new Handler(msg1 -> {
                    if (msg1.what == 1) {
                        Museum museum = (Museum) msg1.obj;
                        addMuseum(museum);

                        return true;
                    }

                    return false;
                });
                for (Integer id : mumIds) {
                    NetworkUtils.HttpRequestGet(NetworkUtils.ResultType.MUSEUM_ID,getMuseumHandler,id);
                }
            }
        };
        MuseumCollectUtil.Build(handler);

        refreshLayout = findViewById(R.id.collectRefresh);
        refreshLayout.setRefreshHeader(new MaterialHeader(this));
        refreshLayout.setOnRefreshListener(refreshlayout -> {
            container.removeAllViewsInLayout();
            MuseumCollectUtil.Build(handler);
            refreshlayout.finishRefresh(500/*,false*/);//??????false??????????????????
        });

    }

    private void addMuseum(Museum museum) {
        View v = LayoutInflater.from(this).inflate(R.layout.item_museum_brief,container,false);
        TextView museumName = v.findViewById(R.id.museum_name);
        ImageView museumBriefIcon = v.findViewById(R.id.museum_brief_icon);
        TextView museumIntro = v.findViewById(R.id.museun_intro);
        TextView museumTime = v.findViewById(R.id.museum_time);

        museumName.setText(museum.getName());
        museumIntro.setText(museum.getIntroduction());
        museumTime.setText(museum.getOpeningHours());
        Glide.with(v).load(ImageUtils.genURL(museum.getName()))
                .centerCrop()
                .placeholder(R.drawable.ic_museum_explain)
                .into(museumBriefIcon);

        v.setOnClickListener(v1 -> {
            Intent intent = new Intent(this, MuseumIntroActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("museum",museum);
            intent.putExtra("museum_data",bundle);
            startActivity(intent);
        });
        container.addView(v);
    }

    public void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //??????back???finish??????activity
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

}
