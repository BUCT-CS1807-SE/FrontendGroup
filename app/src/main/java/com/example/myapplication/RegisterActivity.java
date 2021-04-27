package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myapplication.activity.BaseActivity;
import com.example.myapplication.activity.HomePageActivity;
import com.example.myapplication.api.Api;
import com.example.myapplication.api.ApiConfig;
import com.example.myapplication.api.TtitCallback;
import com.example.myapplication.util.StringUtils;

import java.util.HashMap;

public class RegisterActivity extends BaseActivity  {

    private static final String TAG = "RegisterActivity";
    private Button btnRegister;
    private EditText etAccount,etPass,etPassConfirm;
    private CheckBox cbAgree;


    @Override
    protected int initLayout() {
        return R.layout.activity_register;
    }

    protected void initView() {
        etAccount = findViewById(R.id.et_account);
        etPass = findViewById(R.id.et_password);
//        etPassConfirm = findViewById(R.id.et_password_confirm);
//        cbAgree = findViewById(R.id.cb_agree);
//        btnRegister = findViewById(R.id.btn_register);
    }


    protected void initData() {
//        btnRegister.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String name = etAccount.getText().toString();
//                String pass = etPass.getText().toString();
//                String passConfirm = etPassConfirm.getText().toString();
//
//                Log.d(TAG, "name: "+name);
//                Log.d(TAG, "pass: "+pass);
//                Log.d(TAG, "passConfirm: "+passConfirm);
//                if(TextUtils.isEmpty(name)){
//                    Toast.makeText(RegisterActivity.this,"用户名不能为空",Toast.LENGTH_LONG).show();
//                    return;
//                }
//                if(TextUtils.isEmpty(pass)){
//                    Toast.makeText(RegisterActivity.this,"密码不能为空",Toast.LENGTH_LONG).show();
//                    return;
//                }
//                if(!TextUtils.equals(pass,passConfirm)) {
//                    Toast.makeText(RegisterActivity.this, "密码不一致", Toast.LENGTH_LONG).show();
//                    return;
//                }
//
//                if(!cbAgree.isChecked()) {
//                    Toast.makeText(RegisterActivity.this, "请同意用户协议", Toast.LENGTH_LONG).show();
//                    return;
//                }
//                register(name, pass);
//            }
//        });
    }

    private void register(String account, String pwd) {
        if (StringUtils.isEmpty(account)) {
            showToast("请输入账号");
            return;
        }
        if (StringUtils.isEmpty(pwd)) {
            showToast("请输入密码");
            return;
        }
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("mobile", account);
        params.put("password", pwd);
        Api.config(ApiConfig.REGISTER, params).postRequest(this,new TtitCallback() {
            @Override
            public void onSuccess(final String res) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        navigateToWithFlag(HomePageActivity.class,
                                Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        showToast(res);
                    }
                });
            }

            @Override
            public void onFailure(Exception e) {
                Log.e("onFailure", e.toString());
            }
        });
    }


}