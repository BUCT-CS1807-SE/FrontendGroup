package com.example.myapplication.fragment;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amap.api.services.interfaces.IShareSearch;
import com.bumptech.glide.Glide;
import com.bumptech.glide.ListPreloader;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.integration.recyclerview.RecyclerViewPreloader;
import com.bumptech.glide.util.ViewPreloadSizeProvider;
import com.example.myapplication.R;
import com.example.myapplication.adapter.SearchResultAdapter;
import com.example.myapplication.dao.SearchHistoryDao;
import com.example.myapplication.entity.Museum;
import com.example.myapplication.entity.Museum;
import com.example.myapplication.entity.SearchHistory;
import com.example.myapplication.util.ImageUtils;
import com.example.myapplication.util.NetworkUtils;
import com.example.myapplication.view.FlowLayout;
import com.scwang.smart.refresh.header.MaterialHeader;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.crypto.spec.DHGenParameterSpec;

import static com.example.myapplication.util.NetworkUtils.HttpRequestGet;

public class SearchFragment extends BaseFragment {

    private static final String TAG = "SearchFragment";
    private TextView searchKey;
    private ImageButton search;
    private RecyclerView result;
    private List<Museum> museums;
    private RequestManager requestManager;
    private FlowLayout flowLayout;
    private List<String> list=new ArrayList<>();
    private List<SearchHistory> HistoryList=new ArrayList<>();
    private View DeleteHistory;

    private class SearchResultPreloadModelProvider<U> implements ListPreloader.PreloadModelProvider {

        @NonNull
        @Override
        public List getPreloadItems(int position) {
            Museum data = museums.get(position);
            String url = ImageUtils.genURL(data.getName());
            if (TextUtils.isEmpty(url)) {
                return Collections.emptyList();
            }
            return Collections.singletonList(url);
        }

        @Nullable
        @org.jetbrains.annotations.Nullable
        @Override
        public RequestBuilder<?> getPreloadRequestBuilder(@NonNull Object url) {
            return requestManager.load(url);
        }
    }

    private SearchResultPreloadModelProvider modelProvider;
    private ViewPreloadSizeProvider sizeProvider;
    private RecyclerViewPreloader<String> preloader;


    @Override
    protected void initData() {

    }

    public static SearchFragment newInstance() {
        SearchFragment fragment = new SearchFragment();
        return fragment;
    }

    @Override
    protected int initLayout() {
        return R.layout.fragment_search;
    }

    @Override
    protected void initView() {
        requestManager = Glide.with(this);
        search=mRootView.findViewById(R.id.search);
        result = mRootView.findViewById(R.id.searchResult);

        modelProvider = new SearchResultPreloadModelProvider();
        sizeProvider = new ViewPreloadSizeProvider();
        preloader = new RecyclerViewPreloader<>(requestManager, modelProvider, sizeProvider, 5);

        //@author 黄熠
        //初始化博物馆信息RecyclerView



        //--------------------------


        //初始化博物馆历史记录list
        flowLayout = mRootView.findViewById(R.id.flow);

        //list.add("Android");
        GetHistory();



        //搜索框响应事件
        searchKey = mRootView.findViewById(R.id.searchKey);
        search = mRootView.findViewById(R.id.search);
        search.setOnClickListener(v -> {
            String key = searchKey.getText().toString();
            if (key.isEmpty()) {
                showToast("请输入搜索关键字");
                return;
            }

            //@TODO 搜索,网络发起请求并调用adapter展示
            Handler handler=new Handler(Looper.myLooper()){
                @Override
                public void handleMessage(@NonNull Message msg) {
                    super.handleMessage(msg);
                    if(msg.what==1){
                        museums= (List<Museum>) msg.obj;
                        if (museums.size() == 0) {
                            showToast("没有搜索到这个博物馆信息");
                            return;
                        }
                        result.addOnScrollListener(preloader);
                        result.setAdapter(new SearchResultAdapter((ArrayList<Museum>) museums));
                        result.setLayoutManager(new LinearLayoutManager(SearchFragment.this.getContext()));
                    }
                }
            };
            HttpRequestGet(NetworkUtils.ResultType.MUSEUM,handler, key);
            //@TODO 存入搜索历史
            InsertHistory(key);
            GetHistory();

        });

        //删除按钮相应事件
        DeleteHistory=mRootView.findViewById(R.id.DeleteHistory);
        DeleteHistory.setOnClickListener(v -> {
            DeleteHistory();
            GetHistory();
            showToast("已删除搜索记录");
        });

        //将list数据添加进flowlayout中

        //下拉刷新
        RefreshLayout refreshLayout = (RefreshLayout)mRootView.findViewById(R.id.searchRefresh);
        refreshLayout.setRefreshHeader(new MaterialHeader(mRootView.getContext()));
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {

                String key = searchKey.getText().toString();
                if (key.isEmpty()) {
                    showToast("请输入搜索关键字");
                    refreshlayout.finishRefresh(500/*,false*/);//传入false表示刷新失败
                    return;
                }

                //@TODO 搜索,网络发起请求并调用adapter展示
                Handler handler=new Handler(Looper.myLooper()){
                    @Override
                    public void handleMessage(@NonNull Message msg) {
                        super.handleMessage(msg);
                        if(msg.what==1){
                            museums= (List<Museum>) msg.obj;
                            refreshlayout.finishRefresh(500/*,false*/);//传入false表示刷新失败
                            if (museums.size() == 0) {
                                showToast("没有搜索到这个博物馆信息");
                                return;
                            }
                            result.addOnScrollListener(preloader);
                            result.setAdapter(new SearchResultAdapter((ArrayList<Museum>) museums));
                            result.setLayoutManager(new LinearLayoutManager(SearchFragment.this.getContext()));
                        }
                    }
                };
                HttpRequestGet(NetworkUtils.ResultType.MUSEUM,handler, key);
            }
        });

    }

    private void GetHistory()
    {
        list.clear();
        HistoryList= SearchHistoryDao.findAll();
        for(int i=0;i<HistoryList.size();i++)
        {
            list.add(HistoryList.get(i).getSearchContent());
            Log.d(TAG, "GetHistory: "+ HistoryList.get(i).getSearchContent());
        }
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(10, 5, 10, 5);
        if (flowLayout != null) {
            flowLayout.removeAllViews();
        }
        for (int i = 0; i < list.size(); i++) {
            TextView tv = new TextView(this.getContext());
            String key= list.get(i);
            tv.setPadding(28, 10, 28, 10);
            tv.setText(key);
            tv.setMaxEms(10);
            tv.setSingleLine();
            tv.setOnClickListener(v -> {
                ToSearch(key);
            });
            //tv.setBackgroundResource(R.drawable.selector_playsearch);
            tv.setLayoutParams(layoutParams);
            flowLayout.addView(tv, layoutParams);
        }
    }

    private void InsertHistory(String key)
    {
        SearchHistory s1=new SearchHistory();
        s1.setSearchContent(key);
        int flag=0;
        HistoryList=SearchHistoryDao.findAll();
        for(int i=0;i<HistoryList.size();i++)
        {
            Log.d(TAG, "InsertHistory: "+HistoryList.get(i).getSearchContent());
            if(HistoryList.get(i).getSearchContent().equals(key))
            {
                flag=1;
                break;
            }
        }
        if(flag==0)
        {
            SearchHistoryDao.insert(s1);
        }
    }

    private void DeleteHistory()
    {
        SearchHistoryDao.deleteAll();
    }

    private void ToSearch(String key)
    {
        searchKey.setText(key);
        //@TODO 搜索,网络发起请求并调用adapter展示
        Handler handler=new Handler(Looper.myLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                if(msg.what==1){
                    museums= (List<Museum>) msg.obj;
                    if (museums.size() == 0) {
                        showToast("没有搜索到这个博物馆信息");
                        return;
                    }
                    result.addOnScrollListener(preloader);
                    result.setAdapter(new SearchResultAdapter((ArrayList<Museum>) museums));
                    result.setLayoutManager(new LinearLayoutManager(SearchFragment.this.getContext()));
                }
            }
        };
        HttpRequestGet(NetworkUtils.ResultType.MUSEUM,handler, key);

    }



}
