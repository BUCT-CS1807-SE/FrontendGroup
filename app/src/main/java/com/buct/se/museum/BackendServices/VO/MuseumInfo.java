package com.buct.se.museum.BackendServices.VO;

import java.math.BigInteger;

public class MuseumInfo {

    private String id;

    private String address;

    private String describe;

    private String name;

    //票价，单位为分，防止浮点数精度问题
    private BigInteger price;



}
