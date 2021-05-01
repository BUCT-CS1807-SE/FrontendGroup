package com.example.myapplication.Cluster;

import com.amap.api.maps.model.LatLng;
import com.example.myapplication.Cluster.ClusterItem;

/**
 * Created by yiyi.qi on 16/10/10.
 */

public class RegionItem implements com.example.myapplication.Cluster.ClusterItem {
    private LatLng mLatLng;
    private String mTitle;
    public RegionItem(LatLng latLng, String title) {
        mLatLng=latLng;
        mTitle=title;
    }

    @Override
    public LatLng getPosition() {
        // TODO Auto-generated method stub
        return mLatLng;
    }
    public String getTitle(){
        return mTitle;
    }

}
