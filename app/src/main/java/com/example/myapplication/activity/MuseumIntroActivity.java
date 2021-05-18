package com.example.myapplication.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.adapter.MuseumExhibitionAdapter;
import com.example.myapplication.adapter.MuseumItemAdapter;
import com.example.myapplication.entity.Comment;
import com.example.myapplication.entity.CommentIsLiked;
import com.example.myapplication.entity.Exhibition;
import com.example.myapplication.entity.Item;
import com.example.myapplication.entity.Museum;
import com.example.myapplication.entity.MuseumCollectedPost;
import com.example.myapplication.entity.MuseumNew;
import com.example.myapplication.entity.Rating;
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

import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.example.myapplication.util.NetworkUtils.HttpRequestDelete;
import static com.example.myapplication.util.NetworkUtils.HttpRequestGet;
import static com.example.myapplication.util.NetworkUtils.HttpRequestPost;

/**
 * 博物馆详情页
 *
 * @author 黄熠
 */
public class MuseumIntroActivity extends BaseActivity implements OnBannerListener {

    private final static Integer COMMENT_LIKED = 1;
    private final static Integer COMMENT_UNLIKED = 2;
    private static final String TAG = "MuseumIntro";

    private InfoContainerView briefIntro;
    private InfoContainerView news;
    private InfoContainerView arrive;
    private InfoContainerView comment;
    private InfoContainerView item;
    private InfoContainerView exhibition;
    private InfoContainerView grade;

    private TextView commentContent;
    private Button submitComment;

    private ScrollView scroller;
    private LinearLayout content_linearlayout;

    private SpeedDialView more;

    private Banner banner;
    private ArrayList<String> list_path;

    private List<Comment> comments;
    private List<MuseumNew> museumNews;
    private List<Item> items;
    private List<Exhibition> exhibitions;
    private float score_environment = 2;
    private float score_service = 2;
    private float score_show = 2;

    private Museum museum;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);//设置透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);//设置透明导航栏
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int initLayout() {
        return R.layout.activity_museum_intro;
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
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
        content_linearlayout = findViewById(R.id.content_linearLayout);
        scroller.setOnScrollChangeListener((v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            int paddingTop = content_linearlayout.getPaddingTop();
            int scrollY2 = Math.min(scrollY, paddingTop);
            float transparent = (paddingTop - scrollY2) * 1.0f / paddingTop;
            banner.setTransitionAlpha(transparent);
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
        briefIntroduction.setPadding(30, 0, 30, 0);
        briefIntro.addElement(briefIntroduction);

        //----------新闻----------
        news = findViewById(R.id.news);
        Handler getNewsHandler = new Handler(Looper.myLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                if (msg.what == 1) {
                    museumNews = (List<MuseumNew>) msg.obj;
                    initNews();
                } else {
                    showToast("新闻获取失败");
                }
            }
        };
        HttpRequestGet(NetworkUtils.ResultType.NEW, getNewsHandler, museum_name);

        //----------到达方式----------
        arrive = findViewById(R.id.arrive);
        TextView arrive_view = new TextView(arrive.getContainer().getContext());
        arrive_view.setText(museum.getHowtogo());
        arrive_view.setPadding(30, 0, 30, 0);
        arrive.addElement(arrive_view);

        //----------评分----------
        grade = findViewById(R.id.grade);
        View grade_view = LayoutInflater.from(grade.getContainer().getContext()).inflate(R.layout.museum_grade, grade.getContainer(), false);
        addGrade(grade_view);
        grade.addElement(grade_view);

        //----------评论----------
        //获取评论
        comment = findViewById(R.id.comment);
        //添加评论框
        View commentView = LayoutInflater.from(comment.getContainer().getContext()).inflate(R.layout.museum_comment_input, grade.getContainer(), false);
        comment.addElement(commentView);
        commentContent = commentView.findViewById(R.id.comment_content_input);
        submitComment = commentView.findViewById(R.id.comment_send);
        submitComment.setOnClickListener(v -> {
            String content = commentContent.getText().toString(); //评论字符串
            if (content.isEmpty()) {
                showToast("请输入评论");
                return;
            }

            //@TODO 提交评论
            Comment comment = new Comment(1, MainActivity.person.getId(), museum.getId(), MainActivity.person.getName(), "2020-2-2", content);
            Handler commentPost = new Handler(Looper.myLooper()) {
                private final Comment c = comment;

                @Override
                public void handleMessage(@NonNull Message msg) {
                    super.handleMessage(msg);
                    if (msg.what == 1) {
                        runOnUiThread(()->{
                            addComment(c,true,false);
                            commentContent.setText("");
                        });
                        showToastSync("评论成功");
                    } else {
                        showToastSync("提交评论失败");
                    }
                }
            };
            HttpRequestPost(commentPost, comment);

        });

        comments = null;//网络接口完成后，初始化方式放入handler中
        Handler commentGet = new Handler(Looper.myLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                if (msg.what == 1) {
                    comments = (List<Comment>) msg.obj;
                    initComments();
                }
            }
        };
        HttpRequestGet(NetworkUtils.ResultType.COMMENT, commentGet, museum.getId().toString());
        //----------藏品----------
        item = findViewById(R.id.items);

        items = new ArrayList<>();//初始化，未来转入Handler
        //假数据
        items.add(new Item(1, 1, "", "", "这是一个好藏品", "大宝贝1", ""));
        items.add(new Item(2, 1, "", "", "这是一个坏藏品", "大宝贝2", ""));
        items.add(new Item(3, 1, "", "", "这是一个很好的藏品", "大宝贝3", ""));
        items.add(new Item(4, 1, "", "", "这是一个很坏的藏品", "大宝贝4", ""));

        RecyclerView itemContainer = new RecyclerView(item.getContainer().getContext());
        itemContainer.setAdapter(new MuseumItemAdapter(items));
        LinearLayoutManager manager = new LinearLayoutManager(itemContainer.getContext());
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        itemContainer.setLayoutManager(manager);
        item.addElement(itemContainer);

        //----------展览----------
        exhibition = findViewById(R.id.exhibition);
        exhibitions = new ArrayList<>();
        exhibitions.add(new Exhibition(1, 1, "4856", "644"));
        exhibitions.add(new Exhibition(2, 1, "小绿片", "你爱看的"));
        exhibitions.add(new Exhibition(3, 1, "馍馍", "好吃的"));
        exhibitions.add(new Exhibition(4, 1, "倒装句", "属于是"));
        RecyclerView exhibitionContainer = new RecyclerView(exhibition.getContainer().getContext());
        exhibitionContainer.setAdapter(new MuseumExhibitionAdapter(exhibitions));
        LinearLayoutManager exhibit_manager = new LinearLayoutManager(exhibitionContainer.getContext());
        exhibit_manager.setOrientation(LinearLayoutManager.VERTICAL);
        exhibitionContainer.setLayoutManager(exhibit_manager);
        exhibition.addElement(exhibitionContainer);

        //----------菜单浮动按钮----------
        more = findViewById(R.id.more);
        more.addActionItem(new SpeedDialActionItem.Builder(R.id.museum_menu_explain, R.drawable.ic_museum_explain)
                .setFabBackgroundColor(Color.parseColor("#87a7d6"))
                .setLabel("博物馆讲解")
                .setLabelBackgroundColor(Color.parseColor("#efb336"))
                .setLabelColor(Color.WHITE)
                .create());
        more.addActionItem(new SpeedDialActionItem.Builder(R.id.museum_menu_collect, R.drawable.ic_collected_museum)
                .setFabBackgroundColor(Color.parseColor("#7cba59"))
                .setLabel("收藏博物馆")
                .setLabelBackgroundColor(Color.parseColor("#efb336"))
                .setLabelColor(Color.WHITE)
                .create());

        Handler collectedPost = new Handler(Looper.myLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                if (msg.what == 1) {
                    showToastSync("收藏成功");
                } else showToastSync("收藏失败");
            }
        };
        MuseumCollectedPost museumCollectedPost = new MuseumCollectedPost();
        more.setOnActionSelectedListener(actionItem -> {
            int id = actionItem.getId();
            switch (id) {
                case R.id.museum_menu_collect: {
                    //收藏

                    museumCollectedPost.setId(1);
                    museumCollectedPost.setMumid(museum.getId());
                    museumCollectedPost.setUserid(MainActivity.person.getId());
                    HttpRequestPost(collectedPost, museumCollectedPost);
                    return true;
                }
                case R.id.museum_menu_explain: {
                    //博物馆讲解
                    
                    return true;
                }
            }
            return false;
        });

    }

    /**
     * 添加评论
     *
     * @param comment 评论
     * @param isEnd   是否是最后一个条目，代表新增的最新一条评论，无需获取点赞数
     * @param isLiked 评论是否已经点过赞
     * @author 黄熠
     */
    public void addComment(Comment comment, boolean isEnd, boolean isLiked) {
        LinearLayout container = this.comment.getContainer();
        View commentView = LayoutInflater.from(container.getContext()).inflate(R.layout.museum_comment, container, false);
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

        CommentIsLiked commentIsLiked = new CommentIsLiked();
        like.setOnClickListener(v -> {
            if (comment.getUserid() != MainActivity.person.getId()) {
                Integer state = (Integer) like.getTag();
                Handler commentLikePost = new Handler(Looper.myLooper()) {
                    @Override
                    public void handleMessage(@NonNull Message msg) {
                        super.handleMessage(msg);
                        if (msg.what == 1) {
                            runOnUiThread(()->{
                                TextView view = commentView.findViewById(R.id.liked_number);
                                view.setText(String.valueOf(Integer.valueOf(view.getText().toString())+1));
                            });
                        }
                    }
                };
                if (state == COMMENT_LIKED) {
                    like.setImageResource(R.mipmap.like);
                    like.setTag(COMMENT_UNLIKED);
                    //已经点过赞了，取消点赞，下一步向后台提供数据
                    HttpRequestDelete(NetworkUtils.ResultType.COMMENT_LIKE_CANCEL_POST,commentLikePost,comment.getId());
                } else {
                    like.setImageResource(R.mipmap.like_active);
                    like.setTag(COMMENT_LIKED);
                    //点赞成功，向后台提供数据
                    commentIsLiked.setUserid(MainActivity.person.getId());
                    commentIsLiked.setCommentid(comment.getId());
                    commentIsLiked.setIslike(1);
                    HttpRequestPost(commentLikePost, commentIsLiked);
                }
            }

        });

        if (!isEnd) {
            //获取点赞数
            Handler commentLikeGet = new Handler(Looper.myLooper()) {
                @Override
                public void handleMessage(@NonNull Message msg) {
                    super.handleMessage(msg);
                    if (msg.what == 1) {
                        int liked_number = (int) msg.obj;
                        Context ctx = MuseumIntroActivity.this;
                        if (liked_number > 0) {
                            like.setImageResource(R.mipmap.like);
                            like.setTag(COMMENT_UNLIKED);
                        }

                        TextView view = commentView.findViewById(R.id.liked_number);
                        view.setText(String.valueOf(liked_number));
                    }
                }
            };
            HttpRequestGet(NetworkUtils.ResultType.COMMENT_LIKE, commentLikeGet, comment.getId().toString());
        }

        this.comment.addElement(commentView);
    }

    /**
     * @param grade_view
     * @author Zhy
     */
    public void addGrade(View grade_view) {

        RatingBar grade_environment = grade_view.findViewById(R.id.ratingBar_environment);
        grade_environment.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                score_environment = rating;
                Log.d("Score_Environment", "onRatingChanged: " + String.valueOf(rating));

            }
        });

        RatingBar grade_service = grade_view.findViewById(R.id.ratingBar_service);
        grade_service.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                score_service = rating;
                Log.d("Score_Service", "onRatingChanged: " + String.valueOf(rating));

            }
        });

        RatingBar grade_show = grade_view.findViewById(R.id.ratingBar_show);
        grade_show.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                score_show = rating;
                Log.d("Score_Show", "onRatingChanged: " + String.valueOf(rating));
            }
        });

        Button button = grade_view.findViewById(R.id.grade_commit);
        Handler ratingPost = new Handler(Looper.myLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                if (msg.what == 1) {
                    showToastSync("评分提交成功");
                } else showToastSync("评分提交失败");
            }
        };
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MuseumIntroActivity.this, "score_environment:" + String.valueOf(score_environment) + " score_service:" + String.valueOf(score_service) + " score_show:" + String.valueOf(score_show), Toast.LENGTH_SHORT).show();
                Rating rating = new Rating(MainActivity.person.getId(), museum.getId(), (int)score_environment, (int)score_service, (int)score_show);
                HttpRequestPost(ratingPost, rating);
            }
        });


    }

    /**
     * 添加新闻
     *
     * @param museumNew 博物馆
     * @param isEnd     是否是最后一个条目，如果是则关闭底部边界
     * @author 黄熠
     */

    @SuppressLint("SetTextI18n")
    public void addNew(MuseumNew museumNew, boolean isEnd) {
        LinearLayout container = this.news.getContainer();
        View newsView = LayoutInflater.from(container.getContext()).inflate(R.layout.museum_new, container, false);
        TextView newsTitle = newsView.findViewById(R.id.new_title);
        TextView newsBrief = newsView.findViewById(R.id.new_content);
        TextView newsAuthor = newsView.findViewById(R.id.new_author);
        TextView newsTime = newsView.findViewById(R.id.new_time);
        TextView newsType = newsView.findViewById(R.id.new_type);
        if (isEnd) {
            newsView.findViewById(R.id.museum_newborder).setAlpha(0.0f);
        }

        newsTitle.setText(museumNew.getTitle());
        newsBrief.setText(museumNew.getContent());
        newsAuthor.setText(museumNew.getAuthor());
        newsTime.setText(museumNew.getTime());
        newsType.setText("分类：" + museumNew.getType1() + " ," + museumNew.getType2());

        //创建点击事件监听器
        newsView.setOnClickListener((view) -> {
            //暂时跳转到webView页面展示内容
            Intent in = new Intent(view.getContext(), WebViewActivity.class);
            in.putExtra("link", museumNew.getLink());
            startActivity(in);
        });
        this.news.addElement(newsView);
    }

    @Override
    protected void initData() {
        //------------数   据   造   假-------------

//        museumNews.add(new MuseumNew(0,"这个博物馆出大事了","小编","2021-5-15","www.jd.com","具体是什么大事呢，小编也不知道，下面就一起和小编来看看吧",museum.getName(),"坏事儿","Fake News"));
//        museumNews.add(new MuseumNew(1,"这个博物馆出大事了","小编","2021-5-15","www.taobao.com","具体是什么大事呢，小编也不知道，下面就一起和小编来看看吧",museum.getName(),"好事儿","Fake News"));
//        museumNews.add(new MuseumNew(2,"这个博物馆出大事了","小编","2021-5-15","www.qq.com","具体是什么大事呢，小编也不知道，下面就一起和小编来看看吧",museum.getName(),"坏事儿","Fake News"));
//        initNews();
    }

    private void initComments() {
        for (int i = 0; i < comments.size(); i++) {
            addComment(comments.get(i), false, false);
        }
    }

    private void initNews() {
        for (int i = 0; i < museumNews.size(); i++) {
            if (i == museumNews.size() - 1) {
                addNew(museumNews.get(i), true);
            } else {
                addNew(museumNews.get(i), false);
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
                    .placeholder(R.drawable.ic_museum_explain)
                    .into(imageView);
        }
    }
}
