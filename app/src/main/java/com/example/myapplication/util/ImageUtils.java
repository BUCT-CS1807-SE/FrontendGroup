package com.example.myapplication.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ImageUtils {

    public static final String IMAGE_URL = "http://8.140.136.108/coverpic/";
    public static final String MUSEUM_EXPLAIN_URL = "http://8.140.136.108/prod-api/system/museumexplain/select/pic/";
    public static final String EXHIBITION_EXPLAIN_URL="http://8.140.136.108/prod-api/system/exhibitexplain/select/pic/";
    public static final String COLLECTION_EXPLAIN_URL="http://8.140.136.108/prod-api/system/collectionexplain/select/pic/";

    public static String genURL(String name) {
        return IMAGE_URL+name+".jpg";
    }
    public static String genExplainURL(String name) {
        return MUSEUM_EXPLAIN_URL+name+"";
    }

    public static String genEXHIBITIONURL(String name) {
        return EXHIBITION_EXPLAIN_URL+name+"";
    }
    public static String genCOLLECTIONURL(String name) {
        return COLLECTION_EXPLAIN_URL+name+"";
    }
    public static Bitmap getURLimage(String url) {
        Bitmap bmp = null;
        try {
            URL myurl = new URL(url);
            // 获得连接
            HttpURLConnection conn = (HttpURLConnection) myurl.openConnection();
            conn.setConnectTimeout(6000);//设置超时
            conn.setDoInput(true);
            conn.setUseCaches(false);//不缓存
            conn.connect();
            InputStream is = conn.getInputStream();//获得图片的数据流
            bmp = BitmapFactory.decodeStream(is);
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bmp;
    }

}
