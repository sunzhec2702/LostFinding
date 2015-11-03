package com.example.darren.lostfinding.net;

import android.os.Handler;
import android.os.Message;

import java.net.URI;
import java.net.URISyntaxException;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_10;
import org.java_websocket.drafts.Draft_17;
import org.java_websocket.framing.Framedata;
import org.java_websocket.handshake.ServerHandshake;

/**
 * Created by cyclONE on 2015/11/2.
 */

/** This example demonstrates how to create a websocket connection to a server. Only the most important callbacks are overloaded. */
public class WebSocket extends WebSocketClient {
    private Handler UI=null;

    public void setUI(Handler handler ) {
        UI=handler;
    }
    public WebSocket( URI serverUri , Draft draft ) {
        super( serverUri, draft );
    }

    public WebSocket( URI serverURI ) {
        super(serverURI);
    }

    @Override
    public void onOpen( ServerHandshake handshakedata ) {
        System.out.println( "opened connection" );
        // if you plan to refuse connection based on ip or httpfields overload: onWebsocketHandshakeReceivedAsClient
    }

    @Override
    public void onMessage( String message ) {
        Message msg = new Message();
        msg.what = 1;
        msg.obj = message;
        UI.sendMessage(msg);
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

