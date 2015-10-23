package com.example.darren.lostfinding;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import java.io.IOException;



/**
 * Created by cyclONE on 2015/10/22.
 */
public class BrowserAcitvity extends Activity {
    //public String ServerUrl="http://192.168.0.88:8080/WHOS/find.view?ID=00000001";
    public String ServerUrl="http://www.baidu.com";
    OkHttpClient client;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        //Window window = getWindow();
        //window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.browser);
        client = new OkHttpClient();
        TextView testResult=(TextView) findViewById(R.id.testtext);
        try {
            testResult.setText(this.run(ServerUrl));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String run(String url) throws IOException {
        Request request = new Request.Builder().url(url).build();
        Response response;
        try {
            response = client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        /*
        Response response = client.newCall(request).execute();

        if (response.isSuccessful()) {
            return response.body().string();
        } else {
            throw new IOException("Unexpected code " + response);
        }
        */
        return "0";
    }

}
