package com.example.darren.lostfinding;

/**
 * 注册Activity;
 * @author vivian
 * 
 */


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.example.cyc.Globle;
import com.example.darren.lostfinding.net.MyClient;
import com.squareup.okhttp.Request;

public class RegistActivity extends Activity {
	public boolean DEBUG=false;
	private Button btnBack, btnRegist,btnConf;
	private EditText pwet,useret,cellet,rpwet,confet;
	private Gdata app;
	private View mProgressView;
	RegistActivity la=this;
	private String PRI_loginUrl="http://192.168.0.88:8080/WHOS/register.do";
	private String PUB_loginUrl="http://www.shuide.cc:8112/WHOS/register.do";
	private String loginUrl= Globle.DEBUG?PRI_loginUrl:PUB_loginUrl;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.regist);
		app = (Gdata)getApplication();
		final Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				if (msg.what == 1) {
					Intent result = new Intent(RegistActivity.this, LoginActivity.class);
					startActivity(result);
					la.finish();
				}
			}
		};

		final Handler hUI = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				if (msg.what != 0) {
					btnConf.setEnabled(false);
					btnConf.setText("等待" + msg.what + "秒后重新发送");
				}else{
					btnConf.setText("获取验证码");
					btnConf.setEnabled(true);
				}
			}
		};
		initView();
		initMethod();
		btnRegist.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				attemptLogin(handler);
			}
		});
		btnConf.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				String cell = cellet.getText().toString();
				app.getClient().sendConfMsg(cell);
				Thread thread = new Thread(new Runnable() {
					@Override
					public void run() {
						int tick = 60;
						while (tick != 0) {
							try {
								Thread.sleep(1000);
								tick--;
								Message message = new Message();
								message.what = tick;
								hUI.sendMessage(message);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					}
				});
				thread.start();
			}
		});

	}

	private void initView() {
		// TODO Auto-generated method stub
		btnBack = (Button) findViewById(R.id.btn_regist_back);
		btnRegist = (Button) findViewById(R.id.btn_regist_regist);
		useret=(EditText)findViewById(R.id.userID);
		cellet=(EditText)findViewById(R.id.pNUM);
		pwet=(EditText)findViewById(R.id.et_regist_userPwd);
		rpwet=(EditText)findViewById(R.id.rp_userPwd);
		mProgressView = findViewById(R.id.login_progress);
		btnConf=(Button) findViewById(R.id.btn_msg);
		confet=(EditText)findViewById(R.id.et_confirm);

	}

	private void initMethod() {
		// TODO Auto-generated method stub
		btnBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent result = new Intent(RegistActivity.this, LoginActivity.class);
				startActivity(result);
				finish();
			}
		});
	}

	private boolean attemptLogin(final Handler handler) {

		boolean result=false;
		// Store values at the time of the login attempt.
		String username = useret.getText().toString();
		String cell = cellet.getText().toString();
		String password = pwet.getText().toString();
		String c_password = rpwet.getText().toString();
		String conf = confet.getText().toString();
		boolean cancel = false;
		View focusView = null;

		if(password.compareTo(c_password)!=0){
			pwet.setError("两次密码不一致");
			focusView = pwet;
			cancel = true;
		}

		// Check for a valid password, if the user entered one.
		if (TextUtils.isEmpty(password)) {
			pwet.setError(getString(R.string.error_invalid_password));
			focusView = pwet;
			cancel = true;
		}
		if (cancel) {
			// There was an error; don't attempt login and focus the first
			// form field with an error.
			focusView.requestFocus();
		} else {
			// Show a progress spinner, and kick off a background task to
			// perform the user login attempt.
			showProgress(true);
			MyClient.Param[] par=new MyClient.Param[4];
			par[0]=new MyClient.Param("username",username);
			par[1]=new MyClient.Param("email",cell);
			par[2]=new MyClient.Param("password",password);
			par[3]=new MyClient.Param("confirm",conf);
			app.setName(username);
			app.getSocket();
			app.getClient().postAsyn(loginUrl, new MyClient.ResultCallback<String>() {
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
					//mTv.setText(u);//注意这里是UI线程
					return u;
				}
			}, par);
		}
		return result;
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
