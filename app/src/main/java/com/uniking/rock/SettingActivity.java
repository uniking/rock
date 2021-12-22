package com.uniking.rock;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Toast;

import com.uniking.service.ForegroundService;
import com.uniking.tool.Adb;

/**
 * Created by wzl on 11/17/21.
 */

public class SettingActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        findViewById(R.id.bt_start_foreground).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //----------------------------
                //启动服务
                RockTools.startForegroundService(getApplicationContext());
            }
        });

        findViewById(R.id.bt_close_foreground).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent foregroundService = new Intent(getApplicationContext(), ForegroundService.class);
                foregroundService.putExtra("Foreground", "This is a foreground service.");
                stopService(foregroundService);
            }
        });

        //-------------------
        findViewById(R.id.bt_get_root).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Adb.suDo("ls /");
            }
        });
        findViewById(R.id.bt_get_usage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    if(checkSelfPermission(Settings.ACTION_USAGE_ACCESS_SETTINGS) != PackageManager.PERMISSION_GRANTED){
                        Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
                        startActivityForResult(intent, 1);
                    }
                }
            }
        });
        findViewById(R.id.bt_close_logcat).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Adb.stopLogcat();
            }
        });
    }
}
