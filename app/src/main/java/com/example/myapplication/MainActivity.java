package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.myapplication.activity.HomePageActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity" ;
    private Button btnLogin,btnregister;
    private EditText etAccount,etPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        etAccount = findViewById(R.id.et_account);
        etPassword = findViewById(R.id.et_password);
        btnregister=findViewById(R.id.btn_1);
        btnregister.setOnClickListener(this);
        btnLogin=findViewById(R.id.btn_2);
        btnLogin.setOnClickListener(this);
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
                intent=new Intent(MainActivity.this, HomePageActivity.class);
                Log.d(TAG, "onClick: Login");
                startActivity(intent);
                break;
        }
    }
}