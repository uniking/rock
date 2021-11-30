package com.uniking.rock;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.uniking.tool.Adb;
import com.uniking.tool.DeskShortCut;
import com.uniking.tool.Shutcut2Activity;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wzl on 11/17/21.
 */

public class EnableActivity extends Activity {
    private Spinner spinner;
    private ArrayAdapter<String> adapter;
    private String selectAppLab = "";
    Map<String, String> apps = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enable);

        init();
    }

    @Override
    protected void onResume() {
        super.onResume();

        setApps();
    }

    void setApps(){
        apps.clear();

        //----------------
        spinner = (Spinner) findViewById(R.id.spinner_enable_apps);
        apps = Adb.getNoSysPackageList(getApplication(), 2);
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
        findViewById(R.id.bt_enable_app).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Adb.isEnable(getApplicationContext(), apps.get(selectAppLab))){
                    Toast.makeText(getApplicationContext(), "已经是启用状态", Toast.LENGTH_SHORT).show();
                }else{
                    Adb.enableApp(apps.get(selectAppLab));
                    Toast.makeText(getApplicationContext(), "启用成功", Toast.LENGTH_SHORT).show();
                }

            }
        });

        findViewById(R.id.bt_add_shutcut).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = Shutcut2Activity.getInstance(getApplicationContext()).getAvailableId();
                if(id >= 0){
                    String shotLabel = selectAppLab;
                    String packagename = apps.get(selectAppLab);

                    Shutcut2Activity.getInstance(getApplicationContext()).addShutcut2ActivityMap(id, packagename);
                    DeskShortCut.addShortCutCompact(getApplicationContext(), Shutcut2Activity.preShutcutActivity+id, shotLabel, packagename);
                }else{
                    Toast.makeText(getApplicationContext(), "没有Activity资源了", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}
