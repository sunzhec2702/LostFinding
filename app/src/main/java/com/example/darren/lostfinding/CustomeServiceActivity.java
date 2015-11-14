package com.example.darren.lostfinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.adapter.LvAdapterReminder;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

public class CustomeServiceActivity extends Activity {
	private ListView lvReminder;
	private List<Map<String, String>> item = new ArrayList<Map<String, String>>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.custome_service);
		initView();
	}

	private void initView() {
		// TODO Auto-generated method stub
		lvReminder = (ListView) findViewById(R.id.lv_custome_item);
		for (int i = 0; i < 3; i++) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("名称", "物品1名称");
			map.put("内容", "物品1内容");
			map.put("其他", "物品3其他内容");
			item.add(map);
		}
		LvAdapterReminder adapter = new LvAdapterReminder(item,
				getApplicationContext());
		lvReminder.setAdapter(adapter);
	}

}
