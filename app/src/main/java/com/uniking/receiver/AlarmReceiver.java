package com.uniking.receiver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;

import com.uniking.tool.Adb;
import com.uniking.tool.AppList;

import java.util.Map;

import static android.content.Context.ALARM_SERVICE;

/**
 * Created by wzl on 11/30/21.
 */

public class AlarmReceiver extends BroadcastReceiver {
    final  String Tag = "AlarmReceiver";
    PendingIntent sender;

    public AlarmReceiver(){

    }

    public void freeze(Context context){
        try{
            //冻结应用
            Map<String, String> apps = new AppList(context).getDisableList();
            PackageManager pm = context.getPackageManager();
            for(String pkg : apps.keySet()){
                try{
                    if(pm.getApplicationInfo(pkg, 0).enabled){
                        Log.i("xxx", "冻结"+pkg);
                        Adb.disableApp(pkg);
                        Adb.forceStop(pkg);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(Tag, "receive alarm");
        freeze(context);
    }

    public void setAlarm(Context context, int delay){
        Log.i(Tag, "set alarm");

        Intent intent = new Intent(context,
                AlarmReceiver.class);
        sender = PendingIntent.getBroadcast(
                context, 0, intent, 0);

        long ct = System.currentTimeMillis();
        ct = ct + delay;

        AlarmManager manager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        manager.set(AlarmManager.RTC_WAKEUP, ct, sender);
    }

    public void cancelAlarm(Context context){
        Log.i(Tag, "cancel alarm");
        AlarmManager manager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        manager.cancel(sender);
    }
}
