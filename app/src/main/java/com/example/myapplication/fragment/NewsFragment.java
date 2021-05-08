package com.example.myapplication.fragment;

import android.Manifest;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.google.gson.Gson;

import com.example.myapplication.R;

import com.example.myapplication.api.Api;
import com.example.myapplication.api.ApiConfig;
import com.example.myapplication.api.TtitCallback;
import com.example.myapplication.entity.NewsEntity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;


public class NewsFragment extends BaseFragment implements AMapLocationListener,LocationSource,AMap.OnMapTouchListener,AMap.OnMapClickListener,AMap.OnMarkerClickListener{


//    private NewsAdapter newsAdapter;
    private List<NewsEntity> datas = new ArrayList<>();
    private LinearLayoutManager linearLayoutManager;
    private int pageNum = 1;
    private MyLocationStyle myLocationStyle;
    private MapView mapView;
    private AMap aMap;

    private boolean followMove=true;
    private LocationSource.OnLocationChangedListener mListener;
    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption = null;
    private List<Marker> markerList = new ArrayList<>();
    public LinearLayout museumInfo;
    public NewsFragment() {
    }

    public static NewsFragment newInstance() {
        NewsFragment fragment = new NewsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_news, container, false);
        museumInfo=(LinearLayout)view.findViewById(R.id.museumInfo);
        museumInfo.setVisibility(View.GONE);
        initview(savedInstanceState,view);
        return view;
    }


    @Override
    protected int initLayout() {
        return R.layout.fragment_news;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData()
    {

    }
    private void initview( Bundle savedInstanceState,View view){

        mapView= view.findViewById(R.id.map);
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图
        mapView.onCreate(savedInstanceState);
        //初始化地图控制器对象
        aMap = mapView.getMap();
        myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER);
        myLocationStyle.interval(2000);
        aMap.setMyLocationStyle(myLocationStyle);
        aMap.setOnMapTouchListener(this);
        aMap.setOnMapClickListener(this);
        aMap.setOnMarkerClickListener(this);
        aMap.setLocationSource(this);
        aMap.setMyLocationEnabled(true);
        UiSettings uiSettings =  aMap.getUiSettings();
        uiSettings.setLogoBottomMargin(-150);
    }



    /**
     * 激活定位
     */
    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mListener = onLocationChangedListener;
        if (mLocationClient == null) {
            mLocationClient=new AMapLocationClient(getActivity());
            mLocationOption=new AMapLocationClientOption();
            mLocationClient.setLocationListener(this);
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);//高精度定位
            mLocationOption.setOnceLocationLatest(true);//设置单次精确定位
            mLocationClient.setLocationOption(mLocationOption);
            mLocationClient.startLocation();//启动定位
        }
    }

    /**
     * 停止定位
     */
    @Override
    public void deactivate() {
        mListener = null;
        if (mLocationClient != null) {
            mLocationClient.stopLocation();
            mLocationClient.onDestroy();
        }
        mLocationClient = null;
    }



    /**
     * 接收异步返回的定位结果
     *
     * @param aMapLocation
     */
    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (mListener != null&&aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {
                Log.d("success","定位成功");
                double latidude = aMapLocation.getLatitude();
                double longitude = aMapLocation.getLongitude();

                StringBuffer stringBuffer = new StringBuffer();
                stringBuffer.append("纬度"+latidude+"\n");
                stringBuffer.append("经度"+longitude+"\n");
                if (followMove) {
                    aMap.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(latidude,longitude)));
                }
                mListener.onLocationChanged(aMapLocation);// 显示系统小蓝点
            } else {
                String errText = "定位失败," + aMapLocation.getErrorCode()+ ": " + aMapLocation.getErrorInfo();
                Log.e("AmapErr",errText);
            }
        }
    }

    @Override
    public void onMapClick(LatLng latLng) {
        addMarker(latLng);
    }
    @Override
    public void onTouch(MotionEvent motionEvent) {
        if (followMove) {
            //用户拖动地图后，不再跟随移动，需要跟随移动时再把这个改成true
            followMove = false;
        }
        museumInfo.setVisibility(View.GONE);
        showToast("drag");
    }

    private void addMarker(LatLng latLng) {
        Marker marker = aMap.addMarker(new MarkerOptions().position(latLng).snippet("DefaultMarker"));
        markerList.add(marker);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        showToast("点击了marker");
        museumInfo.setVisibility(View.VISIBLE);
        return true;
    }
    /**
     * 必须重写以下方法
     */
    @Override
    public void onResume(){
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        mLocationClient.onDestroy();
    }


}