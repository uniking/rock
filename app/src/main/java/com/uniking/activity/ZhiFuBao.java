package com.uniking.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.uniking.rock.R;
import com.uniking.tool.Adb;

import static com.uniking.tool.DeskShortCut.launchAPK3;

/**
 * Created by wzl on 11/29/21.
 */

public class ZhiFuBao extends Activity {
    String packageName = "com.eg.android.AlipayGphone";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foreground);
        if(Adb.isEnable(getApplicationContext(), packageName)){
            ;
        }else{
            Adb.enableApp(packageName);
        }
        launchAPK3(getApplicationContext(), packageName);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("aaa", "aaaa");
    }
}
