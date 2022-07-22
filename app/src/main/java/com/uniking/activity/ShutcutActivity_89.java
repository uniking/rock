package com.uniking.activity;

import android.app.Activity;
import android.os.Bundle;

import com.uniking.rock.R;
import com.uniking.tool.Adb;
import com.uniking.tool.Shutcut2Activity;

import static com.uniking.tool.DeskShortCut.launchAPK3;

/**
 * Created by wzl on 11/29/21.
 */

public class ShutcutActivity_89 extends Activity {

    String simpleName = this.getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foreground);

        String packageName = Shutcut2Activity.getInstance(getApplicationContext()).getPackageName(simpleName);

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
        finishAndRemoveTask();
    }
}
