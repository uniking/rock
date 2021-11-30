package com.uniking.tool;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by wzl on 11/15/21.
 * shutcut与activity间的映射关系
 * {"0":"com.tencent.mm", "1":"com.tencent.mobileqq"}
 */

public class Shutcut2Activity {
    Context mContent;
    String shutcutpath;
    final int max_id = 39;
    static Shutcut2Activity self;

    public static final String preShutcutActivity = "com.uniking.activity.ShutcutActivity_";
    public static final String preShutcutActivitySimple = "ShutcutActivity_";

    //负责缓存activity -> packagename , 便于ShutcutActivity_X启动对应apk
    Map<String,String> shutcutCatch = new ConcurrentHashMap<>();

    private Shutcut2Activity(Context context){
        mContent = context;

        File pp = context.getDir("policy", MODE_PRIVATE);
        shutcutpath = pp.getAbsolutePath()+"/shutcut";

        initShutcutCatch();
    }

    public static Shutcut2Activity getInstance(Context context){
        synchronized (Shutcut2Activity.class){
            if(self == null){
                self = new Shutcut2Activity(context);
            }
        }

        return self;
    }

    /**
     * 通过activity名字，得到它对应启动哪个app（包名）
     * @param activityName
     * @return
     */
    public String getPackageName(String activityName){
        return shutcutCatch.get(activityName);
    }

    public int getAvailableId(){
        int vi = -1;
        Set<String> keys = shutcutCatch.keySet();
        for(int i=0;i<max_id;++i){
            if(keys.contains(preShutcutActivitySimple+i)){
                ;
            }else{
                vi=i;
                break;
            }
        }

        return vi;
    }

    public void addShutcut2ActivityMap(int id, String packageName){
        try {
            JSONObject json = null;
            File db = new File(shutcutpath);
            if(db.exists()){
                json = new JSONObject(FileOperate.readFileExclusive(shutcutpath));
            }else{
                json = new JSONObject("{}");
            }


            json.put(preShutcutActivitySimple+id, packageName);

            FileOperate.updateFile(shutcutpath, json.toString());

            shutcutCatch.put(preShutcutActivitySimple+id, packageName);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    /**
     * 启动时初始化缓存
     */
    private void initShutcutCatch(){
        try {
            File da = new File(shutcutpath);
            if(da.exists()){
                JSONObject json = new JSONObject(FileOperate.readFileExclusive(shutcutpath));
                Iterator it = json.keys();
                while (it.hasNext()){
                    String key = (String)it.next();
                    String value = json.getString(key);
                    shutcutCatch.put(key, value);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    /**
     * 删除图标到activity的映射
     * @param packageName
     */
    public void deleteShutcut2ActivityMap(String packageName){
        try {
            String activityname = "";
            JSONObject json = new JSONObject(FileOperate.readFileExclusive(shutcutpath));
            Iterator it = json.keys();
            while (it.hasNext()){
                String key = (String)it.next();
                String value = json.getString(key);
                if(value.equals(packageName)){
                    activityname = key;
                    json.remove(key);
                    break;
                }
            }


            FileOperate.updateFile(shutcutpath, json.toString());

            shutcutCatch.remove(activityname);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
