package com.example.darren.lostfinding;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.darren.lostfinding.net.MyClient;
import com.squareup.okhttp.Request;

public class PersonInformationEditActivity extends Activity {
    private Gdata app;
    private EditText[] liEdit;
    private TextView gold;
    private ImageView tvPopOk, back,userpic;
    private PersonInformationEditActivity LA;
    private static final int REQUEST_CODE = 100;
    //private ToggleButton tbSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.person_information_edit);
        app = (Gdata) getApplication();
        LA = this;
        try {
            initView();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE:
                    // 获取选中图片的路径
                    Bitmap img = null;
                    try {
                        img = MediaStore.Images.Media.getBitmap(getContentResolver(),intent.getData());
                        userpic.setImageBitmap(img);
                        ByteArrayOutputStream os = new ByteArrayOutputStream();
                        img.compress(Bitmap.CompressFormat.PNG, 100, os);
                        FileOutputStream outputStream = openFileOutput(app.getName()+".pic", Context.MODE_PRIVATE);
                        outputStream.write(os.toByteArray());
                        outputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    File fl =new File(getFilesDir()+"/"+app.getName()+".pic");
                    try {
                        MyClient.postAsyn(app.upload_add, new MyClient.ResultCallback<String>() {
                            @Override
                            public void onError(Request request, Exception e) {
                                e.printStackTrace();
                            }
                            @Override
                            public String onResponse(String u) {
                                return u;
                            }
                        }, fl, app.getName()+".PNG");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    app.getdb().updatePic(app.getName(),0);
                    break;
            }
        }
    }

    private void initView() throws IOException {
        // TODO Auto-generated method stub
        userpic=(ImageView) findViewById(R.id. iv_personInfoEidt_UserIcon);
        Gdata.setPicLocal(app.getName(),userpic);
        liEdit = new EditText[3];
        liEdit[0] = (EditText) findViewById(R.id.et_personInfoEdit_NickName);
        liEdit[1] = (EditText) findViewById(R.id.et_personInfoEdit_phoneNum);
        liEdit[2] = (EditText) findViewById(R.id.et_personInfoEdit_add);
        gold = (TextView) findViewById(R.id.et_personInfoEdit_money);
        Intent ihere = this.getIntent();
        final Bundle bhere = ihere.getExtras();
        liEdit[0].setText(bhere.getString("l1"));
        liEdit[1].setText(bhere.getString("l2"));
        gold.setText(bhere.getString("l3"));
        liEdit[2].setText(bhere.getString("l4"));

        back = (ImageView) findViewById(R.id.back);
        Button znm = (Button) findViewById(R.id.bt_pic);
        znm.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent innerIntent = new Intent(Intent.ACTION_GET_CONTENT); // "android.intent.action.GET_CONTENT"
                innerIntent.setType("image/*");
                Intent wrapperIntent = Intent.createChooser(innerIntent,
                        "选择二维码图片");
                LA.startActivityForResult(wrapperIntent, REQUEST_CODE);
            }
        });
        back.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {
                finish();
            }
        });
        tvPopOk = (ImageView) findViewById(R.id.permit);
        tvPopOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Map<String, String> obj = new HashMap<String, String>();
                obj.put("username", app.getName());
                obj.put("nickname", liEdit[0].getText().toString());
                obj.put("cell", liEdit[1].getText().toString());
                obj.put("address", liEdit[2].getText().toString());
                MyClient.postAsyn(app.pinfo_add2, new MyClient.ResultCallback<String>() {
                    @Override
                    public void onError(Request request, Exception e) {
                        e.printStackTrace();
                    }

                    @Override
                    public String onResponse(String u) {
                        if (u.indexOf("成功") > -1) {
                            Toast.makeText(getApplicationContext(), "修改成功",
                                    Toast.LENGTH_SHORT).show();
                            LA.finish();
                        } else {
                            Toast.makeText(getApplicationContext(), "提交失败，请检查网络",
                                    Toast.LENGTH_SHORT).show();
                        }
                        return u;
                    }
                }, obj);
            }
        });
    }
}