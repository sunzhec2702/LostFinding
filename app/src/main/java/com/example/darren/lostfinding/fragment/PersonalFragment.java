package com.example.darren.lostfinding.fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cyc.CircleImageView;
import com.example.darren.lostfinding.Gdata;
import com.example.darren.lostfinding.LoginActivity;
import com.example.darren.lostfinding.MainActivity;
import com.example.darren.lostfinding.PersonInformationEditActivity;
import com.example.darren.lostfinding.R;
import com.example.darren.lostfinding.net.MyClient;
import com.squareup.okhttp.Request;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by cyclONE on 2015/12/16.
 */
public class PersonalFragment extends Fragment implements View.OnClickListener{
    private static final int REQUEST_CODE = 100;
    public static final int RESULT_OK  = -1;
    View view;
    PersonalFragment LA;
    TextView ln,qr,name;
    CircleImageView ImageP;
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
        view=inflater.inflate(R.layout.person_infomation, container, false);
        LA=this;
        init();
        return view;

    }
    void init(){
        name=(TextView)view.findViewById(R.id.tv_personInfo_UserName);
        ln=(TextView)view.findViewById(R.id.lnnum);
        qr=(TextView)view.findViewById(R.id.qrnum);
        MyClient.getAsyn(Gdata.pinfo_add + Gdata.getName(), new MyClient.ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {
                e.printStackTrace();
            }

            @Override
            public String onResponse(String u) {
                Message msg = new Message();
                msg.what = 1;
                msg.obj = u;
                hUI.sendMessage(msg);
                return u;
            }
        });

        ImageP=(CircleImageView) view.findViewById(R.id.iv_personInfo_UserIcon);
        if(!Gdata.setPicLocal(Gdata.getName(),ImageP)){
            Gdata.setPicNet(Gdata.getName(),ImageP);
        }
        Button bout=(Button)view.findViewById(R.id.logout);
        bout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)LA.getActivity()).getGdata().delLog("login");
                ((MainActivity)LA.getActivity()).getGdata().delLog("chat");
                ((MainActivity)LA.getActivity()).getGdata().getSocket().close();
                ((MainActivity)LA.getActivity()).getGdata().reset();
                Intent intent = new Intent(LA.getActivity(),
                        LoginActivity.class);
                startActivity(intent);
                LA.getActivity().finish();
            }
        });
        ImageP.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                Intent innerIntent = new Intent(Intent.ACTION_GET_CONTENT); // "android.intent.action.GET_CONTENT"
                innerIntent.setType("image/*");
                Intent wrapperIntent = Intent.createChooser(innerIntent,
                        "选择二维码图片");
                startActivityForResult(wrapperIntent, REQUEST_CODE);
            }
        });
        view.findViewById(R.id.ll_count).setOnClickListener(this);
        view.findViewById(R.id.ll_share).setOnClickListener(this);
        view.findViewById(R.id.ll_rating).setOnClickListener(this);
    }
    final Handler hUI = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                try {
                    JSONObject obj = null;
                    obj = new JSONObject((String)msg.obj);
                    ln.setText(obj.getString("LNPoint"));
                    qr.setText(obj.getString("NumQr"));
                    name.setText(obj.getString(Gdata.getName()));
                    /*liEdit[0].setText(app.getName());
                    liEdit[2].setText(obj.getString("cell"));
                    liEdit[3].setText(obj.getString("gold"));
                    liEdit[1].setText(obj.getString("nickname"));
                    liEdit[4].setText("0");
                    liEdit[5].setText(obj.getString("address"));
                    setEdit(false);*/
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE:
                    // 获取选中图片的路径
                    Bitmap img = null;
                    try {
                        img = MediaStore.Images.Media.getBitmap(LA.getActivity().getContentResolver(),intent.getData());
                        ImageP.setImageBitmap(img);
                        ByteArrayOutputStream os = new ByteArrayOutputStream();
                        img.compress(Bitmap.CompressFormat.PNG, 100, os);
                        FileOutputStream outputStream = LA.getActivity().openFileOutput(Gdata.getName() + ".pic", Context.MODE_PRIVATE);
                        outputStream.write(os.toByteArray());
                        outputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    File fl =new File( LA.getActivity().getFilesDir()+"/"+Gdata.getName()+".pic");
                    try {
                        MyClient.postAsyn(Gdata.upload_add, new MyClient.ResultCallback<String>() {
                            @Override
                            public void onError(Request request, Exception e) {
                                e.printStackTrace();
                            }
                            @Override
                            public String onResponse(String u) {
                                return u;
                            }
                        }, fl, Gdata.getName()+".PNG");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Gdata.getdb().updatePic(Gdata.getName(),0);
                    break;
            }
        }
    }

    @Override
    public void onClick(View  v){
        Toast.makeText(LA.getActivity().getApplicationContext(), "功能开发中...敬请期待",
                Toast.LENGTH_SHORT).show();
    }
}
