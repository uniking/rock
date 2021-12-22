package com.uniking.rock;

import android.app.Application;
import android.content.Intent;
import android.os.Build;
import android.widget.Toast;

import com.uniking.service.ForegroundService;
import com.uniking.tool.Adb;

import jackmego.com.jieba_android.JiebaSegmenter;
import xcrash.XCrash;

/**
 * Created by JackMeGo on 2017/7/4.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // 异步初始化
        JiebaSegmenter.init(getApplicationContext());

        // xcrash
        xcrash.XCrash.init(this, new XCrash.InitParameters()
                .setLogDir(getExternalFilesDir("msmlog").toString()));

        //启动后台服务
        RockTools.startForegroundService(getApplicationContext());

        //初始化休眠
        Adb.initIdle();

        //关闭logcat
        Adb.stopLogcat();
    }
}
