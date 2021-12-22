package com.uniking.rock;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.Toast;

import com.uniking.service.ForegroundService;

/**
 * Created by wzl on 12/22/21.
 */

public class RockTools {
    public static void startForegroundService(Context context){
        //启动后台服务
        //----------------------------
        //启动服务
        if (!ForegroundService.serviceIsLive) {
            // Android 8.0使用startForegroundService在前台启动新服务
            Intent foregroundService = new Intent(context, ForegroundService.class);
            foregroundService.putExtra("Foreground", "This is a foreground service.");
            if (Build.VERSION.SDK_INT  >= Build.VERSION_CODES.O) {
                context.startForegroundService(foregroundService);
            } else {
                context.startService(foregroundService);
            }
        } else {
            Toast.makeText(context, "前台服务正在运行中...", Toast.LENGTH_SHORT).show();
        }
    }
}
