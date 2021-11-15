package com.uniking.rock;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.uniking.receiver.ScreenBroadcastReceiver;
import com.uniking.tool.Adb;
import com.uniking.tool.AppList;

import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private Spinner spinner;
    private ArrayAdapter<String> adapter;
    private String selectApp = "";
    Map<String, String> apps;
    ScreenBroadcastReceiver mScreenReceiver;
    Intent mForegroundService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //----------------
        spinner = (Spinner) findViewById(R.id.spinner_apps);
        apps = Adb.getNoSysPackageList(getApplication());
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, Adb.Set2List(apps.keySet()));
        if(adapter != null){
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    selectApp = adapter.getItem(position);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    ;
                }
            });
        }else{
            Toast.makeText(this, "adapter==null", Toast.LENGTH_SHORT).show();
        }


        //----------------
        mScreenReceiver = new ScreenBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_USER_PRESENT);
        registerReceiver(mScreenReceiver, filter);

        //-------------------
        findViewById(R.id.bt_get_root).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Adb.suDo("ls /");
            }
        });

        findViewById(R.id.bt_enable_app).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Adb.isEnable(getApplicationContext(), apps.get(selectApp))){
                    Toast.makeText(getApplicationContext(), "已经是启用状态", Toast.LENGTH_SHORT).show();
                }else{
                    Adb.enableApp(apps.get(selectApp));
                    Toast.makeText(getApplicationContext(), "启用成功", Toast.LENGTH_SHORT).show();
                }

            }
        });

        findViewById(R.id.bt_disenable_app).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Adb.isEnable(getApplicationContext(), apps.get(selectApp))){
                    Adb.disableApp(apps.get(selectApp));
                    Toast.makeText(getApplicationContext(), "禁用成功", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(), "已经是禁用状态", Toast.LENGTH_SHORT).show();
                }

            }
        });

        findViewById(R.id.bt_add_disenable_list).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AppList(getApplicationContext()).addDisableList(apps.get(selectApp), selectApp);
            }
        });

        findViewById(R.id.bt_remove_disenable_list).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AppList(getApplicationContext()).deleteDisableList(apps.get(selectApp));
            }
        });

        findViewById(R.id.bt_print_app).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, String> apps = new AppList(getApplicationContext()).getDisableList();
                StringBuilder sb = new StringBuilder();
                for(String app : apps.keySet()){
                    sb.append(apps.get(app));
                    sb.append("\n");
                }

                EditText et = findViewById(R.id.et_msg);
                et.setText(sb.toString());
            }
        });

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

    @Override
    protected void onDestroy() {
        unregisterReceiver(mScreenReceiver);

        super.onDestroy();
    }
}
