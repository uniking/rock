package com.uniking.rock;

import android.app.TabActivity;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.Toast;

import com.uniking.receiver.ScreenBroadcastReceiver;
import com.uniking.service.ForegroundService;

import java.util.Map;

public class MainActivity extends TabActivity {
    private Spinner spinner;
    private ArrayAdapter<String> adapter;
    private String selectAppLab = "";
    Map<String, String> apps;
    ScreenBroadcastReceiver mScreenReceiver;
    Intent mForegroundService;
    private TabHost tabHost;

    void init(){
        //----------------
        mScreenReceiver = new ScreenBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        //filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_USER_PRESENT);
        registerReceiver(mScreenReceiver, filter);

        //----------------------------
        //启动服务
        if (!ForegroundService.serviceIsLive) {
            // Android 8.0使用startForegroundService在前台启动新服务
            mForegroundService = new Intent(this, ForegroundService.class);
            mForegroundService.putExtra("Foreground", "This is a foreground service.");
            if (Build.VERSION.SDK_INT  >= Build.VERSION_CODES.O) {
                startForegroundService(mForegroundService);
            } else {
                startService(mForegroundService);
            }
        } else {
            Toast.makeText(this, "前台服务正在运行中...", Toast.LENGTH_SHORT).show();
        }
    }

    void initTab(){
        TabHost tabHost = getTabHost();

        TabHost.TabSpec spec = tabHost.newTabSpec("t1");
        Intent it1 = new Intent(this, IdleActivity.class);
        spec.setContent(it1);
        spec.setIndicator("打盹");  // Naming the name of Tab
        tabHost.addTab(spec);

        TabHost.TabSpec spec2 = tabHost.newTabSpec("t2");
        Intent it2 = new Intent(this,DisableActivity.class);
        spec2.setContent(it2);
        spec2.setIndicator("禁用");
        tabHost.addTab(spec2);


        TabHost.TabSpec spec3 = tabHost.newTabSpec("t3");
        Intent it3 = new Intent(this,EnableActivity.class);
        spec3.setContent(it3);
        spec3.setIndicator("恢复");
        tabHost.addTab(spec3);

        tabHost.setCurrentTab(0); // Setting the default Tab
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initTab();
        init();
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(mScreenReceiver);

        super.onDestroy();
    }
}
