package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.activity.HomePageActivity;

public class Edit_message extends AppCompatActivity {

    private ImageButton btnback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_message);

        initView();

    }

    protected void initView() {
        btnback = findViewById(R.id.ib_back);
        btnback.setOnClickListener((view)->{
            Intent intent;
            intent = new Intent(Edit_message.this, HomePageActivity.class);
            startActivity(intent);
        });
    }
}
