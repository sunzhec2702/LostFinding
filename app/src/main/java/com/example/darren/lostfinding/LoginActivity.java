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
import android.widget.TextView;

import com.squareup.okhttp.Request;

import java.util.HashMap;
import java.util.Map;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    private EditText mEmailView, mPasswordView;
    private View mProgressView;
    private Gdata app;
    public boolean loginflag = false;
    LoginActivity la = this;
    TextView btnRegist, btnForget;
    Button mEmailSignInButton;
    String name = null;
    String password = null;
    private SQLiteOpenHelper helper;
    private HashMap<String, Object> map;
    boolean log = false;

    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        app = (Gdata) getApplication();
        initView();
        map = (HashMap<String, Object>) app.getMsg("login");
        if (map != null && !map.isEmpty()) {
            name = (String) map.get("login2");
            if (name != null && (password = (String) map.get(name)) != null) {
                showProgress(true);
                initControl(1);
                Login(handler);
            } else {
                if (name != null) {
                    app.delLog("login", name);
                }
                initControl(0);
            }
        } else {
            initControl(0);
        }
    }

    private void initControl(int type) {
        if (type == 1) {
            //自动登录
            mEmailSignInButton.setText("自动登录中。。。");
            mEmailSignInButton.setEnabled(false);
        } else if (type == 0) {
            mEmailSignInButton.setText("用户登录");
            mEmailSignInButton.setEnabled(true);
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

    private void initView() {
        mEmailView = (EditText) findViewById(R.id.et_login_UserID);
        mPasswordView = (EditText) findViewById(R.id.et_login_UserPwd);
        btnRegist = (TextView) findViewById(R.id.btn_login_Regist);
        btnForget = (TextView) findViewById(R.id.btn_login_forgetPwd);
        mEmailSignInButton = (Button) findViewById(R.id.btn_login_Login);
        mProgressView = findViewById(R.id.login_progress);

    }

    final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                //testResult.setText(tt);
                Intent result = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(result);
                la.finish();
            } else if (msg.what == -1) {
                mPasswordView.setError("用户名或密码不对");
                View focusView = mPasswordView;
                focusView.requestFocus();
            } else if (msg.what == 0) {
                mPasswordView.setError("用户名或密码不对");
                View focusView = mPasswordView;
                focusView.requestFocus();
                initControl(0);
            }else if (msg.what == -10) {
                mPasswordView.setError("登录超时，请检查网络设置");
                endProgress();
                View focusView = mPasswordView;
                focusView.requestFocus();
                initControl(0);
            }
        }
    };
    Thread timeoutthread = new Thread(new Runnable() {
        @Override
        public void run() {
            int tick = 10;
            while (tick != 0) {
                try {
                    Thread.sleep(1000);
                    tick--;

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            Message message = new Message();
            message.what = -10;
            handler.sendMessage(message);
        }
    });

    private void endProgress() {
        mEmailView.setVisibility(View.VISIBLE);
        mPasswordView.setVisibility(View.VISIBLE);
        btnForget.setVisibility(View.VISIBLE);
        btnRegist.setVisibility(View.VISIBLE);
        mProgressView.setVisibility(View.GONE);
        initControl(0);
    }

    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        mEmailView.setVisibility(View.GONE);
        mPasswordView.setVisibility(View.GONE);
        btnForget.setVisibility(View.GONE);
        btnRegist.setVisibility(View.GONE);
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
        name = mEmailView.getText().toString();
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
        } else {
            showProgress(true);
            MyClient.Param[] par = new MyClient.Param[2];
            par[0] = new MyClient.Param("username", name);
            par[1] = new MyClient.Param("password", password);
            MyClient.postAsyn(app.login_add, new MyClient.ResultCallback<String>() {
                @Override
                public void onError(Request request, Exception e) {
                    e.printStackTrace();
                }

                @Override
                public String onResponse(String u) {
                    if (u.indexOf("欢迎") != -1) {
                        if (u.indexOf("公用品管理员") != -1) {
                            app.setPUB(true);
                        } else {
                            app.setPUB(false);
                        }
                        app.setName(name);
                        app.getSocket();
                        HashMap<String, Object> map = new HashMap<String, Object>();
                        map.put("login2", name);
                        map.put(name, password);
                        app.saveMsg("login", map);
                        if (handler != null) {
                            Message message = new Message();
                            message.what = 1;
                            handler.sendMessage(message);
                        }

                    } else {
                        showProgress(false);
                        if (handler != null) {
                            Message message = new Message();
                            message.what = -1;
                            handler.sendMessage(message);
                        }
                    }
                    //mTv.setText(u);//注意这里是UI线程
                    return u;
                }
            }, par);
            timeoutthread.start();
        }
    }

    private void Login(final Handler handler) {
        MyClient.Param[] par = new MyClient.Param[2];
        par[0] = new MyClient.Param("username", name);
        par[1] = new MyClient.Param("password", password);
        MyClient.postAsyn(app.login_add, new MyClient.ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {
                e.printStackTrace();
            }

            @Override
            public String onResponse(String u) {
                if (u.indexOf("欢迎") != -1) {
                    if (u.indexOf("公用品管理员") != -1) {
                        app.setPUB(true);
                    } else {
                        app.setPUB(false);
                    }
                    app.setName(name);
                    app.getSocket();
                    HashMap<String, Object> map = new HashMap<String, Object>();
                    if (handler != null) {
                        Message message = new Message();
                        message.what = 1;
                        handler.sendMessage(message);
                    }
                } else {
                    if (handler != null) {
                        app.delLog("login", name);
                        Message message = new Message();
                        message.what = 0;
                        handler.sendMessage(message);
                    }
                }
                //mTv.setText(u);//注意这里是UI线程
                return u;
            }
        }, par);
        timeoutthread.start();
    }
}

