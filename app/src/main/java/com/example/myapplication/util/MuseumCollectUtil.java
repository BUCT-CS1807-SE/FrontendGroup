package com.example.myapplication.util;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.myapplication.MainActivity;
import com.example.myapplication.entity.MuseumCollectedPost;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static com.example.myapplication.util.NetworkUtils.HttpRequestGet;

public class MuseumCollectUtil {

    private static ConcurrentHashMap<Integer, MuseumCollectedPost> museumCollected;

    protected static ConcurrentHashMap<Integer,MuseumCollectedPost> getMuseumCollected() {
        return museumCollected;
    }

    public static boolean JudgeMuseumCollected(int museumId) {
        return museumCollected.containsKey(museumId);
    }

    public static void setMuseumCollected(ConcurrentHashMap<Integer,MuseumCollectedPost> set) {
        museumCollected = set;
    }

    public static MuseumCollectedPost getCollectInfo(int museumId) {
        return museumCollected.get(museumId);
    }

    public static Set<Integer> getMuseumIds() {
        return museumCollected.keySet();
    }

    public static void Build() {
        Handler museumCollectedHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                if(msg.what==1){
                    MuseumCollectUtil.setMuseumCollected((ConcurrentHashMap<Integer,MuseumCollectedPost>) msg.obj);
                } else {
                    Log.d("UTIL", "handleMessage: ERROR on updating museumCollected");
                }
            }
        };
        HttpRequestGet(NetworkUtils.ResultType.MUSEUM_COLLECTION_GET,museumCollectedHandler, MainActivity.person.getId());
    }

    public static void Build(Handler handler) {
        Handler museumCollectedHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                if(msg.what==1){
                    MuseumCollectUtil.setMuseumCollected((ConcurrentHashMap<Integer,MuseumCollectedPost>) msg.obj);
                    Message message = new Message();
                    message.what = 1;
                    handler.handleMessage(message);
                } else {
                    Log.d("UTIL", "handleMessage: ERROR on updating museumCollected");
                }
            }
        };
        HttpRequestGet(NetworkUtils.ResultType.MUSEUM_COLLECTION_GET,museumCollectedHandler, MainActivity.person.getId());
    }

}
