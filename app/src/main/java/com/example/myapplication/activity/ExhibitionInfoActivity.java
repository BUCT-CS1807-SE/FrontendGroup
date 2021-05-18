package com.example.myapplication.activity;


import android.content.Intent;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.example.myapplication.databinding.ActivityExihibitionInfoBinding;
import com.example.myapplication.databinding.ActivityItemInfoBinding;
import com.example.myapplication.entity.Exhibition;
import com.example.myapplication.entity.Item;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;


import com.example.myapplication.R;

import java.util.Objects;

import butterknife.BindView;

/**
 * @author zhy
 * 展览Activity
 */
public class ExhibitionInfoActivity extends AppCompatActivity {
    private ActivityExihibitionInfoBinding binding;
    private Exhibition exhibition;
    private FloatingActionButton explainExhibition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exihibition_info);
        binding = ActivityExihibitionInfoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        exhibition = (Exhibition) getIntent().getBundleExtra("exhibition_data").get("exhibition");

        setSupportActionBar(binding.toolbar);
        binding.exhibitionInfoName.setText(exhibition.getExhibitionName());
        binding.exhibitionIntroContent.setText(exhibition.getExhibitionDescribe());

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);//添加默认的返回图标
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        getSupportActionBar().setTitle(exhibition.getExhibitionName());

        explainExhibition = findViewById(R.id.explainItem);
        explainExhibition.setOnClickListener(v -> {
            //跳转展览讲解
            Toast.makeText(this,"展览讲解页面",Toast.LENGTH_SHORT).show();
            //
            Intent intent=new Intent();
            intent.putExtra("id",exhibition.getId().toString());
            intent.putExtra("ShowName",exhibition.getExhibitionName());
            intent.putExtra("kind","EXHIBITION");
            intent.setClass(this,UserexplainActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //点击back键finish当前activity
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

}
