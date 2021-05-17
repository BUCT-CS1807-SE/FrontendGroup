package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{

    private Button btn1;
    private EditText etAccount,etPassword,etPhone,etEmail,etBirthday;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        btn1 = findViewById(R.id.btn_1);
        btn1.setOnClickListener(this);
        etPhone = findViewById(R.id.et_1);
        etAccount = findViewById(R.id.et_2);
        etPassword = findViewById(R.id.et_3);
        etBirthday = findViewById(R.id.et_4);
        etEmail = findViewById(R.id.et_5);
    }

    @Override
    public void onClick(View v) {
        String phone = etPhone.getText().toString();
        String account = etAccount.getText().toString();
        String password = etPassword.getText().toString();
        String birthday = etBirthday.getText().toString();
        String email = etEmail.getText().toString();
        switch (v.getId()){
            case R.id.btn_1:
                System.out.println(phone+account+password+birthday+email);
                OkHttpClient client=new OkHttpClient.Builder()
                        .build();
                String url="http://8.140.136.108/prod-api/system/users";
                OkHttpClient mOkHttpClient = new OkHttpClient();
                JSONObject obj = new JSONObject();
                obj.put("name",account);
                obj.put("password",password);
                obj.put("phone",phone);
                obj.put("email",email);
                obj.put("pic","");
                obj.put("isforbidded",0);
                RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), obj.toJSONString());
                Request request = new Request.Builder().url(url).post(body).build();
                mOkHttpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.e("测试1", e+"");
                        Toast.makeText(RegisterActivity.this,"注册失败，有问题请联系管理员",Toast.LENGTH_LONG).show();
                        return;
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        Log.e("测试2", response.body().string());
                        Looper.prepare();
                        Toast.makeText(RegisterActivity.this,"注册成功！",Toast.LENGTH_LONG).show();
                        Intent intent;
                        intent=new Intent(RegisterActivity.this, MainActivity.class);
                        startActivity(intent);
                        Looper.loop();
                    }
                });
                break;
        }
    }
}