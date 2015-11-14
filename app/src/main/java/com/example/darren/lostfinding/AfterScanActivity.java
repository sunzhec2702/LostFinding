package com.example.darren.lostfinding;

/**
 * @author vivian
 *
 */

import android.R.string;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.os.Message;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cyc.ChatMsgEntity;
import com.example.cyc.ChatingActivity;
import com.example.cyc.EditQrInfoActivity;
import com.example.cyc.Globle;
import com.example.darren.lostfinding.net.MyClient;
import com.squareup.okhttp.Request;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AfterScanActivity extends Activity {

	private Button btnNotice;
	private ImageView calling;
	private ImageView giving_message;
	private String Result;
	private TextView Name1,  Contact1, Mark1, Name2,Contact2,Mark2, MainInfo;
	private String owner, cell,QRID;
	private Gdata app;
	private int type;// 0 私人 1 公用
	private int kinds_of_qr;// 1 已购，2 已绑，3 已丢， 4 已归还
	private int kinds_of_people_individually;// 0物主， 1 拾到着(在公用品管理中0是管理者，1是借阅者)
	private String PRI_manageUrl="http://192.168.0.88:8080/WHOS/manage.do";
	private String PUB_manageUrl="http://www.shuide.cc:8112/WHOS/manage.do";
	private String manageUrl= Globle.DEBUG?PRI_manageUrl:PUB_manageUrl;
	private String ttts;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_tips_finder);
		app = (Gdata)getApplication();
		Intent ihere = this.getIntent();
		final Bundle bhere = ihere.getExtras();
		Result = bhere.getString("result");
		initView();
		initMethod(type);
	}

	private void initView() {
		// TODO Auto-generated method stub
		btnNotice = (Button) findViewById(R.id.bt_afterscan_notice);
		calling = (ImageView) findViewById(R.id.iv_afterscan_calling);
		giving_message = (ImageView) findViewById(R.id.iv_afterscan_message);
		MainInfo = (TextView) findViewById(R.id.main_content);
		Name1 = (TextView) findViewById(R.id.name1);
		Name2 = (TextView) findViewById(R.id.name2);
		Contact1 = (TextView) findViewById(R.id.contact1);
		Contact2 = (TextView) findViewById(R.id.contact2);
		Mark2 = (TextView) findViewById(R.id.mark2);
		Mark1 = (TextView) findViewById(R.id.mark1);

		String 	tp = Result.substring(Result.indexOf("<p hidden> 物品状态 ")+"<p hidden> 物品类型 ".length());
		kinds_of_qr=Integer.parseInt(tp.substring(0, tp.indexOf("<p>")));

		tp = Result.substring(Result.indexOf("<p hidden> 物品编码 ") + "<p hidden> 物品类型 ".length());
		QRID=tp.substring(0, tp.indexOf("<p>"));


		tp = Result.substring(Result.indexOf("<p hidden> 物品类型 ") + "<p hidden> 物品类型 ".length());
		type=Integer.parseInt(tp.substring(0, tp.indexOf("<p>")));
		tp = Result.substring(Result.indexOf("<h1> 用户名 ") + "<h1> 用户名 ".length());
		tp=tp.substring(0,tp.indexOf("</h1>"));
		kinds_of_people_individually=app.getName().compareTo(tp)==0?0:1;
		if (type  == 0 && kinds_of_people_individually == 0 ) {// 情况1，【私人-失主】情景
			String ts = Result.substring(Result.indexOf("<h1> 名称 ")
					+ "<h1> 名称 ".length());
			MainInfo.setText("这是您自己的 "
					+ ts.substring(0, ts.indexOf("</h1>")) + " 以下是详细信息");
			ttts=ts.substring(0, ts.indexOf("</h1>"));
			Name1.setText("物主：");
			ts = ts.substring(ts.indexOf("<h1> 用户名 ") + "<h1> 用户名 ".length());
			owner = ts.substring(0, ts.indexOf("</h1>"));
			Name2.setText(owner);

			Contact1.setText("联系方式：");
			ts = ts.substring(ts.indexOf("<h1> 联系方式 ") + "<h1> 联系方式 ".length());
			cell = ts.substring(0, ts.indexOf("</h1>"));
			Contact2.setText(cell);

			Mark1.setText("备注：");
			ts = ts.substring(ts.indexOf("<h1> 备注 ") + "<h1> 备注 ".length());
			Mark2.setText(ts.substring(0, ts.indexOf("</h1>")));

			switch (kinds_of_qr) {
				case 1:// 失主，扫描到已购买的二维码，需要去绑定二维码
					btnNotice.setText("编辑我的二维码");

				case 2://失主，扫描到已绑定的二维码，可能需要二维码编辑状态
					btnNotice.setText("编辑我的二维码");

				case 3://失主，扫描到已经丢失的二维码，需要编辑状态成“已归还”
					btnNotice.setText("找到您的东西啦！");

				case 4://失主，扫描到已归还的二维码，可能需要二维码编辑状态
					btnNotice.setText("编辑我的二维码");

			}

		}

		if (type  == 0 && kinds_of_people_individually == 1) {// 情况2，【私人-拾到者】情景，只能报失
			String ts = Result.substring(Result.indexOf("<h1> 名称 ")
					+ "<h1> 名称 ".length());
			MainInfo.setText("您通过扫码找到一个他人的"
					+ ts.substring(0, ts.indexOf("</h1>")) + "以下是详细信息");

			Name1.setText("失主名称");
			ts = ts.substring(ts.indexOf("<h1> 用户名 ") + "<h1> 用户名 ".length());
			owner = ts.substring(0, ts.indexOf("</h1>"));
			Name2.setText(owner);

			Contact1.setText("联系方式");
			ts = ts.substring(ts.indexOf("<h1> 联系方式 ") + "<h1> 联系方式 ".length());
			cell = ts.substring(0, ts.indexOf("</h1>"));
			Contact2.setText(cell);

			Mark1.setText("备注");
			ts = ts.substring(ts.indexOf("<h1> 备注 ") + "<h1> 备注 ".length());
			Mark2.setText(ts.substring(0, ts.indexOf("</h1>")));

			btnNotice.setText("报失");

		}

		if (type  == 1 && kinds_of_people_individually == 0) {// 情况3，【公用品-管理人】情景
			String ts = Result.substring(Result.indexOf("<h1> 名称 ")
					+ "<h1> 名称 ".length());
			MainInfo.setText("这是您管理的："
					+ ts.substring(0, ts.indexOf("</h1>")) + "以下是详细信息");

			Name1.setText("物品名称");
			ts = ts.substring(ts.indexOf("<h1> 用户名 ") + "<h1> 用户名 ".length());
			owner = ts.substring(0, ts.indexOf("</h1>"));
			Name2.setText(owner);

			Contact1.setText("联系方式");
			ts = ts.substring(ts.indexOf("<h1> 联系方式 ") + "<h1> 联系方式 ".length());
			cell = ts.substring(0, ts.indexOf("</h1>"));
			Contact2.setText(cell);

			Mark1.setText("备注");
			ts = ts.substring(ts.indexOf("<h1> 备注 ") + "<h1> 备注 ".length());
			Mark2.setText(ts.substring(0, ts.indexOf("</h1>")));

			switch (kinds_of_qr) {
				case 1:// 管理员，扫描到已购买的二维码，需要去绑定二维码
					btnNotice.setText("去绑定物品");

				case 2://管理员，扫描到已绑定的二维码，可能需要二维码编辑状态
					btnNotice.setText("编辑我的二维码");

				case 3://管理员，扫描到已经借出（已丢的状态）的二维码，需要编辑状态成“已归还”
					btnNotice.setText("办理归还");

				case 4://管理员，扫描到已归还的二维码，可能需要二维码编辑状态
					btnNotice.setText("编辑我的二维码");

			}

		}

		if (type ==1 && kinds_of_people_individually == 1) {// 情况4，【公用品-借阅人】情景，只能借阅
			String ts = Result.substring(Result.indexOf("<h1> 名称 ")
					+ "<h1> 名称 ".length());
			MainInfo.setText("您正在办理借阅 "
					+ ts.substring(0, ts.indexOf("</h1>")) + "以下是详细信息");

			Name1.setText("管理员：");
			ts = ts.substring(ts.indexOf("<h1> 用户名 ") + "<h1> 用户名 ".length());
			owner = ts.substring(0, ts.indexOf("</h1>"));
			Name2.setText(owner);

			Contact1.setText("管理员");
			ts = ts.substring(ts.indexOf("<h1> 联系方式 ") + "<h1> 联系方式 ".length());
			cell = ts.substring(0, ts.indexOf("</h1>"));
			Contact2.setText(cell);

			Mark1.setText("物品详细信息");
			ts = ts.substring(ts.indexOf("<h1> 备注 ") + "<h1> 备注 ".length());
			Mark2.setText(ts.substring(0, ts.indexOf("</h1>")));

			btnNotice.setText("借阅");

		}

	}

	private void initMethod(int t) {
		if (t == 0) {

		}
		// TODO Auto-generated method stub


		calling.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent aintent = new Intent();
				aintent.setAction("android.intent.action.CALL");
				aintent.setData(Uri.parse("tel:" + cell));
				startActivity(aintent);// 方法内部会自动添加类别,android.intent.category.DEFAULT
				/*
				 * Intent aIntent=new Intent();
				 * aIntent.setClass(AfterScanActivity.this,
				 * AutoTelephoneActivity.class); startActivity(aIntent);
				 */
			}
		});

		giving_message.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put(owner,"ALOHA");
				app.saveMsg("chat", map);
				Intent result = new Intent(AfterScanActivity.this,
						ChatingActivity.class);
				result.putExtra("DSTID", owner);
				startActivity(result);
				// finish();//自动发送消息
			}
		});

		OnClickListener serchType=new OnClickListener() {
			HashMap<String, String> map = new HashMap<String, String>();
			@Override
			public void onClick(View arg0) {
				//编辑
				if(kinds_of_qr!=3 && kinds_of_people_individually==0){
					Intent result = new Intent(AfterScanActivity.this, EditQrInfoActivity.class);
					result.putExtra("id", QRID);
					result.putExtra("name", ttts);
					result.putExtra("mark", Mark2.getText().toString());
					startActivity(result);
				}
				//借阅&&丢失
				else if(kinds_of_people_individually==1){
					map.put("dfid", QRID);
					map.put("dfname", app.getName());
					map.put("method", "next");
					app.getClient().postAsyn(manageUrl, RC, map);
				}
				//归还
				else if(kinds_of_qr==3 && kinds_of_people_individually==0){
					map.put("dfid",QRID);
					map.put("method", "return");
					app.getClient().postAsyn(manageUrl,RC,map);
				}
				finish();
			}
		};
		btnNotice.setOnClickListener(serchType);
	}
	MyClient.ResultCallback<String> RC = new MyClient.ResultCallback<String>() {
		@Override
		public void onError(Request request, Exception e) {
			e.printStackTrace();
		}
		@Override
		public String onResponse(String u) {
			return u;
		}
	};
	InputFilter[] disEdit = new InputFilter[] {
		new InputFilter() {
			public CharSequence filter(CharSequence source, int start,
									   int end, Spanned dest, int dstart, int dend) {
				return source.length() < 1 ? dest.subSequence(dstart, dend) : "";
			}
		}
	};

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		finish();
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			finish();
			return true;
		}else {
			return super.onKeyDown(keyCode, event);
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		finish();
	}
}
