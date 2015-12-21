package com.example.darren.lostfinding;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;

import com.example.adapter.QR_Item_Adapter;
import com.example.adapter.QR_Item_Adapter.*;
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
	private Button btnbk,btnok;
	private MyListView lvItem;
	private MyQrActivity LA;
	private List<QREntity> qrInfo;
	private List<Change> qrVis;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_qr);
		app=(Gdata)getApplication();
		LA=this;
		initView();
		initControl();
	}

	private void initView(){
		btnbk = (Button) findViewById(R.id.back);
		btnok= (Button) findViewById(R.id.OK);
	}
	private void initControl(){
		qrInfo=new ArrayList<QREntity>();
		qrVis=new ArrayList<Change>();
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
		btnok.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Map<String, String> obj = new HashMap<String, String>();
				for(int i=0;i<qrVis.size();i++){
					obj.put(String.valueOf(i), qrVis.get(i).toString());
				}
				MyClient.postAsyn(app.vis_add, new MyClient.ResultCallback<String>() {
					@Override
					public void onError(Request request, Exception e) {
						e.printStackTrace();
					}

					@Override
					public String onResponse(String u) {
						LA.finish();
						return u;
					}
				}, obj);
			}
		});
	}
	private void getEntity() {
		Map<String, String> obj = new HashMap<String, String>();
		obj.put("username", app.getName());
		MyClient.postAsyn(app.statu_add, new MyClient.ResultCallback<String>() {
			@Override
			public void onError(Request request, Exception e) {
				e.printStackTrace();
			}
			@Override
			public String onResponse(String u) {
				if (u.length() > 0) {
					qrInfo.clear();
					qrInfo=QREntity.getQRfromString(u);
					QR_Item_Adapter adapter = new QR_Item_Adapter(getApplicationContext(), qrInfo, app.getPUB());
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
				String rr=app.search_add+(String) msg.obj+"&app=1.0.0";
				MyClient.getAsyn(rr, new MyClient.ResultCallback<String>() {
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
			}else if(msg.what == 777){//修改显示状态
				qrVis.add((Change)msg.obj);
			}else if(msg.what == 888){//展示图片
				Intent result = new Intent(MyQrActivity.this,
						QRShowActivity.class);
				result.putExtra("DSTID", app.search_add+(String) msg.obj);
				startActivity(result);
			}
		}
	};

	@Override
	protected void  onResume(){
		super.onResume();
		getEntity();
	}
}
