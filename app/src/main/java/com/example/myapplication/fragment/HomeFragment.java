package com.example.myapplication.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.example.myapplication.activity.HomePageActivity;
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

import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

public class HomeFragment extends BaseFragment implements OnBannerListener, View.OnClickListener {
    private InfoContainerView environmentRank;
    private InfoContainerView exhibitionRank;
    private InfoContainerView serviceRank;
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private String[] mTitles;
    private ViewPager viewPager;
    private SlidingTabLayout slidingTabLayout;
    private Banner mBanner;

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
        mBanner = mRootView.findViewById(R.id.mBanner);
        //图片
        int[] imageResourceID = new int[]{R.drawable.a1,R.drawable.a2,R.drawable.a3};
        List<Integer> imgeList = new ArrayList<>();
        //轮播标题
        String[] mtitle = new String[]{"首页", "国家一级博物馆各地统计图", "排名前十的博物馆评分", "博物馆票价情况"};
        List<String> titleList = new ArrayList<>();
        for (int i = 0; i < imageResourceID.length; i++) {
            imgeList.add(imageResourceID[i]);//把图片资源循环放入list里面
            titleList.add(mtitle[i]);//把标题循环设置进列表里面
            //设置图片加载器，通过Glide加载图片
            mBanner.setImageLoader(new ImageLoader() {
                @Override
                public void displayImage(Context context, Object path, ImageView imageView) {
                    Glide.with(getActivity()).load(path).into(imageView);
                }
            });
            mBanner.setBannerAnimation(Transformer.Accordion);
            mBanner.setImages(imgeList);//图片
            mBanner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE);
            mBanner.setBannerTitles(titleList);
            mBanner.setIndicatorGravity(BannerConfig.CENTER);
            mBanner.setDelayTime(2500);//设置轮播时间3秒切换下一图
            mBanner.setOnBannerListener(this);//设置监听
            mBanner.start();
        }
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

    @Override
    public void OnBannerClick(int position) {
        showToast("你点击了第" + (position + 1) + "张轮播图");
    }

    @Override
    public void onClick(View v) {

    }
    @Override
    public void onStart() {
        super.onStart();
        mBanner.startAutoPlay();//开始轮播
    }
    @Override
    public void onStop() {
        super.onStop();
        mBanner.stopAutoPlay();//结束轮播
    }
}