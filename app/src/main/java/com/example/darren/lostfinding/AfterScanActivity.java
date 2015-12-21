package com.example.darren.lostfinding;

/**
 * @author vivian
 */

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.darren.lostfinding.net.MyClient;
import com.squareup.okhttp.Request;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.SendMessageToWX;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.mm.sdk.openapi.WXMediaMessage;
import com.tencent.mm.sdk.openapi.WXWebpageObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class AfterScanActivity extends Activity {

    private Button btnNotice;
    ImageView giving_message,calling,hpic;
    private String Result;
    private TextView Name1, Contact1, Mark1, Name2, Contact2, Mark2, Take1, Take2, Time1, Time2, MainInfo;
    private String owner, N_owner, cell, QRID, QRname, QRtime;
    private Gdata app;
    private int type;// 0 私人 1 公用
    private int kinds_of_qr;// 1 已购，2 已绑，3 已丢， 4 已归还
    private int kinds_of_people_individually;// 0物主， 1 拾到着(在公用品管理中0是管理者，1是借阅者)
    LinearLayout ll_report,ll_button;
    private boolean secret;
    private AfterScanActivity LA;

    private IWXAPI api;
    public static final String APP_ID = "wx7cc4b5351edaa215";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qr_activity_find);
        app = (Gdata) getApplication();
        LA = this;
        Intent ihere = this.getIntent();
        final Bundle bhere = ihere.getExtras();
        Result = bhere.getString("result");
        try {
            initView();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        initMethod();
    }

    private void initView() throws JSONException {
        // TODO Auto-generated method stub
        btnNotice = (Button) findViewById(R.id.report_button);
        calling = (ImageView) findViewById(R.id.iv_afterscan_calling);
        giving_message = (ImageView) findViewById(R.id.iv_afterscan_message);
        hpic= (ImageView) findViewById(R.id.userpic);
        Name1 = (TextView) findViewById(R.id.name);
        Name2 = (TextView) findViewById(R.id.name2);
        ll_button=(LinearLayout)findViewById(R.id.ll_button);
        ll_report=(LinearLayout)findViewById(R.id.ll_report);
        JSONObject obj = new JSONObject(Result);
        type = Integer.parseInt(obj.getString("pub"));
        kinds_of_qr = Integer.parseInt(obj.getString("state"));
        Mark1=(TextView) findViewById(R.id.mark);
        owner = obj.getString("user");
        System.out.println(app.getName());
        if (app.getName().compareTo(owner) == 0) {
            kinds_of_people_individually = 0;
        } else {
            kinds_of_people_individually = 1;
        }
        cell = obj.getString("cell");
        QRID = obj.getString("id");
        QRname = obj.getString("name");
        N_owner = obj.getString("owner");
        QRtime = obj.getString("time");
        if (QRtime.indexOf("null") == -1) {
            QRtime = obj.getString("time").substring(0, obj.getString("time").indexOf(".0"));
        }
        secret = obj.getString("vis").indexOf('0') > 0 ? true : false;
        if (obj.getString("mark")!=null){
            Mark1.setText(obj.getString("mark"));
        }else {
            Mark1.setText("无备注");
        }
        Name1.setText(QRname );
        Name2.setText(owner);
        if(!Gdata.setPicLocal(owner, hpic)) {
            Gdata.setPicNet(owner, hpic);
        }
    }

    private void initMethod() {
        // TODO Auto-generated method stub


        calling.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                if (secret) {
                    Toast.makeText(getApplicationContext(), "对方隐私设置电话不可见！",
                            Toast.LENGTH_SHORT).show();
                }else{//和物主联系
                    Intent aintent = new Intent();
                    aintent.setAction("android.intent.action.CALL");
                    aintent.setData(Uri.parse("tel:" + cell));
                    startActivity(aintent);
                }
            }
        });

        giving_message.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                HashMap<String, Object> map = new HashMap<String, Object>();
                map.put(owner, "ALOHA");
                app.saveMsg("chat", map);
                Intent result = new Intent(AfterScanActivity.this,
                        ChatingActivity.class);
                result.putExtra("DSTID", owner);
                startActivity(result);
                // finish();//自动发送消息
            }
        });

        OnClickListener serchType = new OnClickListener() {
            HashMap<String, String> map = new HashMap<String, String>();
            @Override
            public void onClick(View arg0) {
                map.put("dfid", QRID);
                map.put("dfname", app.getName());
                map.put("method", "next");
                MyClient.postAsyn(app.manage_add, RC, map);
                new AlertDialog.Builder(AfterScanActivity.this)
                        .setIcon(android.R.drawable.btn_star)
                        .setTitle("提示")
                        .setMessage("是否将相关信息分享到朋友圈")
                        .setPositiveButton("确定",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        api = WXAPIFactory.createWXAPI(LA, APP_ID, false);
                                        api.registerApp(APP_ID);
                                        String rr = "我用“谁的？”发现了" + owner + "丢失的" + QRname + "。";
                                        WXWebpageObject webpage = new WXWebpageObject();
                                        webpage.webpageUrl = "http://www.wannengye.com/view/index/564484e1c9bc4";
                                        WXMediaMessage msg = new WXMediaMessage(webpage);
                                        msg.title = rr;
                                        msg.description = "[谁的？——让生活更简单！]";
                                        //Bitmap thumb = BitmapFactory.decodeResource(getResources(), R.drawable.whose_logo);
                                        //msg.thumbData = Util.bmpToByteArray(thumb, true);
                                        //msg.setThumbImage(thumb);
                                        SendMessageToWX.Req req = new SendMessageToWX.Req();
                                        req.transaction = buildTransaction("webpage");
                                        req.message = msg;
                                        req.scene = SendMessageToWX.Req.WXSceneTimeline;
                                        api.sendReq(req);
                                        finish();

                                        api = WXAPIFactory.createWXAPI(LA, APP_ID, false);
                                        api.registerApp(APP_ID);
                                        final EditText editor = new EditText(AfterScanActivity.this);
                                        editor.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                                        editor.setText(R.string.send_text_default);
                                    }
                                })
                        .setNegativeButton("取消",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        // TODO Auto-generated method stub
                                        dialog.dismiss();
                                    }
                                }).show();// show很关键
                ll_report.setVisibility(View.GONE);
                ll_button.setVisibility(View.VISIBLE);
            }
        };
        btnNotice.setOnClickListener(serchType);
    }

    MyClient.ResultCallback<String> RC = new MyClient.ResultCallback<String>() {
        @Override
        public void onError(Request request, Exception e) {
            e.printStackTrace();
        }

        @Override
        public String onResponse(String u) {
            return u;
        }
    };
    InputFilter[] disEdit = new InputFilter[]{
            new InputFilter() {
                public CharSequence filter(CharSequence source, int start,
                                           int end, Spanned dest, int dstart, int dend) {
                    return source.length() < 1 ? dest.subSequence(dstart, dend) : "";
                }
            }
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            finish();
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }
}
