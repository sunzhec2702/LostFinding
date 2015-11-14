package com.example.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.darren.lostfinding.R;

public class LvAdapterReminder extends BaseAdapter {

	private List<Map<String, String>> item = new ArrayList<Map<String, String>>();
	private Context context;

	public LvAdapterReminder() {
		super();
		// TODO Auto-generated constructor stub
		initItem();
	}
	
	public LvAdapterReminder(List<Map<String, String>> item, Context context) {
		super();
		this.item = item;
		this.context = context;
		initItem();
	}

	private void initItem() {
		// TODO Auto-generated method stub
		for (int i = 0; i < 3; i++) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("名称", "物品1名称");
			map.put("内容", "物品1内容");
			map.put("其他", "物品3其他内容");
			item.add(map);
		}

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return item.size();

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
			convertView= LayoutInflater.from(context).inflate(
					R.layout.item_reminder, null);
			reHoder.tv = (TextView) convertView.findViewById(R.id.tv_itemReminder);
			// 设置控件集到convertView
			convertView.setTag(reHoder);
		} else {
			reHoder = (ReHolderView) convertView.getTag();
		}
		//设置文本内容
		reHoder.tv.setText(item.get(position).get("名称"));
		//设置文本监听
		reHoder.tv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//在此添加跳转
			}
		});
		return convertView;
	}

	class ReHolderView {

		public TextView tv;

	}

}
