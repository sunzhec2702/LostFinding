package com.example.adapter;

import java.util.ArrayList;
import java.util.List;

import android.os.Handler;

import android.content.Context;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.example.cyc.QREntity;
import com.example.darren.lostfinding.R;


public class QR_Item_Adapter extends BaseAdapter {
    private boolean PUB;
    private Context context;
    private List<QREntity> items = new ArrayList<QREntity>();
    Handler hUI;

    public void setHandler(Handler h) {
        hUI = h;
    }

    public QR_Item_Adapter(Context context, List<QREntity> items, boolean b) {
        super();
        PUB = b;
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
            reHoder.tv2 = (TextView) convertView
                    .findViewById(R.id.tv_itemMain_content);
            reHoder.ima = (ImageView) convertView.findViewById(R.id.pic);
            reHoder.tv1 = (TextView) convertView
                    .findViewById(R.id.tv_itemMain_state);
            reHoder.tv3 = (TextView) convertView
                    .findViewById(R.id.tv_vis);
            //reHoder.VIS = (Switch) convertView.findViewById(R.id.vis_displayPhoneNumber);
            reHoder.TH = (LinearLayout) convertView.findViewById(R.id.touch);
            // 设置控件集到convertView
            convertView.setTag(reHoder);
        } else {
            reHoder = (ReHolderView) convertView.getTag();
        }
        // 设置文本内容
        reHoder.tv1.setText(items.get(position).getId());

        switch (items.get(position).getStatu()) {
            case 1:
                reHoder.tv2.setText("尚未绑定物品");
                break;
            case 2:
                if (PUB) {
                    reHoder.tv2.setText("物品在库中");
                } else {
                    reHoder.tv2.setText("物品状态正常");
                }
                break;
            case 3:
                if (PUB) {
                    reHoder.tv2.setText("物品借出");
                } else {
                    reHoder.tv2.setText("被别人捡到");
                }
                break;
        }
        //reHoder.VIS.setChecked(items.get(position).getVisible() == 1 ? true : false);
        reHoder.tv3.setText(items.get(position).getVisible() == 1 ? "是" : "否");
        final String rr = items.get(position).getId();
        reHoder.ima.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Message message = new Message();
                message.what = 888;
                message.obj = rr;
                hUI.sendMessage(message);
            }
        });
        /*reHoder.VIS.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                // TODO Auto-generated method stub
                Message message = new Message();
                message.what = 777;
                message.obj = new Change(rr, isChecked);
                hUI.sendMessage(message);
            }
        });*/

        reHoder.TH.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Message message = new Message();
                message.what = 666;
                message.obj = rr;
                hUI.sendMessage(message);
            }
        });
        return convertView;
    }

    class ReHolderView {
        public TextView tv1, tv2,tv3;
        //public Switch VIS;
        public LinearLayout TH;
        public ImageView ima;
    }

    public class Change {
        public String id;
        public boolean vis;
        Change(String s, boolean b) {
            id = s;
            vis = b;
        }

        public String toString() {
            return id + "///" + String.valueOf(vis ? 1 : 0);
        }
    }
}
