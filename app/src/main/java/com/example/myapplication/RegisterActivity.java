package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
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
        Intent intent;
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
                RequestBody formBody = new FormBody.Builder()
                        .add("id","11")
                        .add("name", "ssd")
                        .add("password", "12345678")
                        .add("phone","10086")
                        .add("email","456@qq.com")
                        .build();

                Request request = new Request.Builder().url(url).post(formBody).build();
                mOkHttpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.e("测试", e+"");
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        Log.e("测试", response.body().string());
                    }

                });
                Toast.makeText(RegisterActivity.this,"注册成功！",Toast.LENGTH_LONG).show();
                intent=new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(intent);
                break;
        }
    }
}