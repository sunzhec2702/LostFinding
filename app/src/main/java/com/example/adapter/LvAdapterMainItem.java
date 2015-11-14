package com.example.adapter;

import java.util.ArrayList;
import java.util.List;
import android.os.Handler;

import android.content.Context;
import android.content.Intent;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.cyc.Globle;
import com.example.cyc.QREntity;
import com.example.darren.lostfinding.AfterScanActivity;
import com.example.darren.lostfinding.Gdata;
import com.example.darren.lostfinding.MainActivity;
import com.example.darren.lostfinding.R;
import com.example.darren.lostfinding.net.MyClient;
import com.squareup.okhttp.Request;


public class LvAdapterMainItem extends BaseAdapter {
	private Context context;
	private List<QREntity> items = new ArrayList<QREntity>();
	Handler hUI;
	public void setHandler(Handler h){
		hUI=h;
	}

	public LvAdapterMainItem(Context context, List<QREntity> items) {
		super();
		this.context = context;
		this.items = items;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return items.size();

	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		// 添加控件到List
		ReHolderView reHoder = null;
		if (convertView == null) {
			reHoder = new ReHolderView();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.item_lv_main_qr, null);
			reHoder.tv1 = (TextView) convertView
					.findViewById(R.id.tv_itemMain_content);
			reHoder.tv2 = (TextView) convertView
					.findViewById(R.id.tv_itemMain_state);
			reHoder.v = convertView.findViewById(R.id.v_itemMain_state);
			// 设置控件集到convertView
			convertView.setTag(reHoder);
		} else {
			reHoder = (ReHolderView) convertView.getTag();
		}
		// 设置文本内容
		reHoder.tv1.setText(items.get(position).getId());
		reHoder.tv2.setText(items.get(position).getStatu());
		/*
		switch (position % 3) {
		case 0:
			reHoder.v.setBackgroundColor(Color.BLUE);
			break;
		case 1:
			reHoder.v.setBackgroundColor(Color.RED);
			break;
		case 2:
			reHoder.v.setBackgroundColor(Color.YELLOW);
			break;
		}
		*/

		// 设置文本监听
		final String rr=items.get(position).getId();
		reHoder.tv1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Message message = new Message();
				message.what = 666;
				message.obj=rr;
				hUI.sendMessage(message);
			}
		});
		return convertView;
	}

	class ReHolderView {
		public TextView tv1, tv2;
		public View v;
	}

}
