package com.example.myapplication;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.NonNull;

import com.example.myapplication.entity.Museum;
import com.example.myapplication.util.NetworkUtils;

import org.junit.Test;

import java.util.List;

import static com.example.myapplication.util.NetworkUtils.HttpRequestGet;
import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        Handler handler=new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                if(msg.what==1){
                    List<Museum> museums= (List<Museum>) msg.obj;
                    System.out.println(museums);
                }
            }
        };

        HttpRequestGet(NetworkUtils.ResultType.MUSEUM,handler, "故宫");
    }
}