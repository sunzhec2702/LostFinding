package com.example.darren.lostfinding;

/**
 * 忘记密码界面
 * @author vivian
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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.example.cyc.Globle;
import com.example.darren.lostfinding.net.MyClient;
import com.squareup.okhttp.Request;

public class ForgetActivity extends Activity {
	
	
	private Button btnBack,btnForget,btnMsg;
	private EditText etCell,etMsg;
	private Gdata app;
	private View mProgressView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.forget);
		app = (Gdata)getApplication();
		initView();
		initMethod();
		final Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				if (msg.what == 1) {
					Intent result = new Intent(ForgetActivity.this, RestpwActivity.class);
					String cell = etCell.getText().toString();
					result.putExtra("result", cell);
					startActivity(result);
					finish();
				}
			}
		};
		final Handler hUI = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				if (msg.what != 0) {
					btnMsg.setEnabled(false);
					btnMsg.setText(msg.what);
				} else {
					btnMsg.setText("获取验证码");
					btnMsg.setEnabled(true);
				}
			}
		};

		btnMsg.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String cell = etCell.getText().toString();
				MyClient.sendConfMsg(cell);
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

		btnForget.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				showProgress(true);
				String cell = etCell.getText().toString();
				String msg = etMsg.getText().toString();
				MyClient.Param[] par=new MyClient.Param[2];
				par[0]=new MyClient.Param("key",cell);
				par[1]=new MyClient.Param("forgot",msg);
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
			}
		});
	}

	private void initView() {
		//初始化界面
		// TODO Auto-generated method stub
		btnBack=(Button) findViewById(R.id.btn_forget_back);
		btnForget=(Button) findViewById(R.id.btn_forget_forget);
		btnMsg=(Button) findViewById(R.id.btn_msg);
		etCell=(EditText)findViewById(R.id.et_cell);
		etMsg=(EditText)findViewById(R.id.et_forget_userID);
		mProgressView = findViewById(R.id.login_progress);
	}

	private void initMethod() {
		//初始化方法
		// TODO Auto-generated method stub

		btnBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent result = new Intent(ForgetActivity.this, LoginActivity.class);
				startActivity(result);
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
