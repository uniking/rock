package com.uniking.tool;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

import com.uniking.rock.R;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Adb{
    public static Set<String> List2Set(List<String> list){
        Set<String> set = new HashSet<>();
        for(String one : list){
            set.add(one);
        }

        return set;
    }

    public static List<String> Set2List(Set<String> set){
        List<String> list = new ArrayList<>();
        for(String one : set){
            list.add(one);
        }

        return list;
    }

    // <packageName, Label>
    public static Map<String, String> getPackageList(Context ctx) {
        Map<String, String> apps = new HashMap<>();
        PackageManager pm = ctx.getPackageManager();
        List<ApplicationInfo> iapp = pm.getInstalledApplications(0);
        for (ApplicationInfo one : iapp) {
            apps.put(pm.getApplicationLabel(one).toString(), one.packageName);
            //one.enabled;
        }

        return apps;
    }

    public static Map<String, String> getEnablePackageList(Context ctx) {
        Map<String, String> apps = new HashMap<>();
        PackageManager pm = ctx.getPackageManager();
        List<ApplicationInfo> iapp = pm.getInstalledApplications(0);
        for (ApplicationInfo one : iapp) {
            if(one.enabled){
                apps.put(pm.getApplicationLabel(one).toString(), one.packageName);
            }
        }

        return apps;
    }

    /*
    type 0所有的app， 1可用的app， 2禁用的app
     */
    public static Map<String, String> getNoSysPackageList(Context ctx, int type) {
        Map<String, String> apps = new HashMap<>();
        PackageManager pm = ctx.getPackageManager();
        List<ApplicationInfo> iapp = pm.getInstalledApplications(0);
        for (ApplicationInfo one : iapp) {
            if((one.flags&ApplicationInfo.FLAG_SYSTEM)==ApplicationInfo.FLAG_SYSTEM){
                ;
            }else{
                switch (type){
                    case 0:
                        apps.put(pm.getApplicationLabel(one).toString(), one.packageName);
                        break;
                    case 1:
                        if(one.enabled){
                            apps.put(pm.getApplicationLabel(one).toString(), one.packageName);
                        }
                        break;
                    case 2:
                        if(!one.enabled){
                            apps.put(pm.getApplicationLabel(one).toString(), one.packageName);
                        }
                        break;
                    default:
                        ;
                }

            }
        }

        return apps;
    }

    public static boolean isEnable(Context context, String packageName){
        boolean bRtn = true;

        try{
            PackageManager pm = context.getPackageManager();
            bRtn = pm.getApplicationInfo(packageName, 0).enabled;
        }catch (Exception e){
            e.printStackTrace();
        }

        return bRtn;
    }

    public static String suDo(String cmd){
        String msg = null;
        try{
            StringBuilder sb = new StringBuilder();
            Process p = Runtime.getRuntime().exec(new String[]{"su","-c", cmd});
            InputStream is = p.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            p.waitFor();
            is.close();
            reader.close();
            p.destroy();

            msg = sb.toString();
        }catch (Exception e){
            ;
        }

        return msg;
    }

    public static void disableApp(String packageName){
        suDo("pm disable-user " + packageName);
    }

    public static void enableApp(String packageName){
        suDo("pm enable " + packageName);
    }

    public static void initIdle(){
        //关闭深度休眠
        suDo("dumpsys deviceidle disable deep");
        //开启浅度休眠
        suDo("dumpsys deviceidle enable light");
        //调节浅度休眠周期
        suDo("settings put global device_idle_constants light_idle_factor=2.0,inactive_to=2592000000,motion_inactive_to=2592000000,light_after_inactive_to=15000,light_pre_idle_to=30000,light_max_idle_to=86400000,light_idle_to=43200000,light_idle_maintenance_max_budget=30000,light_idle_maintenance_min_budget=10000,min_time_to_alarm=60000");
    }

    /*
    Now forced in to deep idle mode
    进入深度休眠模式
     */
    public static void forceLightIdle(){
        // dumpsys deviceidle step deep
        suDo("dumpsys deviceidle force-idle light");
    }

    /*
    退出深度休眠模式
     */
    public static void unforceIdle(){
        // dumpsys deviceidle step light
        suDo("dumpsys deviceidle unforce");
    }

    public static void ignoreWakupLock(String packageName){
        suDo("cmd appops set " + packageName + " WAKE_LOCK ignore");
    }

    public static String getLightStatus(){
        return suDo("dumpsys deviceidle get light");
    }

    public static String getDeepStatus(){
        return suDo("dumpsys deviceidle get deep");
    }

    public static String enabledDeep(){
        return suDo("dumpsys deviceidle enabled deep");
    }

    public static String enabledLight(){
        return suDo("dumpsys deviceidle enabled light");
    }

    public static String stopLogcat(){
        return suDo("stop logd");
    }
    public static void forceStop(String packageName){
        suDo("am force-stop " + packageName);
    }
}