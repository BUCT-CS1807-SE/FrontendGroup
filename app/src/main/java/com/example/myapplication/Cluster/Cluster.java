package com.example.myapplication.Cluster;

import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yiyi.qi on 16/10/10.
 */

public class Cluster {


    private LatLng mLatLng;
    private List<com.example.myapplication.Cluster.ClusterItem> mClusterItems;
    private Marker mMarker;


    Cluster( LatLng latLng) {

        mLatLng = latLng;
        mClusterItems = new ArrayList<com.example.myapplication.Cluster.ClusterItem>();
    }

    void addClusterItem(com.example.myapplication.Cluster.ClusterItem clusterItem) {
        mClusterItems.add(clusterItem);
    }

    int getClusterCount() {
        return mClusterItems.size();
    }



    LatLng getCenterLatLng() {
        return mLatLng;
    }

    void setMarker(Marker marker) {
        mMarker = marker;
    }

    Marker getMarker() {
        return mMarker;
    }

    List<com.example.myapplication.Cluster.ClusterItem> getClusterItems() {
        return mClusterItems;
    }
}
