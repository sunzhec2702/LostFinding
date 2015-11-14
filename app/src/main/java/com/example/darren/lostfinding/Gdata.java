package com.example.darren.lostfinding;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.example.cyc.ChatMsgEntity;
import com.example.cyc.Globle;
import com.example.darren.lostfinding.net.MyClient;
import com.example.darren.lostfinding.net.WebSocket;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by cyclONE on 2015/10/30.
 */
public class Gdata extends Application{
    public MyClient cyc;
    private WebSocket ws=null;
    private final String PRI_ServUrl="ws://192.168.0.88:8080/WHOS/websocket/";
    private final String PUB_ServUrl="ws://www.shuide.cc:8112/WHOS/websocket/";
    private final String ServUrl= Globle.DEBUG?PRI_ServUrl:PUB_ServUrl;
    public String name;
    public String chatName=null;
    private boolean PUB=false;
    private int[] itemNum = { 0, 0, 0 };

    private HashMap<String ,ArrayList<ChatMsgEntity>> ChatData;

    public int[] getItemNum() {
        return itemNum;
    }

    public void setItemNum(int[] itemNum) {
        this.itemNum = itemNum;
    }

    public void setName(String n){
        name=n;
    }
    public String getName(){
        return name;
    }
    public void setPUB(boolean n){
        PUB=n;
    }
    public boolean getPUB(){
        return PUB;
    }
    public WebSocket getSocket(){
        if (this.ws==null){
            try {
                this.ws = new WebSocket(new URI(ServUrl + name+"/"+name));
                this.ws.setAPP(this);
                this.ws.connectBlocking();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return ws;
    }
    public void setChat(String s){
        this.chatName=s;
    }
    public String getChat(){
        return this.chatName;
    }

    public MyClient getClient(){
        return this.cyc.getInstance();
    }

    @Override
    public void onCreate(){
        super.onCreate();
        cyc.getInstance();
        ChatData=new HashMap<String ,ArrayList<ChatMsgEntity>>();
    }
    public HashMap<String ,ArrayList<ChatMsgEntity>> getChatData(){
        return ChatData;
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
