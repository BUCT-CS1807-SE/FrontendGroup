package com.example.myapplication.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.adapter.SearchResultAdapter;
import com.example.myapplication.entity.Comment;
import com.example.myapplication.entity.Item;
import com.example.myapplication.entity.Museum;
import com.example.myapplication.entity.MuseumNew;
import com.example.myapplication.fragment.SearchFragment;
import com.example.myapplication.util.ImageUtils;
import com.example.myapplication.util.NetworkUtils;
import com.example.myapplication.view.InfoContainerView;
import com.leinardi.android.speeddial.SpeedDialActionItem;
import com.leinardi.android.speeddial.SpeedDialView;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import static com.example.myapplication.util.NetworkUtils.HttpRequestGet;
import static com.example.myapplication.util.NetworkUtils.HttpRequestPost;

/**
 * 博物馆详情页
 * @author 黄熠
 */
public class MuseumIntroActivity extends BaseActivity implements OnBannerListener {

    private final static Integer COMMENT_LIKED = 1;
    private final static Integer COMMENT_UNLIKED = 2;

    private InfoContainerView briefIntro;
    private InfoContainerView news;
    private InfoContainerView arrive;
    private InfoContainerView comment;
    private InfoContainerView item;
    private InfoContainerView show;
    private InfoContainerView grade;

    private ScrollView scroller;

    private SpeedDialView more;

    private Banner banner;
    private ArrayList<String> list_path;

    private ArrayList<Comment> comments;
    private ArrayList<MuseumNew> museumNews;
    private ArrayList<Item> items;
    private float score_environment=2;
    private float score_service=2;
    private float score_show=2;

    private Museum museum;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int initLayout() {
        return R.layout.activity_museum_intro;
    }

    @SuppressLint({"SetTextI18n", "ClickableViewAccessibility"})
    @Override
    protected void initView() {
        Bundle bundle = getIntent().getBundleExtra("museum_data");
        museum = (Museum) bundle.getSerializable("museum");
        String museum_name = museum.getName();

//        TextView name = findViewById(R.id.museum_title);
//        name.setText(museum_name);

        //---------滑动组件---------
        scroller = findViewById(R.id.scroller);
        scroller.setOnDragListener((v, event) -> {
            int paddingTop = scroller.getPaddingTop();
            int scrollY = Math.min(scroller.getScrollY(), paddingTop);
            float transparent = (paddingTop-scrollY)*1.0f/scrollY;
            scroller.setAlpha(transparent);

            if (transparent <= 0.01) {
                scroller.setVisibility(View.INVISIBLE);
            } else {
                scroller.setVisibility(View.VISIBLE);
            }
            return false;
        });

        //---------轮播图----------
        banner = findViewById(R.id.mBanner);
        list_path = new ArrayList<>();
        list_path.add(ImageUtils.genURL(museum.getName()));
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        banner.setImageLoader(new MyLoader());
        banner.setBannerAnimation(Transformer.Default);
        banner.setDelayTime(3000);
        banner.isAutoPlay(true);
        banner.setIndicatorGravity(BannerConfig.CENTER);
        banner.setImages(list_path)
                .setOnBannerListener(this)
                .start();

        //----------简介----------
        briefIntro = findViewById(R.id.briefIntro);
        briefIntro.setTitle(museum_name);
        TextView briefIntroduction = new TextView(briefIntro.getContainer().getContext());
        briefIntroduction.setText(museum.getIntroduction());
        briefIntroduction.setPadding(30,0,30,0);
        briefIntro.addElement(briefIntroduction);

        //----------新闻----------
        news = findViewById(R.id.news);
        museumNews = new ArrayList<>();

        //----------到达方式----------
        arrive = findViewById(R.id.arrive);
        TextView arrive_view = new TextView(arrive.getContainer().getContext());
        arrive_view.setText(museum.getHowtogo());
        arrive_view.setPadding(30,0,30,0);
        arrive.addElement(arrive_view);

        //----------评分----------
        grade = findViewById(R.id.grade);
        View grade_view = LayoutInflater.from(grade.getContainer().getContext()).inflate(R.layout.museum_grade,grade.getContainer(),false);
        addGrade(grade_view);
        grade.addElement(grade_view);

        //----------评论----------
        comment = findViewById(R.id.comment);
        comments=null;//网络接口完成后，初始化方式放入handler中
        Handler handlerGet=new Handler(Looper.myLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                if(msg.what==1){
                    comments= (ArrayList<Comment>) msg.obj;
                    initComments();
                    comment.setTitle("评论 ("+comments.size()+")");
                }
            }
        };
        HttpRequestGet(NetworkUtils.ResultType.COMMENT,handlerGet,museum.getId().toString());

        Handler handlerPost=new Handler(Looper.myLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                if(msg.what==1){
                    showToastSync("评论成功");
                } else {
                    showToastSync("提交评论失败");
                }
            }
        };
        HttpRequestPost(NetworkUtils.ResultType.COMMENT_POST,handlerPost);
        //----------藏品----------
        item = findViewById(R.id.items);
        items = new ArrayList<>();//初始化，未来转入Handler

        //----------展览----------
        show = findViewById(R.id.show);

        //----------菜单浮动按钮----------
        more = findViewById(R.id.more);
        more.addActionItem(new SpeedDialActionItem.Builder(R.id.museum_menu_explain, R.mipmap.explain)
                .setFabBackgroundColor(Color.WHITE)
                .setLabel("博物馆讲解")
                .create());
        more.addActionItem(new SpeedDialActionItem.Builder(R.id.museum_menu_collect, R.mipmap.icon_collect)
                .setFabBackgroundColor(Color.WHITE)
                .setLabel("收藏博物馆")
                .create());
        more.setOnActionSelectedListener(actionItem -> {
            int id = actionItem.getId();
            switch (id) {
                case R.id.museum_menu_collect:{
                    //收藏

                    return true;
                }
                case R.id.museum_menu_explain:{
                    //博物馆讲解

                    return true;
                }
            }
            return false;
        });

    }

    /**
     * 添加评论
     * @author 黄熠
     * @param comment 评论
     * @param isEnd 是否是最后一个条目，如果是则关闭底部边界
     * @param isLiked 评论是否已经点过赞
     */
    public void addComment(Comment comment,boolean isEnd,boolean isLiked) {
        LinearLayout container = this.comment.getContainer();
        View commentView = LayoutInflater.from(container.getContext()).inflate(R.layout.museum_comment,container,false);
        TextView commentById = commentView.findViewById(R.id.comment_content);
        commentById.setText(comment.getContent());
        TextView username = commentView.findViewById(R.id.comment_username);
        username.setText(comment.getUsername());
        TextView date = commentView.findViewById(R.id.comment_date);
        date.setText(comment.getTime());
        ImageView like = commentView.findViewById(R.id.comment_like);
        if (isLiked) {
            like.setImageResource(R.mipmap.like_active);
            like.setTag(COMMENT_LIKED);
        }
        like.setOnClickListener(v -> {
            Integer state = (Integer)like.getTag();
            if (state == COMMENT_LIKED) {
                like.setImageResource(R.mipmap.like);
                like.setTag(COMMENT_UNLIKED);
                //已经点过赞了，取消点赞，下一步向后台提供数据

            } else {
                like.setImageResource(R.mipmap.like_active);
                like.setTag(COMMENT_LIKED);
                //点赞成功，向后台提供数据

            }
        });

        if (isEnd) {
            commentView.findViewById(R.id.comment_border).setAlpha(0.0f);
        }
        this.comment.addElement(commentView);
    }

    /**
     * @author Zhy
     * @param grade_view
     */
    public void addGrade(View grade_view){

        RatingBar grade_environment = grade_view.findViewById(R.id.ratingBar_environment);
        grade_environment.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                score_environment=rating;
                Log.d("Score_Environment", "onRatingChanged: "+String.valueOf(rating));

            }
        });

        RatingBar grade_service = grade_view.findViewById(R.id.ratingBar_service);
        grade_service.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                score_service=rating;
                Log.d("Score_Service", "onRatingChanged: "+String.valueOf(rating));

            }
        });

        RatingBar grade_show = grade_view.findViewById(R.id.ratingBar_show);
        grade_show.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
               score_show=rating;
                Log.d("Score_Show", "onRatingChanged: "+String.valueOf(rating));
            }
        });

        Button button = grade_view.findViewById(R.id.grade_commit);
        button.setOnClickListener(v -> Toast.makeText(MuseumIntroActivity.this,"score_environment:"+String.valueOf(score_environment)+" score_service:"+String.valueOf(score_service)+" score_show:"+String.valueOf(score_show),Toast.LENGTH_SHORT).show());


    }

    /**
     * 添加新闻
     * @author 黄熠
     * @param museumNew 博物馆
     * @param isEnd 是否是最后一个条目，如果是则关闭底部边界
     */

    public void addNew(MuseumNew museumNew,boolean isEnd) {
        LinearLayout container = this.news.getContainer();
        View newsView = LayoutInflater.from(container.getContext()).inflate(R.layout.museum_new,container,false);
        TextView newsTitle = newsView.findViewById(R.id.new_title);
        TextView newsBrief = newsView.findViewById(R.id.new_content);
        TextView newsAuthor = newsView.findViewById(R.id.new_author);
        TextView newsTime = newsView.findViewById(R.id.new_time);
        if (isEnd) {
            newsView.findViewById(R.id.museum_newborder).setAlpha(0.0f);
        }

        newsTitle.setText(museumNew.getTitle());
        newsBrief.setText(museumNew.getContent());
        newsAuthor.setText(museumNew.getAuthor());
        newsTime.setText(museumNew.getTime());

        //创建点击事件监听器
        newsView.setOnClickListener((view)->{
            //暂时跳转到webView页面展示内容
            Intent in = new Intent(view.getContext(),WebViewActivity.class);
            in.putExtra("link",museumNew.getLink());
            startActivity(in);
        });
        this.news.addElement(newsView);
    }

    @Override
    protected void initData() {
        //------------数   据   造   假-------------

        museumNews.add(new MuseumNew(0,"这个博物馆出大事了","小编","2021-5-15","www.jd.com","具体是什么大事呢，小编也不知道，下面就一起和小编来看看吧",museum.getName()));
        museumNews.add(new MuseumNew(1,"这个博物馆出大事了","小编","2021-5-15","www.taobao.com","具体是什么大事呢，小编也不知道，下面就一起和小编来看看吧",museum.getName()));
        museumNews.add(new MuseumNew(2,"这个博物馆出大事了","小编","2021-5-15","www.qq.com","具体是什么大事呢，小编也不知道，下面就一起和小编来看看吧",museum.getName()));
        initNews();
    }

    private void initComments() {
        for (int i = 0; i < comments.size(); i++) {
            if (i == comments.size()-1) {
                addComment(comments.get(i),true,true);
            } else {
                addComment(comments.get(i),false,false);
            }
        }
    }

    private void initNews() {
        for (int i = 0; i < museumNews.size(); i++) {
            if (i == museumNews.size()-1) {
                addNew(museumNews.get(i),true);
            } else {
                addNew(museumNews.get(i),false);
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        banner.startAutoPlay();//开始轮播
    }

    @Override
    protected void onStop() {
        super.onStop();
        banner.stopAutoPlay();//结束轮播
    }

    //对轮播图设置点击监听事件
    @Override
    public void OnBannerClick(int position) {
        Toast.makeText(this, "你点击了第" + (position + 1) + "张轮播图", Toast.LENGTH_SHORT).show();
    }

    private class MyLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            Glide.with(context.getApplicationContext())
                    .load((String) path)
                    .into(imageView);
        }
    }
}
