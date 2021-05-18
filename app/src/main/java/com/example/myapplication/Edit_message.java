package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.myapplication.activity.HomePageActivity;
import com.example.myapplication.fragment.MyFragment;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Edit_message extends AppCompatActivity implements View.OnClickListener{

    private ImageButton btnback;
    private Button btnsubmit;
    private EditText etName,etPassword,etNewpass,etPassconfirm,etEmail;
    private TextView tv_name,tv_email;
    String nickname = MainActivity.person.getName();
    String email = MainActivity.person.getEmail();
    String password = MainActivity.person.getPassword();
    int id = MainActivity.person.getId();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_message);

        initView();

    }

    protected void initView() {
        etPassword = findViewById(R.id.et_2);
        etNewpass= findViewById(R.id.et_3);
        etPassconfirm = findViewById(R.id.et_4);
        etEmail = findViewById(R.id.et_5);
        etName = findViewById(R.id.et_6);

        tv_name = findViewById(R.id.usernames);
        tv_name.setText(nickname);
        tv_email = findViewById(R.id.emailnames);
        tv_email.setText(email);

        btnback = findViewById(R.id.ib_back);
        btnback.setOnClickListener(this);
        btnsubmit = findViewById(R.id.btn_1);
        btnsubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String etname = etName.getText().toString();
        String etpassword = etPassword.getText().toString();
        String etnewpass = etNewpass.getText().toString();
        String etpassconfirm = etPassconfirm.getText().toString();
        String etemail = etEmail.getText().toString();
        switch (v.getId()){
            case R.id.ib_back:
                Intent intent;
                intent = new Intent(Edit_message.this, HomePageActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_1:
                if(password.equals(etpassword)){
                    if(etnewpass.equals(etpassconfirm))
                    {
                        OkHttpClient client=new OkHttpClient.Builder()
                                .build();
                        String url="http://8.140.136.108/prod-api/system/users";
                        OkHttpClient mOkHttpClient = new OkHttpClient();
                        JSONObject obj = new JSONObject();
                        obj.put("name",etname);
                        obj.put("password",etnewpass);
                        obj.put("phone",MainActivity.person.getPhone());
                        obj.put("email",etemail);
                        obj.put("id",id);
                        //obj.put("pic","");
                        //obj.put("isforbidded",0);
                        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), obj.toJSONString());
                        Request request = new Request.Builder().url(url).put(body).build();
                        mOkHttpClient.newCall(request).enqueue(new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                Log.e("测试1", e+"");
                                Toast.makeText(Edit_message.this,"修改失败，有问题请联系管理员（管理员联系方式：13968774365）",Toast.LENGTH_LONG).show();
                                return;
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                Log.e("测试2", response.body().string());
                                Looper.prepare();
                                int code = response.code();
                                System.out.println("AAAAAAAAAAAAAAA:"+code);
                                Toast.makeText(Edit_message.this,"修改成功！",Toast.LENGTH_LONG).show();
                                Intent intent;
                                intent=new Intent(Edit_message.this, MainActivity.class);
                                startActivity(intent);
                                Looper.loop();
                            }
                        });
                    }
                    else {
                        Toast.makeText(Edit_message.this,"修改失败，有问题请联系管理员（管理员联系方式：13968774365）",Toast.LENGTH_LONG).show();
                        return;
                    }
                }
                else {
                    Toast.makeText(Edit_message.this, "密码错误！", Toast.LENGTH_LONG).show();
                    return;
                }
        }
    }
}
