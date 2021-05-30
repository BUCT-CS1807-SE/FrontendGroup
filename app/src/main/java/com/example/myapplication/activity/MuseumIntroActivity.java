package com.example.myapplication.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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
import com.chad.library.adapter.base.util.TouchEventUtil;
import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.adapter.MuseumExhibitionAdapter;
import com.example.myapplication.adapter.MuseumItemAdapter;
import com.example.myapplication.adapter.SearchResultAdapter;
import com.example.myapplication.entity.Comment;
import com.example.myapplication.entity.CommentIsLiked;
import com.example.myapplication.entity.CommentLikedSingleInfo;
import com.example.myapplication.entity.Exhibition;
import com.example.myapplication.entity.Item;
import com.example.myapplication.entity.Museum;
import com.example.myapplication.entity.MuseumCollectedPost;
import com.example.myapplication.entity.MuseumNew;
import com.example.myapplication.entity.Rating;
import com.example.myapplication.fragment.SearchFragment;
import com.example.myapplication.util.ImageUtils;
import com.example.myapplication.util.MuseumCollectUtil;
import com.example.myapplication.util.NetworkUtils;
import com.example.myapplication.view.InfoContainerView;
import com.leinardi.android.speeddial.FabWithLabelView;
import com.leinardi.android.speeddial.SpeedDialActionItem;
import com.leinardi.android.speeddial.SpeedDialView;
import com.scwang.smart.refresh.header.MaterialHeader;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;
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
import static com.example.myapplication.util.NetworkUtils.HttpRequestGetComment;
import static com.example.myapplication.util.NetworkUtils.HttpRequestGetGrade;
import static com.example.myapplication.util.NetworkUtils.HttpRequestPost;
import static com.example.myapplication.util.NetworkUtils.HttpRequestPut;

/**
 * 博物馆详情页
 *
 * @author 黄熠
 */
public class MuseumIntroActivity extends BaseActivity {

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
    private SpeedDialActionItem collectMuseum;

    private Banner banner;
    private ArrayList<String> list_path;

    private List<Comment> comments;
    private List<MuseumNew> museumNews;
    private List<Item> items;
    private List<Exhibition> exhibitions;
    private float score_environment = 2;
    private float score_service = 2;
    private float score_show = 2;
    private Rating rating = null;

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

        //---------轮播图----------
        banner = findViewById(R.id.mBanner);
        list_path = new ArrayList<>();
        list_path.add(ImageUtils.genURL(museum.getName()));
        banner.setViewPagerIsScroll(true);
        Handler getAddress = new Handler(Looper.myLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                if (msg.what == 1) {
                    List<String> arr = (List)msg.obj;
                    for (String s : arr) {
                        list_path.add(ImageUtils.genINTERIORURL(s));
                    }
                    banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
                    banner.setImageLoader(new MyLoader());
                    banner.setBannerAnimation(Transformer.Default);
                    banner.setDelayTime(6000);
                    banner.isAutoPlay(true);
                    banner.setIndicatorGravity(BannerConfig.CENTER);
                    banner.setImages(list_path)
                            .start();
                }
            }
        };
        HttpRequestGet(NetworkUtils.ResultType.INTERIOR,getAddress,museum.getId());

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
        scroller.setOnTouchListener((v, event) -> {
            banner.dispatchTouchEvent(event);
            return false;
        });


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
        comments = null;//网络接口完成后，初始化方式放入handler中
        Handler commentGet = new Handler(Looper.myLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                if (msg.what == 1) {
                    comment.getContainer().removeAllViewsInLayout();
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
                    comments = (List<Comment>) msg.obj;
                    for (Comment comment1 : comments) {
                        Handler commentLikedSingleGet = new Handler(Looper.myLooper()) {
                            private Comment c = comment1;
                            @Override
                            public void handleMessage(@NonNull Message msg) {
                                super.handleMessage(msg);
                                if (msg.what == 1) {
                                    List<CommentLikedSingleInfo> comment_like_info= (List<CommentLikedSingleInfo>) msg.obj;
                                    if(comment_like_info.size()==0){
                                        addComment(c,false,false);
                                    }else{
                                        addComment(c,false,true);
                                    }
                                }
                            }
                        };
                        HttpRequestGet(NetworkUtils.ResultType.COMMENT_LIKE_GET,commentLikedSingleGet,comment1.getId(),MainActivity.person.getId());
                    }
                }
            }
        };
        HttpRequestGet(NetworkUtils.ResultType.COMMENT, commentGet, museum.getId().toString());



        //----------藏品----------
        item = findViewById(R.id.items);
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
        HttpRequestGet(NetworkUtils.ResultType.ITEMS,getItems,museum.getId(),"");


        //----------展览----------
        exhibition = findViewById(R.id.exhibition);
        RecyclerView exhibitionContainer = new RecyclerView(exhibition.getContainer().getContext());
        LinearLayoutManager exhibit_manager = new LinearLayoutManager(exhibitionContainer.getContext());
        exhibit_manager.setOrientation(LinearLayoutManager.VERTICAL);
        exhibitionContainer.setLayoutManager(exhibit_manager);
        Handler getExhibition = new Handler(Looper.myLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                if (msg.what == 1) {
                    exhibitions = (List<Exhibition>) msg.obj;
                    for (int i = exhibitions.size()-1;i > 0;i--) {
                        Exhibition ex = exhibitions.get(i);
                        if (ex.getExhibitionName().equals(exhibitions.get(i-1).getExhibitionName())){
                            exhibitions.remove(i);
                        }
                    }

                    //未知原因的museumId被消除
                    for (Exhibition exhibition1 : exhibitions) {
                        exhibition1.setMuseumId(museum.getId());
                    }
                    runOnUiThread(() -> {
                        exhibitionContainer.setAdapter(new MuseumExhibitionAdapter(exhibitions));
                        exhibition.addElement(exhibitionContainer);
                    });
                } else {
                    showToast("获取展览失败");
                }
            }
        };
        HttpRequestGet(NetworkUtils.ResultType.SHOWS,getExhibition,museum.getId(),"");


        //----------菜单浮动按钮----------
        more = findViewById(R.id.more);
        more.addActionItem(new SpeedDialActionItem.Builder(R.id.museum_menu_explain, R.drawable.ic_museum_explain)
                .setFabBackgroundColor(Color.parseColor("#87a7d6"))
                .setLabel("博物馆讲解")
                .setLabelBackgroundColor(Color.parseColor("#efb336"))
                .setLabelColor(Color.WHITE)
                .create());
        if (MuseumCollectUtil.JudgeMuseumCollected(museum.getId())) {
            collectMuseum = new SpeedDialActionItem.Builder(R.id.museum_menu_collect, R.drawable.ic_collected_museum)
                    .setFabBackgroundColor(Color.parseColor("#7cba59"))
                    .setLabel("博物馆收藏")
                    .setLabelBackgroundColor(Color.parseColor("#efb336"))
                    .setLabelColor(Color.WHITE)
                    .create();
        } else {
            collectMuseum = new SpeedDialActionItem.Builder(R.id.museum_menu_collect, R.drawable.ic_collect_museum)
                    .setFabBackgroundColor(Color.parseColor("#7cba59"))
                    .setLabel("博物馆收藏")
                    .setLabelBackgroundColor(Color.parseColor("#efb336"))
                    .setLabelColor(Color.WHITE)
                    .create();
        }
        more.addActionItem(collectMuseum);

        Handler collectedPost = new Handler(Looper.myLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                if (msg.what == 1) {
                    MuseumCollectUtil.Build();
                    runOnUiThread(() -> {
                        FabWithLabelView byId = findViewById(R.id.museum_menu_collect);
                        byId.getFab().setImageResource(R.drawable.ic_collected_museum);
                    });
                    showToastSync("收藏成功");
                } else {
                    showToastSync("收藏失败");
                }
            }
        };

        Handler collectHandler = new Handler(Looper.myLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                if (msg.what == 1) {
                    MuseumCollectUtil.Build();
                    //取消收藏
                    runOnUiThread(() -> {
                        FabWithLabelView byId = findViewById(R.id.museum_menu_collect);
                        byId.getFab().setImageResource(R.drawable.ic_collect_museum);
                    });
                    showToastSync("取消收藏成功");
                } else {
                    showToastSync("取消收藏失败");
                }
            }
        };
        MuseumCollectedPost museumCollectedPost = new MuseumCollectedPost();
        more.setOnActionSelectedListener(actionItem -> {
            int id = actionItem.getId();
            switch (id) {
                case R.id.museum_menu_collect: {
                    //收藏
                    if (MuseumCollectUtil.JudgeMuseumCollected(museum.getId())) {
                        //取消收藏
                        int collectId = MuseumCollectUtil.getCollectInfo(museum.getId()).getId();
                        HttpRequestDelete(NetworkUtils.ResultType.MUSEUM_COLLECTION_POST,collectHandler,collectId);
                    } else {
                        //收藏
                        museumCollectedPost.setId(1);
                        museumCollectedPost.setMumid(museum.getId());
                        museumCollectedPost.setUserid(MainActivity.person.getId());
                        HttpRequestPost(collectedPost, museumCollectedPost);
                    }
                    return true;
                }
                case R.id.museum_menu_explain: {
                    //博物馆讲解
                    Intent intent=new Intent();
                    intent.putExtra("id",museum.getId().toString());
                    intent.putExtra("ShowName",museum.getName());
                    intent.putExtra("kind","MUSEUM");
                    intent.setClass(this,UserexplainActivity.class);
                    startActivity(intent);
                    return true;
                }
            }
            return false;
        });
        RefreshLayout refreshLayout = (RefreshLayout)findViewById(R.id.museumRefresh);
        refreshLayout.setRefreshHeader(new MaterialHeader(this));
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                refreshlayout.finishRefresh(500/*,false*/);//传入false表示刷新失败
            }
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
            Integer state = (Integer) like.getTag();
            Handler commentLikePost = new Handler(Looper.myLooper()) {
                @Override
                public void handleMessage(@NonNull Message msg) {
                    super.handleMessage(msg);
                    if (msg.what == 1) {
                        runOnUiThread(()->{
                            TextView view = commentView.findViewById(R.id.liked_number);
                            view.setText(String.valueOf(Integer.parseInt(view.getText().toString())+1));
                        });
                    }
                }
            };
            Handler commentLikeCancel = new Handler(Looper.myLooper()) {
                @Override
                public void handleMessage(@NonNull Message msg) {
                    super.handleMessage(msg);
                    if (msg.what == 1) {
                        runOnUiThread(()->{
                            TextView view = commentView.findViewById(R.id.liked_number);
                            view.setText(String.valueOf(Integer.parseInt(view.getText().toString())-1));
                        });
                    }
                }
            };
            if (state == COMMENT_LIKED) {

                //已经点过赞了，取消点赞，下一步向后台提供数据
                Handler getDetailInfo = new Handler(Looper.myLooper()) {
                    @Override
                    public void handleMessage(@NonNull Message msg) {
                        super.handleMessage(msg);
                        if (msg.what == 1) {
                            String ids = "";
                            List<Integer> list = (List<Integer>) msg.obj;
                            for (Integer integer : list) {
                                ids += integer;
                                if (!list.get(list.size()-1).equals(integer)) {
                                    ids += ",";
                                }
                            }
                            like.setImageResource(R.mipmap.like);
                            like.setTag(COMMENT_UNLIKED);
                            HttpRequestDelete(NetworkUtils.ResultType.COMMENT_LIKE_CANCEL_POST,commentLikeCancel,ids);
                        } else {
                            showToastSync("取消点赞失败");
                        }
                    }
                };
                HttpRequestGetComment(getDetailInfo, comment.getId());
            } else {
                like.setImageResource(R.mipmap.like_active);
                like.setTag(COMMENT_LIKED);
                //点赞成功，向后台提供数据
                commentIsLiked.setUserid(MainActivity.person.getId());
                commentIsLiked.setCommentid(comment.getId());
                commentIsLiked.setIslike(1);
                HttpRequestPost(commentLikePost, commentIsLiked);
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

        RatingBar grade_service = grade_view.findViewById(R.id.ratingBar_service);
        grade_service.setOnRatingBarChangeListener((ratingBar, rating, fromUser) -> {
            score_service = rating;
            Log.d("Score_Service", "onRatingChanged: " + String.valueOf(rating));

        });

        RatingBar grade_show = grade_view.findViewById(R.id.ratingBar_show);
        grade_show.setOnRatingBarChangeListener((ratingBar, rating, fromUser) -> {
            score_show = rating;
            Log.d("Score_Show", "onRatingChanged: " + String.valueOf(rating));
        });

        RatingBar grade_environment = grade_view.findViewById(R.id.ratingBar_environment);
        grade_environment.setOnRatingBarChangeListener((ratingBar, rating, fromUser) -> {
            score_environment = rating;
            Log.d("Score_Show", "onRatingChanged: " + String.valueOf(rating));
        });

        Handler getRating = new Handler(Looper.myLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                if (msg.what == 1) {
                    List<Rating> list = (List<Rating>) msg.obj;
                    if(list.size() <= 0) {
                        return;
                    }
                    rating = list.get(0);
                    score_environment = rating.getScoreone();
                    score_service = rating.getScoretwo();
                    score_show = rating.getScorethree();

                    grade_service.setRating(score_service);
                    grade_show.setRating(score_show);
                    grade_environment.setRating(score_environment);
                }
            }
        };
        HttpRequestGetGrade(getRating,museum.getId());

        Button button = grade_view.findViewById(R.id.grade_commit);
        Handler ratingPost = new Handler(Looper.myLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                if (msg.what == 1) {
                    showToastSync("评分提交成功");
                } else {
                    showToastSync("评分提交失败");
                };
            }
        };
        Handler ratingPut = new Handler(Looper.myLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                if (msg.what == 1) {
                    showToastSync("评分修改成功");
                } else {
                    showToastSync("评分修改失败");
                };
            }
        };
        button.setOnClickListener(v -> {
            if (this.rating == null) {
                Rating rating = new Rating(null,MainActivity.person.getId(), museum.getId(), (int)score_environment, (int)score_service, (int)score_show);
                HttpRequestPost(ratingPost, rating);
            } else {
                assert rating != null;
                rating.setScoreone((int)score_environment);
                rating.setScoretwo((int)score_service);
                rating.setScorethree((int)score_show);
                HttpRequestPut(ratingPut, rating);
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
        TextView newsAuthor = newsView.findViewById(R.id.new_author);
        TextView newsTime = newsView.findViewById(R.id.new_time);
        TextView newsType = newsView.findViewById(R.id.new_type);
        if (isEnd) {
            newsView.findViewById(R.id.museum_newborder).setAlpha(0.0f);
        }

        newsTitle.setText(museumNew.getTitle());
        newsAuthor.setText(museumNew.getAuthor());
        newsTime.setText(museumNew.getTime());
        if ((museumNew.getType1() != null&&museumNew.getType2() != null)
        &&(!"?".equals(museumNew.getType1())&&!"?".equals(museumNew.getType2()))) {
            newsType.setText("分类：" + museumNew.getType1() + " ," + museumNew.getType2());
        }

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
