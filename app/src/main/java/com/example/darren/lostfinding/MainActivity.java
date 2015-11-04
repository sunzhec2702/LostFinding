package com.example.darren.lostfinding;

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
    private String decodeResult;
    private Button scanButton,memberButoon,marketButoon,ChatButoon,msgButton;
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
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        scanButton = (Button) findViewById(R.id.scanner);
        memberButoon = (Button) findViewById(R.id.member);
        marketButoon = (Button) findViewById(R.id.market);
        ChatButoon = (Button) findViewById(R.id.chat);
        msgButton=(Button) findViewById(R.id.msg);
        scanResult = (TextView) findViewById(R.id.scanResult);
        //testButoon=(Button) findViewById(R.id.test);
        positionView = (TextView) findViewById(R.id.positionText);
        posUpdate = new PositionUpdate(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
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
        });

        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent scanIntent = new Intent(MainActivity.this, CaptureActivity.class);
                startActivityForResult(scanIntent, 0);
            }
        });

        memberButoon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                app.getClient().getAsyn(memUrl, new MyClient.ResultCallback<String>() {
                    @Override
                    public void onError(Request request, Exception e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(String u) {
                        Intent result = new Intent(MainActivity.this, BrowserAcitvity.class);
                        result.putExtra("result", u);
                        startActivity(result);
                    }
                });
            }
        });
        ChatButoon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent result = new Intent(MainActivity.this, ChatActivity.class);
                startActivity(result);

            }
        });

        msgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent result = new Intent(MainActivity.this, WXEntryActivity.class);
                startActivity(result);

            }
        });


    }

}
