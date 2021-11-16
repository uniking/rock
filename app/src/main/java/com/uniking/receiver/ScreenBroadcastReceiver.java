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

            //冻结应用
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

            //进入light idle模式
            if("IDLE".equals(Adb.getLightIdle())){
                ;//Adb.unforceIdle();
            }else{
                Adb.forceLightIdle();
            }
        }


    }


    @Override
    public void onReceive(Context context, Intent intent) {

        try{
            action = intent.getAction();
            if (Intent.ACTION_SCREEN_OFF.equals(action)) { // 锁屏
                disableTimer = new Timer();
                disableTimer.schedule(new DisableTask(context), 1000*60*10);
            } else if (Intent.ACTION_USER_PRESENT.equals(action)) { // 解锁
                //取消timer
                if(disableTimer != null){
                    disableTimer.cancel();
                    disableTimer = null;
                }

                //取消light idle模式
                if(Adb.getLightIdle().startsWith("IDLE")){
                    Adb.unforceIdle();
                }
            }
        }catch (Exception e){
            ;
        }
    }
}
