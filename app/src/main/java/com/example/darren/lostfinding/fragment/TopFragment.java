package com.example.darren.lostfinding.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.example.adapter.TopAdapter;
import com.example.cyc.TopEntity;
import com.example.darren.lostfinding.Gdata;
import com.example.darren.lostfinding.R;
import com.example.darren.lostfinding.net.MyClient;
import com.squareup.okhttp.Request;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by cyclONE on 2015/12/16.
 */
public class TopFragment extends Fragment {
    ListView lvItem;
    TextView mTextView; // 显示的内容
    TopFragment LA;
    View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // super.onCreateView(inflater, container, savedInstanceState);
        // Inflate the layout for this fragment
        LA=this;
        view=inflater.inflate(R.layout.top_fragment, container, false);
        initTop();
        return view;
    }

    void initTop(){
        lvItem = (ListView) view.findViewById(R.id.lv_main_main);
        Map<String, String> obj = new HashMap<String, String>();
        obj.put("user", Gdata.getName());
        MyClient.postAsyn(Gdata.top_add, new MyClient.ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {
                e.printStackTrace();
            }

            @Override
            public String onResponse(String u) {
                if (u.length() > 0) {
                    ArrayList<TopEntity> data = TopEntity.build(u);
                    lvItem.setAdapter(new TopAdapter(LA.getActivity(), R.layout.item_lv_goodperson, data));
                }
                return u;
            }
        }, obj);
    }
}
