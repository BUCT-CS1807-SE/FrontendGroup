package com.example.myapplication;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;


public class HomeActivity extends AppCompatActivity {

    private static final String TAG = "HomeActivity";
//    private TextView tv_airqlty,tv_tianqi,tv_kongqi,tv_city;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        tv_tianqi =(TextView) findViewById(R.id.tv_tianqi);
//        tv_kongqi =(TextView) findViewById(R.id.tv_kongqi);
//        tv_airqlty =(TextView) findViewById(R.id.tv_airqlty);
//        tv_city =(TextView) findViewById(R.id.tv_city);

    }


    @Override
    protected void onStart() {
        super.onStart();

    }

}