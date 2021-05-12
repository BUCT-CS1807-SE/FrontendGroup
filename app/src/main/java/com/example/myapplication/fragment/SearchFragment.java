package com.example.myapplication.fragment;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.ListPreloader;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.integration.recyclerview.RecyclerViewPreloader;
import com.bumptech.glide.util.ViewPreloadSizeProvider;
import com.example.myapplication.R;
import com.example.myapplication.adapter.SearchResultAdapter;
import com.example.myapplication.entity.Museum;
import com.example.myapplication.entity.Museum;
import com.example.myapplication.util.ImageUtils;
import com.example.myapplication.util.NetworkUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.example.myapplication.util.NetworkUtils.HttpRequestGet;

public class SearchFragment extends BaseFragment {

    private TextView searchKey;
    private ImageButton search;
    private RecyclerView result;
    private List<Museum> museums;
    private RequestManager requestManager;

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
                            showToastSync("没有搜索到这个博物馆信息");
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

        });

        //--------------------

    }

}
