package com.example.darren.lostfinding;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cyc.Globle;
import com.example.darren.lostfinding.Gdata;
import com.example.darren.lostfinding.R;
import com.example.darren.lostfinding.net.MyClient;
import com.squareup.okhttp.Request;

import java.util.HashMap;
import java.util.Map;

public class EditQrInfoActivity extends AppCompatActivity {

    private EditText Name1, Mark1;
    private Switch VIS;
    private Button BTok;
    private Gdata app;
    ImageView OKcancle,camera;
    String QRid;
    boolean secret;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_qr_info);
        app=(Gdata)getApplication();
        initView();
        initControl();
        Intent ihere=this.getIntent();
        final Bundle bhere=ihere.getExtras();
        QRid=bhere.getString("id");
        Name1.setText(bhere.getString("name"));
        Mark1.setText(bhere.getString("mark"));
        if(bhere.getString("vis").indexOf("yes")>-1){
            VIS.setChecked(false);
        }else{
            VIS.setChecked(true);
        }

    }
    private void initView() {
        Name1 = (EditText) findViewById(R.id.name);
        Mark1 = (EditText) findViewById(R.id.mark);
        BTok= (Button) findViewById(R.id.btn_submit);
        OKcancle= (ImageView) findViewById(R.id.back);
        VIS=(Switch)findViewById(R.id.sw_cell);
        camera= (ImageView) findViewById(R.id.camera);
    }
    private void initControl() {
        VIS.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                // TODO Auto-generated method stub
                if (isChecked) {
                    secret=false;
                } else {
                    secret=true;
                }
            }
        });


        OKcancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                finish();
                // finish();//自动发送消息
            }
        });
        BTok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                Map<String, String> obj = new HashMap<String, String>();
                obj.put("dfid", QRid);
                obj.put("dfname", Name1.getText().toString());
                obj.put("dfmark", Mark1.getText().toString());
                obj.put("method", "edit");
                if(secret){
                    obj.put("visible", "0");
                }else{
                    obj.put("visible", "1");
                }
                MyClient.postAsyn(app.manage_add, new MyClient.ResultCallback<String>() {
                    @Override
                    public void onError(Request request, Exception e) {
                        e.printStackTrace();
                    }

                    @Override
                    public String onResponse(String u) {
                        return u;
                    }
                }, obj);
                finish();
                // finish();//自动发送消息
            }
        });
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                Toast.makeText(getApplicationContext(), "功能开发中...敬请期待",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}
