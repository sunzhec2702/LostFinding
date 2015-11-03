package com.example.darren.lostfinding;
import android.app.Application;

import com.example.darren.lostfinding.net.MyClient;
import com.example.darren.lostfinding.net.WebSocket;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by cyclONE on 2015/10/30.
 */
public class Gdata extends Application{
    public MyClient cyc;
    private WebSocket ws=null;
    private final String ServUrl="ws://192.168.0.88:8080/WHOS/websocket/";
    public String name;
    public void setName(String n){
        name=n;
    }
    public String getName(){
        return name;
    }
    public WebSocket getSocket(){
        if (ws==null){
            try {
                ws = new WebSocket(new URI(ServUrl + name+"/"+name));
                ws.connectBlocking();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return ws;
    }
    public MyClient getClient(){
        return cyc.getInstance();
    }

    @Override
    public void onCreate(){
        super.onCreate();
        cyc.getInstance();
    }
}
