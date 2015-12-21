package com.example.darren.lostfinding;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Message;
import android.widget.ImageView;

import com.example.cyc.ChatMsgEntity;
import com.example.cyc.Globle;
import com.example.cyc.LDB;
import com.example.darren.lostfinding.net.MyClient;
import com.example.darren.lostfinding.net.WebSocket;
import com.squareup.okhttp.Request;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by cyclONE on 2015/10/30.
 */
public class Gdata extends Application{
    private MyClient cyc;
    private static LDB whose_db;
    private WebSocket ws=null;
    private ArrayList<String> pri_add=new ArrayList<String>();
    private ArrayList<String> pub_add=new ArrayList<String>();
    public static String ws_add,manage_add,main_add,regist_add,login_add,statu_add,search_add,vis_add,pinfo_add
            ,pinfo_add2,market_add,top_add,upload_add;
    public static String name;
    private static boolean PUB=false;
    private int[] itemNum = { 0, 0, 0 };
    public static LDB getdb(){
        return whose_db;
    }
    private void init_add(){
        //websocket
        pri_add.add("ws://192.168.0.88:8080/web_whose/websocket/");
        pub_add.add("ws://www.shuide.cc:8112/web_whose/websocket/");
        //管理
        pri_add.add("http://192.168.0.88:8080/web_whose/manage.do");
        pub_add.add("http://www.shuide.cc:8112/web_whose/manage.do");
        //app_main
        pri_add.add("http://192.168.0.88:8080/web_whose/app/statu?id=");
        pub_add.add("http://www.shuide.cc:8112/web_whose/app/statu?id=");
        //注册
        pri_add.add("http://192.168.0.88:8080/web_whose/register.do");
        pub_add.add("http://www.shuide.cc:8112/web_whose/register.do");

        pri_add.add("http://192.168.0.88:8080/web_whose/login.do");
        pub_add.add("http://www.shuide.cc:8112/web_whose/login.do");

        pri_add.add("http://192.168.0.88:8080/web_whose/app/statu");
        pub_add.add("http://www.shuide.cc:8112/web_whose/app/statu");

        pri_add.add("http://192.168.0.88:8080/web_whose/search?ID=");
        pub_add.add("http://www.shuide.cc:8112/web_whose/search?ID=");

        pri_add.add("http://192.168.0.88:8080/web_whose/vischange.do");
        pub_add.add("http://www.shuide.cc:8112/web_whose/vischange.do");

        pri_add.add("http://192.168.0.88:8080/web_whose/psinfo?name=");
        pub_add.add("http://www.shuide.cc:8112/web_whose/psinfo?name=");

        pri_add.add("http://192.168.0.88:8080/web_whose/market.do");
        pub_add.add("http://www.shuide.cc:8112/web_whose/market.do");

        pri_add.add("http://192.168.0.88:8080/web_whose/GetTopList.do");
        pub_add.add("http://www.shuide.cc:8112/web_whose/GetTopList.do");

        pri_add.add("http://192.168.0.88:8080/web_whose/uploadpic");
        pub_add.add("http://www.shuide.cc:8112/web_whose/uploadpic");

        ws_add= Globle.DEBUG?pri_add.get(0):pub_add.get(0);
        manage_add= Globle.DEBUG?pri_add.get(1):pub_add.get(1);
        main_add= Globle.DEBUG?pri_add.get(2):pub_add.get(2);
        regist_add= Globle.DEBUG?pri_add.get(3):pub_add.get(3);
        login_add= Globle.DEBUG?pri_add.get(4):pub_add.get(4);
        statu_add= Globle.DEBUG?pri_add.get(5):pub_add.get(5);
        search_add= Globle.DEBUG?pri_add.get(6):pub_add.get(6);
        vis_add= Globle.DEBUG?pri_add.get(7):pub_add.get(7);
        pinfo_add= Globle.DEBUG?pri_add.get(8):pub_add.get(8);
        pinfo_add2= pinfo_add.substring(0,pinfo_add.length()-6);
        market_add= Globle.DEBUG?pri_add.get(9):pub_add.get(9);
        top_add= Globle.DEBUG?pri_add.get(10):pub_add.get(10);
        upload_add= Globle.DEBUG?pri_add.get(11):pub_add.get(11);
    }
    private HashMap<String ,ArrayList<ChatMsgEntity>> ChatData;

    public void setName(String n){
        name=n;
    }
    public static String getName(){
        return name;
    }
    public static void setPUB(boolean n){
        PUB=n;
    }
    public static boolean getPUB(){
        return PUB;
    }
    public WebSocket getSocket(){
        if (this.ws==null){
            try {
                ws = new WebSocket(new URI(ws_add + name));
                ws.setAPP(this);
                ws.connectBlocking();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return ws;
    }

    @Override
    public void onCreate(){
        super.onCreate();
        cyc.getInstance();
        whose_db=new LDB(this);
        ChatData=new HashMap<String ,ArrayList<ChatMsgEntity>>();
        init_add();
    }

    public void reset(){
        cyc=null;
        cyc.getInstance();
        ws=null;
        ChatData=new HashMap<String ,ArrayList<ChatMsgEntity>>();
    }
    public HashMap<String ,ArrayList<ChatMsgEntity>> getChatData(){
        return ChatData;
    }

    public static void setPicNet(final String name,final ImageView iv) {
        MyClient.getAsyn(Gdata.upload_add + "?user=" + name, new MyClient.ResultCallback<byte[]>() {
            @Override
            public void onError(Request request, Exception e) {
                e.printStackTrace();
            }
            @Override
            public String onResponse(byte[] u) {
                if (u.length > 30) {
                    Bitmap bm = BitmapFactory.decodeByteArray(u, 0, u.length);
                    ByteArrayOutputStream os = new ByteArrayOutputStream();
                    bm.compress(Bitmap.CompressFormat.PNG, 100, os);
                    try {
                        String s1=Globle.APPDIR;
                        File file = new File(s1);
                        if (!file.exists()) {
                            file.mkdir();
                        }
                        file=new File(s1+name+".pic");
                        FileOutputStream outputStream = new FileOutputStream(file);
                        outputStream.write(os.toByteArray());
                        outputStream.close();
                        getdb().updatePic(name,0);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    iv.setImageBitmap(bm);
                }else {
                    iv.setImageResource(R.drawable.uname_icon);
                }
                return null;
            }
        });
    }

    public static boolean setPicLocal(final String name,final ImageView iv) {
        int Iview=getdb().getPic(name);
        if(Iview!=-1){
            try {
                FileInputStream stream = new FileInputStream(Globle.APPDIR + name + ".pic");
                ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int len = 0;
                while ((len = stream.read(buffer)) > 0) {
                    byteArray.write(buffer, 0, len);
                };
                Bitmap bm= BitmapFactory.decodeByteArray(byteArray.toByteArray(), 0, byteArray.toByteArray().length);
                iv.setImageBitmap(bm);
                return true;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
    public boolean saveMsg(String fileName, Map<String, Object> map) {
        boolean flag = false;
        // 一般Mode都使用private,比较安全
        SharedPreferences preferences = getSharedPreferences(fileName,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        // Map类提供了一个称为entrySet()的方法，这个方法返回一个Map.Entry实例化后的对象集。
        // 接着，Map.Entry类提供了一个getKey()方法和一个getValue()方法，
        // 因此，上面的代码可以被组织得更符合逻辑
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String key = entry.getKey();
            Object object = entry.getValue();
            // 根据值得不同类型，添加
            if (object instanceof Boolean) {
                Boolean new_name = (Boolean) object;
                editor.putBoolean(key, new_name);
            } else if (object instanceof Integer) {
                Integer integer = (Integer) object;
                editor.putInt(key, integer);
            } else if (object instanceof Float) {
                Float f = (Float) object;
                editor.putFloat(key, f);
            } else if (object instanceof Long) {
                Long l = (Long) object;
                editor.putLong(key, l);
            } else if (object instanceof String) {
                String s = (String) object;
                editor.putString(key, s);
            }
        }
        flag = editor.commit();
        return flag;
    }

    // 读取数据
    public Map<String, ?> getMsg(String fileName) {
        Map<String, ?> map = null;
        // 读取数据用不到edit
        SharedPreferences preferences = getSharedPreferences(fileName,
                Context.MODE_APPEND);
        //Context.MODE_APPEND可以对已存在的值进行修改
        map = preferences.getAll();
        return map;
    }

    public void delLog(String name) {
        Map<String, ?> map = null;
        // 读取数据用不到edit
        SharedPreferences preferences = getSharedPreferences(name,
                Context.MODE_APPEND);
        //Context.MODE_APPEND可以对已存在的值进行修改
        preferences.edit().clear().commit();
    }

    public void delLog(String name,String Key) {
        Map<String, ?> map = null;
        // 读取数据用不到edit
        SharedPreferences preferences = getSharedPreferences(name,
                Context.MODE_APPEND);
        //Context.MODE_APPEND可以对已存在的值进行修改
        preferences.edit().remove(Key).commit();
    }
}
