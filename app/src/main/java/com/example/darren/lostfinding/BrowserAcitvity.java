package com.example.darren.lostfinding;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.example.darren.lostfinding.net.GetResponse;
import com.example.darren.scanner.CaptureActivity;


import org.w3c.dom.Text;

import java.io.IOException;
//import android.os.StrictMode;


/**
 * Created by cyclONE on 2015/10/22.
 */
public class BrowserAcitvity extends Activity {
    public String TestUrl="http://192.168.0.88:8080/WHOS/find.view?ID=00000001";
    TextView testResult;
    String tt;
    //private Button backButton;
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        //Window window = getWindow();
        //window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.browser);
        Intent ihere=this.getIntent();
        final Bundle bhere=ihere.getExtras();
        /*if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }*/
        testResult=(TextView) findViewById(R.id.testtext);

        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == 1) {
                    testResult.setText(tt);
                }
            }
        };

        Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {
                GetResponse GR=new GetResponse();
                GR.setUrl(bhere.getString("result"));
                tt=GR.get();
                Message message = new Message();
                message.what = 1;
                handler.sendMessage(message);
            }

        });
        thread.start();
        //testResult.setText(GR.getReslut());
        //backButton = (Button) findViewById(R.id.goback);
        /*backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });*/
    }


}

