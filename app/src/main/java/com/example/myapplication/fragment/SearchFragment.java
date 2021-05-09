package com.example.myapplication.fragment;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.adapter.SearchResultAdapter;
import com.example.myapplication.entity.Museum;
import com.example.myapplication.entity.Museum;
import com.example.myapplication.util.NetworkUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.ArrayList;
import java.util.List;

import static com.example.myapplication.util.NetworkUtils.HttpRequestGet;

public class SearchFragment extends BaseFragment {

    private TextView searchKey;
    private ImageButton search;
    private RecyclerView result;

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
        search=mRootView.findViewById(R.id.search);

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
                        List<Museum> museums= (List<Museum>) msg.obj;
                        System.out.println(museums.toString());
                        result = mRootView.findViewById(R.id.searchResult);
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
