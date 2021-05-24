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
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.ArrayMap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsoluteLayout;
import android.widget.Button;
import android.widget.EditText;
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
import androidx.constraintlayout.widget.ConstraintLayout;
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

import okhttp3.MediaType;

import static com.example.myapplication.MainActivity.person;
import static com.example.myapplication.util.ImageUtils.getURLimage;
import static com.example.myapplication.util.NetworkUtils.AAC;
import static com.example.myapplication.util.NetworkUtils.HttpRequestGet;
import static com.example.myapplication.util.NetworkUtils.HttpRequestPost;
import static com.example.myapplication.util.NetworkUtils.TYPE_IMAGE_PNG;
import static com.example.myapplication.util.NetworkUtils.WAV;
import static com.example.myapplication.util.Pathutil.getFileAbsolutePath;

public class UserexplainActivity extends BaseActivity{

    private static final String TAG = "UserexplainActivity";
    private byte[] imagestream;
    private byte[] voicestream;
    private MediaType imageType=TYPE_IMAGE_JPG;
    private MediaType voiceType=MP3;

    MediaPlayer[] Amp=new MediaPlayer[1024];
    SeekBar [] Asb=new SeekBar[1024];
    Handler[] Ahandler=new Handler[1024];
    Start[] Astart=new Start[1024];
    Updatasb[] Aupdatasb=new Updatasb[1024];

    private ArrayList<Museum_explain> Mexplain;
    private ArrayList<Museum_explain> PostidTemp;
    private Museum_explain Oexplain;
    private CardView uploadItem;

    private String kind="MUSEUM";// EXHIBITION  COLLECTION MUSEUM
    private int id=2;//博物馆或藏品的id
    private String ShowName;
    private Integer createid=1;// 用户id

    private static final int IMAGE_CHOOSER = 0x1234;
    private static final int VOICE_CHOOSER = 0x4321;
    private static final int REQUEST_PERMISSION_CODE=0x1235;
    private static final int REQUEST_CODE = 1024;

    private ScrollView scroller;
    private LinearLayout content_linearlayout;
    private LinearLayout offical;

    private Integer postid=1;
    private Museum_explain Mpostid=null;
    private LinearLayout container=null;

    public static final MediaType TYPE_IMAGE_JPG = MediaType.parse("image/jpg");
    public static final MediaType MP3 = MediaType.parse("audio/mpeg");
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //setContentView(R.layout.user_explain);
        Intent intent=getIntent();
        kind=intent.getStringExtra("kind");
        id=Integer.parseInt(intent.getStringExtra("id"));
        ShowName=intent.getStringExtra("ShowName");
        createid=person.getId();
        requestPermission();
        initView();

    }
    @Override
    protected void onDestroy() {
        for(int i=0;i<1024;i++)
        {
            if(Amp[i]!=null&&Amp[i].isPlaying())
                Amp[i].stop();
        }
        if(container!=null)
            container.removeAllViews();
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
        //-----------------官方讲解------------------------
        initOffical();//官方讲解只有一条

        //-----------------用户讲解--------------
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
        if(kind.equals("MUSEUM"))
            HttpRequestGet(NetworkUtils.ResultType.MUSEUM_EXPLAIN,explainGet,id);
        else if(kind.equals("EXHIBITION"))
            HttpRequestGet(NetworkUtils.ResultType.EXHI_EXPLAIN,explainGet,id);
        else if(kind.equals("COLLECTION"))
            HttpRequestGet(NetworkUtils.ResultType.OBJECT_EXPLAIN,explainGet,id);


        //上传讲解
        uploadItem=findViewById(R.id.card_up);
        ImageView addImage=findViewById(R.id.imageView2);
        ImageView addVioce=findViewById(R.id.imageView3);
        imageListener upI=new imageListener();
        addImage.setOnClickListener(upI);
        VoiceListener upV=new VoiceListener();
        addVioce.setOnClickListener(upV);
        EditText edit=findViewById(R.id.edit_2_4);
        Button submitbutton=findViewById(R.id.button);
        submitbutton.setOnClickListener(v ->
        {
            String content = edit.getText().toString(); //评论字符串
            if (content.isEmpty()) {
                showToast("请输入评论");
                return;
            }
            Handler explainPost=new Handler(Looper.myLooper()){
                @Override
                public void handleMessage(@NonNull Message msg) {
                    super.handleMessage(msg);
                    if(msg.what==1){
                        showToastSync("上传讲解成功");
                    }
                    else {
                        showToastSync("提交讲解失败");
                    }
                }
            };
            Museum_explain museum_explain=new Museum_explain(0,createid,0,id,content,0,0);
            HttpRequestPost(explainPost,museum_explain,kind);

            //---------------init postid-----------
            initPostid();

        });

    }
    public void initPostid()
    {

        Handler helpGet=new Handler(Looper.myLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                if(msg.what==1){
                    PostidTemp=(ArrayList<Museum_explain>) msg.obj;
                    if(PostidTemp.size()>0)
                    {
                        Mpostid= PostidTemp.get(0);
                        postid=Mpostid.getId();

                        Handler imagePost=new Handler(Looper.myLooper()){
                            @Override
                            public void handleMessage(@NonNull Message msg) {
                                super.handleMessage(msg);
                                if(msg.what==1){
                                    showToastSync("上传图片成功");
                                }
                                else {
                                    showToastSync("提交图片失败");
                                }
                            }
                        };
                        if(imagestream==null)
                            System.out.println("the imagefile is null");
                        else
                        {
                            System.out.println(imagestream.length);
                            if(kind.equals("MUSEUM"))
                                HttpRequestPost(NetworkUtils.ResultType.MUSEUM_PIC_POST,postid.toString(),imagePost,"image",null,imagestream,imageType);
                            else if(kind.equals("EXHIBITION"))
                                HttpRequestPost(NetworkUtils.ResultType.EXHI_PIC_POST,postid.toString(),imagePost,"image",null,imagestream,imageType);
                            else if(kind.equals("COLLECTION"))
                                HttpRequestPost(NetworkUtils.ResultType.OBJECT_PIC_POST,postid.toString(),imagePost,"image",null,imagestream,imageType);
                            imagestream=null;

                        }



                        Handler voicePost=new Handler(Looper.myLooper()){
                            @Override
                            public void handleMessage(@NonNull Message msg) {
                                super.handleMessage(msg);
                                if(msg.what==1){
                                    showToastSync("上传音频成功");
                                }
                                else {
                                    showToastSync("提交音频失败");
                                }
                            }
                        };
                        if(voicestream==null)
                            System.out.println("the voicefile is null");
                        else
                        {
                            System.out.println(voicestream.length);
                            if(kind.equals("MUSEUM"))
                                HttpRequestPost(NetworkUtils.ResultType.MUSEUM_VOI_POST,postid.toString(),voicePost,"voice",null,voicestream,voiceType);
                            else if(kind.equals("EXHIBITION"))
                                HttpRequestPost(NetworkUtils.ResultType.EXHI_VOI_POST,postid.toString(),voicePost,"voice",null,voicestream,voiceType);
                            else if(kind.equals("COLLECTION"))
                                HttpRequestPost(NetworkUtils.ResultType.OBJECT_VOI_POST,postid.toString(),voicePost,"voice",null,voicestream,voiceType);
                            voicestream=null;
                        }
                    }

                    // showToast("postid:" +postid);
                }
            }
        };
        if(kind.equals("MUSEUM"))
            HttpRequestGet(NetworkUtils.ResultType.MUSEUM_HELP,helpGet,createid.toString());
        else if(kind.equals("EXHIBITION"))
            HttpRequestGet(NetworkUtils.ResultType.EXHI_HELP,helpGet,createid.toString());
        else if(kind.equals("COLLECTION"))
            HttpRequestGet(NetworkUtils.ResultType.OBJECT_HELP,helpGet,createid.toString());
    }
    public void initOffical()
    {
        Oexplain=null;
        Handler OexplainGet=new Handler(Looper.myLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                if(msg.what==1){
                    Oexplain = ((ArrayList<Museum_explain>) msg.obj).get(0);
                    if(Oexplain.getType()==1&&Oexplain.getState()==1)
                    {
                        int index=0;
                        offical=findViewById(R.id.offical);
                        View officaView = LayoutInflater.from(offical.getContext()).inflate(R.layout.userexplain_part,offical,true);

                        TextView officaltext=officaView.findViewById(R.id.title_1);
                        officaltext.setText("官方讲解");
                        ImageView Image=officaView.findViewById(R.id.menu_add5);

                        showToast(Oexplain.getId()+"讲解id");
                        if(kind.equals("MUSEUM"))
                            Glide.with(officaView).load(ImageUtils.genExplainURL(Oexplain.getId().toString())).centerCrop().placeholder(R.drawable.ic_museum_explain).into(Image);
                        else if(kind.equals("EXHIBITION"))
                            Glide.with(officaView).load(ImageUtils.genEXHIBITIONURL(Oexplain.getId().toString())).centerCrop().placeholder(R.drawable.ic_museum_explain).into(Image);
                        else if(kind.equals("COLLECTION"))
                            Glide.with(officaView).load(ImageUtils.genCOLLECTIONURL(Oexplain.getId().toString())).centerCrop().placeholder(R.drawable.ic_museum_explain).into(Image);

                        StringBuffer Voiceuri;
                        if(kind.equals("MUSEUM"))
                            Voiceuri=new StringBuffer("http://8.140.136.108/prod-api/system/museumexplain/select/voice/");
                        else if(kind.equals("EXHIBITION"))
                            Voiceuri=new StringBuffer("http://8.140.136.108/prod-api/system/exhibitexplain/select/voice/");
                        else if(kind.equals("COLLECTION"))
                            Voiceuri=new StringBuffer("http://8.140.136.108/prod-api/system/collectionexplain/select/voice/");
                        else
                            Voiceuri=new StringBuffer("http://8.140.136.108/prod-api/system/museumexplain/select/voice/");

                        Voiceuri.append(Oexplain.getId());
                        if((Amp[index] =MediaPlayer.create(officaView.getContext(), Uri.parse(Voiceuri.toString())))!=null)
                        {
                            int Duration;
                            Button play,pause;
                            play=(Button)officaView.findViewById(R.id.play);
                            pause=(Button)officaView.findViewById(R.id.pause);
                            Asb[index]=(SeekBar)officaView.findViewById(R.id.seekbar_id);


                            Ahandler[index]=new Handler();

                            Astart[index]=new Start(index);
                            Aupdatasb[index]=new Updatasb(index);
                            View.OnClickListener playlis=new View.OnClickListener(){

                                @Override
                                public void onClick(View v) {
                                    Ahandler[index].post(Astart[index]);
                                    //调用handler播放

                                }

                            };
                            View.OnClickListener pauselis=new View.OnClickListener(){

                                @Override
                                public void onClick(View v) {
                                    Amp[index].pause();
                                    //暂停
                                }

                            };
                            SeekBar.OnSeekBarChangeListener sbLis=new SeekBar.OnSeekBarChangeListener(){

                                @Override
                                public void onProgressChanged(SeekBar seekBar, int progress,
                                                              boolean fromUser) {

                                }

                                @Override
                                public void onStartTrackingTouch(SeekBar seekBar) {


                                }

                                @Override
                                public void onStopTrackingTouch(SeekBar seekBar) {
                                    Amp[index].seekTo(Asb[index].getProgress());

                                }

                            };
                            play.setOnClickListener(playlis);
                            pause.setOnClickListener(pauselis);
                            Asb[index].setOnSeekBarChangeListener(sbLis);
                            Duration=Amp[index].getDuration();Asb[index].setMax(Duration);
                        }
                        else
                        {
                            //showToast("not a audio");
                            Button play,pause;
                            play=(Button)officaView.findViewById(R.id.play);
                            pause=(Button)officaView.findViewById(R.id.pause);
                            Asb[index]=(SeekBar)officaView.findViewById(R.id.seekbar_id);
                            TextView text=officaView.findViewById(R.id.title_6);
                            text.setText("暂无音频");
                            play.setVisibility(View.INVISIBLE);
                            pause.setVisibility(View.INVISIBLE);
                            Asb[index].setVisibility(View.INVISIBLE);
                        }



                        TextView text=officaView.findViewById(R.id.title_8);
                        text.setText(Oexplain.getText());
                        //offical.addView(officaView);


                    }


                }
            }
        };
        if(kind.equals("MUSEUM"))
            HttpRequestGet(NetworkUtils.ResultType.MUSEUM_EXPLAIN,OexplainGet,id);
        else if(kind.equals("EXHIBITION"))
            HttpRequestGet(NetworkUtils.ResultType.EXHI_EXPLAIN,OexplainGet,id);
        else if(kind.equals("COLLECTION"))
            HttpRequestGet(NetworkUtils.ResultType.OBJECT_EXPLAIN,OexplainGet,id);


    }
    class imageListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);//意图：文件浏览器
            intent.setType("*/*");// TODO 限制文件类型
            // intent.putExtra(Intent.EXTRA_MIME_TYPES, new String[]{TYPE_IMAGE_JPG});
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            startActivityForResult(intent, IMAGE_CHOOSER);

        }
    }
    class VoiceListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);//意图：文件浏览器
            intent.setType("*/*");// TODO 限制文件类型
            //intent.putExtra(Intent.EXTRA_MIME_TYPES, new String[]{MP3});
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
                    String path = getFileAbsolutePath(uploadItem.getContext(), uri);
                    Toast.makeText(this, path, Toast.LENGTH_SHORT).show();
                    File file=new File(path);
                    long len=file.length();
                    imagestream=new byte[(int) len];
                    try {
                        InputStream in=new FileInputStream(file);
                        in.read(imagestream);
                        in.close();
                        if(file.getPath().endsWith(".png")) imageType=TYPE_IMAGE_PNG;
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

                    //MediaPlayer play=MediaPlayer.create(uploadItem.getContext(),uri);play.start();
                    long len=file.length();
                    voicestream=new byte[(int) len];
                    try {
                        InputStream in=new FileInputStream(file);
                        in.read(voicestream);
                        in.close();//TODO 已获得音频字节流
                        if(file.getPath().endsWith(".aac")) voiceType=AAC;
                        else if(file.getPath().endsWith(".wav")) voiceType=WAV;
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
        // showToast("thesize"+Mexplain.size()+" the id"+Mexplain.get(0).getCollectionid());
        for (int i = 0; i < Mexplain.size(); i++) {
            if (i == Mexplain.size()-1) {
                addexplain(Mexplain.get(i),i+1,false);
            } else {
                addexplain(Mexplain.get(i),i+1,false);
            }
        }

    }

    public void addexplain(Museum_explain explain,int index,boolean isLiked) throws IOException {
        //showToast("图片插入的讲解id为"+explain.getId());
        //ConstraintLayout container = findViewById(R.id.RelativeLayout0);

        if(explain.getType()==0&&explain.getState()==1)//民间讲解且通过审核
        {
            container = findViewById(R.id.RelativeLayout0);
            View explainView = LayoutInflater.from(container.getContext()).inflate(R.layout.userexplain_part,container,true);
            ImageView Image=explainView.findViewById(R.id.menu_add5);

            showToast(explain.getId()+"");
            if(kind.equals("MUSEUM"))
                Glide.with(explainView).load(ImageUtils.genExplainURL(explain.getId().toString())).centerCrop().placeholder(R.drawable.ic_museum_explain).into(Image);
            else if(kind.equals("EXHIBITION"))
                Glide.with(explainView).load(ImageUtils.genEXHIBITIONURL(explain.getId().toString())).centerCrop().placeholder(R.drawable.ic_museum_explain).into(Image);
            else if(kind.equals("COLLECTION"))
                Glide.with(explainView).load(ImageUtils.genCOLLECTIONURL(explain.getId().toString())).centerCrop().placeholder(R.drawable.ic_museum_explain).into(Image);

            StringBuffer Voiceuri;
            if(kind.equals("MUSEUM"))
                Voiceuri=new StringBuffer("http://8.140.136.108/prod-api/system/museumexplain/select/voice/");
            else if(kind.equals("EXHIBITION"))
                Voiceuri=new StringBuffer("http://8.140.136.108/prod-api/system/exhibitexplain/select/voice/");
            else if(kind.equals("COLLECTION"))
                Voiceuri=new StringBuffer("http://8.140.136.108/prod-api/system/collectionexplain/select/voice/");
            else
                Voiceuri=new StringBuffer("http://8.140.136.108/prod-api/system/museumexplain/select/voice/");

            Voiceuri.append(explain.getId());
            if((Amp[index] =MediaPlayer.create(this, Uri.parse(Voiceuri.toString())))!=null)
            {
                int Duration;
                Button play,pause;
                play=(Button)explainView.findViewById(R.id.play);
                pause=(Button)explainView.findViewById(R.id.pause);
                Asb[index]=(SeekBar)explainView.findViewById(R.id.seekbar_id);


                Ahandler[index]=new Handler();

                Astart[index]=new Start(index);
                Aupdatasb[index]=new Updatasb(index);
                View.OnClickListener playlis=new View.OnClickListener(){

                    @Override
                    public void onClick(View v) {
                        Ahandler[index].post(Astart[index]);
                        //调用handler播放

                    }

                };
                View.OnClickListener pauselis=new View.OnClickListener(){

                    @Override
                    public void onClick(View v) {
                        Amp[index].pause();
                        //暂停
                    }

                };
                SeekBar.OnSeekBarChangeListener sbLis=new SeekBar.OnSeekBarChangeListener(){

                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress,
                                                  boolean fromUser) {
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        Amp[index].seekTo(Asb[index].getProgress());
                        //SeekBar确定位置后，跳到指定位置
                    }

                };
                play.setOnClickListener(playlis);
                pause.setOnClickListener(pauselis);
                Asb[index].setOnSeekBarChangeListener(sbLis);
                Duration=Amp[index].getDuration();Asb[index].setMax(Duration);
            }
            else
            {
                //showToast("not a audio");
                Button play,pause;
                play=(Button)explainView.findViewById(R.id.play);
                pause=(Button)explainView.findViewById(R.id.pause);
                Asb[index]=(SeekBar)explainView.findViewById(R.id.seekbar_id);
                TextView text=explainView.findViewById(R.id.title_6);
                text.setText("暂无音频");
                play.setVisibility(View.INVISIBLE);
                pause.setVisibility(View.INVISIBLE);
                Asb[index].setVisibility(View.INVISIBLE);
            }



            TextView text=explainView.findViewById(R.id.title_8);
            text.setText(explain.getText());
            //container.addView(explainView);


        }
    }

    class Start implements Runnable
    {
        private int in;
        public Start(int in)
        {
            this.in=in;
        }
        public void run()
        {
            if(Amp[in]!=null)
            {
                Amp[in].start();
                Ahandler[in].post(Aupdatasb[in]);
            }
        }
    }
    class Updatasb implements Runnable
    {
        private int in;
        public Updatasb(int in)
        {
            this.in=in;
        }
        public void run()
        {
            Asb[in].setProgress(Amp[in].getCurrentPosition());
            Ahandler[in].postDelayed(Aupdatasb[in], 1000);
        }
    }



}