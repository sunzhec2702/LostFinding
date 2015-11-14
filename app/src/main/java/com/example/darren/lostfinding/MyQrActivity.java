package com.example.darren.lostfinding;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.example.adapter.LvAdapterMainItem;
import com.example.cyc.Globle;
import com.example.cyc.MyListView;
import com.example.cyc.QREntity;
import com.example.darren.lostfinding.net.MyClient;
import com.squareup.okhttp.Request;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyQrActivity extends Activity {
	private Gdata app;
	private Button btnbk;
	private MyListView lvItem;

	private List<QREntity> qrInfo;
	private String PRI_QRUrl="http://192.168.0.88:8080/WHOS/app/statu";
	private String PUB_QRUrl="http://www.shuide.cc:8112/WHOS/app/statu";
	private String QRUrl= Globle.DEBUG?PRI_QRUrl:PUB_QRUrl;

	private String PRI_getUrl="http://192.168.0.88:8080/WHOS/search?ID=";
	private String PUB_getUrl="http://www.shuide.cc:8112/WHOS/search?ID=";
	private String getUrl= Globle.DEBUG?PRI_getUrl:PUB_getUrl;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_qr);
		app=(Gdata)getApplication();
		initView();
		initControl();
	}

	private void initView(){
		btnbk = (Button) findViewById(R.id.back);
	}
	private void initControl(){
		qrInfo=new ArrayList<QREntity>();
		lvItem = (MyListView) findViewById(R.id.lv_MyQR_items);
		getEntity();
		lvItem.setonRefreshListener(new MyListView.OnRefreshListener() {
			@Override
			public void onRefresh() {
				getEntity();
			}
		});
		btnbk.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
	}
	private void getEntity() {
		Map<String, String> obj = new HashMap<String, String>();
		obj.put("username", app.getName());
		app.getClient().postAsyn(QRUrl, new MyClient.ResultCallback<String>() {
			@Override
			public void onError(Request request, Exception e) {
				e.printStackTrace();
			}
			@Override
			public String onResponse(String u) {
				if (u.length() > 0) {
					qrInfo.clear();
					String r1, r2;
					while (u.indexOf("value=!@#$") != -1) {
						r1 = u.substring("key=!@#$".length(), u.indexOf("value=!@#$"));
						u = u.substring(u.indexOf("value=!@#$") + "value=!@#$".length());
						if (u.indexOf("value=!@#$") != -1) {
							r2 = u.substring(0, u.indexOf("key=!@#$"));
							if (r2.indexOf("\r") != -1) {
								r2 = r2.substring(0, r2.indexOf("\r"));
							}
							u = u.substring(u.indexOf("key=!@#$"));
						} else {
							r2 = u;
							break;
						}
						qrInfo.add(new QREntity(r1, r2));
					}
					LvAdapterMainItem adapter = new LvAdapterMainItem(getApplicationContext(), qrInfo, app.getPUB());
					adapter.setHandler(hUI);
					lvItem.setAdapter(adapter);
					lvItem.onRefreshComplete();
				}
				return u;
			}
		}, obj);
	}
	final Handler hUI = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == 666) {
				String rr=getUrl+(String) msg.obj;
				app.getClient().getAsyn(rr, new MyClient.ResultCallback<String>() {
					@Override
					public void onError(Request request, Exception e) {
						e.printStackTrace();
					}
					@Override
					public String onResponse(String u) {
						Intent result = new Intent(MyQrActivity.this, AfterScanActivity.class);
						result.putExtra("result", u);
						startActivity(result);
						return u;
					}
				});
			}
		}
	};
}
