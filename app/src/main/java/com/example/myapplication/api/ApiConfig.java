package com.example.myapplication.api;

public class ApiConfig {
    public static final int PAGE_SIZE = 5;
    public static final String BASE_URl = "http://8.140.136.108/prod-api";
    public static final String MapMarkerList = "/system/museum/list"; //地图标点
    public static final String Libitem = "/system/exhibitcollection/select/all/";
    public static final String exhiItem = "/system/exhibitcollection/list";
    public static final String LOGIN = "/app/login"; //登录
    public static final String REGISTER = "/app/register";//注册
    public static final String VIDEO_LIST_ALL = "/app/videolist/listAll";//所有类型视频列表
    public static final String VIDEO_LIST_BY_CATEGORY = "/app/videolist/getListByCategoryId";//各类型视频列表
    public static final String VIDEO_CATEGORY_LIST = "/app/videocategory/list";//视频类型列表
    public static final String NEWS_LIST = "/app/news/api/list";//资讯列表
    public static final String VIDEO_UPDATE_COUNT = "/app/videolist/updateCount";//更新点赞,收藏,评论
    public static final String VIDEO_MYCOLLECT = "/app/videolist/mycollect";//我的收藏
}
