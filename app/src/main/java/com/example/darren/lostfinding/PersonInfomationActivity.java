package com.example.darren.lostfinding;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.cyc.Globle;
import com.example.darren.lostfinding.net.MyClient;
import com.squareup.okhttp.Request;

import org.json.JSONException;
import org.json.JSONObject;

public class PersonInfomationActivity extends Activity {

	private TextView[] liEdit;
	private ImageView ImageP;
	private ImageView back;
	private Button edit;
	private Gdata app;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.person_infomation);
		app=(Gdata)getApplication();
		initView();
	}

	final Handler hUI = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == 1) {
				try {
					JSONObject obj = null;
					obj = new JSONObject((String)msg.obj);
					liEdit[0].setText(app.getName());
					liEdit[2].setText(obj.getString("cell"));
					liEdit[3].setText(obj.getString("gold"));
					liEdit[1].setText(obj.getString("nickname"));
					liEdit[4].setText("0");
					liEdit[5].setText(obj.getString("address"));
					setEdit(false);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
	};

	private void initView() {

		liEdit = new TextView[6];
		//liEdit[0] = (TextView) findViewById(R.id.et_personInfo_UserName);
		//liEdit[1] = (TextView) findViewById(R.id.et_personInfo_NickName);
		//liEdit[2] = (TextView) findViewById(R.id.et_personInfo_phoneNum);
		//liEdit[3] = (TextView) findViewById(R.id.et_personInfo_money);
		//liEdit[4] = (TextView) findViewById(R.id.et_personInfo_goodTime);
		//liEdit[5] = (TextView) findViewById(R.id.et_personInfo_add);
		MyClient.getAsyn(app.pinfo_add + app.getName(), new MyClient.ResultCallback<String>() {
			@Override
			public void onError(Request request, Exception e) {
				e.printStackTrace();
			}

			@Override
			public String onResponse(String u) {
				Message msg = new Message();
				msg.what = 1;
				msg.obj = u;
				hUI.sendMessage(msg);
				return u;
			}
		});

		ImageP=(ImageView) findViewById(R.id.iv_personInfo_UserIcon);
		if(!Gdata.setPicLocal(app.getName(),ImageP)){
			Gdata.setPicNet(app.getName(),ImageP);
		}
		ImageP.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				/*Intent intent = new Intent();
				intent.setClass(PersonInfomationActivity.this,
						PersonInformationEditActivity.class);
				intent.putExtra("l1", liEdit[1].getText().toString());
				intent.putExtra("l2", liEdit[2].getText().toString());
				intent.putExtra("l3", liEdit[3].getText().toString());
				intent.putExtra("l4", liEdit[5].getText().toString());
				startActivity(intent);
				finish();*/
			}
		});

	}




	private void setEdit(Boolean bl) {
		// 循环设置不可编辑

		for (int i = 0; i < liEdit.length; i++) {
			// liEdit[i].setFocusable(bl);
			liEdit[i].setEnabled(bl);

		}

	}

}
