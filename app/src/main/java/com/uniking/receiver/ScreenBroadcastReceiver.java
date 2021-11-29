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
    Object clock = new Object();
    Timer disableTimer;
    Timer idleTimer;

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
        }
    }

    class IdleTask extends TimerTask{
        Context mContext;
        public IdleTask(Context context){
            mContext = context;
        }
        @Override
        public void run() {
            //进入light idle模式
            if("IDLE".equals(Adb.getLightStatus())){
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
                synchronized (clock){
                    //锁屏10分钟冻结应用
                    disableTimer = new Timer();
                    disableTimer.schedule(new DisableTask(context), 1000*60*10);

                    //锁屏30秒进入idle
                    //发现文件管理的ftp服务阻止了进入idle,还是强制进入吧
//                    idleTimer = new Timer();
//                    idleTimer.schedule(new IdleTask(context), 1000*30);
                }

            } else if (Intent.ACTION_USER_PRESENT.equals(action)) { // 解锁
                synchronized (clock){
                    //取消timer
                    if(disableTimer != null){
                        disableTimer.cancel();
                        disableTimer = null;
                    }
//                    if(idleTimer != null){
//                        idleTimer.cancel();
//                        idleTimer = null;
//                    }
                }

                //取消light idle模式
//                if(Adb.getLightStatus().startsWith("IDLE")){
//                    Adb.unforceIdle();
//                }
            }
        }catch (Exception e){
            ;
        }
    }
}
