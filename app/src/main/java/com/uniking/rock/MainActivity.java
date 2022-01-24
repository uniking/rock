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

    private TabHost tabHost;

    void init(){
        //监听锁屏，解锁
        mScreenReceiver = new ScreenBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        //filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_USER_PRESENT);
        registerReceiver(mScreenReceiver, filter);
    }

    void initTab(){
        TabHost tabHost = getTabHost();

        TabHost.TabSpec spec0 = tabHost.newTabSpec("t0");
        Intent it0 = new Intent(this, SettingActivity.class);
        spec0.setContent(it0);
        spec0.setIndicator("配置");  // Naming the name of Tab
        tabHost.addTab(spec0);

        TabHost.TabSpec spec1 = tabHost.newTabSpec("t1");
        Intent it1 = new Intent(this, IdleActivity.class);
        spec1.setContent(it1);
        spec1.setIndicator("打盹");  // Naming the name of Tab
        tabHost.addTab(spec1);

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

        TabHost.TabSpec spec4 = tabHost.newTabSpec("t4");
        Intent it4 = new Intent(this,BigbangActivity.class);
        spec4.setContent(it4);
        spec4.setIndicator("Bang");
        tabHost.addTab(spec4);

        TabHost.TabSpec spec5 = tabHost.newTabSpec("t5");
        Intent it5 = new Intent(this,BackupActivity.class);
        spec5.setContent(it5);
        spec5.setIndicator("备份");
        tabHost.addTab(spec5);

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
