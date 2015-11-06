package com.example.darren.lostfinding;

/**
 * 忘记密码界面
 * @author vivian
 */
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class ForgetActivity extends Activity {
	
	
	private Button btnBack,btnForget;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.forget);
		initView();
		initMethod();
	}

	private void initView() {
		//初始化界面
		// TODO Auto-generated method stub
		btnBack=(Button) findViewById(R.id.btn_forget_back);
		btnForget=(Button) findViewById(R.id.btn_forget_forget);
	}

	private void initMethod() {
		//初始化方法
		// TODO Auto-generated method stub
		btnBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		btnForget.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
				
			}
		});
		
	}

	

}
