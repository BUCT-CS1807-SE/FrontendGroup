package com.example.myapplication.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.myapplication.activity.MuseumIntroActivity;
import com.example.myapplication.activity.WebViewActivity;
import com.example.myapplication.entity.Museum;
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

    protected void initData() {

        LinearLayout environmentRankContainer = this.environmentRank.getContainer();
        View environmentView = LayoutInflater.from(environmentRankContainer.getContext()).inflate(R.layout.museum_rank,environmentRankContainer,false);
        environmentRank.addElement(environmentView);
        environmentView.setOnClickListener((v)->{
            Intent intent = new Intent(environmentRankContainer.getContext(), MuseumIntroActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("museum",new Museum(1,"sda","sadad","北京","","","","","","","","","","","","","") );
            intent.putExtra("museum_data",bundle);
            environmentRankContainer.getContext().startActivity(intent);

        });

        LinearLayout exhibitionRankContainer = this.exhibitionRank.getContainer();
        View exhibitionView = LayoutInflater.from(exhibitionRankContainer.getContext()).inflate(R.layout.museum_rank,exhibitionRankContainer,false);
        exhibitionRank.addElement(exhibitionView);
        exhibitionView.setOnClickListener((v)->{
            Intent intent = new Intent(exhibitionRankContainer.getContext(), MuseumIntroActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("museum",new Museum(1,"sda","sadad","北京","","","","","","","","","","","","","") );
            intent.putExtra("museum_data",bundle);
            exhibitionRankContainer.getContext().startActivity(intent);

        });
        LinearLayout serviceRankContainer = this.serviceRank.getContainer();
        View serviceView = LayoutInflater.from(serviceRankContainer.getContext()).inflate(R.layout.museum_rank,serviceRankContainer,false);
        serviceRank.addElement(serviceView);
        serviceView.setOnClickListener((v)->{
            Intent intent = new Intent(serviceRankContainer.getContext(), MuseumIntroActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("museum",new Museum(1,"sda","sadad","北京","","","","","","","","","","","","","") );
            intent.putExtra("museum_data",bundle);
            serviceRankContainer.getContext().startActivity(intent);

        });
    }
}