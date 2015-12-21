package com.example.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.cyc.Globle;
import com.example.cyc.QREntity;
import com.example.cyc.TopEntity;
import com.example.darren.lostfinding.Gdata;
import com.example.darren.lostfinding.R;
import com.example.darren.lostfinding.net.MyClient;
import com.squareup.okhttp.Request;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by cyclONE on 2015/12/15.
 */
public class TopAdapter extends ArrayAdapter<TopEntity> {
    private int mResourceId;
    private Context context;
    private Handler reciever;

    public TopAdapter(Context c, int textViewResourceId, List<TopEntity> data) {
        super(c, textViewResourceId, data);
        this.mResourceId = textViewResourceId;
        context = c;
    }

    public TopAdapter setH(Handler h) {
        reciever = h;
        return this;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        TextView T1, T2, T3;
        ImageView I2;
        LinearLayout LL;
        final TopEntity d = getItem(position);
        View view = LayoutInflater.from(context).inflate(R.layout.item_lv_goodperson, null);
        final ImageView I1 = (ImageView) view
                .findViewById(R.id.iv_ItemLvGoodPerson_icon);
        I2 = (ImageView) view
                .findViewById(R.id.iv_ItemLvGoodPerson_star);
        T1 = (TextView) view
                .findViewById(R.id.tv_ItemLvGoodPerson_nickName);
        T2 = (TextView) view
                .findViewById(R.id.tv_ItemLvGoodPerson_num);
        T3 = (TextView) view
                .findViewById(R.id.tv_ItemLvGoodPerson_startNum);
        final Handler pichandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == 1) {
                    byte[] s = (byte[]) msg.obj;
                    Bitmap bm = BitmapFactory.decodeByteArray(s, 0, s.length);
                    ByteArrayOutputStream os = new ByteArrayOutputStream();
                    bm.compress(Bitmap.CompressFormat.PNG, 100, os);
                    try {
                        FileOutputStream outputStream = new FileOutputStream(Globle.APPDIR + d.getName() + ".pic");
                        outputStream.write(os.toByteArray());
                        outputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    I1.setImageBitmap(bm);
                    Gdata.getdb().updatePic(d.getName(), 0);
                }
            }
        };
        T1.setText(d.getName());
        T2.setText("Top " + d.getOrder());
        T3.setText(d.getPoint() + "\n 点赞数");
        I2.setImageResource(R.drawable.star);
        if(!Gdata.setPicLocal(d.getName(),I1)){
            Gdata.setPicNet(d.getName(),I1);
        }
        /*LL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message message = new Message();
                message.what = 1;
                message.obj=d;
                reciever.sendMessage(message);
            }
        });*/
        return view;
    }
}
