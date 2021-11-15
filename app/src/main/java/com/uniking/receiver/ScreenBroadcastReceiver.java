package com.uniking.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;

import com.uniking.tool.Adb;
import com.uniking.tool.AppList;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by wzl on 11/15/21.
 */

public class ScreenBroadcastReceiver extends BroadcastReceiver {
    private String action = null;
    Timer disableTimer;

    class DisableTask extends TimerTask{
        Context mContext;

        public DisableTask(Context context){
            mContext = context;
        }

        @Override
        public void run() {
            Map<String, String> apps = new AppList(mContext).getDisableList();

            PackageManager pm = mContext.getPackageManager();
            for(String pkg : apps.keySet()){
                try{
                    if(pm.getApplicationInfo(pkg, 0).enabled){
                        Log.i("xxx", "冻结"+pkg);
                        Adb.disableApp(pkg);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }


    @Override
    public void onReceive(Context context, Intent intent) {

        action = intent.getAction();
        if (Intent.ACTION_SCREEN_ON.equals(action)) { // 开屏
            Log.i("xxx", "开屏");
        } else if (Intent.ACTION_SCREEN_OFF.equals(action)) { // 锁屏
            Log.i("xxx", "锁屏");
            disableTimer = new Timer();
            disableTimer.schedule(new DisableTask(context), 1000*60*10);
        } else if (Intent.ACTION_USER_PRESENT.equals(action)) { // 解锁
            Log.i("xxx", "解锁");
            if(disableTimer != null){
                disableTimer.cancel();
                disableTimer = null;
            }

        }
    }
}
