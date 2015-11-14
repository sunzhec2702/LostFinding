package com.example.darren.lostfinding;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.internal.widget.AdapterViewCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.cyc.ChatingActivity;
import com.example.darren.lostfinding.net.WebSocket;
import com.example.darren.scanner.CaptureActivity;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.os.Handler;


public class ChatActivity extends AppCompatActivity {
    private Button backButton;
    private ListView chatList;
    private Gdata app;
    private WebSocket socket= null;
    private Map<String,List<String>> chatmap;
    private List<String> namelist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_list);
        app = (Gdata)getApplication();
        socket=app.getSocket();


    }

    private void initView(){
        backButton=(Button)findViewById(R.id.btn_back);
        chatList=(ListView)findViewById(R.id.listview);
    }

    private void initControl(){
        namelist=new ArrayList<String>();
        chatmap = (HashMap<String, List<String>>) app.getMsg("chat");
        if (chatmap != null && !chatmap.isEmpty()) {
            Iterator it=chatmap.keySet().iterator();
            String key;
            ArrayList<String> value;
            while(it.hasNext()){
                key=it.next().toString();
                value=(ArrayList<String>)chatmap.get(key);
                namelist.add(key);
            }
        }

        chatList.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, namelist));
        chatList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                         long arg3) {
                Intent result = new Intent(ChatActivity.this, ChatingActivity.class);
                result.putExtra("DSTID", namelist.get(arg2));
                startActivity(result);
            }
        });

    }

}
