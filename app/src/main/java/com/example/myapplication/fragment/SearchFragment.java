package com.example.myapplication.fragment;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;

import com.example.myapplication.R;
import com.example.myapplication.entity.Museum;
import com.example.myapplication.util.NetworkUtils;

import java.util.ArrayList;
import java.util.List;

import static com.example.myapplication.util.NetworkUtils.HttpRequestGet;

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
            }
        });
    }
    @Override
    protected void initData() {

    }

}
