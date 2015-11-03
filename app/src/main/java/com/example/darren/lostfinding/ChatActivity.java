package com.example.darren.lostfinding;

import android.content.Intent;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.darren.lostfinding.net.WebSocket;
import com.example.darren.scanner.CaptureActivity;

import java.net.URI;
import java.net.URISyntaxException;
import android.os.Handler;


public class ChatActivity extends AppCompatActivity {
    public TextView chatResult;
    public EditText input,dst;
    private Button sendButton;
    private final String ServUrl="ws://192.168.0.88:8080/WHOS/websocket/";
    private String srcID;
    private String dstID=null;
    private Gdata app;
    private WebSocket socket= null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        app = (Gdata)getApplication();
        srcID=app.getName();
        socket=app.getSocket();

        chatResult = (TextView) findViewById(R.id.scanResult);
        input=(EditText)findViewById(R.id.editText);
        dst=(EditText)findViewById(R.id.editText2);
        sendButton=(Button)findViewById(R.id.sendButton);


        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == 1) {
                    chatResult.setText((String)msg.obj);
                }
            }
        };
        socket.setUI(handler);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (dstID==null || dstID.compareTo(dst.getText().toString()) != 0) {
                        dstID=dst.getText().toString();
                        socket = new WebSocket(new URI(ServUrl + srcID+"/"+dstID));
                        socket.connectBlocking();
                    }
                    socket.send(input.getText().toString());
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        });
    }
}
