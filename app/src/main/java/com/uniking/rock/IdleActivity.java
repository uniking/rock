package com.uniking.rock;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.uniking.tool.Adb;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wzl on 11/17/21.
 */

public class IdleActivity extends Activity {
    private Spinner spinner;
    private ArrayAdapter<String> adapter;
    private String selectAppLab = "";
    Map<String, String> apps = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_idle);

        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setApps();

        TextView tv = findViewById(R.id.tv_show);
        String de = Adb.enabledDeep();
        String le = Adb.enabledLight();
        String lightstatus = Adb.getLightStatus();

        String msg = "没有root权限";
        if(de != null){
            msg = "light开关:"+le+"deep开关:"+de + "light状态:"+lightstatus;
        }


        tv.setText(msg);
    }

    void setApps(){
        apps.clear();

        //----------------
        spinner = (Spinner) findViewById(R.id.spinner_idle_apps);
        apps = Adb.getNoSysPackageList(getApplication(), 1);
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, Adb.Set2List(apps.keySet()));
        if(adapter != null){
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    selectAppLab = adapter.getItem(position);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    ;
                }
            });
            if(!adapter.isEmpty()){
                selectAppLab = adapter.getItem(0);
            }
        }else{
            Toast.makeText(this, "adapter==null", Toast.LENGTH_SHORT).show();
        }
    }

    void init(){
        //----------------
        spinner = (Spinner) findViewById(R.id.spinner_idle_apps);
        apps = Adb.getNoSysPackageList(getApplication(), 1);
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, Adb.Set2List(apps.keySet()));
        if(adapter != null){
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    selectAppLab = adapter.getItem(position);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    ;
                }
            });
        }else{
            Toast.makeText(this, "adapter==null", Toast.LENGTH_SHORT).show();
        }

        findViewById(R.id.bt_init_idle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Adb.initIdle();
            }
        });
//        findViewById(R.id.bt_entry_idle).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Adb.forceLightIdle();
//            }
//        });
        findViewById(R.id.bt_ignore_wakuplock).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Adb.ignoreWakupLock(apps.get(selectAppLab));
            }
        });
        findViewById(R.id.bt_add_whitelist).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Adb.addIdleWhiteList(apps.get(selectAppLab));
            }
        });
    }
}
