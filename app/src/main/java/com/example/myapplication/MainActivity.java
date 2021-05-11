package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.myapplication.activity.HomePageActivity;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class  MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity" ;
    private Button btnLogin,btnregister;
    private EditText etPhone,etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        etPhone = findViewById(R.id.et_phone);
        etPassword = findViewById(R.id.et_password);
        btnregister=findViewById(R.id.btn_1);
        btnregister.setOnClickListener(this);
        btnLogin=findViewById(R.id.btn_2);
        btnLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v){
        String phone = etPhone.getText().toString();
        String password = etPassword.getText().toString();
        Intent intent;
        intent = new Intent(MainActivity.this, HomePageActivity.class);
        startActivity(intent);

//        switch (v.getId()){
//            case R.id.btn_1:
//                Intent intent1;
//                intent1=new Intent(MainActivity.this, RegisterActivity.class);
//                Log.d(TAG, "onClick: Register");
//                startActivity(intent1);
//                break;
//            case R.id.btn_2:
//                Log.d(TAG, "onClick: Login");
//                OkHttpClient client_l=new OkHttpClient.Builder()
//                        .build();
//                String url_l="http://8.140.136.108/prod-api/system/users/list?password="+password+"&phone="+phone;
//                Request request_l=new Request.Builder()
//                        .url(url_l)
//                        .get()
//                        .build();
//                Call call_l=client_l.newCall(request_l);
//                call_l.enqueue(new Callback() {
//                    @Override
//                    public void onFailure(Call call_l, IOException e) {
//                        Log.i("EEEE",e.getMessage());
//                    }
//
//                    @Override
//                    public void onResponse(Call call_l, Response response) throws IOException {
//                        int code = response.code();
//                        if(code==200) {
//                            String result = response.body().string();
//                            System.out.println(result);
//                            JSONObject outcome;
//                            outcome = JSON.parseObject(result);
//                            String total;
//                            total = outcome.getString("total");
//                            int isOK = Integer.valueOf(total);
//                            System.out.println(isOK);
//                           // LoginRegUtils.LoginRequestJudge();
//                            if(isOK == 1) {
//                                Looper.prepare();
//                                Toast.makeText(MainActivity.this, "登录成功！", Toast.LENGTH_LONG).show();
//                                Intent intent;
//                                intent = new Intent(MainActivity.this, HomePageActivity.class);
//                                startActivity(intent);
//                                Looper.loop();
//
//                           }
//                              else {
//                                Looper.prepare();
//                                Toast.makeText(MainActivity.this, "密码或手机号错误，登录失败！", Toast.LENGTH_LONG).show();
//                                Looper.loop();
//                                  return;
//                             }
//                            }else{
//                                System.out.println("查询失败！");
//                                return;
//                            }
//                        }
//                    });
//                    break;
//            }
        }
    }

