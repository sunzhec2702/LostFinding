package com.example.darren.lostfinding;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.darren.scanner.CaptureActivity;
import java.util.logging.Logger;

public class MainActivity extends AppCompatActivity {
    private String decodeResult;
    private Button scanButton;
    private TextView scanResult;
    final String logTag = "LostFinding";

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(logTag, "in the onActivityResult");
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            String result = data.getStringExtra("result");
            if (result != null)
                scanResult.setText(result);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        scanButton = (Button) findViewById(R.id.scanner);
        scanResult = (TextView) findViewById(R.id.scanResult);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent scanIntent = new Intent(MainActivity.this, CaptureActivity.class);
                startActivityForResult(scanIntent, 0);
            }
        });


    }

}
