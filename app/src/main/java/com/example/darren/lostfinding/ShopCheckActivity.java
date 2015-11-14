package com.example.darren.lostfinding;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cyc.Globle;
import com.example.darren.lostfinding.net.MyClient;
import com.squareup.okhttp.Request;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class ShopCheckActivity extends AppCompatActivity {

    private Button btn1, btn2, btn3, btnMinus1, btnMinus2, btnMinus3, btnPlus1,
            btnPlus2, btnPlus3;
    private TextView tvNum,tvPopCancle,tvPopOk;
    private int[] itemNum = { 0, 0, 0 };
    private PopupWindow popup;
    private EditText etNum1, etNum2, etNum3;
    private FrameLayout frameCart;
    private Gdata app;
    private String PRI_merchantUrl="http://192.168.0.88:8080/WHOS/market.do";
    private String PUB_merchantUrl="http://www.shuide.cc:8112/WHOS/market.do";
    private String merchantUrl= Globle.DEBUG?PRI_merchantUrl:PUB_merchantUrl;

    private void initView() {
        // TODO Auto-generated method stub
        btnMinus1 = (Button) findViewById(R.id.btn_popShopCart_minus1);
        btnMinus2 = (Button) findViewById(R.id.btn_popShopCart_minus2);
        btnMinus3 = (Button) findViewById(R.id.btn_popShopCart_minus3);
        btnPlus1 = (Button) findViewById(R.id.btn_popShopCart_plus1);
        btnPlus2 = (Button) findViewById(R.id.btn_popShopCart_plus2);
        btnPlus3 = (Button) findViewById(R.id.btn_popShopCart_plus3);
        etNum1 = (EditText) findViewById(R.id.et_popShopCart_num);
        etNum2 = (EditText) findViewById(R.id.et_popShopCart_num2);
        etNum3 = (EditText) findViewById(R.id.et_popShopCart_num3);
        tvPopCancle = (TextView) findViewById(R.id.tv_popShopCart_cancle);
        tvPopOk = (TextView) findViewById(R.id.tv_popShopCart_ok);
    }

    private void initControl() {
        // TODO Auto-generated method stub
        btnMinus1.setOnClickListener(new MinClick(1));
        btnMinus2.setOnClickListener(new MinClick(2));
        btnMinus3.setOnClickListener(new MinClick(3));
        btnPlus1.setOnClickListener(new AddClick(1));
        btnPlus2.setOnClickListener(new AddClick(2));
        btnPlus3.setOnClickListener(new AddClick(3));

        tvPopCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                finish();
            }
        });
        tvPopOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Map<String, String> obj = new HashMap<String, String>();
                obj.put("user", app.getName());
                obj.put("num1", String.valueOf(itemNum[0]));
                obj.put("num2", String.valueOf(itemNum[1]));
                obj.put("num3", String.valueOf(itemNum[2]));
                app.getClient().postAsyn(merchantUrl, new MyClient.ResultCallback<String>() {
                    @Override
                    public void onError(Request request, Exception e) {
                        e.printStackTrace();
                    }

                    @Override
                    public String onResponse(String u) {
                        if (u.indexOf("欢迎") != -1) {
                            Toast.makeText(getApplicationContext(), "交易成功",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "添加购物车成功",
                                    Toast.LENGTH_SHORT).show();
                        }
                        return u;
                    }
                }, obj);
            }
        });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_shoppingcart);
        app = (Gdata)getApplication();
        initView();
        initControl();
    }

    class AddClick implements View.OnClickListener {
        private int type;

        public AddClick(int type) {
            super();
            this.type = type;
        }
        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub
            //itemNum[0]++;

			switch (type) {
			case 1:
				itemNum[0]++;
                etNum1.setText(String.valueOf(itemNum[0]));
				break;
			case 2:
				itemNum[1]++;
                etNum2.setText(String.valueOf(itemNum[1]));
				break;
			case 3:
				itemNum[2]++;
                etNum3.setText(String.valueOf(itemNum[2]));
				break;

			}
            app.setItemNum(itemNum);

        }
    }
    class MinClick implements View.OnClickListener {
        private int type;

        public MinClick(int type) {
            super();
            this.type = type;
        }
        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub
            //itemNum[0]++;
            switch (type) {
                case 1:
                    if(itemNum[0]>0){
                        itemNum[0]--;
                        etNum1.setText(String.valueOf(itemNum[0]));
                    }
                    break;
                case 2:
                    if(itemNum[1]>0){
                        itemNum[1]--;
                        etNum2.setText(String.valueOf(itemNum[1]));
                    }
                    break;
                case 3:
                    if(itemNum[2]>0){
                        itemNum[2]--;
                        etNum3.setText(String.valueOf(itemNum[2]));
                    }
                    break;
            }
            app.setItemNum(itemNum);
        }
    }
}
