package com.example.darren.lostfinding;

import com.example.cyc.Globle;
import com.example.darren.lostfinding.net.MyClient;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.squareup.okhttp.Request;

import java.util.HashMap;
import java.util.Map;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    private EditText mEmailView,mPasswordView;
    private View mProgressView;
    private String PRI_loginUrl="http://192.168.0.88:8080/WHOS/login.do";
    private String PUB_loginUrl="http://www.shuide.cc:8112/WHOS/login.do";
    private String loginUrl= Globle.DEBUG?PRI_loginUrl:PUB_loginUrl;
    private Gdata app;
    public boolean loginflag=false;
    LoginActivity la=this;
    Button btnRegist,btnForget;
    String name=null;
    String password=null;
    private SQLiteOpenHelper helper;
    private HashMap<String, Object> map;
    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        app = (Gdata)getApplication();

        mProgressView = findViewById(R.id.login_progress);

        map = (HashMap<String, Object>) app.getMsg("login");
        if (map != null && !map.isEmpty()) {
            name=(String)map.get("login2");
            if (name!=null) {
                password=(String)map.get(name);
                Login();
                app.setName(name);
                //若值为true,用户无需输入密码，直接跳转进入操作界面
                Intent intent = new Intent(LoginActivity.this,
                        MainActivity.class);
                startActivity(intent);
                finish();
            }
        }else {
            mEmailView = (EditText) findViewById(R.id.et_login_UserID);
            mPasswordView = (EditText) findViewById(R.id.et_login_UserPwd);
            btnRegist = (Button) findViewById(R.id.btn_login_Regist);
            btnForget = (Button) findViewById(R.id.btn_login_forgetPwd);

            final Handler handler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    if (msg.what == 1) {
                        //testResult.setText(tt);
                        Intent result = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(result);
                        la.finish();
                    }else if(msg.what==-1){
                        mPasswordView.setError("用户名或密码不对");
                        View focusView = mPasswordView;
                        focusView.requestFocus();
                    }
                }
            };

            Button mEmailSignInButton = (Button) findViewById(R.id.btn_login_Login);
            mEmailSignInButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    attemptLogin(handler);
                }
            });

            btnRegist.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent result = new Intent(LoginActivity.this, RegistActivity.class);
                    startActivity(result);
                    la.finish();
                }
            });
            btnForget.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent result = new Intent(LoginActivity.this, ForgetActivity.class);
                    startActivity(result);
                    la.finish();
                }
            });
        }
    }


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

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin(final Handler handler) {
        boolean cancel = false;

            mEmailView.setError(null);
            mPasswordView.setError(null);
            name =mEmailView.getText().toString();
            password = mPasswordView.getText().toString();
            View focusView = null;

            // Check for a valid password, if the user entered one.
            if (TextUtils.isEmpty(password)) {
                mPasswordView.setError(getString(R.string.error_invalid_password));
                focusView = mPasswordView;
                cancel = true;
            }

            if (cancel) {
                focusView.requestFocus();
            }
        else  {
            showProgress(true);
                MyClient.Param[] par=new MyClient.Param[2];
            par[0]=new MyClient.Param("username",name);
            par[1]= new MyClient.Param("password", password);
            app.getClient().postAsyn(loginUrl, new MyClient.ResultCallback<String>() {
                @Override
                public void onError(Request request, Exception e) {
                    e.printStackTrace();
                }
                @Override
                public String onResponse(String u) {
                    if (u.indexOf("欢迎") != -1) {
                        if(u.indexOf("公用品管理员") != -1){
                            app.setPUB(true);
                        }else{
                            app.setPUB(false);
                        }
                        app.setName(name);
                        app.getSocket();
                        HashMap<String, Object> map = new HashMap<String, Object>();
                        map.put("login2", name);
                        map.put(name, password);
                        app.saveMsg("login", map);
                        if(handler!=null){
                            Message message = new Message();
                            message.what = 1;
                            handler.sendMessage(message);
                        }

                    } else {
                        showProgress(false);
                        if(handler!=null) {
                            Message message = new Message();
                            message.what = -1;
                            handler.sendMessage(message);
                        }
                    }
                    //mTv.setText(u);//注意这里是UI线程
                    return u;
                }
            }, par);
        }
    }

    private void Login() {
            MyClient.Param[] par=new MyClient.Param[2];
            par[0]=new MyClient.Param("username",name);
            par[1]= new MyClient.Param("password", password);
            app.getClient().postAsyn(loginUrl, new MyClient.ResultCallback<String>() {
                @Override
                public void onError(Request request, Exception e) {
                    e.printStackTrace();
                }
                @Override
                public String onResponse(String u) {
                    if (u.indexOf("欢迎") != -1) {
                        if(u.indexOf("公用品管理员") != -1){
                            app.setPUB(true);
                        }else{
                            app.setPUB(false);
                        }
                        app.setName(name);
                        app.getSocket();
                        HashMap<String, Object> map = new HashMap<String, Object>();

                    } else {
                    }
                    //mTv.setText(u);//注意这里是UI线程
                    return u;
                }
            }, par);
        }

}

