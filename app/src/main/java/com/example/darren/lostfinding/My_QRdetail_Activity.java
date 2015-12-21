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

public class My_QRdetail_Activity extends Activity {

    private Button btnNotice;
    private ImageView calling;
    ImageView giving_message,Ivstate,Ivreturn,Ivback;
    private String Result;
    TextView Name1, Contact1, Mark1, Name2, Contact2, Mark2, Take1, Take2, Time1, Time2, MainInfo,Tvstate;
    private String owner, N_owner, cell, QRID, QRname, QRtime;
    private Gdata app;
    private int type;// 0 私人 1 公用
    private int kinds_of_qr;// 1 已购，2 已绑，3 已丢， 4 已归还
    private int kinds_of_people_individually;// 0物主， 1 拾到着(在公用品管理中0是管理者，1是借阅者)
    private boolean secret;
    private My_QRdetail_Activity LA;
    LinearLayout llgood,llreport,llbutton;
    private IWXAPI api;
    public static final String APP_ID = "wx7cc4b5351edaa215";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qr_activity_owner);
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
        Ivback=(ImageView)findViewById(R.id.capture_button_cancel);
        btnNotice = (Button) findViewById(R.id.report_button);
        calling = (ImageView) findViewById(R.id.iv_afterscan_calling);
        giving_message = (ImageView) findViewById(R.id.iv_afterscan_message);
        MainInfo = (TextView) findViewById(R.id.tv_main);
        Mark2 = (TextView) findViewById(R.id.mark2);
        Take2 = (TextView) findViewById(R.id.take2);
        Time2 = (TextView) findViewById(R.id.time2);
        Tvstate= (TextView) findViewById(R.id.tv_state);
        Ivreturn= (ImageView) findViewById(R.id.iv_return);
        llreport=(LinearLayout)findViewById(R.id.ll_report);
        llgood=(LinearLayout)findViewById(R.id.ll_goodman);
        llbutton=(LinearLayout)findViewById(R.id.ll_button);
        Ivstate=(ImageView) findViewById(R.id.iv_state);
        JSONObject obj = new JSONObject(Result);
        type = Integer.parseInt(obj.getString("pub"));
        kinds_of_qr = Integer.parseInt(obj.getString("state"));
        owner = obj.getString("user");
        System.out.println(app.getName());
        System.out.println(owner);
        if (app.getName().compareTo(owner) == 0) {
            kinds_of_people_individually = 0;
        } else {
            kinds_of_people_individually = 1;
        }
        //kinds_of_people_individually=app.getName().compareTo(owner)==0?0:1;
        cell = obj.getString("cell");
        QRID = obj.getString("id");
        QRname = obj.getString("name");
        N_owner = obj.getString("owner");
        QRtime = obj.getString("time");
        if (QRtime.indexOf("null") == -1) {
            QRtime = obj.getString("time").substring(0, obj.getString("time").indexOf(".0"));
        }
        MainInfo.setText(QRname);
        Mark2.setText(obj.getString("mark"));
        if (kinds_of_qr==1){
            Ivstate.setImageResource(R.drawable.yellow_circle);
            Tvstate.setText("未绑定物品");
        }else {
            Ivstate.setImageResource(R.drawable.green_circle);
            Tvstate.setText("正常使用");
        }
        secret = obj.getString("vis").indexOf('0') > 0 ? true : false;
        if (kinds_of_qr==3) {// 情况1，【私人-失主】情景
            llbutton.setVisibility(View.GONE);
            llgood.setVisibility(View.VISIBLE);
            llreport.setVisibility(View.VISIBLE);
            Take2.setText(N_owner);
            Time2.setText(QRtime);
            Ivstate.setImageResource(R.drawable.pink_circle);
            Tvstate.setText("物品丢失");
        }

    }

    private void initMethod() {
        // TODO Auto-generated method stub
        calling.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                MyClient.getAsyn(app.main_add + N_owner, new MyClient.ResultCallback<String>() {
                    @Override
                    public void onError(Request request, Exception e) {
                        e.printStackTrace();
                    }

                    @Override
                    public String onResponse(String u) {
                        if (u.indexOf("\n") > -1) {
                            u = u.substring(0, u.indexOf("\n"));
                        }
                        Intent aintent = new Intent();
                        aintent.setAction("android.intent.action.CALL");
                        aintent.setData(Uri.parse("tel:" + u));
                        startActivity(aintent);
                        return u;
                    }
                });
            }
        });

        giving_message.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                HashMap<String, Object> map = new HashMap<String, Object>();
                map.put(N_owner, "ALOHA");
                app.saveMsg("chat", map);
                Intent result = new Intent(My_QRdetail_Activity.this,
                        ChatingActivity.class);
                result.putExtra("DSTID", N_owner);
                startActivity(result);
                // finish();//自动发送消息
            }
        });
        btnNotice.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                Intent result = new Intent(My_QRdetail_Activity.this, EditQrInfoActivity.class);
                result.putExtra("id", QRID);
                result.putExtra("name", QRname);
                result.putExtra("mark", Mark2.getText().toString());
                result.putExtra("vis", secret?"NO":"YES");
                startActivity(result);
            }
        });
        Ivback.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                finish();
            }
        });
        Ivreturn.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                new AlertDialog.Builder(My_QRdetail_Activity.this)
                        .setIcon(android.R.drawable.btn_star)
                        .setTitle("提示")
                        .setMessage("确定已经找回了物品？")
                        .setPositiveButton("朕从不反悔！",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        HashMap<String, String> map = new HashMap<String, String>();
                                        map.put("dfid", QRID);
                                        map.put("method", "return");
                                        MyClient.postAsyn(app.manage_add, RC, map);
                                    }
                                })
                        .setNegativeButton("朕手滑了",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        // TODO Auto-generated method stub
                                        dialog.dismiss();
                                        LA.finish();
                                    }
                                }).show();// show很关键
            }
        });
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
