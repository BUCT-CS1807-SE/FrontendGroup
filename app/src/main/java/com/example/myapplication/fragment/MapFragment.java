package com.example.myapplication.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.example.myapplication.Cluster.ClusterClickListener;
import com.example.myapplication.Cluster.ClusterItem;
import com.example.myapplication.Cluster.ClusterOverlay;
import com.example.myapplication.Cluster.ClusterRender;
import com.example.myapplication.Cluster.RegionItem;

import com.example.myapplication.R;

import com.example.myapplication.activity.MuseumIntroActivity;
import com.example.myapplication.activity.UserexplainActivity;
import com.example.myapplication.api.Api;
import com.example.myapplication.api.ApiConfig;
import com.example.myapplication.api.TtitCallback;
import com.example.myapplication.entity.MapListResponse;
import com.example.myapplication.entity.Museum;
import com.example.myapplication.entity.RowsDTO;
import com.example.myapplication.entity.exhibitionItem;
import com.example.myapplication.entity.exhibitionResponse;
import com.example.myapplication.entity.exhtestEntity;
import com.example.myapplication.util.StringUtils;
import com.example.myapplication.xpopup.MapBottom;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.enums.PopupPosition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;


public class MapFragment extends BaseFragment implements AMapLocationListener,LocationSource,AMap.OnMapTouchListener,AMap.OnMapClickListener, ClusterRender,
        AMap.OnMapLoadedListener, ClusterClickListener {


//    private NewsAdapter newsAdapter;
    List<RowsDTO> datas =new ArrayList<>();
    List<RowsDTO> neardatas =new ArrayList<>();
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
    public CardView museumInfo;
    private TextView museumLevel;

    private ClusterOverlay mClusterOverlay;
    private int clusterRadius = 120;
    private ClusterRender clusterRender;
    private ClusterClickListener clusterClickListener;
    private TextView Libname;
    private TextView TurnToUserexplain;
    private Boolean FirstLoaded;
    private FloatingActionButton nearButton;
    private MapBottom mapBottom =null;
    private boolean flag = false;
    private Integer readyNum = 0;
    private Integer curpos = 0;
    public MapFragment() {
    }

    public static MapFragment newInstance() {
        MapFragment fragment = new MapFragment();
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
        clusterRender=this;
        clusterClickListener=this;
        museumInfo=(CardView) view.findViewById(R.id.component1);
        Libname=(TextView)view.findViewById(R.id.textView2);
        nearButton=view.findViewById(R.id.fab_poi);
        TurnToUserexplain=view.findViewById(R.id.textView3);
        museumLevel=view.findViewById(R.id.textView4);
        museumInfo.setVisibility(View.GONE);
        FirstLoaded=false;
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
        Libname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MuseumIntroActivity.class);
                Bundle bundle = new Bundle();
                Museum museum = turnRowDtotoMuseum(datas.get(curpos));
                bundle.putSerializable("museum",museum);
                intent.putExtra("museum_data",bundle);
                startActivity(intent);
            }
        });
        TurnToUserexplain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.putExtra("id",datas.get(curpos).getId().toString());
                intent.putExtra("ShowName",datas.get(curpos).getName());
                intent.putExtra("kind","MUSEUM");
                intent.setClass(getActivity(), UserexplainActivity.class);
                startActivity(intent);
            }
        });
        nearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if(readyNum < neardatas.size())
//                {
//                    showToast("正在搜索附近博物馆，请稍后....");
//                    return;
//                }
                mapBottom=new MapBottom(getActivity(),neardatas);

                new XPopup.Builder(getActivity())
                        .popupPosition(PopupPosition.Right)//右边
                        .hasStatusBarShadow(true) //启用状态栏阴影
                        .asCustom(mapBottom)
                        .show();
            }
        });


    }
    private void initview( Bundle savedInstanceState,View view){
        mapView= view.findViewById(R.id.map_view);
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
        aMap.setOnMapLoadedListener(this);
//        aMap.setOnMarkerClickListener(this);
        aMap.setLocationSource(this);
        aMap.setMyLocationEnabled(true);
        UiSettings uiSettings =  aMap.getUiSettings();
        uiSettings.setLogoBottomMargin(-150);
        uiSettings.setZoomControlsEnabled(false);
        uiSettings.setScaleControlsEnabled(true);

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

    }

    @Override
    public void onTouch(MotionEvent motionEvent) {
        if (followMove) {
            //用户拖动地图后，不再跟随移动，需要跟随移动时再把这个改成true
            followMove = false;
        }
        museumInfo.setVisibility(View.GONE);
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


    @Override
    public void onMapLoaded() {
        getMapMarkerList();
    }

    private void getMapMarkerList()
    {
        HashMap<String,Object> params= new HashMap<>();
        Api.config(ApiConfig.MapMarkerList,params).getRequest(new TtitCallback(){

            @Override
            public void onSuccess(String res) {
                MapListResponse response = new Gson().fromJson(res,MapListResponse.class);
                if(response!=null)
                {
                    datas =response.getRows();
                }
                new Thread() {
                    public void run() {
                        List<ClusterItem> items = new ArrayList<ClusterItem>();
                        double curlat=aMap.getMyLocation().getLatitude();
                        double curlon=aMap.getMyLocation().getLongitude();
                        LatLng curlatLng=new LatLng(curlat,curlon,false);
                        for (int i = 0; i < datas.size()-5; i++) {

                            double lat = datas.get(i).getLatitude();
                            double lon = datas.get(i).getLongitude();

                            LatLng latLng = new LatLng(lat, lon, false);

                            if(AMapUtils.calculateLineDistance(curlatLng,latLng)<100000)
                            {
                                neardatas.add(datas.get(i));
                            }
                            RegionItem regionItem = new RegionItem(latLng,
                                    datas.get(i).getName(),datas.get(i).getMuseumlevel(),i);
                            items.add(regionItem);
                        }

                        getExhinbitionName();

                        mClusterOverlay = new ClusterOverlay(aMap, items,
                                dp2px(getActivity(), clusterRadius),
                                getActivity());
                        mClusterOverlay.setClusterRenderer(clusterRender);
                        mClusterOverlay.setOnClusterClickListener(clusterClickListener);
                    }

                }
                        .start();

            }

            @Override
            public void onFailure(Exception e) {

            }
        });
    }

    @Override
    public void onClick(Marker marker, List<ClusterItem> clusterItems) {
        if(clusterItems.size()==1)
        {
            curpos=clusterItems.get(0).getpos();
            Libname.setText(clusterItems.get(0).getTitle());
            museumLevel.setText(clusterItems.get(0).getLevel());
            museumInfo.setVisibility(View.VISIBLE);
            aMap.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(marker.getPosition(), 17, 0, 0)));
        }
        else
        {
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            for (ClusterItem clusterItem : clusterItems) {
                builder.include(clusterItem.getPosition());
            }
            LatLngBounds latLngBounds = builder.build();
            aMap.animateCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 10)
            );
        }
    }

    @Override
    public Drawable getDrawAble(int clusterNum) {
        return null;
    }
    public int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public void getExhinbitionName(){
        for(int i=0;i<neardatas.size();i++)
        {
            HashMap<String,Object> params= new HashMap<>();
            int finalI = i;
            Api.config(ApiConfig.Libitem+neardatas.get(i).getId(),params).getRequest(new TtitCallback(){
                @Override
                public void onSuccess(String res) {
                    exhibitionResponse response = new Gson().fromJson(res,exhibitionResponse.class);
                    if(response!=null)
                    {
                        if(response.getRows().size()!=0)
                            neardatas.get(finalI).setExhName(response.getRows().get(0).getExhibitname());
                        else
                            neardatas.get(finalI).setExhName("暂无");
                        HashMap<String,Object> params= new HashMap<>();
                        params.put("museumid",neardatas.get(finalI).getId());
                        params.put("exhibitname",neardatas.get(finalI).getExhName());
                        Api.config(ApiConfig.exhiItem,params).getRequest(new TtitCallback(){
                            @Override
                            public void onSuccess(String res) {
                                exhibitionItem response = new Gson().fromJson(res,exhibitionItem.class);
                                if(response!=null)
                                {
                                    List<exhtestEntity> tempExhEntity = new ArrayList<>();
                                    for(int j=0;j<response.getRows().size();j++)
                                    {
                                        exhtestEntity temp =new exhtestEntity();
                                        temp.setName(response.getRows().get(j).getCollectionname());
                                        temp.setImageUrl(response.getRows().get(j).getCollectionimageurl());
                                        tempExhEntity.add(temp);
                                    }
                                    neardatas.get(finalI).setExhItem(tempExhEntity);
                                    readyNum++;
                                }
                            }

                            @Override
                            public void onFailure(Exception e) {
                                readyNum++;
                            }
                        });
                    }
                }

                @Override
                public void onFailure(Exception e) {

                }
            });
        }
    }
    private Museum turnRowDtotoMuseum(RowsDTO t)
    {
        Museum ans=new Museum();
        ans.setId(t.getId());
        ans.setName(t.getName());
        ans.setType(t.getType());
        ans.setAddress(t.getAddress());
        ans.setTicketPrice(t.getTicketprice().toString());
        ans.setOpeningHours(t.getOpeninghours());
        ans.setSuggestedtraveltime(t.getSuggestedtraveltime());
        ans.setMuseumlevel(t.getMuseumlevel());
        ans.setUnits(t.getUnits());
        ans.setAttractionlevel(t.getAttractionlevel());
        ans.setNumber(t.getNumber());
        ans.setIntroduction(t.getIntroduction());
        //ans.setScenery(t.getScenery().toString());
        ans.setHowtogo(t.getHowtogo());
        ans.setScenicspotsaround(t.getScenicspotsaround());
        ans.setCover(t.getCover());
        //ans.setNote(t.getNote().toString());
        return ans;
    }



}