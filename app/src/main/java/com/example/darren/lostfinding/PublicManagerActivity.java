package com.example.darren.lostfinding;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.cyc.Globle;
import com.example.cyc.MyListView;
import com.example.cyc.QREntity;
import com.example.darren.lostfinding.net.MyClient;
import com.squareup.okhttp.Request;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PublicManagerActivity extends AppCompatActivity {
    private Button btn_out,btn_in;
    private MyListView list_ui;
    private LinearLayout outLY;
    private int sMode=1;//0在库 1借阅
    private List<QREntity> sData;
    private Gdata app;
    private MyListAdapter ad;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.public_manager);
        app = (Gdata)getApplication();
        initView();
        initControl();
    }

    private void initView(){
        btn_out=(Button) findViewById(R.id.item_out);
        btn_in=(Button) findViewById(R.id.item_in);
        list_ui=(MyListView) findViewById(R.id.lv_MyQR_items);
        outLY=(LinearLayout) findViewById(R.id.out_title);
    }

    private void freshList(){
        //0在库 1借阅
        for(int i=0;i<sData.size();){
            if(sMode==1){
                if(sData.get(i).getStatu()!=3){
                    sData.remove(i);
                }else{
                    i++;
                }
            }else {
                if(sData.get(i).getStatu()!=2){
                    sData.remove(i);
                }else{
                    i++;
                }
            }
        }
        ad=new MyListAdapter(getApplicationContext(),R.layout.item_public,sData,sMode);
        list_ui.setAdapter(ad);
        list_ui.onRefreshComplete();
    }
   private void initControl(){

       btn_out.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               sMode = 1;
               btn_out.setBackgroundColor(Color.GRAY);
               btn_in.setBackgroundColor(Color.WHITE);
               outLY.setVisibility(View.VISIBLE);
               Map<String, String> obj = new HashMap<String, String>();
               obj.put("username", app.getName());
               MyClient.postAsyn(app.statu_add, new MyClient.ResultCallback<String>() {
                           @Override
                           public void onError(Request request, Exception e) {
                               e.printStackTrace();
                           }
                           @Override
                           public String onResponse(String u) {
                               sData = QREntity.getQRfromString(u);
                               freshList();
                               return u;
                           }
               }, obj);
           }
       });

       btn_in.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               sMode=0;
               btn_out.setBackgroundColor(Color.WHITE);
               btn_in.setBackgroundColor(Color.GRAY);
               Map<String, String> obj = new HashMap<String, String>();
               obj.put("username", app.getName());
               outLY.setVisibility(View.GONE);
               MyClient.postAsyn(app.statu_add, new MyClient.ResultCallback<String>() {
                  @Override
                  public void onError(Request request, Exception e) {
                      e.printStackTrace();
                  }
                  @Override
                  public String onResponse(String u) {
                      sData = QREntity.getQRfromString(u);
                      freshList();
                      return u;
                  }
               }, obj);
           }
       });
   }

    class MyListAdapter extends ArrayAdapter<QREntity> {
        private int mResourceId;
        private int viewMode;
        public MyListAdapter(Context context,int textViewResourceId, List<QREntity> data,int mode){
            super(context, textViewResourceId, data);
            this.mResourceId = textViewResourceId;
            this.viewMode=mode;//0在库 1借阅
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            TextView T1,T2,T3,T4,T5;
            QREntity d = getItem(position);
            View view = getLayoutInflater().inflate(mResourceId, null);
            T1 = (TextView) view.findViewById(R.id.item_id);
            T2 = (TextView) view.findViewById(R.id.item_name);
            T3 = (TextView) view.findViewById(R.id.item_owner);
            T4 = (TextView) view.findViewById(R.id.item_time_1);
            T5 = (TextView) view.findViewById(R.id.item_time_2);
            if(viewMode==0){
                T1.setText(d.getId());
                T2.setText(d.getName());
                T3.setText("在库");
                T4.setVisibility(View.GONE);
                T5.setVisibility(View.GONE);

            }else{
                T1.setText(d.getId());
                T2.setText(d.getName());
                T3.setText(d.getOwner());
                T4.setText(d.getTime().substring(0, d.getTime().indexOf(" ")));
                T5.setText(d.getTime().substring(d.getTime().indexOf(" ")+1));
            }
                //convertView.setTag(TV);
            return view;
        }
    }
}
