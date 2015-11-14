package com.example.darren.lostfinding;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ToggleButton;

public class PersonInfomationActivity extends Activity {

	private EditText[] liEdit;
	private ImageView ivSet;
	// private ImageView ImageBank;
	private ToggleButton tbSet;
	private Button edit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.person_infomation);
		initView();
	}

	private void initView() {
		// TODO Auto-generated method stub
		liEdit = new EditText[5];
		liEdit[0] = (EditText) findViewById(R.id.et_personInfo_UserName);
		liEdit[1] = (EditText) findViewById(R.id.et_personInfo_NickName);
		liEdit[2] = (EditText) findViewById(R.id.et_personInfo_phoneNum);
		liEdit[3] = (EditText) findViewById(R.id.et_personInfo_money);
		liEdit[4] = (EditText) findViewById(R.id.et_personInfo_goodTime);
		setEdit(false);
		// edit=(Button) findViewById(R.id.bt_PersonInfo_Edit);
		// edit.
		// ImageBank = (ImageView) findViewById(R.id.iv_PersonInfo_bank);
		// ImageBank.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View arg0) {
		// // TODO Auto-generated method stub
		// finish();
		// }
		// });

		// tbSet = (ToggleButton) findViewById(R.id.tb_PersonInfo_Set);
		// tbSet.setChecked(false);
		// tbSet.setOnCheckedChangeListener(new OnCheckedChangeListener() {
		//
		// @Override
		// public void onCheckedChanged(CompoundButton buttonView,
		// boolean isChecked) {
		// // TODO Auto-generated method stub
		// setEdit(isChecked);
		// }
		// });

		edit = (Button) findViewById(R.id.bt_PersonInfo_Edit);
		edit.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(PersonInfomationActivity.this,
						PersonInformationEditActivity.class);
				startActivity(intent);

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
