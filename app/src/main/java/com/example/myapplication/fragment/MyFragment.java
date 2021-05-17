package com.example.myapplication.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.myapplication.Edit_message;
import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
//import com.example.myapplication.activity.LoginActivity;
//import com.example.myapplication.activity.MyCollectActivity;

import butterknife.OnClick;

import static android.content.Context.MODE_PRIVATE;


public class MyFragment extends BaseFragment {

//    @BindView(R.id.img_header)
//    ImageView imgHeader;
private ImageButton btncollection,btnhistory,btnexplain,btnaccord,btnabout;
    private Button btnchangeInf,btnreturnmain;
    private TextView tvnickname;
    String nickname = MainActivity.person.getName();

    public MyFragment() {
    }

    public static MyFragment newInstance() {
        MyFragment fragment = new MyFragment();
        return fragment;
    }

    @Override
    protected int initLayout() {
        return R.layout.fragment_personal;
    }

    @Override
    protected void initView() {
        btncollection = mRootView.findViewById(R.id.btn_main_login);
        btncollection.setOnClickListener((view)->{

            SharedPreferences token_sp = mRootView.getContext().getSharedPreferences("token",MODE_PRIVATE);
            SharedPreferences.Editor edit = token_sp.edit();
            edit.putString("token",null);
            Intent intent;
            intent = new Intent(mRootView.getContext(),MainActivity.class);
            startActivity(intent);

        });
        btnchangeInf = mRootView.findViewById(R.id.btn_changeinf);
        btnchangeInf.setOnClickListener((view)->{
            Intent intent;
            intent = new Intent(mRootView.getContext(), Edit_message.class);
            startActivity(intent);
        });

        tvnickname = mRootView.findViewById(R.id.tv_1);
        tvnickname.setText(nickname);
    }

    @Override
    protected void initData() {

    }
//    @OnClick({ R.id.rl_logout})
//    public void onViewClicked(View view) {
//        switch (view.getId()) {
//            case R.id.rl_logout:
//                removeByKey("token");
//                navigateToWithFlag(MainActivity.class,
//                        Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                break;
//        }
//    }

}