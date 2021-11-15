package com.uniking.tool;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by wzl on 11/15/21.
 */

public class AppList {
    Context mContent;
    String disablelistpath;

    public AppList(Context context){
        mContent = context;

        File pp = context.getDir("policy", MODE_PRIVATE);
        disablelistpath = pp.getAbsolutePath()+"/disable";
    }


    /*
    {"disable":{"com.msmsdk.tesp":"沙箱应用", "":""}}
     */
    public Map<String, String> getDisableList(){
        Map<String, String> rtn = new HashMap<>();

        try {
            File da = new File(disablelistpath);
            if(da.exists()){
                JSONObject json = new JSONObject(FileOperate.readFileExclusive(disablelistpath));
                JSONObject disable = (JSONObject) json.get("disable");
                Iterator it = disable.keys();
                while (it.hasNext()){
                    String key = (String)it.next();
                    String value = disable.getString(key);
                    rtn.put(key, value);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return rtn;
    }

        /*
    {"disable":["", "", ""]}

    <packagename, lab>
     */
    public void addDisableList(String packageName, String lab){
        try {
            JSONObject json = null;
            File db = new File(disablelistpath);
            if(db.exists()){
                json = new JSONObject(FileOperate.readFileExclusive(disablelistpath));
            }else{
                json = new JSONObject("{\"disable\":{}}");
            }

            JSONObject disable = (JSONObject) json.get("disable");

            disable.put(packageName, lab);

            FileOperate.updateFile(disablelistpath, json.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void deleteDisableList(String packageName){
        try {
            JSONObject json = new JSONObject(FileOperate.readFileExclusive(disablelistpath));
            JSONObject disable = (JSONObject) json.get("disable");

            disable.remove(packageName);

            FileOperate.updateFile(disablelistpath, json.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
