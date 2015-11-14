package com.example.darren.lostfinding;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adapter.LvAdapterMainItem;
import com.example.cyc.DialogHelper;
import com.example.cyc.Globle;
import com.example.cyc.MyListView;
import com.example.cyc.QREntity;
import com.example.cyc.UpdateManager;
import com.example.darren.lostfinding.net.MyClient;
import com.example.darren.scanner.CaptureActivity;
import com.squareup.okhttp.Request;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private ImageView ivPerson;
    private LinearLayout linearCamera, linearPublish,linearChat,linearMall;
    private TextView tv1, tv2, tvCancle, tvLogOut,tvMyInfo;
    private MyListView lvItem;
    private TextView scanResult;
    private TextView positionView;
    final String logTag = "LostFinding";
    Handler posHandler = new Handler();
    private PositionUpdate posUpdate;
    private boolean isPositionStart = false;
    private Dialog dialog;
    private ImageView ImageBank;
    private View vPerson;
    private Gdata app;
    private List<QREntity> qrInfo;
    private String PRI_memUrl="http://192.168.0.88:8080/WHOS/member.jsp";
    private String PUB_memUrl="http://www.shuide.cc:8112/WHOS/member.jsp";    
    private String memUrl= Globle.DEBUG?PRI_memUrl:PUB_memUrl;

    private String PRI_QRUrl="http://192.168.0.88:8080/WHOS/app/statu";
    private String PUB_QRUrl="http://www.shuide.cc:8112/WHOS/app/statu";
    private String QRUrl= Globle.DEBUG?PRI_QRUrl:PUB_QRUrl;

    private String PRI_getUrl="http://192.168.0.88:8080/WHOS/search?ID=";
    private String PUB_getUrl="http://www.shuide.cc:8112/WHOS/search?ID=";
    private String getUrl= Globle.DEBUG?PRI_getUrl:PUB_getUrl;
    //------------更新窗口--------------
    private UpdateManager updateMan;
    private ProgressDialog updateProgressDialog;

    Runnable posRunnable = new Runnable() {
        @Override
        public void run() {
            Location loc = posUpdate.getLoc();
            if (loc != null) {
                Log.d(logTag, "Not null");
                positionView.setText(loc.getLatitude() + " , " + loc.getLongitude());
            } else {
                Log.d(logTag, "NULL");
            }
            posHandler.postDelayed(posRunnable, 1000);
        }
    };
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(logTag, "in the onActivityResult");
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null && requestCode == 0) {
            String result = data.getStringExtra("result");
            if (result != null)
                scanResult.setText(result);
        }
    }

    UpdateManager.UpdateCallback appUpdateCb = new UpdateManager.UpdateCallback()
    {

        public void downloadProgressChanged(int progress) {
            if (updateProgressDialog != null
                    && updateProgressDialog.isShowing()) {
                updateProgressDialog.setProgress(progress);
            }

        }

        public void downloadCompleted(Boolean sucess, CharSequence errorMsg) {
            if (updateProgressDialog != null
                    && updateProgressDialog.isShowing()) {
                updateProgressDialog.dismiss();
            }
            if (sucess) {
                updateMan.update();
            } else {
                DialogHelper.Confirm(MainActivity.this,
                        R.string.dialog_error_title,
                        R.string.dialog_downfailed_msg,
                        R.string.dialog_downfailed_btndown,
                        new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog,
                                                int which) {
                                updateMan.downloadPackage();

                            }
                        }, R.string.dialog_downfailed_btnnext, null);
            }
        }

        public void downloadCanceled()
        {
            // TODO Auto-generated method stub

        }

        public void checkUpdateCompleted(Boolean hasUpdate,
                                         CharSequence updateInfo) {
            if (hasUpdate) {
                DialogHelper.Confirm(MainActivity.this,
                        getText(R.string.dialog_update_title),
                        getText(R.string.dialog_update_msg).toString()
                                +updateInfo+
                                getText(R.string.dialog_update_msg2).toString(),
                        getText(R.string.dialog_update_btnupdate),
                        new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog,
                                                int which) {
                                updateProgressDialog = new ProgressDialog(
                                        MainActivity.this);
                                updateProgressDialog
                                        .setMessage(getText(R.string.dialog_downloading_msg));
                                updateProgressDialog.setIndeterminate(false);
                                updateProgressDialog
                                        .setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                                updateProgressDialog.setMax(100);
                                updateProgressDialog.setProgress(0);
                                updateProgressDialog.show();

                                updateMan.downloadPackage();
                            }
                        },getText( R.string.dialog_update_btnnext), null);
            }

        }
    };

    final Handler hUI = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 666) {
                String rr=getUrl+(String) msg.obj;
                app.getClient().getAsyn(rr, new MyClient.ResultCallback<String>() {
                    @Override
                    public void onError(Request request, Exception e) {
                        e.printStackTrace();
                    }
                    @Override
                    public String onResponse(String u) {
                        Intent result = new Intent(MainActivity.this, AfterScanActivity.class);
                        result.putExtra("result", u);
                        startActivity(result);
                        return u;
                    }
                });
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app = (Gdata)getApplication();
        setContentView(R.layout.main);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        qrInfo=new ArrayList<QREntity>();
        scanResult = (TextView) findViewById(R.id.scanResult);
        //positionView = (TextView) findViewById(R.id.positionText);
        posUpdate = new PositionUpdate(this);
        updateMan = new UpdateManager(MainActivity.this, appUpdateCb,app);
        updateMan.checkUpdate();
        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isPositionStart) {
                    positionView.setText("Started");
                    isPositionStart = true;
                    posHandler.post(posRunnable);
                }
                else {
                    isPositionStart = false;
                    posHandler.removeCallbacks(posRunnable);
                    positionView.setText("stopped");
                }
            }
        });*/

        /*linearChat=(LinearLayout) findViewById(R.id.linear_main_chat);
        linearChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent result = new Intent(MainActivity.this, ChatActivity.class);
                startActivity(result);
            }
        });*/

        //个人中心
        ivPerson = (ImageView) findViewById(R.id.iv_main_self);
        ImageBank = (ImageView) findViewById(R.id.iv_publish_bank);
        ivPerson.setFocusable(true);
        vPerson = LayoutInflater.from(MainActivity.this).inflate(
                R.layout.dialog_person_center, null);
        ivPerson.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                // new AlertDialog.Builder(Mai)
                dialog = new Dialog(MainActivity.this);

                dialog.show();
                dialog.getWindow().setContentView(vPerson);
                WindowManager windowManager = getWindowManager();
                Display display = windowManager.getDefaultDisplay();
                WindowManager.LayoutParams lp = dialog.getWindow()
                        .getAttributes();
                lp.width = (int) (display.getWidth()); // 设置宽度
                dialog.getWindow().setAttributes(lp);
            }
        });
        //扫码
        linearCamera = (LinearLayout) findViewById(R.id.linear_main_camera);
        linearCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent scanIntent = new Intent(MainActivity.this, CaptureActivity.class);
                startActivityForResult(scanIntent, 0);
                /*
                String rr="http://www.shuide.cc:8112/WHOS/search?ID=00000003";
                app.getClient().getAsyn(rr, new MyClient.ResultCallback<String>() {
                    @Override
                    public void onError(Request request, Exception e) {
                        e.printStackTrace();
                    }

                    @Override
                    public String onResponse(String u) {
                        Intent result = new Intent(MainActivity.this, AfterScanActivity.class);
                        result.putExtra("result", u);
                        startActivity(result);
                        return u;
                       }
                   });
                    */
            }
        });

        linearPublish = (LinearLayout) findViewById(R.id.linear_main_publish);
        linearPublish.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, PublishActivity.class);
                startActivity(intent);

            }
        });

        ImageBank.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                Intent aIntent = new Intent();
                aIntent.setClass(MainActivity.this, LoginActivity.class);
                startActivity(aIntent);
            }
        });

        linearMall=(LinearLayout) findViewById(R.id.linear_main_mall);
        linearMall.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, ShopCheckActivity.class);
                startActivity(intent);
            }
        });

        tvMyInfo = (TextView) vPerson.findViewById(R.id.tv_myInformation);
        tvMyInfo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent();
                intent.setClass(MainActivity.this,
                        PersonInfomationActivity.class);
                startActivity(intent);

            }
        });
        tv1 = (TextView) vPerson.findViewById(R.id.tv_myQR);
        tv1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, MyQrActivity.class);
                startActivity(intent);
            }
        });
        tv2 = (TextView) vPerson.findViewById(R.id.tv_dingzhi);
        tv2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, CustomeServiceActivity.class);
                startActivity(intent);
            }
        });
        tvCancle = (TextView) vPerson
                .findViewById(R.id.tv_dialogPersonCenter_cancle);
        tvCancle.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                dialog.dismiss();
            }
        });
        tvLogOut= (TextView) vPerson
                .findViewById(R.id.tv_logOff);
        tvLogOut.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                app.delLog("login");
                Intent intent = new Intent(MainActivity.this,
                        LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
        lvItem = (MyListView) findViewById(R.id.lv_main_main);
        lvItem.setonRefreshListener(new MyListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getEntity();
            }
        });
        getEntity();
        }
    private void getEntity() {
        Map<String, String> obj = new HashMap<String, String>();
        obj.put("username", app.getName());
        app.getClient().postAsyn(QRUrl, new MyClient.ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {
                e.printStackTrace();
            }

            @Override
            public String onResponse(String u) {
                if (u.length() > 0) {
                    qrInfo.clear();
                    String r1, r2;
                    while (u.indexOf("value=!@#$") != -1) {
                        r1 = u.substring("key=!@#$".length(), u.indexOf("value=!@#$"));
                        u = u.substring(u.indexOf("value=!@#$")+"value=!@#$".length());
                        if (u.indexOf("value=!@#$") != -1) {
                            r2 = u.substring(0, u.indexOf("key=!@#$"));
                            u = u.substring(u.indexOf("key=!@#$"));
                        } else {
                            r2 = u;
                            break;
                        }
                        qrInfo.add(new QREntity(r1,r2));
                    }
                    LvAdapterMainItem adapter = new LvAdapterMainItem(getApplicationContext(), qrInfo);
                    adapter.setHandler(hUI);
                    lvItem.setAdapter(adapter);
                    lvItem.onRefreshComplete();
                }
                return u;
            }
        }, obj);
    }

}
