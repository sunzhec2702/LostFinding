package com.example.darren.lostfinding;


import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.text.Layout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

public class MallActivity extends Activity {

	private Button btn1, btn2, btn3, btnMinus1, btnMinus2, btnMinus3, btnPlus1,
			btnPlus2, btnPlus3,btnCheck;
	private TextView tvNum,tvPopCancle,tvPopOk;
	private int[] itemNum = { 0, 0, 0 };
	private PopupWindow popup;
	private EditText etNum1, etNum2, etNum3;
	private FrameLayout frameCart;
	private Gdata app;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mall);
		app = (Gdata)getApplication();
		initView();
		initControl();

	}



	private void initControl() {
		// TODO Auto-generated method stub

		btnCheck.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				intent.setClass(MallActivity.this, ShopCheckActivity.class);
				startActivity(intent);
			}
		});
	}

	private void initView() {
		// TODO Auto-generated method stub
		btn1 = (Button) findViewById(R.id.btn_mall_item1);
		btn2 = (Button) findViewById(R.id.btn_mall_item2);
		btn3 = (Button) findViewById(R.id.btn_mall_item3);
		tvNum = (TextView) findViewById(R.id.tv_mall_ShopcartNum);
		btnCheck=(Button) findViewById(R.id.btn_mall_account);
		updateCart();
		frameCart=(FrameLayout) findViewById(R.id.frame_mall_shoppingCart);
	}



	// 更新购物车数量
	private void updateCart() {

		int sum = 0;
		itemNum = app.getItemNum();

		for (int i = 0; i < itemNum.length; i++) {
			sum = sum + itemNum[i];
		}
		tvNum.setText(String.valueOf(sum));

	}
	 @Override 
     public void onPause(){
        super.onPause();
        if(popup != null) {
        	popup.dismiss(); 
        }
	 }
}
