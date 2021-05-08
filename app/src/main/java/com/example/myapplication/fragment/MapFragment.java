package com.example.myapplication.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
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
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeAddress;
import com.amap.api.services.geocoder.GeocodeQuery;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeAddress;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.example.myapplication.Cluster.ClusterClickListener;
import com.example.myapplication.Cluster.ClusterItem;
import com.example.myapplication.Cluster.ClusterOverlay;
import com.example.myapplication.Cluster.ClusterRender;
import com.example.myapplication.Cluster.RegionItem;

import com.example.myapplication.R;

import com.example.myapplication.RegisterActivity;
import com.example.myapplication.activity.MuseumIntroActivity;
import com.example.myapplication.activity.RouteActivity;
import com.example.myapplication.entity.NewsEntity;

import java.util.ArrayList;
import java.util.List;


public class MapFragment extends BaseFragment implements AMapLocationListener,LocationSource,AMap.OnMapTouchListener,AMap.OnMapClickListener, ClusterRender,
        AMap.OnMapLoadedListener, ClusterClickListener, View.OnKeyListener , GeocodeSearch.OnGeocodeSearchListener {


//    private NewsAdapter newsAdapter;
    private List<NewsEntity> datas = new ArrayList<>();
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
    MarkerOptions markerOptions;
    private ClusterOverlay mClusterOverlay;
    private int clusterRadius = 120;
    private ClusterRender clusterRender;
    private ClusterClickListener clusterClickListener;
    private Button MoreInf;
    private TextView Libname;
    private EditText etAddress;//目的地输入框
    private String city;
    private static final int PARSE_SUCCESS_CODE = 1000;//解析成功标识码
    private GeocodeSearch geocodeSearch;//地理编码搜索


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
        museumInfo=(CardView) view.findViewById(R.id.museumInfo);
        MoreInf =(Button)view.findViewById(R.id.button4);
        Libname=(TextView)view.findViewById(R.id.LibName);
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
        MoreInf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MuseumIntroActivity.class);
                intent.putExtra("museum_name",Libname.getText());
                startActivity(intent);
            }
        });
    }
    private void initview( Bundle savedInstanceState,View view){
        etAddress=view.findViewById(R.id.et_address);
        etAddress.setOnKeyListener(this);
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
        aMap.setOnMapLoadedListener(this);
//        aMap.setOnMarkerClickListener(this);
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
                city = aMapLocation.getCity();
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


//    private void addMarker(LatLng latLng) {
//        Marker marker = aMap.addMarker(new MarkerOptions().position(latLng).title("标题").snippet("详细信息").icon(BitmapDescriptorFactory.fromBitmap(bitmap)));
//        marker.showInfoWindow();
//        markerList.add(marker);
//    }
//public  void addMarkersToMap(Context context, AMap aMap, LatLng latlng) {
//    if (aMap != null) {
//        View view = View.inflate(context, R.layout.marker_view, null);
//        TextView textView =  view.findViewById(R.id.tv_libName);
//        Bitmap bitmap = convertViewToBitmap(view);
//        markerOptions = new MarkerOptions()
//                .position(latlng)
//                .draggable(true)
//               .icon(BitmapDescriptorFactory.fromBitmap(bitmap))
//               ;
//        Marker marker = aMap.addMarker(markerOptions);
//        markerList.add(marker);
//    }
//}

//    public static Bitmap convertViewToBitmap(View view) {
//        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
//        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
//        view.buildDrawingCache();
//        Bitmap bitmap = view.getDrawingCache();
//        return bitmap;
//    }


//    @Override
//    public boolean onMarkerClick(Marker marker) {
//        if (!marker.isInfoWindowShown()) {
//            //显示
//            marker.showInfoWindow();
//        } else {
//            //隐藏
//            marker.hideInfoWindow();
//        }
//        museumInfo.setVisibility(View.VISIBLE);
//        return true;
//    }
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
        //添加测试数据
        new Thread() {
            public void run() {

                List<ClusterItem> items = new ArrayList<ClusterItem>();

                //随机100个点
                for (int i = 0; i < 100; i++) {

                    double lat = Math.random()*50 + 39.474923;
                    double lon = Math.random()*50 + 116.027116;

                    LatLng latLng = new LatLng(lat, lon, false);
                    RegionItem regionItem = new RegionItem(latLng,
                            "test" + i);
                    items.add(regionItem);

                }
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
    public void onClick(Marker marker, List<ClusterItem> clusterItems) {
        if(clusterItems.size()==1)
        {
            Libname.setText(clusterItems.get(0).getTitle());
            museumInfo.setVisibility(View.VISIBLE);
            aMap.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(marker.getPosition(), 10, 0, 0)));
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


    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP) {
            //获取输入框的值
            String address = etAddress.getText().toString().trim();
            if (address == null || address.isEmpty()) {
                //showMsg("请输入地址");
            } else {
                //InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                //隐藏软键盘
                //imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);

                // name表示地址，第二个参数表示查询城市，中文或者中文全拼，citycode、adcode
                GeocodeQuery query = new GeocodeQuery(address, city);
                geocodeSearch.getFromLocationNameAsyn(query);
            }
            return true;
        }
        return false;
    }

    @Override
    public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int rCode) {
        //解析result获取地址描述信息
        if (rCode == PARSE_SUCCESS_CODE) {
            RegeocodeAddress regeocodeAddress = regeocodeResult.getRegeocodeAddress();
            //显示解析后的地址
            Log.d("MainActivity", regeocodeAddress.getFormatAddress());
            //showMsg("地址：" + regeocodeAddress.getFormatAddress());

            LatLonPoint latLonPoint = regeocodeResult.getRegeocodeQuery().getPoint();
            LatLng latLng = new LatLng(latLonPoint.getLatitude(), latLonPoint.getLongitude());
            //addMarker(latLng);
        } else {
            Log.d("fail","获取地址失败");
        }

    }

    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int rCode) {
        if (rCode == PARSE_SUCCESS_CODE) {
            List<GeocodeAddress> geocodeAddressList = geocodeResult.getGeocodeAddressList();
            if (geocodeAddressList != null && geocodeAddressList.size() > 0) {
                LatLonPoint latLonPoint = geocodeAddressList.get(0).getLatLonPoint();
                //显示解析后的坐标
                //showMsg("坐标：" + latLonPoint.getLongitude() + "，" + latLonPoint.getLatitude());
            }

        } else {
            //showMsg("获取坐标失败");
        }


    }


}