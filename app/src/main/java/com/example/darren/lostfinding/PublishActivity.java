package com.example.darren.lostfinding;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class PublishActivity extends Activity {
	
	
	private Button btnCancle,btnOK;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.publish);
		initView();
	}

	private void initView() {
		// TODO Auto-generated method stub
		btnCancle=(Button) findViewById(R.id.btn_publish_cancle);
		btnOK=(Button) findViewById(R.id.btn_publish_ok);
		
		btnCancle.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		btnOK.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
		
	}

	
}
