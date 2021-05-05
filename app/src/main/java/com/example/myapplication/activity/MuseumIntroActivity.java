package com.example.myapplication.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.entity.Comment;
import com.example.myapplication.entity.Museum;
import com.example.myapplication.entity.MuseumNew;
import com.example.myapplication.entity.NewsEntity;
import com.example.myapplication.util.ImageUtils;
import com.example.myapplication.view.InfoContainerView;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * 博物馆详情页
 * @author 黄熠
 */
public class MuseumIntroActivity extends BaseActivity implements OnBannerListener {

    private InfoContainerView briefIntro;
    private InfoContainerView news;
    private InfoContainerView arrive;
    private InfoContainerView comment;
    private InfoContainerView show;

    private Banner banner;
    private ArrayList<String> list_path;

    private ArrayList<Comment> comments;
    private ArrayList<MuseumNew> museumNews;

    private Museum museum;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int initLayout() {
        return R.layout.activity_museum_intro;
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void initView() {
        Bundle bundle = getIntent().getBundleExtra("museum_data");
        museum = (Museum) bundle.getSerializable("museum");
        String museum_name = museum.getName();

//        TextView name = findViewById(R.id.museum_title);
//        name.setText(museum_name);

        //---------轮播图----------
        banner = findViewById(R.id.mBanner);
        list_path = new ArrayList<>();
        list_path.add(ImageUtils.genURL(museum.getId()));
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
        briefIntro.addElement(briefIntroduction);

        //----------新闻----------
        news = findViewById(R.id.news);
        museumNews = new ArrayList<>();

        //----------到达方式----------
        arrive = findViewById(R.id.arrive);
        TextView arrive_view = new TextView(arrive.getContainer().getContext());
        arrive_view.setText(museum.getHowtogo());
        arrive.addElement(arrive_view);

        //----------评论----------
        comment = findViewById(R.id.comment);
        comments = new ArrayList<>();//网络接口完成后，初始化方式放入handler中

        //----------展览----------
        show = findViewById(R.id.show);

    }

    /**
     * 添加评论
     * @author 黄熠
     * @param comment
     */
    public void addComment(Comment comment) {
        LinearLayout container = this.comment.getContainer();
        View commentView = LayoutInflater.from(container.getContext()).inflate(R.layout.museum_comment,container,false);
        TextView commentById = commentView.findViewById(R.id.comment_content);
        commentById.setText(comment.getContent());
        TextView username = commentView.findViewById(R.id.comment_username);
        username.setText(comment.getUsername());
        TextView date = commentView.findViewById(R.id.comment_date);
        date.setText(comment.getTime());
        this.comment.addElement(commentView);
    }

    /**
     * 添加新闻
     * @author 黄熠
     * @param museumNew
     */
    public void addNew(MuseumNew museumNew) {
        LinearLayout container = this.news.getContainer();
        View newsView = LayoutInflater.from(container.getContext()).inflate(R.layout.museum_new,container,false);
        TextView newsTitle = newsView.findViewById(R.id.new_title);
        TextView newsBrief = newsView.findViewById(R.id.new_content);
        TextView newsAuthor = newsView.findViewById(R.id.new_author);
        TextView newsTime = newsView.findViewById(R.id.new_time);

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
        comments.add(new Comment(0,1,1,"路人甲","201-5-3","好家伙"));
        comments.add(new Comment(1,2,1,"路人乙","201-5-3","针不辍"));
        comments.add(new Comment(2,3,1,"路人丙","201-5-3","zhou，去吃锅"));
        comments.add(new Comment(3,4,1,"啊这","201-5-3","啊这"));
        comments.add(new Comment(4,5,1,"这啊","201-5-3","这啊"));
        initComments();

        museumNews.add(new MuseumNew(0,"这个博物馆出大事了","小编","2021-5-15","www.jd.com","具体是什么大事呢，小编也不知道，下面就一起和小编来看看吧",museum.getName()));
        museumNews.add(new MuseumNew(1,"这个博物馆出大事了","小编","2021-5-15","www.taobao.com","具体是什么大事呢，小编也不知道，下面就一起和小编来看看吧",museum.getName()));
        museumNews.add(new MuseumNew(2,"这个博物馆出大事了","小编","2021-5-15","www.qq.com","具体是什么大事呢，小编也不知道，下面就一起和小编来看看吧",museum.getName()));
        initNews();
    }

    private void initComments() {
        for (Comment comment1 : comments) {
            addComment(comment1);
        }
    }

    private void initNews() {
        for (MuseumNew aNew : museumNews) {
            addNew(aNew);
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
