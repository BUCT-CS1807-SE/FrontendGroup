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
import com.example.myapplication.entity.SearchOutcome;
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
        Button search=mRootView.findViewById(R.id.buttonSearch);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Handler handler=new Handler(Looper.myLooper()){
                    @Override
                    public void handleMessage(@NonNull Message msg) {
                        super.handleMessage(msg);
                        if(msg.what==1){
                            List<Museum> museums= (List<Museum>) msg.obj;
                        }
                    }
                };
                HttpRequestGet(NetworkUtils.ResultType.ALL_MUSEUM,handler);
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

            //@TODO 存入搜索历史

        });
        //--------------------

        //@author 黄熠
        //初始化博物馆信息RecyclerView
        ArrayList<SearchOutcome> dataset = new ArrayList<SearchOutcome>(){{
            add(new SearchOutcome(
                    "故宫博物院",
                    "不知道啥类型",
                    "北京市",
                    "60",
                    "8:30 - 17:00",
                    "无",
                    "很牛",
                    "这啥参数啊",
                    "不知道",
                    "？？？啥参数",
                    "故宫博物院是一个博物馆",
                    "好",
                    "好",
                    "好",
                    "好",
                    "好"
            ));
            add(new SearchOutcome(
                    "国家博物馆",
                    "不知道啥类型",
                    "北京市",
                    "60",
                    "8:30 - 17:00",
                    "无",
                    "很牛",
                    "这啥参数啊",
                    "不知道",
                    "？？？啥参数",
                    "国家博物馆是一个博物馆",
                    "好",
                    "好",
                    "好",
                    "好",
                    "好"
            ));
            add(new SearchOutcome(
                    "北京博物馆",
                    "不知道啥类型",
                    "北京市",
                    "60",
                    "8:30 - 17:00",
                    "无",
                    "很牛",
                    "这啥参数啊",
                    "不知道",
                    "？？？啥参数",
                    "北京博物馆是一个博物馆",
                    "好",
                    "好",
                    "好",
                    "好",
                    "好"
            ));
        }};//假数据

        result = mRootView.findViewById(R.id.searchResult);
        result.setAdapter(new SearchResultAdapter(dataset));
        result.setLayoutManager(new LinearLayoutManager(this.getContext()));
        //--------------------------


    }
    private void HttpRequestGet() {
        OkHttpClient client=new OkHttpClient.Builder()
                .build();
        String url="https://dict.youdao.com/";
        String plus="w/eng/你好/#keyfrom=dict2.index";

        Request request=new Request.Builder()
                .url(url)
                .get()
                .build();
        Call call=client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("EEE",e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result=response.body().string();
                Log.e("EEE",result);
            }
        });
    }
    @Override
    protected void initData() {

    }

}
