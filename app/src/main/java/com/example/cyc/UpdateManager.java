package com.example.cyc;

/**
 * Created by cyclONE on 2015/11/12.
 */


import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.darren.lostfinding.Gdata;
import com.squareup.okhttp.Response;

public class UpdateManager {

    private String curVersion;
    private String newVersion;
    private int curVersionCode;
    private int newVersionCode;
    private String updateInfo;
    private UpdateCallback callback;
    private Context ctx;

    private int progress;
    private Boolean hasNewVersion;
    private Boolean canceled;


    public static final String PRI_UPDATE_DOWNURL = "http:/192.168.0.88:8080/WHOS/update/WHOS.apk";
    public static final String PUB_UPDATE_DOWNURL = "http:/www.shuide.cc:8112/WHOS/update/WHOS.apk";
    public static final String PRI_UPDATE_CHECKURL= "http:/192.168.0.88:8080/WHOS/update";
    public static final String PUB_UPDATE_CHECKURL = "http:/www.shuide.cc:8112/WHOS/update";

    public static final String UPDATE_DOWNURL = Globle.DEBUG?PRI_UPDATE_DOWNURL:PUB_UPDATE_DOWNURL;
    public static final String UPDATE_CHECKURL =Globle.DEBUG?PRI_UPDATE_CHECKURL:PUB_UPDATE_CHECKURL;

    public static final String UPDATE_APKNAME = "update_test.apk";
    //public static final String UPDATE_VERJSON = "ver.txt";
    public static final String UPDATE_SAVENAME = "updateapk.apk";
    private static final int UPDATE_CHECKCOMPLETED = 1;
    private static final int UPDATE_DOWNLOADING = 2;
    private static final int UPDATE_DOWNLOAD_ERROR = 3;
    private static final int UPDATE_DOWNLOAD_COMPLETED = 4;
    private static final int UPDATE_DOWNLOAD_CANCELED = 5;

    private String savefolder = "/mnt/sdcard/";
    private Gdata app;
    //private String savefolder = "/sdcard/";
    //public static final String SAVE_FOLDER =Storage. // "/mnt/innerDisk";
    public UpdateManager(Context context, UpdateCallback updateCallback,Gdata rr) {
        app=rr;
        ctx = context;
        callback = updateCallback;
        //savefolder = context.getFilesDir();
        canceled = false;
        getCurVersion();
    }

    public String getNewVersionName() {
        return newVersion;
    }

    public String getUpdateInfo() {
        return updateInfo;
    }

    private void getCurVersion() {
        try {
            PackageInfo pInfo = ctx.getPackageManager().getPackageInfo(
                    ctx.getPackageName(), 0);
            curVersion = pInfo.versionName;
            curVersionCode = pInfo.versionCode;
        } catch (NameNotFoundException e) {
            Log.e("update", e.getMessage());
            curVersion = "1.1.1000";
            curVersionCode = 111000;
        }

    }

    public void checkUpdate() {
        hasNewVersion = false;
        new Thread() {
            @Override
            public void run() {
                Log.i("@@@@@", ">>>>>>>>>>>>>>>>>>>>>>>>>>>getServerVerCode() ");
                try {
                    String verjson = app.getClient().getLatestVersion (UPDATE_CHECKURL);
                    if (verjson.length() > 0) {
                        JSONObject JSON = new JSONObject(verjson);
                        try {
                            newVersion = JSON.getString("verName");
                            newVersionCode = Integer.parseInt(JSON.getString("verCode"));
                            updateInfo = "";
                            if (newVersionCode > curVersionCode) {
                                hasNewVersion = true;
                            }
                        } catch (Exception e) {
                            newVersionCode = -1;
                            newVersion = "";
                            updateInfo = "";
                        }
                    }
                } catch (Exception e) {
                    Log.e("update", e.getMessage());
                }
                updateHandler.sendEmptyMessage(UPDATE_CHECKCOMPLETED);
            }

            ;
            // ***************************************************************
        }.start();

    }

    public void update() {
        Intent intent = new Intent(Intent.ACTION_VIEW);

        intent.setDataAndType(
                Uri.fromFile(new File(savefolder, UPDATE_SAVENAME)),
                "application/vnd.android.package-archive");
        ctx.startActivity(intent);
    }

    // +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    public void downloadPackage() {


        new Thread() {
            @Override
            public void run() {
                try {
                    Response res=app.getClient().getAsyn(UPDATE_DOWNURL);
                    /*
                    URL url = new URL(UPDATE_DOWNURL);

                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.connect();
                    int length = conn.getContentLength();
                    InputStream is = conn.getInputStream();
                    */
                    Long length = res.body().contentLength();
                    InputStream is=res.body().byteStream();

                    File ApkFile = new File(savefolder, UPDATE_SAVENAME);


                    if (ApkFile.exists()) {
                        ApkFile.delete();
                    }


                    FileOutputStream fos = new FileOutputStream(ApkFile);

                    int count = 0;
                    byte buf[] = new byte[512];

                    do {

                        int numread = is.read(buf);
                        count += numread;
                        progress = (int) (((float) count / length) * 100);

                        updateHandler.sendMessage(updateHandler.obtainMessage(UPDATE_DOWNLOADING));
                        if (numread <= 0) {

                            updateHandler.sendEmptyMessage(UPDATE_DOWNLOAD_COMPLETED);
                            break;
                        }
                        fos.write(buf, 0, numread);
                    } while (!canceled);
                    if (canceled) {
                        updateHandler.sendEmptyMessage(UPDATE_DOWNLOAD_CANCELED);
                    }
                    fos.close();
                    is.close();
                } catch (MalformedURLException e) {
                    e.printStackTrace();

                    updateHandler.sendMessage(updateHandler.obtainMessage(UPDATE_DOWNLOAD_ERROR, e.getMessage()));
                } catch (IOException e) {
                    e.printStackTrace();

                    updateHandler.sendMessage(updateHandler.obtainMessage(UPDATE_DOWNLOAD_ERROR, e.getMessage()));
                }

            }
        }.start();
    }

    public void cancelDownload() {
        canceled = true;
    }

    Handler updateHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case UPDATE_CHECKCOMPLETED:

                    callback.checkUpdateCompleted(hasNewVersion, newVersion);
                    break;
                case UPDATE_DOWNLOADING:

                    callback.downloadProgressChanged(progress);
                    break;
                case UPDATE_DOWNLOAD_ERROR:

                    callback.downloadCompleted(false, msg.obj.toString());
                    break;
                case UPDATE_DOWNLOAD_COMPLETED:

                    callback.downloadCompleted(true, "");
                    break;
                case UPDATE_DOWNLOAD_CANCELED:

                    callback.downloadCanceled();
                default:
                    break;
            }
        }
    };

    public interface UpdateCallback {
        public void checkUpdateCompleted(Boolean hasUpdate,
                                         CharSequence updateInfo);

        public void downloadProgressChanged(int progress);

        public void downloadCanceled();

        public void downloadCompleted(Boolean sucess, CharSequence errorMsg);
    }

}

