package com.uniking.rock;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by wzl on 11/15/21.
 */

public class ForegroundService extends Service {
    static boolean serviceIsLive = false;
    private static final int NOTIFICATION_DOWNLOAD_PROGRESS_ID = 0x0001;
    private static final String TAG = ForegroundService.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG, "onCreate");

        // 获取服务通知
        Notification notification = createForegroundNotification(getApplicationContext());
        //将服务置于启动状态 ,NOTIFICATION_ID指的是创建的通知的ID
        startForeground(NOTIFICATION_DOWNLOAD_PROGRESS_ID, notification);
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.e(TAG, "onBind");
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "onStartCommand");

        // 标记服务启动
        ForegroundService.serviceIsLive = true;
        // 数据获取
        String data = intent.getStringExtra("Foreground");
        Toast.makeText(this, data, Toast.LENGTH_SHORT).show();

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        // 标记服务关闭
        ForegroundService.serviceIsLive = false;
        // 移除通知
        stopForeground(true);

        Log.e(TAG, "onDestroy");
        super.onDestroy();
    }

    /**
     * 创建服务通知
     */
    private Notification createForegroundNotification(Context context) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // 唯一的通知通道的id.
        String notificationChannelId = "notification_channel_id_01";

        // Android8.0以上的系统，新建消息通道
        if (Build.VERSION.SDK_INT  >= Build.VERSION_CODES.O) {
            //用户可见的通道名称
            String channelName = "Foreground Service Notification";
            //通道的重要程度
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel = new NotificationChannel(notificationChannelId, channelName, importance);
            notificationChannel.setDescription("Channel description");
            //LED灯
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            //震动
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(notificationChannel);
            }
        }


//        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext());
//        //通知小图标
//        builder.setSmallIcon(context.getApplicationInfo().icon);
//        //通知标题
//        builder.setContentTitle("Rock");
//        //通知内容
//        builder.setContentText("应用冻结服务");
//        //设定通知显示的时间
//        builder.setWhen(System.currentTimeMillis());
//        //设定启动的内容
//        Intent activityIntent = new Intent(this, NotificationActivity.class);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, activityIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//        builder.setContentIntent(pendingIntent);
//
//        //创建通知并返回
//        return builder.build();

        Notification notification = new Notification();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);  //点击通知栏后想要被打开的页面MainActivity.class
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);  //点击通知栏触发跳转
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            notification = new Notification.Builder(this, notificationChannelId)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle("rock")
                    .setContentText("应用冻结服务")
                    .setContentIntent(pendingIntent)
                    .build();
        }else{
            NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext());
            //通知小图标
            builder.setSmallIcon(context.getApplicationInfo().icon);
            //通知标题
            builder.setContentTitle("Rock");
            //通知内容
            builder.setContentText("应用冻结服务");
            //设定通知显示的时间
            builder.setWhen(System.currentTimeMillis());
            //设定启动的内容
            Intent activityIntent = new Intent(this, NotificationActivity.class);
            pendingIntent = PendingIntent.getActivity(this, 1, activityIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(pendingIntent);

            //创建通知并返回
            notification = builder.build();
        }
        notification.flags |= Notification.FLAG_NO_CLEAR;
        return notification;
    }
}