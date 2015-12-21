package com.example.darren.lostfinding;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.cyc.ChatMsgEntity;
import com.example.cyc.Globle;
import com.example.darren.lostfinding.net.MyClient;
import com.example.darren.lostfinding.net.WebSocket;
import com.readystatesoftware.viewbadger.BadgeView;
import com.squareup.okhttp.Request;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Chat_mainActivity extends AppCompatActivity {

    private Button backButton;
    private ListView chatList;
    private Gdata app;
    private WebSocket socket= null;
    private Map<String,Objects> chatmap;
    private List<String> namelist;
    private Chat_mainActivity LA;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_main);
        app = (Gdata)getApplication();
        socket=app.getSocket();
        LA=Chat_mainActivity.this;
        initView();
        initControl();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void initView(){
        backButton=(Button)findViewById(R.id.btn_back);
        chatList=(ListView)findViewById(R.id.lv_main_main);
    }

    private void initControl(){
        namelist=new ArrayList<String>();
        chatmap = (HashMap<String, Objects>) app.getMsg("chat");
        if (chatmap != null && !chatmap.isEmpty()) {
            Iterator it=chatmap.keySet().iterator();
            String key;
            while(it.hasNext()){
                key=it.next().toString();
                namelist.add(key);
            }
        }
        LvAdapter ad = new LvAdapter(getApplicationContext(),R.layout.chat_list,namelist);
        chatList.setAdapter(ad);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    class LvAdapter extends ArrayAdapter<String>  {
        private int mResourceId;
        public LvAdapter (Context context,int textViewResourceId, List<String> data){
            super(context, textViewResourceId, data);
            this.mResourceId = textViewResourceId;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            final String d = getItem(position);
            View view = getLayoutInflater().inflate(mResourceId, null);
            final TextView TX = (TextView) view.findViewById(R.id.content);
            final ImageView TI= (ImageView) view.findViewById(R.id.pic);
            if(!Gdata.setPicLocal(d, TI)){
                Gdata.setPicNet(d, TI);
            }
            final BadgeView chatView=new BadgeView(LA,TI);
            TX.setText(d);
            if(app.getChatData().get(d)!=null){
                chatView.setText("NEW!");
                chatView.show();
            }
            TX.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent result = new Intent(Chat_mainActivity.this, ChatingActivity.class);
                    result.putExtra("DSTID", d);
                    chatView.setVisibility(View.GONE);
                    //chatView.show();
                    startActivity(result);
                }
            });
            TI.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent result = new Intent(Chat_mainActivity.this, ChatingActivity.class);
                    result.putExtra("DSTID", d);
                    chatView.setVisibility(View.GONE);
                    //chatView.show();
                    startActivity(result);
                }
            });
            //convertView.setTag(TV);
            return view;
        }
    }

}
