package com.example.myapplication.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import com.example.myapplication.HomeActivity;
import com.example.myapplication.LoginActivity;
import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
//import com.example.myapplication.activity.LoginActivity;
//import com.example.myapplication.activity.MyCollectActivity;

import butterknife.BindView;
import butterknife.OnClick;
import skin.support.SkinCompatManager;


public class MyFragment extends BaseFragment {

//    @BindView(R.id.img_header)
//    ImageView imgHeader;

    public MyFragment() {
    }

    public static MyFragment newInstance() {
        MyFragment fragment = new MyFragment();
        return fragment;
    }

    @Override
    protected int initLayout() {
        return R.layout.fragment_my;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }
    @OnClick({ R.id.rl_logout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_logout:
                removeByKey("token");
                navigateToWithFlag(MainActivity.class,
                        Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                break;
        }
    }

}