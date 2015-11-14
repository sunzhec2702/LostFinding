package com.example.darren.lostfinding;

/**
 * @author vivian
 * 
 */


import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class AutoTelephoneActivity extends Activity {

	private EditText mobile_number;
	private Button calling;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.auto_telephone);
		initView();
		initMethod();
	}


	private void initView() {
		// TODO Auto-generated method stub
		calling = (Button)findViewById(R.id.bt_autotelephone_calling);
		mobile_number = (EditText)findViewById(R.id.et_autotelephone_number);
	}

	private void initMethod() {
		// TODO Auto-generated method stub
		calling.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String mobileText = mobile_number.getText().toString();  
	            Intent aintent = new Intent();
	            aintent.setAction("android.intent.action.CALL");  
	            aintent.setData(Uri.parse("tel:"+mobileText));  
	            startActivity(aintent);//方法内部会自动添加类别,android.intent.category.DEFAULT  
			}
		});
		
	}	
	
}
