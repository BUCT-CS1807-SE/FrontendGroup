package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity" ;
    private Button btn1,btn2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn1=findViewById(R.id.btn_1);
        btn1.setOnClickListener(this);
        btn2=findViewById(R.id.btn_2);
        btn2.setOnClickListener(this);
    }

    @Override
    public void onClick(View v){
        Intent intent;
        switch (v.getId()){
            case R.id.btn_1:
                intent=new Intent(MainActivity.this, RegisterActivity.class);
                Log.d(TAG, "onClick: Register");
                startActivity(intent);
                break;
            case R.id.btn_2:
                intent=new Intent(MainActivity.this, LoginActivity.class);
                Log.d(TAG, "onClick: Login");
                startActivity(intent);
                break;
        }
    }
}