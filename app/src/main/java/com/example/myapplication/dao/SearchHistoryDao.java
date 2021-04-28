package com.example.myapplication.dao;

import android.util.Log;

import com.example.myapplication.entity.SearchHistory;

import org.litepal.LitePal;

import java.util.ArrayList;

public class SearchHistoryDao {
    private static final String TAG = "SearchHistoryDao";

    public SearchHistoryDao() {
    }

    public static ArrayList<SearchHistory> findAll(){
        ArrayList<SearchHistory> arrayList = new ArrayList<>();
        arrayList.addAll(LitePal.findAll(SearchHistory.class));
        return arrayList;
    }

    public static SearchHistory find(Integer id){
        Log.d(TAG, "find: "+LitePal.find(SearchHistory.class,id));
        return LitePal.find(SearchHistory.class,id);
    }

    public static void insert(SearchHistory SearchHistory){
        SearchHistory.save();
        Log.d(TAG, "insert: "+SearchHistory.toString());
    }

    public static void delete(Integer id){
        Log.d(TAG, "delete: "+LitePal.find(SearchHistory.class,id));
        LitePal.delete(SearchHistory.class,id);
    }

    public static void deleteAll(){
        Log.d(TAG, "deleteAll");
        LitePal.deleteAll(SearchHistory.class);
    }

    public static void update(SearchHistory SearchHistory){
        String min = LitePal.min( SearchHistory.class,"searchTime",String.class);
        Log.d(TAG, "update from: "+LitePal.where("searchTime = ?",min).find(SearchHistory.class));
        SearchHistory.updateAll("searchTime = ?",min);
        String max = LitePal.max( SearchHistory.class,"searchTime",String.class);
        Log.d(TAG, "update to: "+LitePal.where("searchTime = ?",max).find(SearchHistory.class));
    }

}
