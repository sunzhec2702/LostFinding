package com.example.darren.lostfinding;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class PublishActivity extends Activity {


	private ImageView ImageBank,ImageOk;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.publish);
		initView();
	}

	private void initView() {
		// TODO Auto-generated method stub
		ImageBank=(ImageView) findViewById(R.id.iv_publish_bank);
		ImageOk=(ImageView) findViewById(R.id.iv_publish_ok);

		ImageBank.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		ImageOk.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});


	}


}
