package com.example.cyc;

/**
 * Created by cyclONE on 2015/11/18.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;

public class LDB extends SQLiteOpenHelper{

    private final static String DB_NAME ="whose.db";
    private final static String DB_CHAT ="tb_chat";
    private final static String DB_PIC ="tb_pic";
    private final static int VERSION = 2;
    public LDB(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + DB_CHAT + " (personid integer primary key autoincrement, name text, time text, content text, iscome text);");
        db.execSQL("create table " + DB_PIC + " (personid integer primary key autoincrement, name text, ver int,pic BLOB);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE IF EXISTS " + DB_CHAT;
        db.execSQL(sql);
        sql = "DROP TABLE IF EXISTS " + DB_PIC;
        db.execSQL(sql);
        onCreate(db);
    }

    public Cursor selectAllMSG(String uname) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from "+DB_CHAT+" where name=?",
                new String[]{uname});

        return cursor;
    }

    public Cursor selectAllUSER() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db
                .rawQuery("select distinct name from " + DB_CHAT,
                        new String[0]);
        return cursor;
    }

    //增加操作
    public long insert(String uname,String time,String uc,String come)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        /* ContentValues */
        ContentValues cv = new ContentValues();
        cv.put("name", uname);
        cv.put("time", time);
        cv.put("content", uc);
        cv.put("iscome", come);
        long row = db.insert(DB_CHAT, null, cv);
        return row;
    }

    public long updatePic(String uname,int version)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        /* ContentValues */
        ContentValues cv = new ContentValues();
        cv.put("name", uname);
        cv.put("ver", version);
        long row = db.insert(DB_PIC, null, cv);
        return row;
    }
    public int getPic(String uname){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select pic from " + DB_PIC + " where name=?",
                new String[]{uname});
        if (cursor.moveToFirst()) {
            return cursor.getInt(0);
        }else {
            return -1;
        }
    }

    public Cursor getLastMsg(String uname){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + DB_CHAT+ " where name=? order by personid desc limit 1",
                new String[]{uname});
        if (cursor.moveToFirst()) {
            return cursor;
        }else {
            return null;
        }
    }
}
