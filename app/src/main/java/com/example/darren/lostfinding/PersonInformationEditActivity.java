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

public class PersonInformationEditActivity extends Activity {

	private EditText[] liEdit;
	private ImageView ivSet;
	//private ToggleButton tbSet;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.person_information_edit);
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
		// tbSet = (ToggleButton) findViewById(R.id.tb_PersonInfoEdit_Set);
		// tbSet.setChecked(false);
		// setEdit(false);
		// tbSet.setOnCheckedChangeListener(new OnCheckedChangeListener() {
		//
		// @Override
		// public void onCheckedChanged(CompoundButton buttonView,
		// boolean isChecked) {
		// // TODO Auto-generated method stub
		// setEdit(isChecked);
		// }
		// });
	}

	
	//请马乐补充保存函数
	private void save(){
		
	}
	//请马乐补充充值函数
	private void charging(){
		
	}

}
