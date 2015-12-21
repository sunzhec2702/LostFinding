package com.example.darren.lostfinding;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.cyc.DialogHelper;
import com.example.cyc.UpdateManager;
import com.example.darren.lostfinding.fragment.MessageFragment;
import com.example.darren.lostfinding.fragment.PersonalFragment;
import com.example.darren.lostfinding.fragment.QRFragment;
import com.example.darren.lostfinding.fragment.TopFragment;
import com.example.darren.lostfinding.net.MyClient;
import com.example.darren.scanner.CaptureActivity;
import com.squareup.okhttp.Request;


import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener{
    private RadioGroup rgs;
    Handler posHandler = new Handler();
    private boolean isPositionStart = false;
    private Dialog dialog;
    private Gdata app;
    private MainActivity LA;
    ArrayList<Fragment> fragments;
    FragmentTransaction transaction;
    TextView title;
    int currentTab;
    //------------更新窗口--------------
    private UpdateManager updateMan;
    private ProgressDialog updateProgressDialog;
    public Gdata getGdata(){
        return app;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null && requestCode == 0) {
            String result = data.getStringExtra("result");
            if (result != null){

            }
                //scanResult.setText(result);

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
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        for(int i = 0; i < rgs.getChildCount(); i++){
            if(rgs.getChildAt(i).getId() == checkedId){
                changeTitle(i);
                transaction=getFragmentManager().beginTransaction();
                //obtainFragmentTransaction(i);
                //transaction.addToBackStack(null);
                transaction.replace(R.id.tab_content, fragments.get(i));
                transaction.commit();
                break;
            }
        }
    }
    void init(){
        title=(TextView)findViewById(R.id.tv_title);
        fragments=new ArrayList<Fragment>();
        fragments.add(new QRFragment());
        fragments.add(new TopFragment());
        fragments.add(new MessageFragment());
        fragments.add(new PersonalFragment());
        rgs = (RadioGroup) findViewById(R.id.tabs_rg);
        rgs.setOnCheckedChangeListener(this);
        transaction= getFragmentManager().beginTransaction();
        transaction.replace(R.id.tab_content, fragments.get(0));
        transaction.commit();
        ImageView iv_scan=(ImageView)findViewById(R.id.scan);

        iv_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent scanIntent = new Intent(MainActivity.this, CaptureActivity.class);
                startActivity(scanIntent);
                /*String rr="http://111.20.118.242:8080/web_whose/search?ID=0000005f&app=1.0.4";
                MyClient.getAsyn(rr, new MyClient.ResultCallback<String>() {
                    @Override
                    public void onError(Request request, Exception e) {
                        e.printStackTrace();                    }

                    @Override
                    public String onResponse(String u) {
                        Intent result = new Intent(MainActivity.this, AfterScanActivity.class);
                        result.putExtra("result", u);
                        startActivity(result);
                        return u;
                    }
                });*/
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        app = (Gdata)getApplication();
        app.getSocket().setChatHandler(chatUI);
        LA=this;
        init();
    }

    void changeTitle(int i){
        switch (i){
            case 0:
                title.setText("谁的");
                break;
            case 1:
                title.setText("好人榜");
                break;
            case 2:
                title.setText("消息");
                break;
            case 30:
                title.setText("个人中心");
                break;
        }
    }

    final Handler chatUI = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                /*String text=(String)msg.obj;
                if(text.compareTo("0")==0){
                    chatView.setVisibility(View.GONE);
                }else{
                    chatView.setText((String)msg.obj);
                    chatView.show();
                }*/
            }else if(msg.what == 666){
                app.delLog("login");
                app.delLog("chat");
                app.getSocket().close();
                app.reset();
                new AlertDialog.Builder(MainActivity.this)
                        .setIcon(android.R.drawable.btn_star)
                        .setTitle("下线提示")
                        .setMessage("您的账号在别处登录，如非本人操作，请注意账号安全")
                        .setPositiveButton("确定",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        System.exit(0);
                                    }
                                }).setOnCancelListener(new DialogInterface.OnCancelListener() {

                    @Override
                    public void onCancel(DialogInterface dialog) {
                        System.exit(0);
                        //Toast.makeText(getBaseContext(), "点击了back", Toast.LENGTH_SHORT).show();
                    }
                }).show();
                        ;// show很关键
            }
        }
    };
    private void obtainFragmentTransaction(int index){
        // 设置切换动画
        if(index > currentTab){
            transaction.setCustomAnimations(R.anim.slide_left_in, R.anim.slide_left_out);
        }else{
            transaction.setCustomAnimations(R.anim.slide_right_in, R.anim.slide_right_out);
        }
        //return transaction;
    }
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(MainActivity.this)
                .setIcon(android.R.drawable.btn_star)
                .setTitle("退出？")
                .setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                System.exit(0);
                            }
                        })
                .setNegativeButton("取消",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                dialog.dismiss();
                            }
                        }).show();// show很关键
    }
}
