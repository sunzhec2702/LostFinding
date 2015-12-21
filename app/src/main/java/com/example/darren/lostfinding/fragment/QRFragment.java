package com.example.darren.lostfinding.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.adapter.QR_Item_Adapter;
import com.example.cyc.MyListView;
import com.example.cyc.QREntity;
import com.example.darren.lostfinding.Gdata;
import com.example.darren.lostfinding.My_QRdetail_Activity;
import com.example.darren.lostfinding.QRShowActivity;
import com.example.darren.lostfinding.R;
import com.example.darren.lostfinding.net.MyClient;
import com.squareup.okhttp.Request;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by cyclONE on 2015/12/16.
 */
public class QRFragment extends Fragment {
    MyListView lvItem ;
    List<QREntity> qrInfo;
    QRFragment LA;
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
        view=inflater.inflate(R.layout.qr_fragment, container, false);
        init();
        return view;

    }
    void init(){
        LA=this;
        qrInfo=new ArrayList<QREntity>();
        lvItem = (MyListView)view.findViewById(R.id.lv_MyQR_items);
        getEntity();
        lvItem.setonRefreshListener(new MyListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getEntity();
            }
        });

    }
    private void getEntity() {
        Map<String, String> obj = new HashMap<String, String>();
        obj.put("username", Gdata.getName());
        MyClient.postAsyn(Gdata.statu_add, new MyClient.ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {
                e.printStackTrace();
            }

            @Override
            public String onResponse(String u) {
                if (u.length() > 0) {
                    qrInfo.clear();
                    qrInfo = QREntity.getQRfromString(u);
                    QR_Item_Adapter adapter = new QR_Item_Adapter(LA.getActivity(), qrInfo, Gdata.getPUB());
                    adapter.setHandler(hUI);
                    lvItem.setAdapter(adapter);
                    lvItem.onRefreshComplete();
                }
                return u;
            }
        }, obj);
    }
    final Handler hUI = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 666) {
                String rr=Gdata.search_add+(String) msg.obj+"&app=1.0.0";
                MyClient.getAsyn(rr, new MyClient.ResultCallback<String>() {
                    @Override
                    public void onError(Request request, Exception e) {
                        e.printStackTrace();
                    }
                    @Override
                    public String onResponse(String u) {
                        Intent result = new Intent(LA.getActivity(), My_QRdetail_Activity.class);
                        result.putExtra("result", u);
                        startActivity(result);
                        return u;
                    }
                });
            }else if(msg.what == 888){//展示图片
                Intent result = new Intent(getActivity(),
                        QRShowActivity.class);
                result.putExtra("DSTID", Gdata.search_add+(String) msg.obj);
                startActivity(result);
            }
        }
    };
}