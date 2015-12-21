package com.example.darren.lostfinding.fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.cyc.ChatMsgEntity;
import com.example.darren.lostfinding.ChatingActivity;
import com.example.darren.lostfinding.Gdata;
import com.example.darren.lostfinding.MainActivity;
import com.example.darren.lostfinding.R;
import com.readystatesoftware.viewbadger.BadgeView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by cyclONE on 2015/12/16.
 */
public class MessageFragment extends Fragment {
    ListView chatList;
    ArrayList<String> namelist;
    Map<String,Objects> chatmap;
    View view;
    MessageFragment LA;
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
        view=inflater.inflate(R.layout.top_fragment, container, false);
        LA=this;
        init();
        return view;

    }
    void init(){
        chatList=(ListView)view.findViewById(R.id.lv_main_main);
        namelist=new ArrayList<String>();
        chatmap = (HashMap<String, Objects>) ((MainActivity)getActivity()).getGdata().getMsg("chat");
        if (chatmap != null && !chatmap.isEmpty()) {
            Iterator it=chatmap.keySet().iterator();
            String key;
            while(it.hasNext()){
                key=it.next().toString();
                namelist.add(key);
            }
        }
        LvAdapter ad = new LvAdapter(LA.getActivity().getApplicationContext(),R.layout.chat_list,namelist);
        chatList.setAdapter(ad);
    }

    class LvAdapter extends ArrayAdapter<String> {
        private int mResourceId;
        public LvAdapter (Context context,int textViewResourceId, List<String> data){
            super(context, textViewResourceId, data);
            this.mResourceId = textViewResourceId;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            final String d = getItem(position);
            View view = LA.getActivity().getLayoutInflater().inflate(mResourceId, null);
            final TextView TX = (TextView) view.findViewById(R.id.name);
            final ImageView TI= (ImageView) view.findViewById(R.id.pic);
            final TextView TC= (TextView) view.findViewById(R.id.content);
            final TextView TT= (TextView) view.findViewById(R.id.time);
            final LinearLayout ll= (LinearLayout) view.findViewById(R.id.ll_chat);
            if(!Gdata.setPicLocal(d, TI)){
                Gdata.setPicNet(d, TI);
            }
            Cursor cur=Gdata.getdb().getLastMsg(d);
            if(cur!=null){
                for(cur.moveToFirst();!cur.isAfterLast();cur.moveToNext())
                {
                    int timeColumn = cur.getColumnIndex("time");
                    int textColumn = cur.getColumnIndex("content");
                    TC.setText(cur.getString(textColumn));
                    TT.setText(cur.getString(timeColumn));
                }
            }
            final BadgeView chatView=new BadgeView(LA.getActivity(),TI);
            TX.setText(d);
            if(((MainActivity)LA.getActivity()).getGdata().getChatData().get(d)!=null){
                chatView.setText("NEW!");
                chatView.show();
            }
            ll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent result = new Intent(LA.getActivity(), ChatingActivity.class);
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
