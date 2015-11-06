package com.example.darren.lostfinding;

import com.example.darren.lostfinding.net.MyClient;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.darren.lostfinding.net.MyClient;
import com.google.zxing.client.result.ResultParser;
import com.squareup.okhttp.Request;

import java.util.ArrayList;
import java.util.List;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity  {

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */

    // UI references.
    private EditText mEmailView,mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private String loginUrl="http://192.168.0.88:8080/WHOS/login.do";
    private Gdata app;
    public boolean loginflag=false;
    LoginActivity la=this;
    Button btnRegist,btnForget;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app = (Gdata)getApplication();
        Intent ihere=this.getIntent();
        final Bundle bhere=ihere.getExtras();


        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == 1) {
                    //testResult.setText(tt);
                    Intent result = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(result);
                    la.finish();
                }
            }
        };
        setContentView(R.layout.login);
        // Set up the login form.
        mEmailView = (EditText) findViewById(R.id.et_login_UserID);

        mPasswordView = (EditText) findViewById(R.id.et_login_UserPwd);


        Button mEmailSignInButton = (Button) findViewById(R.id.btn_login_Login);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin(handler);
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

        // 登陆按钮
        btnRegist = (Button) findViewById(R.id.btn_login_Regist);
        // 忘记密码
        btnForget = (Button) findViewById(R.id.btn_login_forgetPwd);

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
            }
        });

    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin(final Handler handler) {


        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }


        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            //showProgress(true);
            MyClient.Param[] par=new MyClient.Param[2];
            par[0]=new MyClient.Param("username",email);
            par[1]= new MyClient.Param("password", password);
            app.setName(email);
            app.getSocket();
            app.getClient().postAsyn(loginUrl, new MyClient.ResultCallback<String>() {
                @Override
                public void onError(Request request, Exception e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(String u) {
                    if (u.indexOf("欢迎") != -1) {
                        Message message = new Message();
                        message.what = 1;
                        handler.sendMessage(message);
                    } else {
                        Message message = new Message();
                        message.what = -1;
                        handler.sendMessage(message);
                    }
                    //mTv.setText(u);//注意这里是UI线程
                }
            }, par);
        }
    }

     /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

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
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }



        protected void onPostExecute(final Boolean success) {
            showProgress(false);

            if (success) {
                finish();
            } else {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
        }


        protected void onCancelled() {
            showProgress(false);
        }
}

