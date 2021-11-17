package com.uniking.rock;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.uniking.tool.Adb;
import com.uniking.tool.AppList;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wzl on 11/17/21.
 */

public class DisableActivity extends Activity {
    private Spinner spinner;
    private ArrayAdapter<String> adapter;
    private String selectAppLab = "";
    Map<String, String> apps = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disable);

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
        spinner = (Spinner) findViewById(R.id.spinner_disable_apps);
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
        findViewById(R.id.bt_disenable_app).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Adb.isEnable(getApplicationContext(), apps.get(selectAppLab))){
                    Adb.disableApp(apps.get(selectAppLab));
                    Toast.makeText(getApplicationContext(), "禁用成功", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(), "已经是禁用状态", Toast.LENGTH_SHORT).show();
                }

            }
        });

        findViewById(R.id.bt_add_disenable_list).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AppList(getApplicationContext()).addDisableList(apps.get(selectAppLab), selectAppLab);
            }
        });

        findViewById(R.id.bt_remove_disenable_list).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AppList(getApplicationContext()).deleteDisableList(apps.get(selectAppLab));
            }
        });

        findViewById(R.id.bt_print_app).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, String> apps = new AppList(getApplicationContext()).getDisableList();
                StringBuilder sb = new StringBuilder();
                for(String app : apps.keySet()){
                    sb.append(apps.get(app));
                    sb.append("\n");
                }

                EditText et = findViewById(R.id.et_msg);
                et.setText(sb.toString());
            }
        });
    }
}
