package com.example.cyc;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.darren.lostfinding.Gdata;
import com.example.darren.lostfinding.R;
import com.example.darren.lostfinding.net.MyClient;
import com.squareup.okhttp.Request;

import java.util.HashMap;
import java.util.Map;

public class EditQrInfoActivity extends AppCompatActivity {

    private TextView Name1, Mark1;
    private EditText Name2, Mark2;
    private Button BTok,OKcancle;
    private Gdata app;
    String QRid;
    private String PRI_updateUrl="http://192.168.0.88:8080/WHOS/manage.do";
    private String PUB_updateUrl="http://www.shuide.cc:8112/WHOS/manage.do";
    private String updateUrl= Globle.DEBUG?PRI_updateUrl:PUB_updateUrl;
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
        Name2.setText(bhere.getString("name"));
        Mark2.setText(bhere.getString("mark"));
    }
    private void initView() {
        Name1 = (TextView) findViewById(R.id.name1);
        Name2 = (EditText) findViewById(R.id.name2);
        Mark2 = (EditText) findViewById(R.id.mark2);
        Mark1 = (TextView) findViewById(R.id.mark1);
        BTok= (Button) findViewById(R.id.btn_submit);
        OKcancle= (Button) findViewById(R.id.btn_back);
    }
    private void initControl() {
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
                obj.put("dfname", Name2.getText().toString());
                obj.put("dfmark", Mark2.getText().toString());
                obj.put("method", "edit");
                app.getClient().postAsyn(updateUrl, new MyClient.ResultCallback<String>() {
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
    }
}
