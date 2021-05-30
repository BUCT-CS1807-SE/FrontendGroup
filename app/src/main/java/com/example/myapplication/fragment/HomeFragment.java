package com.example.myapplication.fragment;

import android.content.Context;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.myapplication.entity.Museum;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.example.myapplication.MainActivity;
import com.example.myapplication.RegisterActivity;
import com.example.myapplication.activity.HomePageActivity;
import com.bumptech.glide.Glide;
import com.example.myapplication.activity.MuseumIntroActivity;
import com.example.myapplication.activity.UserexplainActivity;
import com.example.myapplication.activity.WebViewActivity;
import com.example.myapplication.adapter.MuseumExhibitionAdapter;
import com.example.myapplication.entity.Exhibition;
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

import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.io.IOException;

import static com.example.myapplication.util.NetworkUtils.HttpRequestGet;

public class HomeFragment extends BaseFragment implements OnBannerListener, View.OnClickListener {
    private InfoContainerView environmentRank;
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private String[] mTitles;
    private ViewPager viewPager;
    private SlidingTabLayout slidingTabLayout;
    private Banner mBanner;
    private Button btn1;
    private Button btn2;
    private Button btn3;
    private Button btn4;
    public HomeFragment() {
    }
    @Override
    protected int initLayout() {
        return R.layout.fragment_home;
    }
    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
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

        // super.onCreate(savedInstanceState);
        btn1= mRootView.findViewById(R.id.bt12);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.putExtra("id","1");
                intent.putExtra("ShowName","故宫博物馆");
                intent.putExtra("kind","MUSEUM");
                intent.setClass(HomeFragment.this.getContext(),UserexplainActivity.class);
                startActivity(intent);
            }
        });
        btn2= mRootView.findViewById(R.id.btn123);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.putExtra("id","173");
                intent.putExtra("ShowName","沈阳故宫博物院");
                intent.putExtra("kind","MUSEUM");
                intent.setClass(HomeFragment.this.getContext(),UserexplainActivity.class);
                startActivity(intent);
            }
        });
        btn3= mRootView.findViewById(R.id.ti_18);
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Museum museum=new Museum();;
                Bundle bundle = new Bundle();
                String Name="故宫博物馆";
                String introduction="北京故宫博物院，紫禁城，皇家宫殿，国家5A级旅游景点，世界五大宫 之首，世界文化遗产。故宫博物院建立于1925年10月10日，位于北京故宫紫禁城内，是在明朝、清朝两代皇 宫及其收藏的基础上建立起来的中国综合性博物馆，其文物收藏主要来源于清代宫中旧藏，是第一批全国爱国 主义教育示范基地。北京故宫是第一批全国重点文物保护单位、第一批国家5A级旅游景区，1987年入选《世界 文化遗产名录》。";
                String howtogo="公交车：（一）故宫博物院午门（南门）：天安门东站和天安西站。午门是唯 一观众入口。\n\n1、天安门东站：1路、2路、10路、20路、82路、120路、37路、52路、126路、99路、 203路、205路、210路、728路、专1路、专2路。下车后直接步行经端门到达故宫午门（南门）。\n\n2、天 安门西站。1路、10路、37路、52路、99路、205路、728路、5路、22路、专1路、专2路。下车后直接步行 经端门到达故宫午门（南门）。\n\n（二）故宫博物院神武门（北门）：故宫站和景山公园东门站。神武门是 唯一观众出口。\n\n1、故宫站：101路、103路、109路、202路夜班、211路夜班、685路、619路、614 路、609路、专1路、专2路、124路无轨电车。\n\n2、景山公园东门站：111路无轨电车。\n\n故宫博物院 东华门（东门）和西华门（西门）平时仅做工作人员通道。\n\n地铁1号线：天安门东站或天安门西";
                int Num=1;
                museum.setName(Name);
                museum.setId(Num);
                museum.setHowtogo(howtogo);
                museum.setIntroduction(introduction);
                bundle.putSerializable("museum",museum);
                Intent intent=new Intent(HomeFragment.this.getContext(),MuseumIntroActivity.class);
                intent.putExtra("museum_data",bundle);
                startActivity(intent);

            }
        });

        btn4= mRootView.findViewById(R.id.ti_8);
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Museum museum=new Museum();;
                Bundle bundle = new Bundle();
                String Name="沈阳故宫博物院";
                int Num=173;
                String introduction="沈阳故宫博物院，原是清代初期营建和使用的皇家宫苑，始建于1625 年（明天启五年，后金天命十年）。在宫廷遗址上建立的沈阳故宫博物院是著名的古代宫廷艺术博物馆，藏品 中包含十分丰富的宫廷艺术品。1961年，国务院将沈阳故宫确定为国家第一批全国重点文物保护单位；2004 年7月1日，第28届世界遗产委员会会议批准沈阳故宫作为明清皇宫文化遗产扩展项目列入《世界文化遗产名 录》。沈阳故宫的高台建筑、“宫高殿低”的建筑风格，在中国宫殿建筑史上绝无仅有。";
                String howtogo="乘117、118、132、140、213、222、228、257、276、287、290、 292、294、296、环路公交车到故宫站下车，步行5分钟可到达。\n\n乘105、113、117、131、133、 150、168、218、219、237、248、273、298路公交车到大东门站下车，过大东门(抚近门)往西步行10分 钟可到达。\n\n乘207、212、224、227、326、333、334、503路公交车到大西门(怀远门)站下车，过怀 远门往东步行10分钟可到达。\n\n乘地铁一号线到中街站、怀远门站下车，步行10分钟可到达;乘地铁二号线 在青年大街站换乘一号线。";
                museum.setHowtogo(howtogo);
                museum.setIntroduction(introduction);
                museum.setName(Name);
                museum.setId(Num);
                bundle.putSerializable("museum",museum);
                Intent intent=new Intent(HomeFragment.this.getContext(),MuseumIntroActivity.class);
                intent.putExtra("museum_data",bundle);
                startActivity(intent);


            }
        });
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

    @Override
    public void OnBannerClick(int position) {
        showToast("你点击了第" + (position + 1) + "张轮播图");
    }



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