package com.example.darren.lostfinding;

import com.example.homeApplication.HomeApplication;

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
			btnPlus2, btnPlus3;
	private TextView tvNum,tvPopCancle,tvPopOk;
	private int[] itemNum = { 0, 0, 0 };
	private PopupWindow popup;
	private EditText etNum1, etNum2, etNum3;
	private FrameLayout frameCart;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mall);

		initView();
		initControl();
		initPopupWindow();
	}

	private void initPopupWindow() {
		// TODO Auto-generated method stub
		View v = LayoutInflater.from(this).inflate(R.layout.popup_shoppingcart,
				null);
		popup = new PopupWindow(v);
		btnMinus1 = (Button) v.findViewById(R.id.btn_popShopCart_minus1);
		//btnMinus2 = (Button) v.findViewById(R.id.btn_popShopCart_minus2);
		//btnMinus3 = (Button) v.findViewById(R.id.btn_popShopCart_minus3);
		btnPlus1 = (Button) v.findViewById(R.id.btn_popShopCart_plus1);
		//btnPlus2 = (Button) v.findViewById(R.id.btn_popShopCart_plus2);
		//btnPlus3 = (Button) v.findViewById(R.id.btn_popShopCart_plus3);
		etNum1 = (EditText) v.findViewById(R.id.et_popShopCart_num);
		//etNum2 = (EditText) v.findViewById(R.id.et_popShopCart_num2);
		//etNum3 = (EditText) v.findViewById(R.id.et_popShopCart_num3);
		tvPopCancle = (TextView) v.findViewById(R.id.tv_popShopCart_cancle);
		tvPopOk = (TextView) v.findViewById(R.id.tv_popShopCart_ok);

		tvPopCancle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});

	}

	private void initControl() {

		// TODO Auto-generated method stub
		btn1.setOnClickListener(new ItemButtonListener(1));
		btn2.setOnClickListener(new ItemButtonListener(2));
		btn3.setOnClickListener(new ItemButtonListener(3));
		
		frameCart.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
			popup.showAtLocation(arg0, Gravity.CENTER, 0, 0);
			}
		});
	}

	private void initView() {
		// TODO Auto-generated method stub
		btn1 = (Button) findViewById(R.id.btn_mall_item1);
		btn2 = (Button) findViewById(R.id.btn_mall_item2);
		btn3 = (Button) findViewById(R.id.btn_mall_item3);
		tvNum = (TextView) findViewById(R.id.tv_mall_ShopcartNum);
		
		updateCart();
		frameCart=(FrameLayout) findViewById(R.id.frame_mall_shoppingCart);
		// tvNum.setText(HomeApplication.getHomeApplication().)
	}

	class ItemButtonListener implements OnClickListener {

		private int type;

		public ItemButtonListener(int type) {
			super();
			this.type = type;
		}

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			itemNum[0]++;
			/*switch (type) {
			case 1:
				itemNum[0]++;
				break;
			case 2:
				itemNum[1]++;
				break;
			case 3:
				itemNum[2]++;
				break;

			}*/
			HomeApplication.getHomeApplication().setItemNum(itemNum);
			updateCart();

			Toast.makeText(getApplicationContext(), "添加购物车成功",
					Toast.LENGTH_SHORT).show();

		}
	}

	// 更新购物车数量
	private void updateCart() {

		int sum = 0;
		itemNum = HomeApplication.getHomeApplication().getItemNum();

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
