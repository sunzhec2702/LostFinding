package com.example.darren.lostfinding;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ToggleButton;

import com.example.adapter.LvAdapterMainItem;
import com.example.cyc.Globle;
import com.example.cyc.QREntity;
import com.example.darren.lostfinding.net.MyClient;
import com.squareup.okhttp.Request;

import org.json.JSONException;
import org.json.JSONObject;

public class PersonInformationEditActivity extends Activity {
	private Gdata app;
	private EditText[] liEdit;
	private ImageView ivSet;
	private String PRI_getUrl="http://192.168.0.88:8080/WHOS/psinfo?name=";
	private String PUB_getUrl="http://www.shuide.cc:8112/WHOS/psinfo?name=";
	private String getUrl= Globle.DEBUG?PRI_getUrl:PUB_getUrl;
	//private ToggleButton tbSet;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.person_information_edit);
		app=(Gdata)getApplication();
		initView();
	}

	private void initView() {
		// TODO Auto-generated method stub
		liEdit = new EditText[5];
		liEdit[0] = (EditText) findViewById(R.id.et_personInfoEdit_UserName);
		liEdit[1] = (EditText) findViewById(R.id.et_personInfoEdit_NickName);
		liEdit[2] = (EditText) findViewById(R.id.et_personInfoEdit_phoneNum);
		liEdit[3] = (EditText) findViewById(R.id.et_personInfoEdit_money);
		liEdit[4] = (EditText) findViewById(R.id.et_personInfoEdit_goodTime);
		app.getClient().getAsyn(getUrl+app.getName(),new MyClient.ResultCallback<String>() {
			@Override
			public void onError(Request request, Exception e) {
				e.printStackTrace();
			}
			@Override
			public String onResponse(String u) {
				JSONObject obj = null;
				try {
					obj = new JSONObject(u);
					liEdit[0].setText(app.getName());
					liEdit[2].setText(obj.getString("cell"));
					liEdit[3].setText(obj.getString("gold"));
					liEdit[1].setText("");
					liEdit[4].setText("");

				} catch (JSONException e) {
					e.printStackTrace();
				}

				return u;
			}
		});

	}

	
	//请马乐补充保存函数
	private void save(){
		
	}
	//请马乐补充充值函数
	private void charging(){
		
	}

}
