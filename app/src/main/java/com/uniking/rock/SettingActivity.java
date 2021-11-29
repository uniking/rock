package com.uniking.rock;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Toast;

import com.uniking.activity.QQ;
import com.uniking.activity.Wechat;
import com.uniking.activity.ZhiFuBao;
import com.uniking.service.ForegroundService;
import com.uniking.tool.Adb;
import com.uniking.tool.DeskShortCut;

/**
 * Created by wzl on 11/17/21.
 */

public class SettingActivity extends Activity {
    Intent mForegroundService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        findViewById(R.id.bt_start_foreground).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //----------------------------
                //启动服务
                if (!ForegroundService.serviceIsLive) {
                    // Android 8.0使用startForegroundService在前台启动新服务
                    mForegroundService = new Intent(getApplicationContext(), ForegroundService.class);
                    mForegroundService.putExtra("Foreground", "This is a foreground service.");
                    if (Build.VERSION.SDK_INT  >= Build.VERSION_CODES.O) {
                        startForegroundService(mForegroundService);
                    } else {
                        startService(mForegroundService);
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "前台服务正在运行中...", Toast.LENGTH_SHORT).show();
                }
            }
        });

        findViewById(R.id.bt_close_foreground).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopService(mForegroundService);
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

        findViewById(R.id.bt_zhifubao).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeskShortCut.addShortCutCompact(getApplicationContext(), ZhiFuBao.class, "支付宝", "com.eg.android.AlipayGphone");
            }
        });
        findViewById(R.id.bt_wechat).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeskShortCut.addShortCutCompact(getApplicationContext(), Wechat.class, "微信", "com.tencent.mm");
            }
        });
        findViewById(R.id.bt_qq).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeskShortCut.addShortCutCompact(getApplicationContext(), QQ.class, "QQ", "com.tencent.mobileqq");
            }
        });
    }
}
