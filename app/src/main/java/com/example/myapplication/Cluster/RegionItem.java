package com.example.myapplication.Cluster;

import com.amap.api.maps.model.LatLng;
import com.example.myapplication.Cluster.ClusterItem;

/**
 * Created by yiyi.qi on 16/10/10.
 */

public class RegionItem implements com.example.myapplication.Cluster.ClusterItem {
    private LatLng mLatLng;
    private String mTitle;
    private String museumlevel;
    private int curpos;
    public RegionItem(LatLng latLng, String title,String level,int t) {
        mLatLng=latLng;
        mTitle=title;
        museumlevel=level;
        curpos=t;
    }

    @Override
    public LatLng getPosition() {
        return mLatLng;
    }
    public String getTitle(){
        return mTitle;
    }

    @Override
    public String getLevel() {
        return museumlevel;
    }

    @Override
    public int getpos() {
        return curpos;
    }

}
