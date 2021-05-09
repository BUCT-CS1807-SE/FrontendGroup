package com.example.myapplication.util;

import android.os.Handler;
import android.os.Message;
import android.util.JsonReader;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.myapplication.entity.Comment;
import com.example.myapplication.entity.Museum;

//import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.amap.api.navi.AmapNaviPage.TAG;

public class NetworkUtils {
    /***
     * 评论的实体类我已经建了 ——黄熠
     */

    public enum ResultType{
        ALL_MUSEUM, //博物馆查询结果
        MUSEUM,     //单个博物馆查询
        COMMENT,    //评论查询
        USER_COMMENT,//用户评论查询
        ITEMS,      //藏品查询
        SHOWS,      //展览查询
        TEST,       //测试
        ;
    }
    private static final HashMap<ResultType,String> m=new HashMap<ResultType,String>(){{
        put(ResultType.MUSEUM,"http://8.140.136.108:8080/system/museum/select/all/%s");
        put(ResultType.COMMENT,"http://8.140.136.108:8080/system/comments/select/all/%s");
        put(ResultType.TEST,"http://8.140.136.108:8081/sitemap.json");
    }};
    public static void HttpRequestGet(ResultType resultType, Handler handler,String... args) {
        String url=m.get(resultType);
        if(args!=null){
            Formatter formatter=new Formatter();
            formatter.format(url,args);
            url=formatter.toString();
            System.out.println(url);
        }else{
            url.replaceAll("%s","");
        }
        OkHttpClient client=new OkHttpClient.Builder()
                .build();
        assert url != null;
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
                int code = response.code();
                if(code==200) {
                    String result = response.body().string();
                    JSONObject outcome;
                    outcome = JSON.parseObject(result);
                    Object send = null;
                    switch (resultType){
                        case MUSEUM:{
                           JSONArray data = outcome.getJSONArray("rows");

                           List<Museum> museums=JSON.parseArray(data.toJSONString(),Museum.class);
                           send=museums;
                           System.out.println(museums.toString());
                           break;
                        }
                        case COMMENT:{
                            JSONArray data = outcome.getJSONArray("rows");
                            List<Comment> comments= JSON.parseArray(data.toJSONString(),Comment.class);
                            send=comments;
                            break;
                        }
                        case TEST:{
                            JSONArray data = outcome.getJSONArray("rows");
                            List<Comment> comments= JSON.parseArray(data.toJSONString(),Comment.class);
                            send=comments;
                            System.out.println(comments.toString());
                            break;
                        }
                    }
                    Message message = new Message();
                    message.what = 1;
                    message.obj = send;
                    handler.sendMessage(message);
                    Log.e("EEE", result);
                }else{
                    Message message = new Message();
                    message.what=0;
                    message.obj=null;
                    handler.sendMessage(message);
                }
            }
        });
    }
}
