package com.example.myapplication.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.WalkPath;
import com.example.myapplication.R;
import com.example.myapplication.adapter.DriveSegmentListAdapter;
import com.example.myapplication.adapter.WalkSegmentListAdapter;
import com.example.myapplication.util.MapUtil;


public class RouteDetailActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView tvTitle, tvTime;
    private RecyclerView rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_detail);
        initView();
    }

    private void initView() {
        toolbar = findViewById(R.id.toolbar);
        tvTitle = findViewById(R.id.tv_title);
        tvTime = findViewById(R.id.tv_time);
        rv = findViewById(R.id.rv);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Intent intent = getIntent();
        if (intent == null) {
            return;
        }
        switch (intent.getIntExtra("type", 0)) {
            case 0:
                walkDetail(intent);
                break;
            case 2:
                driveDetail(intent);
                break;
            default:
                break;
        }
    }

    private void walkDetail(Intent intent) {
        tvTitle.setText("步行路线规划");
        WalkPath walkPath = intent.getParcelableExtra("path");
        String dur = MapUtil.getFriendlyTime((int) walkPath.getDuration());
        String dis = MapUtil.getFriendlyLength((int) walkPath.getDistance());
        tvTime.setText(dur + "(" + dis + ")");
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(new WalkSegmentListAdapter(R.layout.item_segment, walkPath.getSteps()));
    }


    private void rideDetail(Intent intent) {
    }


    private void driveDetail(Intent intent) {
        tvTitle.setText("驾车路线规划");
        DrivePath drivePath = intent.getParcelableExtra("path");
        String dur = MapUtil.getFriendlyTime((int) drivePath.getDuration());
        String dis = MapUtil.getFriendlyLength((int) drivePath.getDistance());
        tvTime.setText(dur + "(" + dis + ")");
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(new DriveSegmentListAdapter(R.layout.item_segment, drivePath.getSteps()));
    }

 
    private void busDetail(Intent intent) { }

}
