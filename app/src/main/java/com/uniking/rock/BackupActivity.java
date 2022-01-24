package com.uniking.rock;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.EditText;

import com.uniking.service.ForegroundService;
import com.uniking.tool.Adb;

/**
 * Created by wzl on 11/17/21.
 */

public class BackupActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backup);

        findViewById(R.id.bk_bt_backup).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText pcIp = findViewById(R.id.bk_et_pc_ip);
                EditText pcDir = findViewById(R.id.bk_et_pc_dir);
                EditText phoneDir = findViewById(R.id.bk_et_phone_dir);

                String src = phoneDir.getText().toString();
                String dest = "root@"+pcIp.getText().toString()+":"+pcDir.getText().toString();
                //Adb.suDo("rsync -avh " + src + " " + dest);
            }
        });

        findViewById(R.id.bk_bt_recover).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText pcIp = findViewById(R.id.bk_et_pc_ip);
                EditText pcDir = findViewById(R.id.bk_et_pc_dir);
                EditText phoneDir = findViewById(R.id.bk_et_phone_dir);

                String src = "root@"+pcIp.getText().toString()+":"+pcDir.getText().toString();
                String dest = phoneDir.getText().toString();
                //Adb.suDo("rsync -avh " + src + " " + dest);
            }
        });
    }
}
