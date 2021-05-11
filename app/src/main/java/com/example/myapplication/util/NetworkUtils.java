package com.example.myapplication.util;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.myapplication.entity.Comment;
import com.example.myapplication.entity.Museum;

import java.io.IOException;
import java.util.Formatter;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.content.ContentValues.TAG;

//import org.json.JSONObject;

public class NetworkUtils {
    /***
     * 评论的实体类我已经建了 ——黄熠
     */

    public enum ResultType {
        ALL_MUSEUM, //博物馆查询结果
        MUSEUM,     //单个博物馆查询
        COMMENT,    //评论查询
        COMMENT_POST, //提交评论
        USER_COMMENT,//用户评论查询
        ITEMS,      //藏品查询
        SHOWS,      //展览查询
        TEST,       //测试
        ;
    }

    private static final HashMap<ResultType, String> m = new HashMap<ResultType, String>() {{
        put(ResultType.MUSEUM, "http://8.140.136.108:8080/system/museum/select/all/%s");
        put(ResultType.COMMENT, "http://8.140.136.108:8080/system/comments/select/all/%s");
        put(ResultType.COMMENT_POST,"http://8.140.136.108:8080/system/comments");
        put(ResultType.TEST, "http://8.140.136.108:8081/sitemap.json");
    }};
    private static final OkHttpClient client = new OkHttpClient.Builder()
            .build();

    public static void HttpRequestPost(ResultType resultType,Handler handler
    ) {
        String url = m.get(resultType);
        Comment comment=new Comment(1,1,1,"曾梅","2021-05-19","==这个博物馆不太行==");//假数据
        JSONObject jsonComment= (JSONObject) JSONObject.toJSON(comment);
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody requestBody = RequestBody.create(JSON,jsonComment.toJSONString());

        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        final Call call = client.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.e(TAG, "onFailure: ", e);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                int code = response.code();
                Message message = new Message();
                message.what = 1;
                switch (code){
                    default:{
                        message.what = 0;
                        break;
                    }
                    case 200:{
                        String s = response.body().string();
                        JSONObject obj = JSONObject.parseObject(s);
                        int c = obj.getInteger("code");
                        if (c == 200) {
                            message.what = 1;
                        } else {
                            message.what = 0;
                        }
                        break;
                    }
                }
                handler.handleMessage(message);
                System.out.println("_____________________OK_______________________");
            }
        });
    }

    public static void HttpRequestGet(ResultType resultType, Handler handler, String... args) {
        String url = m.get(resultType);
        if (args != null) {
            Formatter formatter = new Formatter();
//            formatter.format(url,args);
            url = formatter.toString();
            System.out.println(url);
        } else {
            url.replaceAll("%s", "");
        }
        assert url != null;
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("EEE", e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                int code = response.code();
                if (code == 200) {
                    String result = response.body().string();
                    JSONObject outcome;
                    outcome = JSON.parseObject(result);
                    Object send = null;
                    switch (resultType) {
                        case MUSEUM: {
                            JSONArray data = outcome.getJSONArray("rows");
                            List<Museum> museums = JSON.parseArray(data.toJSONString(), Museum.class);
                            send = museums;
                            System.out.println(museums.toString());
                            break;
                        }
                        case COMMENT: {
                            JSONArray data = outcome.getJSONArray("rows");
                            send = JSON.parseArray(data.toJSONString(), Comment.class);
                            break;
                        }
                        case TEST: {
                            JSONArray data = outcome.getJSONArray("rows");
                            List<Comment> comments = JSON.parseArray(data.toJSONString(), Comment.class);
                            send = comments;
                            System.out.println(comments.toString());
                            break;
                        }
                    }
                    Message message = new Message();
                    message.what = 1;
                    message.obj = send;
                    handler.sendMessage(message);
                } else {
                    Message message = new Message();
                    message.what = 0;
                    message.obj = null;
                    handler.sendMessage(message);
                }
            }
        });
    }
}
