package com.example.darren.lostfinding;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.EnumMap;
import java.util.Hashtable;
import java.util.Map;

public class QRShowActivity extends AppCompatActivity {
    private Button bSave,bBack;
    private Bitmap QRbm;
    private ImageView QR;
    private String Result;
    private String name="male";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrshow);
        initView();
        initControl();
    }

    private void initView(){
        QR = (ImageView) findViewById(R.id.qr_view);
        bSave=(Button)findViewById(R.id.btn_save);
        bBack=(Button)findViewById(R.id.btn_back);
        Intent ihere = this.getIntent();
        final Bundle bhere = ihere.getExtras();
        Result = bhere.getString("DSTID");

        QRbm=createQRImage(Result, 300, 300);
        //QRbm=convertStringToIcon(Result);
        QR.setImageBitmap(QRbm);
    }
    private void initControl(){
        bBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                finish();
            }
        });
        bSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                saveQrCodePicture();
            }
        });
    }
    public Bitmap createQRImage(String url, final int width, final int height) {
        try {
            Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            // 图像数据转换，使用了矩阵转换
            BitMatrix bitMatrix = new QRCodeWriter().encode(url,
                    BarcodeFormat.QR_CODE, width, height, hints);
            int[] pixels = new int[width * height];
            // 下面这里按照二维码的算法，逐个生成二维码的图片，
            // 两个for循环是图片横列扫描的结果
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    if (bitMatrix.get(x, y)) {
                        pixels[y * width + x] = 0xff000000;
                    } else {
                        pixels[y * width + x] = 0xffffffff;
                    }
                }
            }

            // 生成二维码图片的格式，使用ARGB_8888
            Bitmap bitmap = Bitmap.createBitmap(width, height,
                    Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
            return bitmap;
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void saveQrCodePicture() {
        final File qrImage = new File(Environment.getExternalStorageDirectory(), "谁的二维码编号"+Result.substring(Result.length()-8)+".jpg");
        if(qrImage.exists())
        {
            qrImage.delete();
        }
        try {
            qrImage.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(qrImage);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if(QRbm == null)
        {
            Toast.makeText(this,"保存失败", Toast.LENGTH_SHORT).show();
            return;
        }
        QRbm.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
        try {
            fOut.flush();
            fOut.close();
            Toast.makeText(this,"图片保存成功", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Bitmap convertStringToIcon(String st)
    {
        // OutputStream out;
        Bitmap bitmap = null;
        try
        {
            byte[] bitmapArray;
            bitmapArray = Base64.decode(st, Base64.DEFAULT);
            bitmap =BitmapFactory.decodeByteArray(bitmapArray, 0,
                            bitmapArray.length);
            return bitmap;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }
}
