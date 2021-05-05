package com.example.myapplication.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.myapplication.R;

public class InfoContainerView extends FrameLayout {

    private View view;

    private TextView title;

    private LinearLayout container;

    public InfoContainerView(Context context) {
        super(context);

        //加载布局文件到此自定义组件
        //注意：第二个参数需填this，表示加载text_layout.xml到此自定义组件中。如果填null，则不加载，即不会显示text_layout.xml中的内容
        view = LayoutInflater.from(context).inflate(R.layout.info_container,this);
        title = view.findViewById(R.id.tilte);
        title.setText("标题");
        container = view.findViewById(R.id.container);
    }

    public InfoContainerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        //获取AttributeSet中所有的XML属性的数量
        int count = attrs.getAttributeCount();
        //遍历AttributeSet中的XML属性
        String title_attr = "";
        for(int i = 0; i < count; i++){
            //获取attr的资源ID
            int attrResId = attrs.getAttributeNameResource(i);
            switch (attrResId){
                case R.attr.title:
                    //customText属性
                    title_attr = attrs.getAttributeValue(i);
                    break;
            }
        }

        //加载布局文件到此自定义组件
        //注意：第二个参数需填this，表示加载text_layout.xml到此自定义组件中。如果填null，则不加载，即不会显示text_layout.xml中的内容
        view = LayoutInflater.from(context).inflate(R.layout.info_container,this);
        title = view.findViewById(R.id.tilte);
        title.setText(title_attr);
        container = view.findViewById(R.id.container);
    }

    public void addElement(View ele) {
        container.addView(ele);
    }

    public void setTitle(String title_attr) {
        title.setText(title_attr);
    }

    public LinearLayout getContainer() {
        return container;
    }
}
