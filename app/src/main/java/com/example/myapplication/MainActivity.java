package com.example.myapplication;

import android.content.SharedPreferences;
import android.os.Looper;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.myapplication.activity.HomePageActivity;
import com.example.myapplication.entity.Personalin;
import com.example.myapplication.util.StringUtils;

import java.io.IOException;

public class  MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity" ;
    private Button btnLogin,btnregister;
    private EditText etPhone,etPassword,editText;
    private CheckBox cbRemember,cbAutoLogin,checkBox;
    public static Personalin person = new Personalin();
    public static String token="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

        initData();

    }


    private void initView() {
        etPhone = findViewById(R.id.et_phone);
        etPassword = findViewById(R.id.et_password);
        btnregister=findViewById(R.id.btn_1);
        btnregister.setOnClickListener(this);
        editText =(EditText) findViewById(R.id.et_password);
        checkBox=(CheckBox) findViewById(R.id.cb_3);
        btnLogin=findViewById(R.id.btn_2);
        btnLogin.setOnClickListener(this);
        cbRemember = findViewById(R.id.cb_1);
        cbAutoLogin = findViewById(R.id.cb_2);


        SharedPreferences token_sp = getSharedPreferences("token",MODE_PRIVATE);
        token=token_sp.getString("token","");
        cbAutoLogin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    cbRemember.setChecked(true);
                }
            }
        });

        cbRemember.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!isChecked){
                    cbAutoLogin.setChecked(false);
                }
            }
        });

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // TODO Auto-generated method stub
                if(isChecked){
                    //如果选中，显示密码
                    editText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }else{
                    //否则隐藏密码
                    editText.setTransformationMethod(PasswordTransformationMethod.getInstance());

                }

            }
        });
    }
    //登陆注册
    private void initData() {
        SharedPreferences spf = getSharedPreferences("spfRecorid",MODE_PRIVATE);
        boolean isRemember = spf.getBoolean("isRemember",false);
        boolean isLogin = spf.getBoolean("isLogin",false);
        String token = spf.getString("token","");

        if(isLogin && token != ""){

            Intent intent;
            intent = new Intent(MainActivity.this, HomePageActivity.class);
            startActivity(intent);
        }

        String phone = spf.getString("phone","");
        String password = spf.getString("password","");

        if(isRemember){
            etPhone.setText(phone);
            etPassword.setText(password);
            cbRemember.setChecked(true);
        }
    }

    public void initPerson(JSONObject json,Personalin person) {

        person.setBirth(json.getJSONArray("rows").getJSONObject(0).getString("birth"));

        person.setName(json.getJSONArray("rows").getJSONObject(0).getString("name"));

        person.setPassword(json.getJSONArray("rows").getJSONObject(0).getString("password"));

        person.setPhone(json.getJSONArray("rows").getJSONObject(0).getString("phone"));

        person.setEmail(json.getJSONArray("rows").getJSONObject(0).getString("email"));

        person.setId(Integer.valueOf(json.getJSONArray("rows").getJSONObject(0).getString("id")));

    }



    @Override
    public void onClick(View v){
        String phone = etPhone.getText().toString();
        String password = etPassword.getText().toString();
        switch (v.getId()){
            case R.id.btn_1:
                Intent intent1;
                intent1=new Intent(MainActivity.this, RegisterActivity.class);
                Log.d(TAG, "onClick: Register");
                startActivity(intent1);
                break;
            case R.id.btn_2:
                Log.d(TAG, "onClick: Login");
                if(StringUtils.isEmpty(phone))
                {
                    Toast.makeText(MainActivity.this, "请输入手机号", Toast.LENGTH_LONG).show();
                    return;
                }
                else if(StringUtils.isEmpty(password)){
                    Toast.makeText(MainActivity.this, "请输入密码", Toast.LENGTH_LONG).show();
                    return;
                }
                OkHttpClient client_l=new OkHttpClient.Builder()
                        .build();
                String url_l="http://8.140.136.108/prod-api/system/users/list?password="+password+"&phone="+phone;
                Request request_l=new Request.Builder()
                        .url(url_l)
                        .get()
                        .build();
                Call call_l=client_l.newCall(request_l);
                call_l.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call_l, IOException e) {
                        Log.i("EEEE",e.getMessage());
                    }

                    @Override
                    public void onResponse(Call call_l, Response response) throws IOException {
                        int code = response.code();
                        if(code==200) {
                            String result = response.body().string();
                            System.out.println(result);
                            JSONObject outcome;
                            outcome = JSON.parseObject(result);
                            String total;
                            total = outcome.getString("total");
                            int isOK = Integer.valueOf(total);
                            System.out.println(isOK);
                            System.out.println(person.getPhone());
                            if(isOK == 1) {
                                initPerson(outcome,person);
                                Looper.prepare();
                                Toast.makeText(MainActivity.this, "登录成功！", Toast.LENGTH_LONG).show();
                                OkHttpClient client=new OkHttpClient.Builder()
                                        .build();
                                String url="http://8.140.136.108/prod-api/login";
                                OkHttpClient mOkHttpClient = new OkHttpClient();
                                JSONObject obj = new JSONObject();
                                obj.put("username","admin");
                                obj.put("password","admin123");
                                obj.put("code","1234");
                                RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), obj.toJSONString());
                                Request request = new Request.Builder().url(url).post(body).build();
                                mOkHttpClient.newCall(request).enqueue(new Callback() {
                                    @Override
                                    public void onFailure(Call call, IOException e) {
                                        Log.e("Wrong", e+"");
                                    }

                                    @Override
                                    public void onResponse(Call call, Response response) throws IOException {
                                        String result = response.body().string();
                                        JSONObject outcome;
                                        outcome = JSON.parseObject(result);
                                        token = outcome.getString("token");
                                        SharedPreferences token_sp = getSharedPreferences("token",MODE_PRIVATE);
                                        SharedPreferences.Editor edit = token_sp.edit();
                                        edit.putString("token",token);
                                        edit.apply();
                                    }
                                });
                                if(cbRemember.isChecked()){
                                    SharedPreferences spf = getSharedPreferences("spfRecorid",MODE_PRIVATE);
                                    SharedPreferences.Editor edit = spf.edit();
                                    edit.putString("phone",phone);
                                    edit.putString("password",password);
                                    edit.putBoolean("isRemember",true);
                                    if(cbAutoLogin.isChecked()){
                                        edit.putBoolean("isLogin",true);
                                    }else{
                                        edit.putBoolean("isLogin",false);
                                    }
                                    edit.apply();
                                }else {
                                    SharedPreferences spf = getSharedPreferences("spfRecorid",MODE_PRIVATE);
                                    SharedPreferences.Editor edit = spf.edit();
                                    edit.putBoolean("isRemember",false);
                                    edit.apply();
                                }
                                Intent intent;
                                intent = new Intent(MainActivity.this, HomePageActivity.class);
                                startActivity(intent);
                                MainActivity.this.finish();
                                Looper.loop();
                            }
                            else {
                                Looper.prepare();
                                Toast.makeText(MainActivity.this, "密码或手机号错误，登录失败！", Toast.LENGTH_LONG).show();
                                Looper.loop();
                                return;
                            }
                        }
                        else{
                            System.out.println("查询失败！");
                            return;
                        }
                    }
                });

                break;

        }

    }


}

