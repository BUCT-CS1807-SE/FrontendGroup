package com.example.myapplication.util;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.myapplication.entity.Museum;

//import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
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
        ALL_MUSEUM,
        MUSEUM,
        COMMENT;
    }
    private static final HashMap<ResultType,String> m=new HashMap<ResultType,String>(){{
        put(ResultType.ALL_MUSEUM,"localhost/dev-api/system/museum/select/all/{id}");
        put(ResultType.MUSEUM,"localhost/dev-api/system/museum/select/all/{id}");
        put(ResultType.COMMENT,"localhost/dev-api/system/museum/select/all/{id}");
    }};
    public static void HttpRequestGet(ResultType resultType, Handler handler) {
        String url=m.get(resultType);
        OkHttpClient client=new OkHttpClient.Builder()
                .build();
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
                    JSONArray data = outcome.getJSONArray("data");
                    List<Museum> museums=JSON.parseArray(data.toJSONString(),Museum.class);
                    Message message = new Message();
                    message.what = 1;
                    message.obj = museums;
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
