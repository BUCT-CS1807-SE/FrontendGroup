package com.example.myapplication.util;

import android.util.Log;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginRegUtils {

    public static void LoginRequestJudge() {
        OkHttpClient client=new OkHttpClient.Builder()
                .build();
        String url="http://8.140.136.108/prod-api/login";
        OkHttpClient mOkHttpClient = new OkHttpClient();
        RequestBody formBody = new FormBody.Builder()
                .add("username","admin")
                .add("password", "admin123")
                .add("code", "1234")
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
    }
}