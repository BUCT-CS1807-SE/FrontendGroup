package com.example.myapplication.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.entity.Comment;
import com.example.myapplication.entity.Museum_explain;
import com.example.myapplication.util.ImageUtils;
import com.example.myapplication.util.NetworkUtils;
import com.example.myapplication.view.InfoContainerView;
import com.youth.banner.listener.OnBannerListener;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.example.myapplication.util.ImageUtils.getURLimage;
import static com.example.myapplication.util.NetworkUtils.HttpRequestGet;
import static com.example.myapplication.util.Pathutil.getFileAbsolutePath;

public class UserexplainActivity extends BaseActivity{
    //音乐播放器

    private static final String TAG = "UserexplainActivity";
    MediaPlayer mp;
    SeekBar sb;
    Handler handler=new Handler();

    private ArrayList<Museum_explain> Mexplain;
    private InfoContainerView eitems;
    private CardView uploadItem;

    private boolean isMuseum=true;
    private Integer id=4;//博物馆或藏品的id

    private static final int IMAGE_CHOOSER = 0x1234;
    private static final int VOICE_CHOOSER = 0x4321;
    private static final int REQUEST_PERMISSION_CODE=0x1235;
    private static final int REQUEST_CODE = 1024;

    private ScrollView scroller;
    private LinearLayout content_linearlayout;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.user_explain);
        requestPermission();
        initView();

    }
    @Override
    protected void onDestroy() {
        if(mp.isPlaying())
            mp.stop();
        super.onDestroy();
    }

    @Override
    protected int initLayout() {
        return R.layout.user_explain;
    }
    public void requestPermission() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // 先判断有没有权限
            if (Environment.isExternalStorageManager()) {
                writeFile();

            } else {
                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                intent.setData(Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, REQUEST_CODE);
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            // 先判断有没有权限
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                writeFile();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);
                //showToast("here");
            }
        } else {
            writeFile();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                writeFile();
            } else {
                showToast("存储权限获取失败");
            }
        }
    }



    /**
     * 模拟文件写入
     */
    private void writeFile() {
        showToast("已获得存储权限");
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void initView()
    {


        //---------滑动组件---------
        scroller = findViewById(R.id.scroller);
        content_linearlayout = findViewById(R.id.content_linearLayout);
        scroller.setOnScrollChangeListener((v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            int paddingTop = content_linearlayout.getPaddingTop();
            int scrollY2 = Math.min(scrollY, paddingTop);
            float transparent = (paddingTop-scrollY2)*1.0f/paddingTop;
        });



        //测试用
//        View cView =LayoutInflater.from(eitems.getContainer().getContext()).inflate(R.layout.userexplain_part,eitems.getContainer(),false);
        // ImageView Image=findViewById(R.id.imageView0);
//        Image.setImageURI(null);
//
//        StringBuffer Imageuri=new StringBuffer("http://8.140.136.108/coverpic/shandongbowuguan.jpg");//需要根据Museum_explain中的Image路径进行修改
//        Bitmap pngBM = getURLimage(Imageuri.toString());
//        Image.setImageBitmap(pngBM);
        //Glide.with(this).load(ImageUtils.genURL("故宫博物院")).centerCrop().placeholder(R.drawable.ic_museum_explain).into(Image);
//        eitems.addElement(cView);
        //eitems.getContainer().addView(cView,0);
        // LinearLayout view = findViewById(R.id.RelativeLayout0);
        //View cView =LayoutInflater.from(view.getContext()).inflate(R.layout.userexplain_part,view,false);
        //view.addView(cView);
        //测试结束 无法显示

        Mexplain=null;
        Handler explainGet=new Handler(Looper.myLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                if(msg.what==1){
                    Mexplain= (ArrayList<Museum_explain>) msg.obj;
                    try {
                        initMexplain();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        if(isMuseum)
            HttpRequestGet(NetworkUtils.ResultType.MUSEUM_EXPLAIN,explainGet,id.toString());
        else
            HttpRequestGet(NetworkUtils.ResultType.OBJECT_EXPLAIN,explainGet,id.toString());


        //上传讲解
        uploadItem=findViewById(R.id.card_up);
        ImageView addImage=findViewById(R.id.imageView2);
        ImageView addVioce=findViewById(R.id.imageView3);
        imageListener upI=new imageListener();
        addImage.setOnClickListener(upI);
        VoiceListener upV=new VoiceListener();
        addVioce.setOnClickListener(upV);


        //TODO here 获得文字以及给提交增加监听
    }
    class imageListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);//意图：文件浏览器
            intent.setType("*/*");// TODO 限制文件类型
            //intent.putExtra(Intent.EXTRA_MIME_TYPES, new String[]{IMG});

            intent.addCategory(Intent.CATEGORY_OPENABLE);
            startActivityForResult(intent, IMAGE_CHOOSER);

        }
    }
    class VoiceListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);//意图：文件浏览器
            intent.setType("*/*");// TODO 限制文件类型
            //intent.putExtra(Intent.EXTRA_MIME_TYPES, new String[]{MP3});//TODO 解决此bug
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            startActivityForResult(intent, VOICE_CHOOSER);

        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (Environment.isExternalStorageManager()) {
                writeFile();
            } else {
                showToast("存储权限获取失败");
            }
        }
        switch (requestCode) {
            case IMAGE_CHOOSER: {

                if (resultCode == RESULT_OK) {

                    Uri uri = data.getData();
                    //对获得的uri做解析
                    String path = getFileAbsolutePath(uploadItem.getContext(), uri);
                    Toast.makeText(this, path, Toast.LENGTH_SHORT).show();
                    File file=new File(path);
                    long len=file.length();
                    byte [] buf=new byte[(int) len];
                    try {
                        InputStream in=new FileInputStream(file);
                        in.read(buf);
                        in.close();//TODO 已获得图片字节流
                    } catch (FileNotFoundException e) {
                        Toast.makeText(this,"FileNotFoundException", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    } catch (IOException e) {
                        Toast.makeText(this,"IOException", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();

                    }


                }
                else
                {
                    Toast.makeText(this,"请选择正确的文件类型", Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case VOICE_CHOOSER: {
                if (resultCode == RESULT_OK) {

                    Uri uri = data.getData();
                    //对获得的uri做解析
                    String path = getFileAbsolutePath(uploadItem.getContext(), uri);

                    Toast.makeText(this, path+"  voice", Toast.LENGTH_SHORT).show();
                    File file=new File(path);

                    MediaPlayer play=MediaPlayer.create(uploadItem.getContext(),uri);play.start();
                    long len=file.length();
                    byte [] buf=new byte[(int) len];
                    try {
                        InputStream in=new FileInputStream(file);
                        in.read(buf);
                        in.close();//TODO 已获得音频字节流
                    } catch (FileNotFoundException e) {
                        Toast.makeText(this,"FileNotFoundException", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    } catch (IOException e) {
                        Toast.makeText(this,"IOException", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();

                    }
                }
                else
                {
                    Toast.makeText(this,"请选择正确的文件类型", Toast.LENGTH_SHORT).show();
                }
                break;
            }
            default:
                break;
        }
    }


    @Override
    protected void initData() {

    }

    public void initMexplain() throws IOException {
        showToast("thesize"+Mexplain.size());
        for (int i = 0; i < Mexplain.size(); i++) {
            if (i == Mexplain.size()-1) {
                addexplain(Mexplain.get(i),true,false);
            } else {
                addexplain(Mexplain.get(i),false,false);
            }
        }

    }

    public void addexplain(Museum_explain explain,boolean isEnd,boolean isLiked) throws IOException {
        showToast(""+explain.getMuseumid());
        LinearLayout container = findViewById(R.id.RelativeLayout0);
        // view.addView(cView);

        View explainView = LayoutInflater.from(container.getContext()).inflate(R.layout.userexplain_part,container,false);
        if(explain.getType()==1&&explain.getState()==1)//民间讲解且通过审核 TODO 按照逻辑改成01
        {
            ImageView Image=explainView.findViewById(R.id.menu_add5);
            Glide.with(explainView).load(ImageUtils.genExplainURL("4")).centerCrop().placeholder(R.drawable.ic_museum_explain).into(Image);

            Button play,pause;
            int Duration;
            play=(Button)explainView.findViewById(R.id.play);
            pause=(Button)explainView.findViewById(R.id.pause);
            sb=(SeekBar)explainView.findViewById(R.id.seekbar_id);
            StringBuffer Voiceuri=new StringBuffer("http://8.140.136.108/prod-api/system/museumexplain/select/voice/205");
            mp =MediaPlayer.create(this, Uri.parse(Voiceuri.toString()));

            play.setOnClickListener(playlis);
            pause.setOnClickListener(pauselis);
            sb.setOnSeekBarChangeListener(sbLis);
            Duration=mp.getDuration();sb.setMax(Duration);


            TextView text=explainView.findViewById(R.id.title_8);
            text.setText(explain.getText());

            container.addView(explainView);


        }
    }
    private View.OnClickListener playlis=new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            handler.post(start);
            //调用handler播放

        }

    };
    Runnable start=new Runnable(){

        @Override
        public void run() {
            // TODO Auto-generated method stub
            if(mp!=null)
            {
                mp.start();
                //mp.seekTo(sb.getProgress());
                handler.post(updatesb);
            }
        }

    };
    Runnable updatesb =new Runnable(){

        @Override
        public void run() {
            // TODO Auto-generated method stub
            sb.setProgress(mp.getCurrentPosition());
            handler.postDelayed(updatesb, 1000);
            //每秒钟更新一次
        }

    };
    private View.OnClickListener pauselis=new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            mp.pause();
            //暂停
        }

    };
    private SeekBar.OnSeekBarChangeListener sbLis=new SeekBar.OnSeekBarChangeListener(){

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress,
                                      boolean fromUser) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            // TODO Auto-generated method stub
            mp.seekTo(sb.getProgress());
            //SeekBar确定位置后，跳到指定位置
        }

    };


}