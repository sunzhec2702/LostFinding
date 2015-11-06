package com.example.darren.lostfinding;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Browser;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.darren.lostfinding.net.MyClient;
import com.example.darren.scanner.CaptureActivity;
import com.google.zxing.client.result.ResultParser;
import com.squareup.okhttp.Request;



import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.SendMessageToWX;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.mm.sdk.openapi.WXAppExtendObject;
import com.tencent.mm.sdk.openapi.WXEmojiObject;
import com.tencent.mm.sdk.openapi.WXImageObject;
import com.tencent.mm.sdk.openapi.WXMediaMessage;
import com.tencent.mm.sdk.openapi.WXMusicObject;
import com.tencent.mm.sdk.openapi.WXTextObject;
import com.tencent.mm.sdk.openapi.WXVideoObject;
import com.tencent.mm.sdk.openapi.WXWebpageObject;
import com.tencent.mm.sdk.openapi.SendAuth;

import net.sourceforge.simcpux.SendToWXActivity;
import net.sourceforge.simcpux.uikit.MMAlert;
import net.sourceforge.simcpux.wxapi.WXEntryActivity;

public class MainActivity extends AppCompatActivity {
    private ImageView ivPerson;
    private LinearLayout linearCamera, linearPublish,linearChat,linearMall;
    private TextView scanResult;
    private TextView positionView;
    final String logTag = "LostFinding";
    Handler posHandler = new Handler();
    private PositionUpdate posUpdate;
    private boolean isPositionStart = false;
    private Gdata app;
    private String memUrl="http://192.168.0.88:8080/WHOS/member.jsp";

    Runnable posRunnable = new Runnable() {
        @Override
        public void run() {
            Location loc = posUpdate.getLoc();
            if (loc != null) {
                Log.d(logTag, "Not null");
                positionView.setText(loc.getLatitude() + " , " + loc.getLongitude());
            } else {
                Log.d(logTag, "NULL");
            }
            posHandler.postDelayed(posRunnable, 1000);
        }
    };
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(logTag, "in the onActivityResult");
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null && requestCode == 0) {
            String result = data.getStringExtra("result");
            if (result != null)
                scanResult.setText(result);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app = (Gdata)getApplication();
        setContentView(R.layout.main);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        scanResult = (TextView) findViewById(R.id.scanResult);
        //positionView = (TextView) findViewById(R.id.positionText);
        posUpdate = new PositionUpdate(this);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isPositionStart) {
                    positionView.setText("Started");
                    isPositionStart = true;
                    posHandler.post(posRunnable);
                }
                else {
                    isPositionStart = false;
                    posHandler.removeCallbacks(posRunnable);
                    positionView.setText("stopped");
                }
            }
        });*/

        linearChat=(LinearLayout) findViewById(R.id.linear_main_chat);
        linearChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent result = new Intent(MainActivity.this, ChatActivity.class);
                startActivity(result);
            }
        });
        /*
        linearChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent result = new Intent(MainActivity.this, WXEntryActivity.class);
                startActivity(result);

            }
        });*/

        //个人中心
        ivPerson = (ImageView) findViewById(R.id.iv_main_self);

        ivPerson.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                // new AlertDialog.Builder(Mai)
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("个人中心")
                        .setItems(
                                new String[] { "定制服务", "已购买二维码", "已扫二维码", "更多" },
                                null).setNegativeButton("确定", null).show();
            }
        });
        //扫码
        linearCamera = (LinearLayout) findViewById(R.id.linear_main_camera);
        linearCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent scanIntent = new Intent(MainActivity.this, CaptureActivity.class);
                startActivityForResult(scanIntent, 0);
            }
        });

        linearPublish = (LinearLayout) findViewById(R.id.linear_main_publish);
        linearPublish.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, PublishActivity.class);
                startActivity(intent);

            }
        });

        linearMall=(LinearLayout) findViewById(R.id.linear_main_mall);
        linearMall.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, MallActivity.class);
                startActivity(intent);

            }
        });
    }

}
