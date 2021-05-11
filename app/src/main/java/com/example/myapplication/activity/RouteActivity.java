package com.example.myapplication.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeAddress;
import com.amap.api.services.geocoder.GeocodeQuery;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.route.BusPath;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RidePath;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkPath;
import com.amap.api.services.route.WalkRouteResult;
import com.example.myapplication.overlay.DrivingRouteOverlay;
import com.example.myapplication.overlay.WalkRouteOverlay;
import com.example.myapplication.util.MapUtil;

import java.util.List;

import static com.example.myapplication.util.MapUtil.convertToLatLng;
import static com.example.myapplication.util.MapUtil.convertToLatLonPoint;
import com.example.myapplication.R;

public class RouteActivity extends AppCompatActivity implements
        AMapLocationListener, LocationSource, AMap.OnMapClickListener,
        RouteSearch.OnRouteSearchListener, EditText.OnKeyListener,
        GeocodeSearch.OnGeocodeSearchListener {

    private static final String TAG = "RouteActivity";
    private MapView mapView;
    private AMap aMap = null;
    public AMapLocationClient mLocationClient = null;
    public AMapLocationClientOption mLocationOption = null;
    private OnLocationChangedListener mListener;
    private UiSettings mUiSettings;
    private MyLocationStyle myLocationStyle = new MyLocationStyle();
    private LatLonPoint mStartPoint;
    private LatLonPoint mEndPoint;
    private RouteSearch routeSearch;
    private static final String[] travelModeArray = {"步行出行", "驾车出行"};
    private static int TRAVEL_MODE = 0;
    private ArrayAdapter<String> arrayAdapter;
    private String city;
    private RelativeLayout bottomLayout;
    private TextView tvTime;
    private EditText etStartAddress, etEndAddress;
    private GeocodeSearch geocodeSearch;
    private static final int PARSE_SUCCESS_CODE = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route);
        initLocation();
        initMap(savedInstanceState);
        mLocationClient.startLocation();
        initRoute();
        initTravelMode();
    }

    private void initTravelMode() {
        Spinner spinner = findViewById(R.id.spinner);
        bottomLayout = findViewById(R.id.bottom_layout);
        tvTime = findViewById(R.id.tv_time);
        etStartAddress = findViewById(R.id.et_start_address);
        etEndAddress = findViewById(R.id.et_end_address);
        etEndAddress.setOnKeyListener(this);
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, travelModeArray);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TRAVEL_MODE = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void initRoute() {
        routeSearch = new RouteSearch(this);
        routeSearch.setRouteSearchListener(this);
    }


    private void initLocation() {
        mLocationClient = new AMapLocationClient(getApplicationContext());
        mLocationClient.setLocationListener(this);
        mLocationOption = new AMapLocationClientOption();
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        mLocationOption.setOnceLocationLatest(true);
        mLocationOption.setNeedAddress(true);
        mLocationOption.setHttpTimeOut(20000);
        mLocationOption.setLocationCacheEnable(false);
        mLocationClient.setLocationOption(mLocationOption);
    }

    private void initMap(Bundle savedInstanceState) {
        mapView = findViewById(R.id.map_view);
        mapView.onCreate(savedInstanceState);
        aMap = mapView.getMap();
        aMap.setMinZoomLevel(16);
        aMap.showIndoorMap(true);
        mUiSettings = aMap.getUiSettings();
        mUiSettings.setZoomControlsEnabled(true);
        mUiSettings.setScaleControlsEnabled(true);
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.drawable.gps_point));
        aMap.setMyLocationStyle(myLocationStyle);
        aMap.setLocationSource(this);
        aMap.setMyLocationEnabled(true);
        aMap.setOnMapClickListener(this);
        geocodeSearch = new GeocodeSearch(this);
        geocodeSearch.setOnGeocodeSearchListener(this);

    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {
                String address = aMapLocation.getAddress();
                etStartAddress.setText(address);
                etStartAddress.setEnabled(false);
                city = aMapLocation.getCity();
                double latitude = aMapLocation.getLatitude();
                double longitude = aMapLocation.getLongitude();
                Log.d(TAG, aMapLocation.getCity());
                Log.d(TAG, address);
                mStartPoint = convertToLatLonPoint(new LatLng(latitude, longitude));
                mLocationClient.stopLocation();
                if (mListener != null) {
                    mListener.onLocationChanged(aMapLocation);
                }

            } else {
                Log.e("AmapError", "location Error, ErrCode:"
                        + aMapLocation.getErrorCode() + ", errInfo:"
                        + aMapLocation.getErrorInfo());
            }
        }
    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mListener = onLocationChangedListener;
        if (mLocationClient == null) {
            mLocationClient.startLocation();
        }
    }

    @Override
    public void deactivate() {
        mListener = null;
        if (mLocationClient != null) {
            mLocationClient.stopLocation();
            mLocationClient.onDestroy();
        }
        mLocationClient = null;
    }


    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mLocationClient != null) {
            mLocationClient.onDestroy();
        }
        mapView.onDestroy();
    }


    @Override
    public void onBusRouteSearched(BusRouteResult busRouteResult, int code) {
    }

    @Override
    public void onDriveRouteSearched(DriveRouteResult driveRouteResult, int code) {
        aMap.clear();
        if (code == AMapException.CODE_AMAP_SUCCESS) {
            if (driveRouteResult != null && driveRouteResult.getPaths() != null) {
                if (driveRouteResult.getPaths().size() > 0) {
                    final DrivePath drivePath = driveRouteResult.getPaths()
                            .get(0);
                    if (drivePath == null) {
                        return;
                    }
                    DrivingRouteOverlay drivingRouteOverlay = new DrivingRouteOverlay(
                            this, aMap, drivePath,
                            driveRouteResult.getStartPos(),
                            driveRouteResult.getTargetPos(), null);
                    drivingRouteOverlay.removeFromMap();
                    drivingRouteOverlay.addToMap();
                    drivingRouteOverlay.zoomToSpan();

                    int dis = (int) drivePath.getDistance();
                    int dur = (int) drivePath.getDuration();
                    String des = MapUtil.getFriendlyTime(dur) + "(" + MapUtil.getFriendlyLength(dis) + ")";

                    tvTime.setText(des);
                    bottomLayout.setVisibility(View.VISIBLE);
                    bottomLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(RouteActivity.this,
                                    RouteDetailActivity.class);
                            intent.putExtra("type", 2);
                            intent.putExtra("path", drivePath);
                            startActivity(intent);
                        }
                    });
                } else if (driveRouteResult.getPaths() == null) {
                    showMsg("没有搜索到相关数据！");
                }
            } else {
                showMsg("没有搜索到相关数据！");
            }
        } else {
            showMsg("错误码；" + code);
        }
    }

    @Override
    public void onWalkRouteSearched(WalkRouteResult walkRouteResult, int code) {
        aMap.clear();
        if (code == AMapException.CODE_AMAP_SUCCESS) {
            if (walkRouteResult != null && walkRouteResult.getPaths() != null) {
                if (walkRouteResult.getPaths().size() > 0) {
                    final WalkPath walkPath = walkRouteResult.getPaths().get(0);
                    if (walkPath == null) {
                        return;
                    }

                    WalkRouteOverlay walkRouteOverlay = new WalkRouteOverlay(
                            this, aMap, walkPath,
                            walkRouteResult.getStartPos(),
                            walkRouteResult.getTargetPos());
                    walkRouteOverlay.removeFromMap();
                    walkRouteOverlay.addToMap();
                    walkRouteOverlay.zoomToSpan();

                    int dis = (int) walkPath.getDistance();
                    int dur = (int) walkPath.getDuration();
                    String des = MapUtil.getFriendlyTime(dur) + "(" + MapUtil.getFriendlyLength(dis) + ")";

                    tvTime.setText(des);
                    bottomLayout.setVisibility(View.VISIBLE);

                    bottomLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(RouteActivity.this,
                                    RouteDetailActivity.class);
                            intent.putExtra("type", 0);
                            intent.putExtra("path", walkPath);
                            startActivity(intent);
                        }
                    });
                } else if (walkRouteResult.getPaths() == null) {
                    showMsg("没有搜索到相关数据！");
                }
            } else {
                showMsg("没有搜索到相关数据！");
            }
        } else {
            showMsg("错误码；" + code);
        }
    }

    @Override
    public void onRideRouteSearched(RideRouteResult rideRouteResult, int code) {
    }



    @Override
    public void onMapClick(LatLng latLng) {
        mEndPoint = convertToLatLonPoint(latLng);
        startRouteSearch();
    }

    private void startRouteSearch() {
        aMap.addMarker(new MarkerOptions()
                .position(convertToLatLng(mStartPoint))
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.start)));
        aMap.addMarker(new MarkerOptions()
                .position(convertToLatLng(mEndPoint))
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.end)));
        final RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(
                mStartPoint, mEndPoint);

        switch (TRAVEL_MODE) {
            case 0:
                RouteSearch.WalkRouteQuery query = new RouteSearch.WalkRouteQuery(fromAndTo, RouteSearch.WalkDefault);
                routeSearch.calculateWalkRouteAsyn(query);
                break;
            case 2:
                break;
            case 1:
                RouteSearch.DriveRouteQuery driveQuery = new RouteSearch.DriveRouteQuery(fromAndTo, RouteSearch.WalkDefault, null, null, "");

                routeSearch.calculateDriveRouteAsyn(driveQuery);
                break;
            case 3:
                break;
            default:
                break;
        }

    }

    private void showMsg(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {

    }


    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int rCode) {
        if (rCode == PARSE_SUCCESS_CODE) {
            List<GeocodeAddress> geocodeAddressList = geocodeResult.getGeocodeAddressList();
            if (geocodeAddressList != null && geocodeAddressList.size() > 0) {
                mEndPoint = geocodeAddressList.get(0).getLatLonPoint();
                startRouteSearch();
            }

        } else {
            showMsg("获取坐标失败");
        }
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP) {

            String endAddress = etEndAddress.getText().toString().trim();
            if (endAddress.isEmpty()) {
                showMsg("请输入要前往的目的地");
            } else {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

                imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);

                GeocodeQuery query = new GeocodeQuery(endAddress, city);
                geocodeSearch.getFromLocationNameAsyn(query);
            }
            return true;
        }
        return false;
    }
}
