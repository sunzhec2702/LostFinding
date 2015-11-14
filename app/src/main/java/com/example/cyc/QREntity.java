package com.example.cyc;

/**
 * Created by cyclONE on 2015/11/13.
 */
public class QREntity {
    private String id;
    private String statu;
    public QREntity(String s1, String s2){
        id =s1;
        statu=s2;
    }
    public String getId(){
        return id;
    }
    public String getStatu(){
        return  statu;
    }
}
