package com.example.darren.lostfinding.net;

import android.os.Handler;
import android.os.Message;

import com.example.cyc.ChatMsgEntity;
import com.example.darren.lostfinding.Gdata;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.framing.Framedata;
import org.java_websocket.handshake.ServerHandshake;

/**
 * Created by cyclONE on 2015/11/2.
 */

/** This example demonstrates how to create a websocket connection to a server. Only the most important callbacks are overloaded. */
public class WebSocket extends WebSocketClient {
    private Gdata app;
    private Handler UI=null,chat=null;

    public void setUI(Handler handler) {
        UI=handler;
    }
    public void setChatHandler(Handler c){
        chat=c;
    }
    public void setAPP(Gdata r) {
        app=r;
    }
    public WebSocket( URI serverUri , Draft draft) {
        super( serverUri, draft );
    }

    public WebSocket( URI serverURI) {
        super(serverURI);
    }

    @Override
    public void onOpen( ServerHandshake handshakedata ) {
        System.out.println("opened connection");
        // if you plan to refuse connection based on ip or httpfields overload: onWebsocketHandshakeReceivedAsClient
    }

    public void refresh(){
        Message msg = new Message();
        msg.what = 1;
        int n=0;
        for (Map.Entry<String ,ArrayList<ChatMsgEntity>> entry : app.getChatData().entrySet()) {
            String key = entry.getKey();
            n+= entry.getValue().size();
        }
        msg.obj=String.valueOf(n);
        chat.sendMessage(msg);
    }

    @Override
    public void onMessage( String message ) {
        Message msg = new Message();
        if(message.compareTo("WC=QUIT")==0){
            msg.what = 666;
            chat.sendMessage(msg);
            return;
        }
        msg.what = 1;
        ChatMsgEntity rr=ChatMsgEntity.parseString(message);
        app.getdb().insert(rr.getName(),rr.getDate(),rr.getText(),rr.getMsgType()?"1":"0");
        ArrayList<ChatMsgEntity> CL=app.getChatData().get(rr.getName());
        if (CL==null) {
            CL=new ArrayList<ChatMsgEntity>();
            CL.add(rr);
            app.getChatData().put(rr.getName(), CL);
        }else{
            app.getChatData().get(rr.getName()).add(rr);
        }
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put(rr.getName(), "ALOHA");
        app.saveMsg("chat", map);
        if (UI!=null){
            UI.sendMessage(msg);
        }else if(chat!=null){
            int n=0;
            for (Map.Entry<String ,ArrayList<ChatMsgEntity>> entry : app.getChatData().entrySet()) {
                String key = entry.getKey();
                n+= entry.getValue().size();
            }
            msg.obj=String.valueOf(n);
            chat.sendMessage(msg);
        }


        //System.out.println( "received: " + message );
    }

    @Override
    public void onFragment( Framedata fragment ) {
        System.out.println( "received fragment: " + new String( fragment.getPayloadData().array() ) );
    }

    @Override
    public void onClose( int code, String reason, boolean remote ) {
        // The codecodes are documented in class org.java_websocket.framing.CloseFrame
        System.out.println( "Connection closed by " + ( remote ? "remote peer" : "us" ) );
    }

    @Override
    public void onError( Exception ex ) {
        ex.printStackTrace();
        // if the error is fatal then onClose will be called additionally
    }
}

