package com.example.myapplication.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.example.myapplication.activity.MuseumIntroActivity;
import com.example.myapplication.activity.WebViewActivity;
import com.example.myapplication.entity.Museum;
import com.example.myapplication.entity.Rating;
import com.example.myapplication.util.ImageUtils;
import com.example.myapplication.util.NetworkUtils;
import com.example.myapplication.view.InfoContainerView;
import com.flyco.tablayout.SlidingTabLayout;
import com.google.gson.Gson;
import com.example.myapplication.R;
import com.example.myapplication.adapter.HomeAdapter;
import com.example.myapplication.api.Api;
import com.example.myapplication.api.ApiConfig;
import com.example.myapplication.api.TtitCallback;
import com.example.myapplication.entity.CategoryEntity;
import com.example.myapplication.entity.VideoCategoryResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HomeFragment extends BaseFragment {
    private InfoContainerView environmentRank;
    private InfoContainerView exhibitionRank;
    private InfoContainerView serviceRank;
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private String[] mTitles;
    private ViewPager viewPager;
    private SlidingTabLayout slidingTabLayout;

    public HomeFragment() {
    }

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    protected int initLayout() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initView() {
        viewPager = mRootView.findViewById(R.id.viewpager);
        slidingTabLayout = mRootView.findViewById(R.id.slidingTabLayout);
        environmentRank = mRootView.findViewById(R.id.environment_rank);
        exhibitionRank = mRootView.findViewById(R.id.exhibition_rank);
        serviceRank = mRootView.findViewById(R.id.service_rank);
    }

    private List<Rating> ratings;
    protected void initData() {

        LinearLayout environmentRankContainer = this.environmentRank.getContainer();

        Handler ratingHandler = new Handler(Looper.myLooper()) {
            @SuppressLint("HandlerLeak")
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                if (msg.what == 1) {
                    ratings = (List<Rating>)msg.obj;

                    Handler getMuseumHandler = new Handler(Looper.myLooper()) {
                        @Override
                        public void handleMessage(@NonNull Message msg) {
                            super.handleMessage(msg);
                            if (msg.what == 1) {
                                Museum museum = (Museum) msg.obj;
                                View environmentView = LayoutInflater.from(environmentRankContainer.getContext()).inflate(R.layout.museum_rank,environmentRankContainer,false);
                                environmentRank.addElement(environmentView);
                                TextView name = environmentView.findViewById(R.id.museum_name);
                                name.setText(museum.getName());
                                TextView summary = environmentView.findViewById(R.id.museun_intro);
                                summary.setText(museum.getIntroduction());
                                TextView time = environmentView.findViewById(R.id.museum_time);
                                time.setText(museum.getOpeningHours());
                                ImageView image = environmentView.findViewById(R.id.museumRankImage);
                                Glide.with(environmentView)
                                        .load(ImageUtils.genURL(museum.getName())).thumbnail(0.1f)
                                        .placeholder(R.drawable.ic_museum_explain)
                                        .centerCrop()
                                        .into(image);

                                ImageView icon = environmentView.findViewById(R.id.rankIcon);
                                Glide.with(environmentView)
                                        .load(R.mipmap.rank)
                                        .into(icon);

                                environmentView.setOnClickListener((v)->{
                                    Intent intent = new Intent(environmentRankContainer.getContext(), MuseumIntroActivity.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putSerializable("museum",museum);
                                    intent.putExtra("museum_data",bundle);
                                    environmentRankContainer.getContext().startActivity(intent);
                                });
                            }
                        }
                    };
                    for (Rating rating : ratings) {
                        NetworkUtils.HttpRequestGet(NetworkUtils.ResultType.MUSEUM_ID,getMuseumHandler,rating.getMuseumid());
                    }
                }
            }
        };
        NetworkUtils.HttpRequestGet(NetworkUtils.ResultType.GRADES,ratingHandler);

//        LinearLayout exhibitionRankContainer = this.exhibitionRank.getContainer();
//        View exhibitionView = LayoutInflater.from(exhibitionRankContainer.getContext()).inflate(R.layout.museum_rank,exhibitionRankContainer,false);
//        exhibitionRank.addElement(exhibitionView);
//        exhibitionView.setOnClickListener((v)->{
//            Intent intent = new Intent(exhibitionRankContainer.getContext(), MuseumIntroActivity.class);
//            Bundle bundle = new Bundle();
//            bundle.putSerializable("museum",new Museum(1,"sda","sadad","北京","","","","","","","","","","","","","") );
//            intent.putExtra("museum_data",bundle);
//            exhibitionRankContainer.getContext().startActivity(intent);
//
//        });
//        LinearLayout serviceRankContainer = this.serviceRank.getContainer();
//        View serviceView = LayoutInflater.from(serviceRankContainer.getContext()).inflate(R.layout.museum_rank,serviceRankContainer,false);
//        serviceRank.addElement(serviceView);
//        serviceView.setOnClickListener((v)->{
//            Intent intent = new Intent(serviceRankContainer.getContext(), MuseumIntroActivity.class);
//            Bundle bundle = new Bundle();
//            bundle.putSerializable("museum",new Museum(1,"sda","sadad","北京","","","","","","","","","","","","","") );
//            intent.putExtra("museum_data",bundle);
//            serviceRankContainer.getContext().startActivity(intent);
//
//        });
    }
}