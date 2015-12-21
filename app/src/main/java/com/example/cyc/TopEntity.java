package com.example.cyc;

import java.util.ArrayList;

/**
 * Created by cyclONE on 2015/12/15.
 */
public class TopEntity {
    private String name;
    private String nickname;
    private String point;
    private String order;
    public String getNickname(){
        return nickname;
    }
    public String getPoint(){
        return point;
    }
    public String getOrder(){
        return order;
    }
    public String getName(){
        return name;
    }
    public TopEntity(String s1,String s2,String s3,String s4){
        name=s1;
        nickname=s2;
        point=s3;
        order=s4;
    }
    public static ArrayList<TopEntity> build(String u){
        ArrayList<TopEntity> al=new ArrayList<TopEntity>();
        String r1,r2,r3,r4;
        while (u.indexOf("PR1=!@#$")>-1) {
            r1 = u.substring("PR1=!@#$".length(), u.indexOf("PR2=!@#$"));
            u=u.substring(u.indexOf("PR2=!@#$"));
            r2 = u.substring(u.indexOf("PR2=!@#$")+"PR2=!@#$".length(), u.indexOf("PR3=!@#$"));
            r3 = u.substring(u.indexOf("PR3=!@#$")+"PR3=!@#$".length(), u.indexOf("PR4=!@#$"));
            if(u.indexOf("PR1=!@#$")>-1){
                r4 = u.substring(u.indexOf("PR4=!@#$")+"PR4=!@#$".length(), u.indexOf("PR1=!@#$"));
                u=u.substring(u.indexOf("PR1=!@#$"));
            }else{
                r4 = u.substring(u.indexOf("PR4=!@#$")+"PR6=!@#$".length());
            }
            if(r3.indexOf("\r")>-1){
                r3=r3.substring(0,r3.indexOf("\r"));
            }
            if(r4.indexOf("\r")>-1){
                r4=r4.substring(0,r4.indexOf("\r"));
            }
            al.add(new TopEntity(r1,r2,r3,r4));
        }
        return  al;
    }
}
