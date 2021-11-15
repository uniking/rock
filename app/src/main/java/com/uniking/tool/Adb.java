package com.uniking.tool;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

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

    public static Map<String, String> getNoSysPackageList(Context ctx) {
        Map<String, String> apps = new HashMap<>();
        PackageManager pm = ctx.getPackageManager();
        List<ApplicationInfo> iapp = pm.getInstalledApplications(0);
        for (ApplicationInfo one : iapp) {
            if((one.flags&ApplicationInfo.FLAG_SYSTEM)==ApplicationInfo.FLAG_SYSTEM){
                ;
            }else{
                apps.put(pm.getApplicationLabel(one).toString(), one.packageName);
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
}