package com.example.darren.lostfinding.net;

import com.example.darren.lostfinding.BrowserAcitvity;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
/**
 * Created by cyclONE on 2015/10/23.
 */
public  class GetResponse{
    String url;
    public void setUrl(String dd){
        url=dd;
    }
    public String get(){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        Response response = null;
        try {
            response = client.newCall(request).execute();
            return response.body().string();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return "error";
    }
}
