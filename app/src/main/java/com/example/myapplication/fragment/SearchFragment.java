package com.example.myapplication.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.myapplication.R;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SearchFragment extends BaseFragment {

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
                HttpRequestGet();
            }
        });
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
