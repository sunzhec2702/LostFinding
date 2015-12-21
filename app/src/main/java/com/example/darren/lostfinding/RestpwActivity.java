package com.example.darren.lostfinding;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.cyc.Globle;
import com.example.darren.lostfinding.net.MyClient;
import com.squareup.okhttp.Request;

public class RestpwActivity extends AppCompatActivity {
    private Button btnBack,btnReset;
    private EditText etPw,etNpw;
    private Gdata app;
    private View mProgressView;
    private String cell;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restpw);
        app = (Gdata)getApplication();
        Intent ihere=this.getIntent();
        final Bundle bhere=ihere.getExtras();
        cell=bhere.getString("result");
        btnBack=(Button) findViewById(R.id.btn_forget_back);
        btnReset=(Button) findViewById(R.id.btn_forget_forget);
        etPw=(EditText)findViewById(R.id.et_pw);
        etNpw=(EditText)findViewById(R.id.et_cpw);
        mProgressView = findViewById(R.id.login_progress);
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == 1) {
                    Intent result = new Intent(RestpwActivity.this, LoginActivity.class);
                    startActivity(result);
                    finish();
                }
            }
        };
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                Intent result = new Intent(RestpwActivity.this, LoginActivity.class);
                startActivity(result);
                finish();
            }
        });
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                String pw1 = etPw.getText().toString();
                String pw2 = etNpw.getText().toString();
                if(pw1.compareTo(pw2)!=0){
                    etPw.setError(getString(R.string.error_invalid_password));
                    View focusView = etPw;
                    focusView.requestFocus();
                    return;
                }
                showProgress(true);
                MyClient.Param[] par=new MyClient.Param[2];
                par[0]=new MyClient.Param("key",cell);
                par[1]=new MyClient.Param("reset",pw1);
                MyClient.postAsyn(app.regist_add, new MyClient.ResultCallback<String>() {
                    @Override
                    public void onError(Request request, Exception e) {
                        e.printStackTrace();
                    }
                    @Override
                    public String onResponse(String u) {
                        if (u.indexOf("成功") != -1) {
                            Message message = new Message();
                            message.what = 1;
                            handler.sendMessage(message);
                        } else {
                            Message message = new Message();
                            message.what = -1;
                            handler.sendMessage(message);
                        }
                        return u;
                    }
                }, par);
                finish();
            }
        });
    }
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);


            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }
}
