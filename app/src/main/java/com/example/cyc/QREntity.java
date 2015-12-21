package com.example.cyc;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cyclONE on 2015/11/13.
 */
public class QREntity {
    private String id;
    private int statu;
    private String name;
    private String owner;
    private String time;
    private int visible;


    public QREntity(String s1, String s2) {
        id = s1;
        statu = Integer.parseInt(s2);
    }

    public QREntity(String qid, String qname, String qowner, String qtime, String qsta,String rr) {
        id = qid;
        statu = Integer.parseInt(qsta);
        name = qname;
        owner = qowner;
        time = qtime;
        visible = Integer.parseInt(rr);
    }

    public String getId() {
        return id;
    }
    public int getStatu() {
        return statu;
    }
    public String getOwner() {
        return owner;
    }
    public String getTime() {
        return time;
    }
    public String getName() {
        return name;
    }
    public int getVisible() {
        return visible;
    }
    public static List<QREntity> getQRfromString(String u){
        ArrayList<QREntity> al=new ArrayList<QREntity>();
        String r1,r2,r3,r4,r5,r6;
        while (u.indexOf("PR1=!@#$")>-1) {
            r1 = u.substring("PR1=!@#$".length(), u.indexOf("PR2=!@#$"));
            u=u.substring(u.indexOf("PR2=!@#$"));
            r2 = u.substring(u.indexOf("PR2=!@#$")+"PR2=!@#$".length(), u.indexOf("PR3=!@#$"));
            r3 = u.substring(u.indexOf("PR3=!@#$")+"PR3=!@#$".length(), u.indexOf("PR4=!@#$"));
            r4 = u.substring(u.indexOf("PR4=!@#$")+"PR4=!@#$".length(), u.indexOf("PR5=!@#$"));
            if(r4.indexOf("null")==-1){
                r4=r4.substring(0,r4.indexOf(".0"));
            }
            r5 = u.substring(u.indexOf("PR5=!@#$")+"PR5=!@#$".length(), u.indexOf("PR6=!@#$"));
            if (r5.indexOf("\r") != -1) {
                r5 = r5.substring(0, r5.indexOf("\r"));
            }
            if(u.indexOf("PR1=!@#$")>-1){
                r6 = u.substring(u.indexOf("PR6=!@#$")+"PR6=!@#$".length(), u.indexOf("PR1=!@#$"));
                u=u.substring(u.indexOf("PR1=!@#$"));
            }else{
                r6 = u.substring(u.indexOf("PR6=!@#$")+"PR6=!@#$".length());
            }
            if (r6.indexOf("\r") != -1) {
                r6 = r6.substring(0, r6.indexOf("\r"));
            }
            al.add(new QREntity(r1,r2,r3,r4,r5,r6));
        }
        return  al;
    }
}

